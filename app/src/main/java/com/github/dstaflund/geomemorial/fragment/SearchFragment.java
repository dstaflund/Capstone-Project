package com.github.dstaflund.geomemorial.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.dstaflund.geomemorial.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private static final String sLogTag = SearchFragment.class.getSimpleName();

    public SearchFragment() {
        super();
        Log.i(sLogTag, "<ctor>");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(sLogTag, "onCreateView");
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}
