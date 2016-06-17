package com.github.dstaflund.geomemorial.ui.fragment.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface FavoritesFragmentPresenter {
    void onCreate(Bundle savedState);
    void onSaveInstanceState(Bundle outState);

    View onCreateView(LayoutInflater i, ViewGroup c, Bundle b);
}
