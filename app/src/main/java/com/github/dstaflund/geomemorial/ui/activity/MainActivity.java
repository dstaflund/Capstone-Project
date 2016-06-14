package com.github.dstaflund.geomemorial.ui.activity;

import android.Manifest;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.dstaflund.geomemorial.GeomemorialApplication;
import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.IntentManager;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbProvider;
import com.github.dstaflund.geomemorial.ui.activity.about.AboutActivity;
import com.github.dstaflund.geomemorial.ui.activity.favorites.FavoritesActivity;
import com.github.dstaflund.geomemorial.ui.activity.preferences.PreferencesActivity;
import com.github.dstaflund.geomemorial.ui.fragment.map.MapFragment;
import com.github.dstaflund.geomemorial.ui.fragment.searchresult.SearchResultFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import static com.github.dstaflund.geomemorial.GeomemorialApplication.setVisibleMapType;
import static com.github.dstaflund.geomemorial.common.util.PreferencesManager.isDefaultMapType;
import static com.github.dstaflund.geomemorial.common.util.ToastManager.newToast;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

public class MainActivity
    extends AppCompatActivity
    implements
    LoaderManager.LoaderCallbacks<Cursor>,
    NavigationView.OnNavigationItemSelectedListener,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener{

    public static final int EMPTY_SEARCH = -1;
    public static final int RESIDENT_LOADER_ID = 0;
    public static final String LAST_SEARCH_REQUEST_KEY = "lastSearchRequest";
    public static final String SAVED_SEARCH_KEY = "savedSearch";

    private GoogleApiClient mGoogleApiClient;
    private SearchRecentSuggestions mSearchRecentSuggestions;
    private NavigationView mNavigationView;
    private MapFragment mMapFragment;
    private SearchResultFragment mSearchResultFragment;
    private SearchView mSearchView;
    private SearchRequest mLastSearchRequest;
    private String mSavedSearchString;
    private boolean mDisplayToast;
    private Bundle mSavedInstanceState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mSearchResultFragment = (SearchResultFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_search_result);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            //noinspection deprecation
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        }

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }

        checkInitialMapType();

        mSearchRecentSuggestions = new SearchRecentSuggestions(
            this,
            GeomemorialDbProvider.AUTHORITY,
            GeomemorialDbProvider.MODE
        );

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Cursor> loader = loaderManager.getLoader(EMPTY_SEARCH);
        if (loader != null && !loader.isReset()) {
            loaderManager.restartLoader(EMPTY_SEARCH, null, this);
        } else {
            loaderManager.initLoader(EMPTY_SEARCH, null, this);
        }

        // Restore the last search if present
        if (savedInstanceState != null) {
            mSavedSearchString = savedInstanceState.getString(SAVED_SEARCH_KEY);
            mLastSearchRequest = savedInstanceState.getParcelable(LAST_SEARCH_REQUEST_KEY);
        }

        // If there was a previous search, then don't display toast otherwise do so
        mDisplayToast = mLastSearchRequest == null;

        // If there was a previous search, then don't zoom camera, otherwise do so
        mMapFragment.ignoreCameraZoom(mLastSearchRequest != null);

        mSavedInstanceState = savedInstanceState;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

        if (mSavedInstanceState != null && mLastSearchRequest != null){
            mSearchResultFragment.clearPager();
            handleIntent(mLastSearchRequest.toIntent());
        }
        else {
            handleIntent(getIntent());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mSearchView != null) {
            outState.putParcelable(LAST_SEARCH_REQUEST_KEY, mLastSearchRequest);
            outState.putString(SAVED_SEARCH_KEY, mSearchView.getQuery().toString());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    private void checkInitialMapType() {
        switch (GeomemorialApplication.getVisibleMapType()) {
            case GoogleMap.MAP_TYPE_NORMAL:
                mNavigationView.getMenu().findItem(R.id.nav_layer_normal).setChecked(true);
                break;

            case GoogleMap.MAP_TYPE_TERRAIN:
                mNavigationView.getMenu().findItem(R.id.nav_layer_terrain).setChecked(true);
                break;

            case GoogleMap.MAP_TYPE_SATELLITE:
                mNavigationView.getMenu().findItem(R.id.nav_layer_satellite).setChecked(true);
                break;

            case GoogleMap.MAP_TYPE_HYBRID:
                mNavigationView.getMenu().findItem(R.id.nav_layer_hybrid).setChecked(true);
                break;
        }
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }

        SearchRequest searchRequest = new SearchRequest(intent);

        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            doSearch(searchRequest);
            return;
        }

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mSearchRecentSuggestions.saveRecentQuery(searchRequest.getExtraDataKey(), null);
            doSearch(searchRequest);
        }
    }

    private void doSearch(

        @NonNull SearchRequest currentSearchRequest
    ) {
        Bundle args = currentSearchRequest.toBundle();

        mLastSearchRequest = currentSearchRequest;

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Cursor> loader = loaderManager.getLoader(RESIDENT_LOADER_ID);

        if (loader != null && !loader.isReset()) {
            loaderManager.restartLoader(RESIDENT_LOADER_ID, args, this);
        } else {
            loaderManager.initLoader(RESIDENT_LOADER_ID, args, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            MenuItem searchMenu = menu.findItem(R.id.search);
            mSearchView = (SearchView) MenuItemCompat.getActionView(searchMenu);
            ComponentName componentName = getComponentName();
            SearchableInfo searchableInfo = searchManager.getSearchableInfo(componentName);
            mSearchView.setSearchableInfo(searchableInfo);
            mSearchView.setIconifiedByDefault(false);

            if (mSavedSearchString != null && ! mSavedSearchString.isEmpty()) {
                searchMenu.expandActionView();
                mSearchView.setQuery(mSavedSearchString, true);
                mSearchView.clearFocus();
            }
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setMapType(int mapTypeId) {
        mMapFragment.setMapType(mapTypeId);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean result;

        switch (item.getItemId()) {
            case R.id.nav_camera:
                IntentManager.startActivity(this, FavoritesActivity.class);
                result = true;
                break;

            case R.id.nav_layer_normal:
                setMapType(MAP_TYPE_NORMAL);
                item.setChecked(isDefaultMapType(this, MAP_TYPE_NORMAL));
                mNavigationView.getMenu().findItem(R.id.nav_layer_terrain).setChecked(false);
                mNavigationView.getMenu().findItem(R.id.nav_layer_satellite).setChecked(false);
                mNavigationView.getMenu().findItem(R.id.nav_layer_hybrid).setChecked(false);
                setVisibleMapType(MAP_TYPE_NORMAL);
                result = true;
                break;

            case R.id.nav_layer_hybrid:
                setMapType(MAP_TYPE_HYBRID);
                item.setChecked(isDefaultMapType(this, MAP_TYPE_HYBRID));
                mNavigationView.getMenu().findItem(R.id.nav_layer_normal).setChecked(false);
                mNavigationView.getMenu().findItem(R.id.nav_layer_terrain).setChecked(false);
                mNavigationView.getMenu().findItem(R.id.nav_layer_satellite).setChecked(false);
                setVisibleMapType(MAP_TYPE_HYBRID);
                result = true;
                break;

            case R.id.nav_layer_terrain:
                setMapType(MAP_TYPE_TERRAIN);
                item.setChecked(isDefaultMapType(this, MAP_TYPE_TERRAIN));
                mNavigationView.getMenu().findItem(R.id.nav_layer_normal).setChecked(false);
                mNavigationView.getMenu().findItem(R.id.nav_layer_satellite).setChecked(false);
                mNavigationView.getMenu().findItem(R.id.nav_layer_hybrid).setChecked(false);
                setVisibleMapType(MAP_TYPE_TERRAIN);
                result = true;
                break;

            case R.id.nav_layer_satellite:
                setMapType(MAP_TYPE_SATELLITE);
                item.setChecked(isDefaultMapType(this, MAP_TYPE_SATELLITE));
                mNavigationView.getMenu().findItem(R.id.nav_layer_normal).setChecked(false);
                mNavigationView.getMenu().findItem(R.id.nav_layer_terrain).setChecked(false);
                mNavigationView.getMenu().findItem(R.id.nav_layer_hybrid).setChecked(false);
                setVisibleMapType(MAP_TYPE_SATELLITE);
                result = true;
                break;

            case R.id.nav_clear_search_history:
                mSearchRecentSuggestions.clearHistory();
                newToast(this, null, getString(R.string.toast_search_history_cleared), Toast.LENGTH_SHORT);
                result = true;
                break;


            case R.id.nav_manage:
                IntentManager.startActivity(this, PreferencesActivity.class);
                result = true;
                break;

            case R.id.nav_send:
                IntentManager.startActivity(this, AboutActivity.class);
                result = true;
                break;

            default:
                result = false;
        }

        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (dl != null) {
            dl.closeDrawer(GravityCompat.START);
        }

        return result;
    }

    @Override
    @Nullable
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle args) {
        switch (loaderId) {
            case EMPTY_SEARCH:
                return null;
            default:
                if (args == null) {
                    return null;
                }

                SearchRequest searchRequest = new SearchRequest(args);
                Uri uri = searchRequest.getUri();
                String query = searchRequest.getQuery();
                String extraDataKey = searchRequest.getExtraDataKey();

                //  Free-form search
                if (uri == null && query != null && extraDataKey == null){
                    return new CursorLoader(
                        getApplicationContext(),
                        MarkerInfo.CONTENT_URI,
                        MarkerInfo.DEFAULT_PROJECTION,
                        MarkerInfo.CONSTRAINT_BY_SEARCH_CRITERIA,
                        MarkerInfo.getSelectionArgsFor(query),
                        MarkerInfo.SORT_ORDER_RESIDENT
                    );
                }

                //  Suggestion Search
                else if (uri != null && query != null && extraDataKey != null){
                    return new CursorLoader(
                        getApplicationContext(),
                        uri,
                        MarkerInfo.DEFAULT_PROJECTION,
                        query,
                        MarkerInfo.getSelectionArgsFor(extraDataKey),
                        MarkerInfo.SORT_ORDER_RESIDENT
                    );
                }

                //  Previous Search
                else if (uri == null && query == null && extraDataKey != null){
                    return new CursorLoader(
                        getApplicationContext(),
                        MarkerInfo.CONTENT_URI,
                        MarkerInfo.DEFAULT_PROJECTION,
                        MarkerInfo.CONSTRAINT_BY_SEARCH_CRITERIA,
                        MarkerInfo.getSelectionArgsFor(extraDataKey),
                        MarkerInfo.SORT_ORDER_RESIDENT
                    );
                }
                else {
                    return null;
                }
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, @Nullable Cursor data) {
        switch (loader.getId()) {
            case EMPTY_SEARCH:
                mMapFragment.clearMap();
                mSearchResultFragment.swapCursor(null);
                break;
            default:
                Resources r = getResources();
                if (data == null || data.getCount() == 0) {
                    if (mDisplayToast) {
                        newToast(
                            this,
                            null,
                            getString(R.string.toast_search_results_empty),
                            Toast.LENGTH_SHORT
                        );
                    }
                } else if (data.getCount() <= r.getInteger(R.integer.max_visible_memorials)) {
                    if (mDisplayToast) {
                        newToast(
                            this,
                            null,
                            getString(
                                R.string.toast_search_results_normal,
                                data.getCount()
                            ),
                            Toast.LENGTH_SHORT
                        );
                    }
                } else {
                    if (mDisplayToast) {
                        newToast(
                            this,
                            null,
                            getString(
                                R.string.toast_search_results_too_many,
                                r.getInteger(R.integer.max_visible_memorials)
                            ),
                            Toast.LENGTH_SHORT
                        );
                    }
                }
                mMapFragment.clearMap();
                mSearchResultFragment.swapCursor(data);
                mDisplayToast= true;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        try {
            mMapFragment.clearMap();
            mSearchResultFragment.swapCursor(null);
        }

        catch(IllegalStateException e){
            // Eat it for now
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
         || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {

            GeomemorialApplication.setLastLocation(
                LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
            );
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public static class SearchRequest implements Parcelable {
        public static final String ACTION = "action";
        public static final String URI = "uri";
        public static final String QUERY = SearchManager.QUERY;
        public static final String USER_QUERY = SearchManager.USER_QUERY;
        public static final String EXTRA_DATA_KEY = SearchManager.EXTRA_DATA_KEY;

        public static final Parcelable.Creator<SearchRequest> CREATOR =
            new Parcelable.Creator<SearchRequest>() {

                @Override
                public SearchRequest createFromParcel(Parcel in) {
                    return new SearchRequest(in);
                }

                @Override
                public SearchRequest[] newArray(int size) {
                    return new SearchRequest[size];
                }
            };

        private String mAction;
        private Uri mUri;
        private String mQuery;
        private String mUserQuery;
        private String mExtraDataKey;

        public Uri getUri(){
            return mUri;
        }

        public String getQuery(){
            return mQuery;
        }

        public String getExtraDataKey(){
            return mExtraDataKey;
        }

        public Bundle toBundle(){
            Bundle b = new Bundle();
            b.putString(ACTION, mAction);
            b.putParcelable(URI, mUri);
            b.putString(QUERY, mQuery);
            b.putString(USER_QUERY, mUserQuery);
            b.putString(EXTRA_DATA_KEY, mExtraDataKey);
            return b;
        }

        public Intent toIntent(){
            Intent i = new Intent(mAction);
            i.setData(mUri);
            i.putExtra(SearchManager.QUERY, mQuery);
            i.putExtra(SearchManager.USER_QUERY, mUserQuery);
            i.putExtra(SearchManager.EXTRA_DATA_KEY, mExtraDataKey);
            return i;
        }

        public SearchRequest(Parcel in){
            super();
            mAction = in.readString();
            mUri = Uri.parse(in.readString());
            mQuery = in.readString();
            mUserQuery = in.readString();
            mExtraDataKey = in.readString();
        }

        public SearchRequest(Bundle in){
            mAction = in.getString(ACTION);
            mUri = in.getParcelable(URI);
            mQuery = in.getString(QUERY);
            mUserQuery = in.getString(USER_QUERY);
            mExtraDataKey = in.getString(EXTRA_DATA_KEY);
        }

        public SearchRequest(Intent i){
            mAction = i.getAction();
            mUri = i.getData();
            mQuery = i.getStringExtra(QUERY);
            mUserQuery = i.getStringExtra(USER_QUERY);
            mExtraDataKey = i.getStringExtra(EXTRA_DATA_KEY);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mAction);
            dest.writeString(mUri == null ? null : mUri.toString());
            dest.writeString(mQuery);
            dest.writeString(mUserQuery);
            dest.writeString(mExtraDataKey);
        }
    }
}
