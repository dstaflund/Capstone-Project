package com.github.dstaflund.geomemorial.ui.fragment.searchexamples;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SearchExamplesFragment extends Fragment implements SearchExamplesFragmentView {
    private SearchExamplesFragmentPresenter mPresenter;

    public SearchExamplesFragment(){
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        mPresenter = new SearchExamplesFragmentPresenterImpl(this);
        mPresenter.onCreate(savedState);
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b) {
        if (mPresenter != null) {
            return mPresenter.onCreateView(i, c, b);
        }
        return null;
    }
}
