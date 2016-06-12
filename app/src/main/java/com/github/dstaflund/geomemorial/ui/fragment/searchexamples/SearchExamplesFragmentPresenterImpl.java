package com.github.dstaflund.geomemorial.ui.fragment.searchexamples;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.dstaflund.geomemorial.R;

public class SearchExamplesFragmentPresenterImpl implements SearchExamplesFragmentPresenter {
    private SearchExamplesFragmentView mView;

    public SearchExamplesFragmentPresenterImpl(@NonNull SearchExamplesFragmentView view){
        super();
        mView = view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        if (mView != null) {
            mView.setRetainInstance(true);
            mView.setHasOptionsMenu(false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b) {
        return i.inflate(R.layout.fragment_search_examples, c, false);
    }
}
