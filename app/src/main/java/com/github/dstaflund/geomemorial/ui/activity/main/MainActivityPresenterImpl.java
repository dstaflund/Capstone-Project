package com.github.dstaflund.geomemorial.ui.activity.main;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.github.dstaflund.geomemorial.GeomemorialApplication;
import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbProvider;
import com.github.dstaflund.geomemorial.ui.activity.main.callback.MainConnectionCallbacks;
import com.github.dstaflund.geomemorial.ui.activity.main.callback.MainLoaderManagerCallbacks;
import com.github.dstaflund.geomemorial.ui.activity.main.listener.MainConnectionFailedListener;
import com.github.dstaflund.geomemorial.ui.activity.main.listener.NavigationItemSelectedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

public class MainActivityPresenterImpl implements MainActivityPresenter {
    public static final int EMPTY_SEARCH = -1;
    public static final int RESIDENT_LOADER_ID = 0;
    public static final String LAST_SEARCH_REQUEST_KEY = "lastSearchRequest";
    public static final String SAVED_SEARCH_KEY = "savedSearch";

    private MainActivityView mView;

    public MainActivityPresenterImpl(@NonNull MainActivityView view){
        super();
        mView = view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mView.setContentView(R.layout.activity_main);
        mView.setMapFragment(mView.getInitialMapFragment());
        mView.setSearchResultFragment(mView.getInitialSearchResultFragment());
        mView.setSupportActionBar(mView.getToolbar());

        DrawerLayout drawer = mView.getDrawerLayout();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            mView.getActivity(),
            drawer,
            mView.getToolbar(),
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        );
        if (drawer != null) {
            //noinspection deprecation
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();

        // Create an instance of GoogleAPIClient.
        GoogleApiClient googleApiClient = mView.getGoogleApiClient();
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(mView.getContext())
                .addConnectionCallbacks(new MainConnectionCallbacks(mView))
                .addOnConnectionFailedListener(new MainConnectionFailedListener())
                .addApi(LocationServices.API)
                .build();
            mView.setGoogleApiClient(googleApiClient);
        }

        mView.setNavigationView(mView.getInitialNavigationView());
        if (mView.getNavigationView() != null) {
            mView.getNavigationView().setNavigationItemSelectedListener(
                new NavigationItemSelectedListener(mView)
            );
        }

        checkInitialMapType();

        SearchRecentSuggestions recentSuggestions = new SearchRecentSuggestions(
            mView.getContext(),
            GeomemorialDbProvider.AUTHORITY,
            GeomemorialDbProvider.MODE
        );
        mView.setSearchRecentSuggestions(recentSuggestions);

        mView.setLoaderCallbacks(new MainLoaderManagerCallbacks(mView));

        LoaderManager loaderManager = mView.getSupportLoaderManager();
        Loader<Cursor> loader = loaderManager.getLoader(EMPTY_SEARCH);
        if (loader != null && !loader.isReset()) {
            loaderManager.restartLoader(EMPTY_SEARCH, null, mView.getLeaderCallbacks());
        } else {
            loaderManager.initLoader(EMPTY_SEARCH, null, mView.getLeaderCallbacks());
        }

        // Restore the last search if present
        if (savedInstanceState != null) {
            mView.setSavedSearchString(savedInstanceState.getString(MainActivityPresenterImpl.SAVED_SEARCH_KEY));
            mView.setLastSearchRequest(
                (SearchRequest) savedInstanceState.getParcelable(MainActivityPresenterImpl.LAST_SEARCH_REQUEST_KEY)
            );
        }

        // If there was a previous search, then don't display toast otherwise do so
        mView.setDisplayToast(mView.getLastSearchRequest() == null);

        // If there was a previous search, then don't zoom camera, otherwise do so
        mView.getMapFragment().ignoreCameraZoom(mView.getLastSearchRequest() != null);

        mView.setSavedInstanceState(savedInstanceState);
    }

    private void checkInitialMapType() {
        switch (GeomemorialApplication.getVisibleMapType()) {
            case GoogleMap.MAP_TYPE_NORMAL:
                mView.getNavigationView().getMenu().findItem(R.id.nav_layer_normal).setChecked(true);
                break;

            case GoogleMap.MAP_TYPE_TERRAIN:
                mView.getNavigationView().getMenu().findItem(R.id.nav_layer_terrain).setChecked(true);
                break;

            case GoogleMap.MAP_TYPE_SATELLITE:
                mView.getNavigationView().getMenu().findItem(R.id.nav_layer_satellite).setChecked(true);
                break;

            case GoogleMap.MAP_TYPE_HYBRID:
                mView.getNavigationView().getMenu().findItem(R.id.nav_layer_hybrid).setChecked(true);
                break;
        }
    }


    @Override
    public void onStart() {
        mView.getGoogleApiClient().connect();

        SearchRequest lastSearchRequest = mView.getLastSearchRequest();
        if (lastSearchRequest != null){
            mView.getMapFragment().ignoreCameraZoom(lastSearchRequest != null);
            mView.getSearchResultFragment().clearPager();
            handleIntent(lastSearchRequest.toIntent());
        }
        else {
            handleIntent(mView.getIntent());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SearchView searchView = mView.getSearchView();
        if (searchView != null) {
            outState.putParcelable(LAST_SEARCH_REQUEST_KEY, mView.getLastSearchRequest());
            outState.putString(SAVED_SEARCH_KEY, searchView.getQuery().toString());
        }
    }

    @Override
    public boolean onBackPressed() {
        DrawerLayout drawer = mView.getDrawerLayout();
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mView.getMenuInflater().inflate(R.menu.menu_activity_main, menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager searchManager = mView.getSystemService();
            ComponentName componentName = mView.getComponentName();
            SearchableInfo searchableInfo = searchManager.getSearchableInfo(componentName);

            MenuItem searchMenu = menu.findItem(R.id.search);
            SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchMenu);
            mSearchView.setSearchableInfo(searchableInfo);
            mSearchView.setIconifiedByDefault(false);

            String savedSearchString = mView.getSavedSearchString();
            if (savedSearchString != null && ! savedSearchString.isEmpty()) {
                searchMenu.expandActionView();
                mSearchView.setQuery(savedSearchString, true);
                mSearchView.clearFocus();
            }

            mView.setSearchView(mSearchView);
            return true;
        }

        return false;
    }

    @Override
    public void onNewIntent(Intent intent) {
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
            SearchRecentSuggestions suggestions = mView.getSearchRecentSuggestions();
            if (suggestions != null) {
                suggestions.saveRecentQuery(searchRequest.getExtraDataKey(), null);
            }
            doSearch(searchRequest);
        }
    }

    private void doSearch(@NonNull SearchRequest currentSearchRequest) {
        Bundle args = currentSearchRequest.toBundle();
        mView.setLastSearchRequest(currentSearchRequest);

        LoaderManager loaderManager = mView.getSupportLoaderManager();
        Loader<Cursor> loader = loaderManager.getLoader(RESIDENT_LOADER_ID);

        if (loader != null && !loader.isReset()) {
            loaderManager.restartLoader(RESIDENT_LOADER_ID, args, mView.getLeaderCallbacks());
        }
        else {
            loaderManager.initLoader(RESIDENT_LOADER_ID, args, mView.getLeaderCallbacks());
        }
    }

}
