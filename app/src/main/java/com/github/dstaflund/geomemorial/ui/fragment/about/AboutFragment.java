package com.github.dstaflund.geomemorial.ui.fragment.about;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutFragment extends Fragment implements AboutFragmentView {
    private AboutFragmentPresenter mPresenter;
    private NestedScrollView mScrollView;

    public AboutFragment(){
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        mPresenter = new AboutFragmentPresenterImpl(this);
        mPresenter.onCreate(savedState);
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b) {
        return mPresenter != null ? mPresenter.onCreateView(i, c, b) : null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPresenter != null) {
            mPresenter.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedState) {
        super.onViewStateRestored(savedState);
        if (mPresenter != null) {
            mPresenter.onViewStateRestored(savedState);
        }
    }

    @Nullable
    @Override
    public NestedScrollView getNestedScrollView() {
        return mScrollView;
    }

    @Override
    public void setNestedScrollView(@NonNull NestedScrollView v) {
        mScrollView = v;
    }
}
