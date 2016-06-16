package com.github.dstaflund.geomemorial.ui.fragment.searchresultitem;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SearchResultItemFragment extends Fragment implements SearchResultItemFragmentView {
    private SearchResultItemFragmentPresenter mPresenter;

    public SearchResultItemFragment(){
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        mPresenter = new SearchResultItemFragmentPresenterImpl(this);
        mPresenter.onCreate(savedState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b) {
        if (mPresenter != null){
            return mPresenter.onCreateView(i, c, b);
        }
        return null;
    }

    @NonNull
    public static SearchResultItemFragment newInstance(@NonNull Cursor c, int position, int count){
        return SearchResultItemFragmentPresenterImpl.newInstance(c, position, count);
    }
}
