package com.github.dstaflund.geomemorial.ui.fragment.about;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.dstaflund.geomemorial.R;

public class AboutFragmentPresenterImpl implements AboutFragmentPresenter {
    private static final String sXPosKey = "X_POS";
    private static final String sYPosKey = "Y_POS";

    private AboutFragmentView mView;

    public AboutFragmentPresenterImpl(@NonNull AboutFragmentView view){
        super();
        mView = view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        mView.setRetainInstance(true);
        mView.setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b) {
        mView.setRootView(i.inflate(R.layout.fragment_about, c, false));
        return mView.getRootView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        NestedScrollView nsv = mView.getNestedScrollView();
        if (nsv != null) {
            outState.putInt(sXPosKey, nsv.getScrollX());
            outState.putInt(sYPosKey, nsv.getScrollY());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedState) {
        if (savedState != null) {
            NestedScrollView nsv = mView.getNestedScrollView();
            if (nsv != null) {
                nsv.setScrollX(savedState.getInt(sXPosKey));
                nsv.setScrollY(savedState.getInt(sYPosKey));
            }
        }
    }
}
