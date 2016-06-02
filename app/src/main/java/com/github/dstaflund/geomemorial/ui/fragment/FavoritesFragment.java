package com.github.dstaflund.geomemorial.ui.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.github.dstaflund.geomemorial.GeomemorialApplication;
import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.CameraUpdateStrategy;
import com.github.dstaflund.geomemorial.common.util.FavoritesMarkerInfo;
import com.github.dstaflund.geomemorial.common.util.SharedIntentManager;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.Geomemorial;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.dstaflund.geomemorial.common.util.CameraUpdateStrategy.setMapLocation;
import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.addFavorite;
import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.getFavorites;
import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.isFavorite;
import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.removeFavorite;
import static com.github.dstaflund.geomemorial.common.util.FavoritesMarkerInfo.getDataFormatters;
import static com.github.dstaflund.geomemorial.common.util.SharedIntentManager.shareGeomemorial;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.IDX_GEOMEMORIAL;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.IDX_HOMETOWN;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.IDX_LATITUDE;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.IDX_LONGITUDE;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.IDX_OBIT;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.IDX_RANK;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.IDX_RESIDENT;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.buildFor;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NONE;
import static com.google.android.gms.maps.MapsInitializer.initialize;

public class FavoritesFragment extends Fragment{
    private ListFragment mList;
    private ArrayAdapter<FavoritesMarkerInfo> mAdapter;

    private int mFirstVisiblePosition;
    private int mTop;
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

        mFirstVisiblePosition = savedState == null ? -1 : savedState.getInt("first_visible_position");
        mTop = savedState == null ? -1 : savedState.getInt("top");

        Log.d("FavoritesFragment", "First Visible Position = " + mFirstVisiblePosition);
        Log.d("FavoritesFragment", "Top = " + mTop);

