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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.dstaflund.geomemorial.R;
import com.google.android.gms.maps.model.LatLng;

public class SearchResultFragment extends Fragment
    implements SearchResultItemFragment.OnPlaceButtonClickedListener{
    private static final String sLogTag = SearchResultFragment.class.getSimpleName();

    private SearchResultPagerAdapter mSearchResultPagerAdapter;
    private SearchResultFragment.OnChangeCursorListener mOnChangeCursorListener;
    private SearchResultItemFragment.OnPlaceButtonClickedListener mOnPlaceButtonClickedListener;
    private View mRoot;
    private ViewPager mViewPager;

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
            Log.e(sLogTag, context.getString(R.string.log_search_result_bad_activity));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setRetainInstance(true);
        setHasOptionsMenu(false);
    }

    @Override
    @NonNull
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedState
    ) {
        mRoot = inflater.inflate(R.layout.fragment_search_result, container, false);
        mSearchResultPagerAdapter = new SearchResultPagerAdapter(getChildFragmentManager(), null);

        mViewPager = (ViewPager) mRoot.findViewById(R.id.pager);
        mViewPager.setAdapter(mSearchResultPagerAdapter);

        return mRoot;
    }

    public void swapCursor(@Nullable Cursor value){
        mSearchResultPagerAdapter.swapCursor(value);
        mViewPager.setAdapter(mSearchResultPagerAdapter);
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
