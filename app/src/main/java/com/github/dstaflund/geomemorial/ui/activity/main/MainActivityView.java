package com.github.dstaflund.geomemorial.ui.activity.main;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;

import com.github.dstaflund.geomemorial.ui.activity.main.callback.MainLoaderManagerCallbacks;
import com.github.dstaflund.geomemorial.ui.fragment.map.MapFragment;
import com.github.dstaflund.geomemorial.ui.fragment.searchresult.SearchResultFragment;
import com.google.android.gms.common.api.GoogleApiClient;

public interface MainActivityView {
    void clearMap();
    void setContentView(int activity_main);
    void setDisplayToast(boolean value);
    void setGoogleApiClient(GoogleApiClient value);
    void setLastSearchRequest(SearchRequest currentSearchRequest);
    void setLoaderCallbacks(MainLoaderManagerCallbacks value);
    void setMapFragment(MapFragment value);
    void setMapType(int value);
    void setNavigationView(NavigationView value);
    void setSavedInstanceState(Bundle value);
    void setSavedSearchString(String value);
    void setSearchRecentSuggestions(SearchRecentSuggestions value);
    void setSearchResultFragment(SearchResultFragment value);
    void setSearchView(SearchView value);
    void setSupportActionBar(Toolbar toolbar);
    void swapCursor(Cursor cursor);

    boolean isDisplayToast();

    Activity getActivity();
    ComponentName getComponentName();
    Context getContext();
    DrawerLayout getDrawerLayout();
    GoogleApiClient getGoogleApiClient();
    Intent getIntent();
    LoaderManager getSupportLoaderManager();
    MainLoaderManagerCallbacks getLeaderCallbacks();
    MapFragment getInitialMapFragment();
    MapFragment getMapFragment();
    MenuInflater getMenuInflater();
    NavigationView getInitialNavigationView();
    NavigationView getNavigationView();
    SearchManager getSystemService();
    SearchRecentSuggestions getSearchRecentSuggestions();
    SearchRequest getLastSearchRequest();
    SearchResultFragment getInitialSearchResultFragment();
    SearchResultFragment getSearchResultFragment();
    SearchView getSearchView();
    String getSavedSearchString();
    Toolbar getToolbar();
}
