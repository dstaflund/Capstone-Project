package com.github.dstaflund.geomemorial.ui.activity;

import android.Manifest;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.dstaflund.geomemorial.GeomemorialApplication;
import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbProvider;
import com.github.dstaflund.geomemorial.ui.fragment.MapFragment;
import com.github.dstaflund.geomemorial.ui.fragment.SearchResultFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
    GoogleApiClient.OnConnectionFailedListener,
    SearchResultFragment.OnPlaceButtonClickedListener,
    SearchResultFragment.OnChangeCursorListener{
    private static final String sLogTag = MainActivity.class.getSimpleName();

    public static final int EMPTY_SEARCH = -1;
    public static final int RESIDENT_LOADER_ID = 0;
    public static final String URI_KEY = "uri";
    public static final String SELECTION_KEY = "selection";
    public static final String SELECTION_ARG_KEY = "selectionArg";


    private boolean mConnected;
    private GoogleApiClient mGoogleApiClient;
    private SearchRecentSuggestions mSearchRecentSuggestions;
    private NavigationView mNavigationView;
    private MapFragment mMapFragment;
    private SearchResultFragment mSearchResultFragment;
    private Location mLastLocation;

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
        drawer.setDrawerListener(toggle);
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
        mNavigationView.setNavigationItemSelectedListener(this);

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

        handleIntent(getIntent());
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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

    private boolean isLocationAware(){
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
         || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }

        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(null, null, query);
            return;
        }

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Uri uri = intent.getData();
            String selection = intent.getStringExtra(SearchManager.QUERY);
            String selectionArg = intent.getStringExtra(SearchManager.EXTRA_DATA_KEY);
            mSearchRecentSuggestions.saveRecentQuery(selectionArg, null);
            doSearch(uri, selection, selectionArg);
        }
    }

    private void doSearch(@NonNull Uri uri, @Nullable String selection, @Nullable String selectionArg) {
        Bundle args = new Bundle();
        args.putParcelable(URI_KEY, uri);
        args.putString(SELECTION_KEY, selection);
        args.putString(SELECTION_ARG_KEY, selectionArg);

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
            SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            ComponentName componentName = getComponentName();
            SearchableInfo searchableInfo = searchManager.getSearchableInfo(componentName);
            searchView.setSearchableInfo(searchableInfo);
            searchView.setIconifiedByDefault(false);
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
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
                FavoritesActivity.startActivity(this);
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
                PreferencesActivity.startActivity(this);
                result = true;
                break;

            case R.id.nav_send:
                AboutActivity.startActivity(this);
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
                    Log.e(sLogTag, getString(R.string.log_main_loader_missing_arguments));
                    return null;
                }

                Uri uri = args.getParcelable(URI_KEY);
                String selection = args.getString(SELECTION_KEY);
                String selectionArg = args.getString(SELECTION_ARG_KEY);
                return new CursorLoader(
                    getApplicationContext(),
                    uri == null ? MarkerInfo.CONTENT_URI : uri,
                    MarkerInfo.DEFAULT_PROJECTION,
                    MarkerInfo.CONSTRAINT_BY_SEARCH_CRITERIA,
                    selectionArg == null
                        ? MarkerInfo.getSelectionArgsFor(selection)
                        : MarkerInfo.getSelectionArgsFor(selectionArg),
                    MarkerInfo.SORT_ORDER_RESIDENT
                );
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, @Nullable Cursor data) {
        switch (loader.getId()) {
            case EMPTY_SEARCH:
                mMapFragment.clearMap();
                mSearchResultFragment.changeCursor(null);
                break;
            default:
                if (data == null || data.getCount() == 0) {
                    Log.d(sLogTag, getResources().getString(R.string.log_search_too_few));
                    newToast(
                        this,
                        null,
                        getString(R.string.toast_search_results_empty),
                        Toast.LENGTH_SHORT
                    );
                } else if (data.getCount() <= getResources().getInteger(R.integer.max_visible_memorials)) {
                    Log.d(sLogTag, getResources().getString(R.string.log_search_just_right));
                    newToast(
                        this,
                        null,
                        getString(
                            R.string.toast_search_results_normal,
                            data.getCount()
                        ),
                        Toast.LENGTH_SHORT
                    );
                } else {
                    Log.d(sLogTag, getResources().getString(R.string.log_search_too_many));
                    newToast(
                        this,
                        null,
                        getString(
                            R.string.toast_search_results_too_many,
                            getResources().getInteger(R.integer.max_visible_memorials)
                        ),
                        Toast.LENGTH_SHORT
                    );
                }
                mMapFragment.clearMap();
                mSearchResultFragment.changeCursor(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mMapFragment.clearMap();
        mSearchResultFragment.changeCursor(null);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mConnected = true;
        Log.i(sLogTag, getString(R.string.log_google_api_client_connected));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
         || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mConnected = false;
        if (GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST == i){
            Log.i(sLogTag, getString(R.string.log_google_api_client_suspended_network_lost));
        }

        else if (GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED == i){
            Log.i(sLogTag, getString(R.string.log_google_api_client_suspended_service_disconnected));
        }

        else {
            Log.i(sLogTag, getString(R.string.log_google_api_client_suspended));
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mConnected = false;
        Log.e(sLogTag, getString(R.string.log_google_api_client_failed) + connectionResult.getErrorMessage());
    }

    @Override
    public void placeButtonClicked(@NonNull LatLng position){
        mMapFragment.zoomInOn(position);
    }

    @Override
    public void recordFinished(@NonNull SearchResultFragment.DataFormatter record){
        MarkerOptions options = new MarkerOptions();
        options.position(record.getLatLng());
        options.title(record.getGeomemorial());
        options.snippet(record.getResident());

        mMapFragment.addMarker(options);
    }

    @Override
    public void cursorFinished(){
        mMapFragment.updateCamera();
    }

    public static void startActivity(Context context){
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
    }
}
