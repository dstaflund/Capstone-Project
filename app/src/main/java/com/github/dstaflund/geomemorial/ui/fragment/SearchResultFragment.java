package com.github.dstaflund.geomemorial.ui.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.receiver.CursorFinishedReceiver;
import com.github.dstaflund.geomemorial.receiver.RecordFinishedReceiver;

public class SearchResultFragment extends Fragment{
    private static final String sCurrentItemKey = "currentItem";

    private SearchResultPagerAdapter mSearchResultPagerAdapter;
    private ViewPager mViewPager;
    private int mLastCurrentItem;

    public SearchResultFragment(){
        super();
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
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
        super.onSaveInstanceState(outState);
        outState.putInt(sCurrentItemKey, mViewPager.getCurrentItem());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    public void clearPager(){
        mViewPager.setAdapter(null);
        mViewPager.removeAllViews();
    }

    public void swapCursor(@Nullable Cursor value){
        mSearchResultPagerAdapter = new SearchResultPagerAdapter(getChildFragmentManager(), value);
        mViewPager.setAdapter(mSearchResultPagerAdapter);
        mSearchResultPagerAdapter.refreshUi();
        mViewPager.setCurrentItem(mLastCurrentItem);
        mLastCurrentItem = 0;
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
                    SearchResultItemFragment.DataObject dataObject = new SearchResultItemFragment.DataObject(mCursor);
                    SearchResultItemFragment.DataFormatter dataFormatter = new SearchResultItemFragment.DataFormatter(
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
