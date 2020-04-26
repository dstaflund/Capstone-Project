package com.github.dstaflund.geomemorial.ui.fragment.searchresult;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.PreferencesManager;
import com.github.dstaflund.geomemorial.ui.fragment.searchresult.adapter.SearchResultFragmentAdapter;

public class SearchResultFragmentPresenterImpl implements SearchResultFragmentPresenter {
    private static final String sCurrentItemKey = "currentItem";

    private SearchResultFragmentView mView;

    public SearchResultFragmentPresenterImpl(@NonNull SearchResultFragmentView view){
        super();
        mView = view;
    }

    @Override
    public void onCreate(Bundle savedState) {
        mView.setRetainInstance(false);
        mView.setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup c, Bundle b) {
        View mRoot = i.inflate(R.layout.fragment_search_result, c, false);

        SearchResultFragmentAdapter adapter = new SearchResultFragmentAdapter(
            mView.getChildFragmentManager(),
            mView.getContext(),
            null
        );
        ViewPager pager = (ViewPager) mRoot.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        if (b != null){
            mView.setLastCurrentItem(b.getInt(sCurrentItemKey));
        }

        mView.setSearchResultFragmentAdapter(adapter);
        mView.setViewPager(pager);
        return mRoot;
    }

    @Override
    public void clearPager() {
        ViewPager pager = mView.getViewPager();
        if (pager != null) {
            pager.setAdapter(null);
            pager.removeAllViews();
        }
    }

    @Override
    public void swapCursor(Cursor value) {
        int lastCurrentItem = mView.getLastCurrentItem();
        Context c = mView.getContext();
        FragmentManager fm = mView.getChildFragmentManager();
        ViewPager vp = mView.getViewPager();

        SearchResultFragmentAdapter a = new SearchResultFragmentAdapter(fm, c, value);
        mView.setSearchResultFragmentAdapter(a);

        vp.setAdapter(a);
        a.refreshUi();
        vp.setCurrentItem(lastCurrentItem == 0 ? PreferencesManager.getLastViewPageItem(c) : lastCurrentItem);
        mView.setLastCurrentItem(0);
        PreferencesManager.setLastViewPageItem(c, 0);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ViewPager pager = mView.getViewPager();
        if (pager != null) {
            outState.putInt(sCurrentItemKey, pager.getCurrentItem());
            PreferencesManager.setLastViewPageItem(mView.getContext(), pager.getCurrentItem());
        }
    }
}
