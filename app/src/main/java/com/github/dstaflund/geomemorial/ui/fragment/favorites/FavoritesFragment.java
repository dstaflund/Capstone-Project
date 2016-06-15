package com.github.dstaflund.geomemorial.ui.fragment.favorites;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.FavoritesMarkerInfo;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.Geomemorial;
import com.github.dstaflund.geomemorial.ui.fragment.favorites.adapter.FavoritesFragmentViewAdapter;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.getFavorites;
import static com.github.dstaflund.geomemorial.common.util.FavoritesMarkerInfo.getDataFormatters;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NONE;

public class FavoritesFragment extends Fragment{
    public static final Integer GEOMEMORIAL_TAG_KEY = R.integer.geomemorial_tag_key;

    private GridView mList;
    private ArrayAdapter<FavoritesMarkerInfo> mAdapter;

    private int mFirstVisiblePosition;
    private View mRoot;

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
        mRoot = inflater.inflate(R.layout.fragment_favorites, container, false);

        mFirstVisiblePosition = savedState == null
            ? -1
            : savedState.getInt("first_visible_position");

        new FavoritesFragmentAsyncTask().execute();

        return mRoot;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (outState != null) {
            outState.putInt("first_visible_position", mList.getFirstVisiblePosition());
        }
    }

    public class FavoritesFragmentAsyncTask extends AsyncTask<Void, Void, List<FavoritesMarkerInfo>> {

        @Override
        @NonNull
        protected List<FavoritesMarkerInfo> doInBackground(@Nullable Void... params) {
            Set<String> ids = getFavorites(getContext());
            if (ids.isEmpty()) {
                return Collections.emptyList();
            }
            Cursor favorites = getContext().getContentResolver().query(
                Geomemorial.CONTENT_URI,
                Geomemorial.DEFAULT_PROJECTION,
                Geomemorial.getSelection(ids),
                Geomemorial.getSelectionArgs(ids),
                Geomemorial.FAVORITES_SORT_ORDER
            );
            return getDataFormatters(favorites);
        }

        @Override
        protected void onPostExecute(@Nullable List<FavoritesMarkerInfo> data) {
            mAdapter = new FavoritesFragmentViewAdapter(getContext(), getLayoutInflater(null), data);

            if (mList == null) {
                mList = (GridView) mRoot.findViewById(android.R.id.list);
                mList.setAdapter(mAdapter);
                mList.setEmptyView(mRoot.findViewById(R.id.empty_favorites));
                mList.setRecyclerListener(new AbsListView.RecyclerListener() {

                    @Override
                    public void onMovedToScrapHeap(@NonNull View view) {
                        FavoritesFragmentViewHolder holder = (FavoritesFragmentViewHolder) view.getTag();
                        if (holder != null && holder.map != null) {
                            holder.map.clear();
                            holder.map.setMapType(MAP_TYPE_NONE);
                        }
                    }
                });
            }
            else {
                mList.setAdapter(null);
                mList.setAdapter(mAdapter);
            }

            if (mFirstVisiblePosition != -1){
                mList.setSelection(mFirstVisiblePosition);
            }
        }
    }
}
