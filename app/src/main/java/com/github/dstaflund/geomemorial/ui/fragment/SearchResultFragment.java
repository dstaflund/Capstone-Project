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
import com.google.android.gms.maps.model.LatLng;

public class SearchResultFragment extends Fragment
    implements SearchResultItemFragment.OnPlaceButtonClickedListener{
    private static final String sLogTag = SearchResultFragment.class.getSimpleName();
    private static final String sCurrentItemKey = "currentItem";

    private SearchResultPagerAdapter mSearchResultPagerAdapter;
    private SearchResultFragment.OnChangeCursorListener mOnChangeCursorListener;
    private SearchResultItemFragment.OnPlaceButtonClickedListener mOnPlaceButtonClickedListener;
    private ViewPager mViewPager;
    private boolean mReturnToLastPage;
    private int mLastCurrentItem;

    public SearchResultFragment(){
        super();
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);

        try {
            mOnChangeCursorListener = (SearchResultFragment.OnChangeCursorListener) context;
            mOnPlaceButtonClickedListener = (SearchResultItemFragment.OnPlaceButtonClickedListener) context;
        }

        catch(Exception e){
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setRetainInstance(false);
        setHasOptionsMenu(false);
    }

    @Override
    public void onPause() {
        super.onPause();
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
        mViewPager.setAdapter(null);
        mViewPager.removeAllViews();
    }

    public void returnToLastPage(boolean value){
        mReturnToLastPage = value;
    }

    public void swapCursor(@Nullable Cursor value){
        mSearchResultPagerAdapter.swapCursor(value);
        mViewPager.setAdapter(mSearchResultPagerAdapter);
        mViewPager.setCurrentItem(mLastCurrentItem);
        mReturnToLastPage = false;
    }

    public void restoreLastVisiblePage(){
        if (mReturnToLastPage){
//            mViewPager.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mViewPager.setCurrentItem(mLastCurrentItem);
//            mReturnToLastPage = false;
//                }
//            }, 10);
        }
    }

    @Override
    public void placeButtonClicked(@NonNull LatLng position) {
        mOnPlaceButtonClickedListener.placeButtonClicked(position);
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

        public void swapCursor(@Nullable Cursor c) {
            if (mCursor != c) {
                if (c != null) {
                    while (c.moveToNext() && c.getPosition() < getContext().getResources().getInteger(R.integer.max_visible_memorials)) {
                        SearchResultItemFragment.DataObject dataObject = new SearchResultItemFragment.DataObject(c);
                        SearchResultItemFragment.DataFormatter dataFormatter = new SearchResultItemFragment.DataFormatter(
                            getActivity(),
                            dataObject,
                            c.getPosition(),
                            c.getCount()
                        );
                        mOnChangeCursorListener.recordFinished(dataFormatter);
                    }
                    c.moveToFirst();
                    c.moveToPrevious();
                }
                mCursor = c;
                notifyDataSetChanged();
            }

            mOnChangeCursorListener.cursorFinished();
        }
    }

    public interface OnChangeCursorListener {
        void recordFinished(@NonNull SearchResultItemFragment.DataFormatter record);
        void cursorFinished();
    }
}
