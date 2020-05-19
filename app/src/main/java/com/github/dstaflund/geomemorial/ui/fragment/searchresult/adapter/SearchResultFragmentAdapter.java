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
    @Nullable private Context mContext;
    @Nullable private Cursor mCursor;

    public SearchResultFragmentAdapter(
        @NonNull FragmentManager fm,
        @Nullable Context context,
        @Nullable Cursor cursor
    ) {
        super(fm);
        mContext = context;
        mCursor = cursor;
    }

    @NonNull
    @Override
    public SearchResultItemFragment getItem(int position) {
        if (mCursor != null) {
            mCursor.moveToPosition(position);
        }
        return SearchResultItemFragment.newInstance(mCursor, position, mCursor != null ? mCursor.getCount() : 0);
    }

    @Override
    public int getCount() {
        int maxVisible = 0;
        if (mContext != null) {
            maxVisible = mContext.getResources().getInteger(R.integer.max_visible_memorials);
        }
        if (mCursor != null) {
            return Math.min(mCursor.getCount(), maxVisible);
        }
        return 0;
    }

    @Override
    public void startUpdate(@NonNull ViewGroup container) {
        super.startUpdate(container);
    }

    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        super.finishUpdate(container);
    }

    public void refreshUi() {
        if (mCursor != null) {
            mCursor.moveToFirst();
            mCursor.moveToPrevious();
            if (mContext != null) {
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
            }
            mCursor.moveToFirst();
            mCursor.moveToPrevious();
        }

        if (mContext != null) {
            CursorFinishedReceiver.sendBroadcast(mContext);
        }
        notifyDataSetChanged();
    }
}
