package com.github.dstaflund.geomemorial.ui.fragment.favorites;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.FavoritesMarkerInfo;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract;
import com.github.dstaflund.geomemorial.ui.fragment.favorites.adapter.FavoritesFragmentViewAdapter;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.getFavorites;
import static com.github.dstaflund.geomemorial.common.util.FavoritesMarkerInfo.getDataFormatters;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NONE;

public class FavoritesFragment extends Fragment {
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
    @Nullable
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b) {
        View root = i.inflate(R.layout.fragment_favorites, c, false);

        setFirstVisiblePosition(b == null ? -1 : b.getInt("first_visible_position"));
        setViewRoot(root);

        new FavoritesFragmentAsyncTask(this).execute();

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mList != null) {
            outState.putInt("first_visible_position", mList.getFirstVisiblePosition());
        }
    }

    private void setAdapter(ArrayAdapter<FavoritesMarkerInfo> value){
        mAdapter = value;
    }

    private GridView getGridView(){
        return mList;
    }

    private GridView findGridView(){
        return (GridView) mRoot.findViewById(android.R.id.list);
    }

    private View findEmptyView(){
        return mRoot.findViewById(R.id.empty_favorites);
    }

    private void setViewRoot(View value) {
        mRoot = value;
    }

    private void setFirstVisiblePosition(int value) {
        mFirstVisiblePosition = value;
    }

    private int getFirstVisiblePosition(){
        return mFirstVisiblePosition;
    }

    private void setGridView(GridView value){
        mList = value;
    }

    public static class FavoritesFragmentAsyncTask extends AsyncTask<Void, Void, List<FavoritesMarkerInfo>> {
        private FavoritesFragment mView;

        FavoritesFragmentAsyncTask(@NonNull FavoritesFragment view) {
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
                        GeomemorialDbContract.Geomemorial.CONTENT_URI,
                        GeomemorialDbContract.Geomemorial.DEFAULT_PROJECTION,
                        GeomemorialDbContract.Geomemorial.getSelection(ids),
                        GeomemorialDbContract.Geomemorial.getSelectionArgs(ids),
                        GeomemorialDbContract.Geomemorial.FAVORITES_SORT_ORDER
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
}
