package com.github.dstaflund.geomemorial.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.dstaflund.geomemorial.R;

public class AboutFragment extends Fragment {
    private static final String X_POS_KEY = "X_POS";
    private static final String Y_POS_KEY = "Y_POST";

    private NestedScrollView mScrollView;
    private View mRoot;

    public AboutFragment(){
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setRetainInstance(true);
        setHasOptionsMenu(false);
    }

    @Override
    @NonNull
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedState
    ) {
        mRoot = inflater.inflate(R.layout.fragment_about, container, false);
        mScrollView = (NestedScrollView) mRoot.findViewById(R.id.fragment_about_nested_scroll_view);
        return mRoot;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mScrollView != null) {
            outState.putInt(X_POS_KEY, mScrollView.getScrollX());
            outState.putInt(Y_POS_KEY, mScrollView.getScrollY());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedState) {
        super.onViewStateRestored(savedState);
        if (savedState != null) {
            mScrollView.setScrollX(savedState.getInt(X_POS_KEY));
            mScrollView.setScrollY(savedState.getInt(Y_POS_KEY));
        }
    }
}
