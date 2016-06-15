package com.github.dstaflund.geomemorial.ui.fragment.searchresultitem;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo;
import com.github.dstaflund.geomemorial.ui.fragment.searchresultitem.listener.FavoriteButtonClickListener;
import com.github.dstaflund.geomemorial.ui.fragment.searchresultitem.listener.PlaceButtonClickListener;
import com.github.dstaflund.geomemorial.ui.fragment.searchresultitem.listener.ShareButtonClickListener;

import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.isFavorite;

public class SearchResultItemFragment extends Fragment {
    private static final String POSITION_KEY = "position_key";
    private static final String COUNT_KEY = "count_key";

    public SearchResultItemFragment(){
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.list_item_search, container, false);

        SearchResultItem dataObject = new SearchResultItem(getArguments());
        final SearchResultItemFormatter formatter = new SearchResultItemFormatter(
            getContext(),
            dataObject,
            getArguments().getInt(POSITION_KEY),
            getArguments().getInt(COUNT_KEY)
        );

        SearchResultItemFragmentViewHolder holder = new SearchResultItemFragmentViewHolder(rootView);
        holder.recordCount.setText(formatter.getRecordCount());
        holder.distance.setText(formatter.getDistance());
        holder.name.setText(formatter.getResident());
        holder.hometown.setText(formatter.getHometown());
        holder.rank.setText(formatter.getRank());
        holder.obit.setText(formatter.getObit());
        holder.geomemorial.setText(formatter.getGeomemorial());
        holder.ntsSheet.setText(formatter.getNtsSheet());
        holder.coordinate.setText(formatter.getCoordinate());

        holder.favoriteButton.setImageDrawable(
            isFavorite(getContext(), formatter.getGeomemorialId())
                ? getContext().getDrawable(R.drawable.ic_favorite_accent_24dp)
                : getContext().getDrawable(R.drawable.ic_favorite_border_accent_24dp)
        );
        holder.favoriteButton.setOnClickListener(new FavoriteButtonClickListener(getContext(), formatter));
        holder.shareButton.setOnClickListener(new ShareButtonClickListener(getContext(), formatter));
        holder.placeButton.setOnClickListener(new PlaceButtonClickListener(getContext(), formatter));

        return rootView;
    }

    public static SearchResultItemFragment newInstance(@NonNull Cursor c, int position, int count){
        Bundle b = new Bundle();
        b.putString(MarkerInfo._ID, c.getString(MarkerInfo.DEFAULT_ID_IDX));
        b.putString(MarkerInfo.COL_GEOMEMORIAL, c.getString(MarkerInfo.DEFAULT_GEOMEMORIAL_IDX));
        b.putString(MarkerInfo.COL_LATITUDE, c.getString(MarkerInfo.DEFAULT_LATITUDE_IDX));
        b.putString(MarkerInfo.COL_LONGITUDE, c.getString(MarkerInfo.DEFAULT_LONGITUDE_IDX));
        b.putString(MarkerInfo.COL_RESIDENT, c.getString(MarkerInfo.DEFAULT_RESIDENT_IDX));
        b.putString(MarkerInfo.COL_HOMETOWN, c.getString(MarkerInfo.DEFAULT_HOMETOWN_IDX));
        b.putString(MarkerInfo.COL_RANK, c.getString(MarkerInfo.DEFAULT_RANK_IDX));
        b.putString(MarkerInfo.COL_OBIT, c.getString(MarkerInfo.DEFAULT_OBIT_IDX));
        b.putString(MarkerInfo.COL_LETTER, c.getString(MarkerInfo.DEFAULT_LETTER_ID_IDX));
        b.putString(MarkerInfo.COL_NTS_SHEET, c.getString(MarkerInfo.DEFAULT_NTS_SHEET_IDX));
        b.putString(
            MarkerInfo.COL_NTS_SHEET_NAME,
            c.getString(MarkerInfo.DEFAULT_NTS_SHEET_NAME_IDX)
        );
        b.putInt(POSITION_KEY, position);
        b.putInt(COUNT_KEY, count);

        SearchResultItemFragment f = new SearchResultItemFragment();
        f.setArguments(b);
        return f;
    }
}
