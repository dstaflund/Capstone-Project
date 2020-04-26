package com.github.dstaflund.geomemorial.ui.fragment.searchresult.adapter;

import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.receiver.CursorFinishedReceiver;
import com.github.dstaflund.geomemorial.receiver.RecordFinishedReceiver;
import com.github.dstaflund.geomemorial.ui.fragment.searchresultitem.SearchResultItem;
import com.github.dstaflund.geomemorial.ui.fragment.searchresultitem.SearchResultItemFormatter;
import com.github.dstaflund.geomemorial.ui.fragment.searchresultitem.SearchResultItemFragment;


//  Adaptation of http://tumble.mlcastle.net/post/25875136857/bridging-cursorloaders-and-viewpagers-on-android
public class SearchResultFragmentAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private Cursor mCursor;

    public SearchResultFragmentAdapter(
        @NonNull FragmentManager fm,
        @NonNull Context context,
        @Nullable Cursor cursor
    ) {
        super(fm);
        mContext = context;
        mCursor = cursor;
    }

    @Override
    public SearchResultItemFragment getItem(int position) {
        if (mCursor == null) {
            return null;
        }

        mCursor.moveToPosition(position);
        return SearchResultItemFragment.newInstance(mCursor, position, mCursor.getCount());
    }

    @Override
    public int getCount() {
        if (mCursor == null) {
            return 0;
        }

        int maxVisible = mContext.getResources().getInteger(R.integer.max_visible_memorials);
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
            while (mCursor.moveToNext() && mCursor.getPosition() < mContext.getResources().getInteger(R.integer.max_visible_memorials)) {
                SearchResultItem dataObject = new SearchResultItem(mCursor);
                SearchResultItemFormatter dataFormatter = new SearchResultItemFormatter(
                    mContext,
                    dataObject,
                    mCursor.getPosition(),
                    mCursor.getCount()
                );
                RecordFinishedReceiver.sendBroadcast(mContext, dataFormatter);
            }
            mCursor.moveToFirst();
            mCursor.moveToPrevious();
        }
        CursorFinishedReceiver.sendBroadcast(mContext);
        notifyDataSetChanged();
    }
}
