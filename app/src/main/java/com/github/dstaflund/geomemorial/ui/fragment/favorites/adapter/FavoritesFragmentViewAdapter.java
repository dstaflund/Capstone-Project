package com.github.dstaflund.geomemorial.ui.fragment.favorites.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.FavoritesMarkerInfo;
import com.github.dstaflund.geomemorial.ui.fragment.favorites.FavoritesFragment;
import com.github.dstaflund.geomemorial.ui.fragment.favorites.FavoritesFragmentViewHolder;
import com.github.dstaflund.geomemorial.ui.fragment.favorites.listener.FavoriteButtonClickListener;
import com.github.dstaflund.geomemorial.ui.fragment.favorites.listener.ShareButtonClickListener;
import com.google.android.gms.maps.MapView;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static com.github.dstaflund.geomemorial.common.util.CameraUpdateStrategy.setMapLocation;
import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.isFavorite;

public class FavoritesFragmentViewAdapter extends ArrayAdapter<FavoritesMarkerInfo> {
    private HashSet<MapView> mMaps = new HashSet<>();
    private LayoutInflater mInflater;

    public FavoritesFragmentViewAdapter(
        @NonNull Context context,
        @NonNull LayoutInflater inflater,
        @Nullable List<FavoritesMarkerInfo> locations
    ) {
        super(
            context,
            R.layout.list_item_favorites,
            R.id.lite_listrow_text,
            locations == null ? Collections.<FavoritesMarkerInfo>emptyList() : locations
        );
        mInflater = inflater;
    }

    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        View row = convertView;
        FavoritesFragmentViewHolder holder;

        if (row == null) {
            row = mInflater.inflate(R.layout.list_item_favorites, null);

            holder = new FavoritesFragmentViewHolder(getContext(), row);
            holder.favoritesButton.setOnClickListener(new FavoriteButtonClickListener(getContext()));
            holder.shareButton.setOnClickListener(new ShareButtonClickListener(getContext()));
            row.setTag(holder);

            holder.initializeMapView();

            mMaps.add(holder.mapView);
        } else {
            holder = (FavoritesFragmentViewHolder) row.getTag();
            holder.refreshMapView();
        }

        holder.favoritesButton.setTag(FavoritesFragment.GEOMEMORIAL_TAG_KEY, getItem(position).geomemorialId);
        holder.shareButton.setTag(FavoritesFragment.GEOMEMORIAL_TAG_KEY, getItem(position).geomemorialId);

        FavoritesMarkerInfo item = getItem(position);
        holder.mapView.setTag(item);

        if (holder.map != null) {
            setMapLocation(holder.map, item, true);
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
}
