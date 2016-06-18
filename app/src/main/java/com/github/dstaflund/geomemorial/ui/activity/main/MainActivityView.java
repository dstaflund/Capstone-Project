package com.github.dstaflund.geomemorial.ui.activity.main;

import android.content.Context;
import android.database.Cursor;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import com.google.android.gms.common.api.GoogleApiClient;

public interface MainActivityView {
    void clearMap();
    void setDisplayToast(boolean value);
    void setMapType(int value);
    void swapCursor(Cursor cursor);

    boolean isDisplayToast();

    Context getContext();
    DrawerLayout getDrawerLayout();
    GoogleApiClient getGoogleApiClient();
    NavigationView getNavigationView();
    SearchRecentSuggestions getSearchRecentSuggestions();
}
