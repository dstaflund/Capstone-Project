package com.github.dstaflund.geomemorial.ui.fragment.favorites;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.github.dstaflund.geomemorial.common.util.FavoritesMarkerInfo;

public interface FavoritesFragmentView {
    void setAdapter(ArrayAdapter<FavoritesMarkerInfo> value);
    void setFirstVisiblePosition(int value);
    void setGridView(GridView value);
    void setHasOptionsMenu(boolean value);
    void setRetainInstance(boolean value);
    void setViewRoot(View value);

    int getFirstVisiblePosition();

    Context getContext();
    GridView findGridView();
    GridView getGridView();
    LayoutInflater getLayoutInflater(Bundle b);
    View findEmptyView();

}
