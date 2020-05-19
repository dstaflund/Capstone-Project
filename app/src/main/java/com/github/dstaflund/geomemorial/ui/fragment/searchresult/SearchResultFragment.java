package com.github.dstaflund.geomemorial.ui.fragment.searchresult;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.PreferencesManager;
import com.github.dstaflund.geomemorial.ui.fragment.searchresult.adapter.SearchResultFragmentAdapter;

public class SearchResultFragment extends Fragment {
    private static final String sCurrentItemKey = "currentItem";

    private SearchResultFragmentAdapter mSearchResultPagerAdapter;
    private ViewPager mViewPager;
    private int mLastCurrentItem;

    public SearchResultFragment(){
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setRetainInstance(false);
        setHasOptionsMenu(false);
    }

    @Override
    @Nullable
    public View onCreateView(
        @NonNull LayoutInflater i,
        @Nullable ViewGroup c,
        @Nullable Bundle b
    ) {
        View mRoot = i.inflate(R.layout.fragment_search_result, c, false);

        Context ctx = getContext();
        if (ctx == null){
            return mRoot;
        }

        SearchResultFragmentAdapter adapter = new SearchResultFragmentAdapter(
                getChildFragmentManager(),
                getContext(),
                null
        );
        ViewPager pager = mRoot.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        if (b != null){
            mLastCurrentItem = b.getInt(sCurrentItemKey);
        }

        mSearchResultPagerAdapter = adapter;
        mViewPager = pager;
        return mRoot;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        ViewPager pager = mViewPager;
        if (pager != null) {
            outState.putInt(sCurrentItemKey, pager.getCurrentItem());
            if (getContext() != null) {
                PreferencesManager.setLastViewPageItem(getContext(), pager.getCurrentItem());
            }
        }
    }

    public void clearPager(){
        ViewPager pager = mViewPager;
        if (pager != null) {
            pager.setAdapter(null);
            pager.removeAllViews();
        }
    }

    public void swapCursor(@Nullable Cursor value){
        int lastCurrentItem = mLastCurrentItem;
        Context c = getContext();
        FragmentManager fm = getChildFragmentManager();
        ViewPager vp = mViewPager;

        SearchResultFragmentAdapter a = new SearchResultFragmentAdapter(fm, c, value);
        mSearchResultPagerAdapter = a;

        vp.setAdapter(a);
        a.refreshUi();
        vp.setCurrentItem(lastCurrentItem == 0 ? PreferencesManager.getLastViewPageItem(c) : lastCurrentItem);
        mLastCurrentItem = 0;
        PreferencesManager.setLastViewPageItem(c, 0);
    }
}
