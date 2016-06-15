package com.github.dstaflund.geomemorial.ui.fragment.searchresult;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.PreferencesManager;
import com.github.dstaflund.geomemorial.receiver.CursorFinishedReceiver;
import com.github.dstaflund.geomemorial.receiver.RecordFinishedReceiver;
import com.github.dstaflund.geomemorial.ui.fragment.searchresultitem.SearchResultItem;
import com.github.dstaflund.geomemorial.ui.fragment.searchresultitem.SearchResultItemFormatter;
import com.github.dstaflund.geomemorial.ui.fragment.searchresultitem.SearchResultItemFragment;

public class SearchResultFragment extends Fragment{
    private static final String sCurrentItemKey = "currentItem";

    private SearchResultPagerAdapter mSearchResultPagerAdapter;
    private ViewPager mViewPager;
    private int mLastCurrentItem;

    public SearchResultFragment(){
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        Log.d("SearchResultFragment", "onCreate");
        super.onCreate(savedState);
        setRetainInstance(false);
        setHasOptionsMenu(false);
    }

    @Override
    @NonNull
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedState
    ) {
        Log.d("SearchResultFragment", "onCreateView");
        View mRoot = inflater.inflate(R.layout.fragment_search_result, container, false);
        mSearchResultPagerAdapter = new SearchResultPagerAdapter(getChildFragmentManager(), null);

        mViewPager = (ViewPager) mRoot.findViewById(R.id.pager);
        mViewPager.setAdapter(mSearchResultPagerAdapter);

        if (savedState != null){
            mLastCurrentItem = savedState.getInt(sCurrentItemKey);
        }

        return mRoot;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("SearchResultFragment", "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putInt(sCurrentItemKey, mViewPager.getCurrentItem());
        PreferencesManager.setLastViewPageItem(getContext(), mViewPager.getCurrentItem());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.d("SearchResultFragment", "onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }

    public void clearPager(){
        mViewPager.setAdapter(null);
        mViewPager.removeAllViews();
    }

    public void swapCursor(@Nullable Cursor value){
        mSearchResultPagerAdapter = new SearchResultPagerAdapter(getChildFragmentManager(), value);
        mViewPager.setAdapter(mSearchResultPagerAdapter);
        mSearchResultPagerAdapter.refreshUi();
        mViewPager.setCurrentItem(mLastCurrentItem == 0 ? PreferencesManager.getLastViewPageItem(getContext()) : mLastCurrentItem);
        mLastCurrentItem = 0;
        PreferencesManager.setLastViewPageItem(getContext(), 0);
    }

    //  Adaptation of http://tumble.mlcastle.net/post/25875136857/bridging-cursorloaders-and-viewpagers-on-android
    public class SearchResultPagerAdapter extends FragmentStatePagerAdapter {
        private Cursor mCursor;

        public SearchResultPagerAdapter(
            @NonNull FragmentManager fm,
            @Nullable Cursor cursor
        ) {
            super(fm);
            mCursor = cursor;
        }

        @Override
        public SearchResultItemFragment getItem(int position) {
            if (mCursor == null){
                return null;
            }

            mCursor.moveToPosition(position);
            return SearchResultItemFragment.newInstance(mCursor, position, mCursor.getCount());
        }

        @Override
        public int getCount() {
            if (mCursor == null){
                return 0;
            }

            int maxVisible = getContext().getResources().getInteger(R.integer.max_visible_memorials);
            return mCursor.getCount() <= maxVisible ? mCursor.getCount() : maxVisible;
        }

        @Override
        public void startUpdate(ViewGroup container) {
            super.startUpdate(container);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
        }

        public void refreshUi() {
            if (mCursor != null) {
                mCursor.moveToFirst();
                mCursor.moveToPrevious();
                while (mCursor.moveToNext() && mCursor.getPosition() < getContext().getResources().getInteger(R.integer.max_visible_memorials)) {
                    SearchResultItem dataObject = new SearchResultItem(mCursor);
                    SearchResultItemFormatter dataFormatter = new SearchResultItemFormatter(
                        getActivity(),
                        dataObject,
                        mCursor.getPosition(),
                        mCursor.getCount()
                    );
                    RecordFinishedReceiver.sendBroadcast(getContext(), dataFormatter);
                }
                mCursor.moveToFirst();
                mCursor.moveToPrevious();
            }
            CursorFinishedReceiver.sendBroadcast(getContext());
            notifyDataSetChanged();
        }
    }
}
