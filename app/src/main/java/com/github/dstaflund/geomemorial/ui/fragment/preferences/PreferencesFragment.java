package com.github.dstaflund.geomemorial.ui.fragment.preferences;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PreferencesFragment extends Fragment implements PreferencesFragmentView {
    private NestedScrollView mScrollView;
    private PreferencesFragmentPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        mPresenter = new PreferencesFragmentPresenterImpl(this);
        mPresenter.onCreate(savedState);
    }

    @Override
    @Nullable
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedState
    ) {
        return mPresenter != null
            ? mPresenter.onCreateView(inflater, container, savedState)
            : null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPresenter != null){
            mPresenter.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedState) {
        super.onViewStateRestored(savedState);
        if (mPresenter != null){
            mPresenter.onViewStateRestored(savedState);
        }
    }

    @Override
    public void setNestedScrollView(@Nullable NestedScrollView value) {
        mScrollView = value;
    }

    @Override
    public NestedScrollView getNestedScrollView() {
        return mScrollView;
    }
}