        new AsyncTask<Void, Void, List<FavoritesMarkerInfo>>() {

            @Override
            @NonNull
            protected List<FavoritesMarkerInfo> doInBackground(@Nullable Void... params) {
                Log.i("FavoritesFragment", "doInBackground");
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
                Log.i("FavoritesFragment", "onPostExecute");
                Log.d("FavoritesFragment", "Data Size = " + (data == null ? 0 : data.size()));
                mAdapter = new FavoritesMapAdapter(getContext(), data);

                if (mList == null) {
                    mList = (ListFragment) getChildFragmentManager().findFragmentById(R.id.list);
                    mList.setListAdapter(mAdapter);
                    mList.getListView().setRecyclerListener(new AbsListView.RecyclerListener() {

                        @Override
                        public void onMovedToScrapHeap(@NonNull View view) {
                            Log.i("FavoritesFragment", "onMovedToScrapHeap");
                            FavoritesMapAdapter.FavoritesViewHolder holder = (FavoritesMapAdapter.FavoritesViewHolder) view.getTag();
                            if (holder != null && holder.map != null) {
                                holder.map.clear();
                                holder.map.setMapType(MAP_TYPE_NONE);
                            }
                        }
                    });
                }
                else {
                    mList.setListAdapter(null);
                    mList.setListAdapter(mAdapter);
                }

                if (mFirstVisiblePosition != -1 && mTop != -1){
                    mList.getListView().setSelectionFromTop(mFirstVisiblePosition, mTop);
                }
            }
        }.execute();
        return mRoot;
    }

    @Override
    public void onPause() {
        Log.i("FavoritesFragment", "onPause");
        super.onPause();
//        mList = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i("FavoritesFragment", "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        if (outState != null) {
            ListView listView = mList.getListView();
            outState.putInt("first_visible_position", listView.getFirstVisiblePosition());

            View v = listView.getChildAt(0);
            outState.putInt("top", (v == null ? 0 : v.getTop() - listView.getPaddingTop()));
        }
    }

    public class FavoritesMapAdapter extends ArrayAdapter<FavoritesMarkerInfo> {
        private Integer sGeomemorialTagKey = R.integer.geomemorial_tag_key;
        private HashSet<MapView> mMaps = new HashSet<>();

        public FavoritesMapAdapter(
            @NonNull Context context,
            @NonNull List<FavoritesMarkerInfo> locations
        ) {
            super(context, R.layout.list_item_favorites, R.id.lite_listrow_text, locations);
            Log.i("FavoritesMapAdapter", "Created");
            Log.i("FavoritesMapAdapter", "Locations size = " + (locations == null ? 0 : locations.size()));
        }

        @Override
        @NonNull
        public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
            Log.d("FavoritesMapAdapter", "getView");
            View row = convertView;
            FavoritesViewHolder holder;

            if (row == null) {
                row = getLayoutInflater(null).inflate(R.layout.list_item_favorites, null);

                holder = new FavoritesViewHolder(row);
                holder.favoritesButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(@NonNull View v) {
                        switch(v.getId()){
                            case R.id.list_item_favorite_favorite_button:
                                ImageButton button = (ImageButton) v;
                                Long geomemorialId = (Long) button.getTag(sGeomemorialTagKey);
                                boolean isChecked = ! isFavorite(getContext(), geomemorialId.toString());

                                if (isChecked){
                                    button.setImageResource(R.drawable.ic_favorite_white_24dp);
                                    addFavorite(getContext(), geomemorialId.toString());
                                }
                                else {
                                    button.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                                    removeFavorite(getContext(), geomemorialId.toString());
                                }
                                break;
                            default:
                        }
                    }
                });
                holder.favoritesButton.setTag(sGeomemorialTagKey, getItem(position).geomemorialId);
                holder.shareButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(@NonNull View v) {
                        switch(v.getId()){
                            case R.id.list_item_favorite_share_button:
                                ImageButton button = (ImageButton) v;
                                Long id = (Long) button.getTag(sGeomemorialTagKey);
                                Cursor c = getContext().getContentResolver().query(
                                    buildFor(id.toString()),
                                    null,
                                    null,
                                    null,
                                    null
                                );

                                if (c != null && c.getCount() > 0) {
                                    c.moveToFirst();
                                    SharedIntentManager.Payload payload = new SharedIntentManager.Payload.Builder(getContext())
                                        .geomemorial(c.getString(IDX_GEOMEMORIAL))
                                        .latitude(c.getString(IDX_LATITUDE))
                                        .longitude(c.getString(IDX_LONGITUDE))
                                        .resident(c.getString(IDX_RESIDENT))
                                        .hometown(c.getString(IDX_HOMETOWN))
                                        .rank(c.getString(IDX_RANK))
                                        .obit(c.getString(IDX_OBIT))
                                        .build();
                                    shareGeomemorial(payload);
                                }
                                break;
                            default:
                        }
                    }
                });
                holder.shareButton.setTag(sGeomemorialTagKey, getItem(position).geomemorialId);
                row.setTag(holder);

                holder.initializeMapView();

                mMaps.add(holder.mapView);
            }

            else {
                holder = (FavoritesViewHolder) row.getTag();
                holder.refreshMapView();
            }

            FavoritesMarkerInfo item = getItem(position);
            holder.mapView.setTag(item);

            if (holder.map != null) {
                setMapLocation(getContext(), holder.map, item);
            }

            holder.title.setText(item.geomemorial);

            FavoritesMarkerInfo info = getItem(position);
            Long geomemorialId = info.geomemorialId;
            boolean isChecked = isFavorite(getContext(), geomemorialId.toString());

            holder.favoritesButton.setImageResource(
                isChecked
                    ? R.drawable.ic_favorite_white_24dp
                    : R.drawable.ic_favorite_border_white_24dp
            );
            return row;
        }

        public HashSet<MapView> getMaps() {
            return mMaps;
        }

        public class FavoritesViewHolder implements OnMapReadyCallback {
            public MapView mapView;
            public TextView title;
            public GoogleMap map;
            public ImageButton favoritesButton;
            public ImageButton shareButton;

            public FavoritesViewHolder(@NonNull View view){
                super();
                mapView = (MapView) view.findViewById(R.id.lite_listrow_map);
                title = (TextView) view.findViewById(R.id.lite_listrow_text);
                favoritesButton = (ImageButton) view.findViewById(R.id.list_item_favorite_favorite_button);
                shareButton = (ImageButton) view.findViewById(R.id.list_item_favorite_share_button);
            }

            public void initializeMapView() {
                if (mapView != null) {
                    mapView.onCreate(null);
                    mapView.getMapAsync(this);
                }
            }

            /**
             * Once I overlaid the map with a scrim, the map's visible areas disappear when
             * scrolling off and then on the screen.  So I call this method to refresh the map but
             * skip the create step in order to avoid flickering.
             */
            public void refreshMapView(){
                mapView.onCreate(null);
                mapView.getMapAsync(this);
            }

            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                initialize(getContext());

                map = googleMap;
                map.getUiSettings().setAllGesturesEnabled(false);
                map.getUiSettings().setCompassEnabled(false);
                map.getUiSettings().setMapToolbarEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                map.getUiSettings().setIndoorLevelPickerEnabled(false);
                map.getUiSettings().setRotateGesturesEnabled(false);
                map.getUiSettings().setScrollGesturesEnabled(false);
                map.getUiSettings().setTiltGesturesEnabled(false);
                map.getUiSettings().setZoomControlsEnabled(false);
                map.getUiSettings().setZoomGesturesEnabled(false);
                map.setMapType(GeomemorialApplication.getVisibleMapType());

                FavoritesMarkerInfo data = (FavoritesMarkerInfo) mapView.getTag();
                if (data != null) {
                    CameraUpdateStrategy.setMapLocation(getContext(), map, data);
                }
            }
        }
    }
}
