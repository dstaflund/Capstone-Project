package com.github.dstaflund.geomemorial.ui.fragment.searchresult;

import android.content.Context;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.github.dstaflund.geomemorial.ui.fragment.searchresult.adapter.SearchResultFragmentAdapter;

public interface SearchResultFragmentView {
    void setHasOptionsMenu(boolean value);
    void setLastCurrentItem(int value);
    void setRetainInstance(boolean value);
    void setSearchResultFragmentAdapter(SearchResultFragmentAdapter value);
    void setViewPager(ViewPager value);

    int getLastCurrentItem();

    Context getContext();
    FragmentManager getChildFragmentManager();
    ViewPager getViewPager();

}
