package com.github.dstaflund.geomemorial.ui.fragment.preferences;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.dstaflund.geomemorial.R;

public class PreferencesFragmentPresenterImpl implements PreferencesFragmentPresenter {
    private static final String sXPosKey = "X_POS";
    private static final String sYPosKey = "Y_POST";

    private PreferencesFragmentView mView;

    public PreferencesFragmentPresenterImpl(@NonNull PreferencesFragmentView view){
        super();
        mView = view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        mView.setRetainInstance(true);
        mView.setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup vg, @Nullable Bundle b) {
        View v = i.inflate(R.layout.fragment_preferences, vg, false);

        NestedScrollView scrollView = (NestedScrollView) v.findViewById(R.id.fragment_preferences_scroll_view);
        mView.setNestedScrollView(scrollView);

        Context c = mView.getContext();
        MapTypeOnCheckChangeListener l = new MapTypeOnCheckChangeListener(mView);
        PreferencesFragmentViewHolder h = new PreferencesFragmentViewHolder(v).initialize(c, l);
        v.setTag(h);

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        NestedScrollView v = mView.getNestedScrollView();
        if (v != null) {
            outState.putInt(sXPosKey, v.getScrollX());
            outState.putInt(sYPosKey, v.getScrollY());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedState) {
        NestedScrollView v = mView.getNestedScrollView();
        if (savedState != null) {
            v.setScrollX(savedState.getInt(sXPosKey));
            v.setScrollY(savedState.getInt(sYPosKey));
        }
    }
}
