package com.github.dstaflund.geomemorial.ui.activity.main;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.dstaflund.geomemorial.GeomemorialApplication;
import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbProvider;
import com.github.dstaflund.geomemorial.ui.activity.main.callback.MainConnectionCallbacks;
import com.github.dstaflund.geomemorial.ui.activity.main.callback.MainLoaderManagerCallbacks;
import com.github.dstaflund.geomemorial.ui.activity.main.listener.MainConnectionFailedListener;
import com.github.dstaflund.geomemorial.ui.activity.main.listener.NavigationItemSelectedListener;
import com.github.dstaflund.geomemorial.ui.fragment.map.MapFragment;
import com.github.dstaflund.geomemorial.ui.fragment.searchresult.SearchResultFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

public class MainActivity
    extends AppCompatActivity
    implements
    MainActivityView{

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
    private MainLoaderManagerCallbacks mLoaderCallbacks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("** MainActivity **", "onCreate");
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
                .addConnectionCallbacks(new MainConnectionCallbacks(this))
                .addOnConnectionFailedListener(new MainConnectionFailedListener())
                .addApi(LocationServices.API)
                .build();
        }

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(new NavigationItemSelectedListener(this));
        }

        checkInitialMapType();

        mSearchRecentSuggestions = new SearchRecentSuggestions(
            this,
            GeomemorialDbProvider.AUTHORITY,
            GeomemorialDbProvider.MODE
        );

        mLoaderCallbacks = new MainLoaderManagerCallbacks(this);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Cursor> loader = loaderManager.getLoader(EMPTY_SEARCH);
        if (loader != null && !loader.isReset()) {
            loaderManager.restartLoader(EMPTY_SEARCH, null, mLoaderCallbacks);
        } else {
            loaderManager.initLoader(EMPTY_SEARCH, null, mLoaderCallbacks);
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

        if (mLastSearchRequest != null){
            mMapFragment.ignoreCameraZoom(mLastSearchRequest != null);
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
            loaderManager.restartLoader(RESIDENT_LOADER_ID, args, mLoaderCallbacks);
        } else {
            loaderManager.initLoader(RESIDENT_LOADER_ID, args, mLoaderCallbacks);
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

    @Override
    public void clearMap() {
        mMapFragment.clearMap();
    }

    @Override
    public void setDisplayToast(boolean value) {
        mDisplayToast = value;
    }

    @Override
    public void swapCursor(Cursor cursor){
        mSearchResultFragment.swapCursor(cursor);
    }

    @Override
    public boolean isDisplayToast() {
        return mDisplayToast;
    }

    public void setMapType(int mapTypeId) {
        mMapFragment.setMapType(mapTypeId);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public DrawerLayout getDrawerLayout() {
        return (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    @Override
    public NavigationView getNavigationView() {
        return mNavigationView;
    }

    @Override
    public SearchRecentSuggestions getSearchRecentSuggestions() {
        return mSearchRecentSuggestions;
    }

    @Override
    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }
}
