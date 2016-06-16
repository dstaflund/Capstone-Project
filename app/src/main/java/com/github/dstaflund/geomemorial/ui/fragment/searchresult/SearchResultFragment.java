package com.github.dstaflund.geomemorial.ui.fragment.searchresult;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.dstaflund.geomemorial.ui.fragment.searchresult.adapter.SearchResultFragmentAdapter;

public class SearchResultFragment extends Fragment implements SearchResultFragmentView {
    private SearchResultFragmentAdapter mSearchResultPagerAdapter;
    private ViewPager mViewPager;
    private int mLastCurrentItem;
    private SearchResultFragmentPresenter mPresenter;

    public SearchResultFragment(){
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        mPresenter = new SearchResultFragmentPresenterImpl(this);
        mPresenter.onCreate(savedState);
    }

    @Override
    @Nullable
    public View onCreateView(
        @NonNull LayoutInflater i,
        @Nullable ViewGroup c,
        @Nullable Bundle b
    ) {
        if (mPresenter != null){
            return mPresenter.onCreateView(i, c, b);
        }
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPresenter != null){
            mPresenter.onSaveInstanceState(outState);
        }
    }

    public void clearPager(){
        if (mPresenter != null){
            mPresenter.clearPager();
        }
    }

    public void swapCursor(@Nullable Cursor value){
        if (mPresenter != null){
            mPresenter.swapCursor(value);
        }
    }

    @Override
    public void setLastCurrentItem(int value) {
        mLastCurrentItem = value;
    }

    @Override
    public void setSearchResultFragmentAdapter(SearchResultFragmentAdapter value) {
        mSearchResultPagerAdapter = value;
    }

    @Override
    public void setViewPager(ViewPager value) {
        mViewPager = value;
    }

    @Override
    public ViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    public int getLastCurrentItem() {
        return mLastCurrentItem;
    }
}
