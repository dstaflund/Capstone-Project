package com.github.dstaflund.geomemorial.ui.activity.main;

import android.content.Context;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

public interface MainActivityView {
    void setMapType(int value);

    Context getContext();
    DrawerLayout getDrawerLayout();
    NavigationView getNavigationView();
    SearchRecentSuggestions getSearchRecentSuggestions();
}
