package com.github.dstaflund.geomemorial.ui.activity.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.ui.activity.main.callback.MainLoaderManagerCallbacks;
import com.github.dstaflund.geomemorial.ui.fragment.map.MapFragment;
import com.github.dstaflund.geomemorial.ui.fragment.searchresult.SearchResultFragment;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity
    extends AppCompatActivity
    implements
    MainActivityView{
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
    private MainActivityPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MainActivityPresenterImpl(this);
        mPresenter.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter != null){
            mPresenter.onStart();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPresenter != null){
            mPresenter.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        if (mPresenter != null){
            mPresenter.onNewIntent(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        if (mPresenter != null){
            if (mPresenter.onCreateOptionsMenu(menu)){
                return true;
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (mPresenter != null){
            if (mPresenter.onBackPressed()){
                return;
            }
        }
        super.onBackPressed();
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
    public SearchManager getSystemService() {
        return (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    }

    @Override
    public void setSearchView(SearchView value) {
        mSearchView = value;
    }

    @Override
    public String getSavedSearchString() {
        return mSavedSearchString;
    }

    @Override
    public void setLastSearchRequest(SearchRequest currentSearchRequest) {
        mLastSearchRequest = currentSearchRequest;
    }

    @Override
    public MainLoaderManagerCallbacks getLeaderCallbacks() {
        return mLoaderCallbacks;
    }

    @Override
    public SearchRequest getLastSearchRequest() {
        return mLastSearchRequest;
    }

    @Override
    public MapFragment getMapFragment() {
        return mMapFragment;
    }

    @Override
    public SearchResultFragment getSearchResultFragment() {
        return mSearchResultFragment;
    }

    @Override
    public SearchView getSearchView() {
        return mSearchView;
    }

    @Override
    public void setMapFragment(MapFragment value) {
        mMapFragment = value;
    }

    @Override
    public void setSearchResultFragment(SearchResultFragment value) {
        mSearchResultFragment = value;
    }

    @Override
    public MapFragment getInitialMapFragment() {
        return (MapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
    }

    @Override
    public SearchResultFragment getInitialSearchResultFragment() {
        return (SearchResultFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_search_result);
    }

    @Override
    public Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    public void setGoogleApiClient(GoogleApiClient value) {
        mGoogleApiClient = value;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public NavigationView getInitialNavigationView() {
        return (NavigationView) findViewById(R.id.nav_view);
    }

    @Override
    public void setNavigationView(NavigationView value) {
        mNavigationView = value;
    }

    @Override
    public void setSearchRecentSuggestions(SearchRecentSuggestions value) {
        mSearchRecentSuggestions = value;
    }

    @Override
    public void setLoaderCallbacks(MainLoaderManagerCallbacks value) {
        mLoaderCallbacks = value;
    }

    @Override
    public void setSavedSearchString(String value) {
        mSavedSearchString = value;
    }

    @Override
    public void setSavedInstanceState(Bundle value) {
        mSavedInstanceState = value;
    }

    @Override
    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }
}
