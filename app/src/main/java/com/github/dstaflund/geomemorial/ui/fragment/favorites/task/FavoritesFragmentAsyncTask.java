package com.github.dstaflund.geomemorial.ui.fragment.favorites.task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.github.dstaflund.geomemorial.common.util.FavoritesMarkerInfo;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.Geomemorial;
import com.github.dstaflund.geomemorial.ui.fragment.favorites.FavoritesFragmentView;
import com.github.dstaflund.geomemorial.ui.fragment.favorites.FavoritesFragmentViewHolder;
import com.github.dstaflund.geomemorial.ui.fragment.favorites.adapter.FavoritesFragmentViewAdapter;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.getFavorites;
import static com.github.dstaflund.geomemorial.common.util.FavoritesMarkerInfo.getDataFormatters;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NONE;

public class FavoritesFragmentAsyncTask extends AsyncTask<Void, Void, List<FavoritesMarkerInfo>> {
    private static FavoritesFragmentView mView;

    public FavoritesFragmentAsyncTask(@NonNull FavoritesFragmentView view) {
        super();
        mView = view;
    }

    @Override
    @Nullable
    protected List<FavoritesMarkerInfo> doInBackground(@Nullable Void... params) {
        Context context = mView.getContext();
        if (context != null) {
            Set<String> ids = getFavorites(context);
            if (ids.isEmpty()) {
                return Collections.emptyList();
            }
            Cursor favorites = context.getContentResolver().query(
                Geomemorial.CONTENT_URI,
                Geomemorial.DEFAULT_PROJECTION,
                Geomemorial.getSelection(ids),
                Geomemorial.getSelectionArgs(ids),
                Geomemorial.FAVORITES_SORT_ORDER
            );
            return getDataFormatters(favorites);
        }
        return null;
    }

    @Override
    protected void onPostExecute(@Nullable List<FavoritesMarkerInfo> data) {
        Context context = mView.getContext();
        if (context != null) {
            ArrayAdapter<FavoritesMarkerInfo> adapter = new FavoritesFragmentViewAdapter(context, mView.getLayoutInflater(null), data);
            mView.setAdapter(adapter);

            GridView gridView = mView.getGridView();
            if (gridView == null) {
                gridView = mView.findGridView();
                gridView.setAdapter(adapter);
                gridView.setEmptyView(mView.findEmptyView());
                gridView.setRecyclerListener(new AbsListView.RecyclerListener() {

                    @Override
                    public void onMovedToScrapHeap(@NonNull View view) {
                        FavoritesFragmentViewHolder holder = (FavoritesFragmentViewHolder) view.getTag();
                        if (holder != null && holder.map != null) {
                            holder.map.clear();
                            holder.map.setMapType(MAP_TYPE_NONE);
                        }
                    }
                });
                mView.setGridView(gridView);
            } else {
                gridView.setAdapter(null);
                gridView.setAdapter(adapter);
            }

            int firstVisiblePosition = mView.getFirstVisiblePosition();
            if (firstVisiblePosition != -1) {
                gridView.setSelection(firstVisiblePosition);
            }
        }
    }
}
