package com.github.dstaflund.geomemorial.ui.fragment.searchresult;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface SearchResultFragmentPresenter {
    void clearPager();
    void onCreate(Bundle savedState);
    void onSaveInstanceState(Bundle outState);
    void swapCursor(Cursor value);

    View onCreateView(LayoutInflater i, ViewGroup c, Bundle b);
}
