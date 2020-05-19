package com.github.dstaflund.geomemorial.ui.fragment.searchresultitem;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.ui.fragment.searchresultitem.listener.FavoriteButtonClickListener;
import com.github.dstaflund.geomemorial.ui.fragment.searchresultitem.listener.PlaceButtonClickListener;
import com.github.dstaflund.geomemorial.ui.fragment.searchresultitem.listener.ShareButtonClickListener;

import static android.provider.BaseColumns._ID;
import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.isFavorite;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.COL_GEOMEMORIAL;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.COL_HOMETOWN;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.COL_LATITUDE;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.COL_LETTER;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.COL_LONGITUDE;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.COL_NTS_SHEET;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.COL_NTS_SHEET_NAME;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.COL_OBIT;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.COL_RANK;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.COL_RESIDENT;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.DEFAULT_GEOMEMORIAL_IDX;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.DEFAULT_HOMETOWN_IDX;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.DEFAULT_ID_IDX;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.DEFAULT_LATITUDE_IDX;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.DEFAULT_LETTER_ID_IDX;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.DEFAULT_LONGITUDE_IDX;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.DEFAULT_NTS_SHEET_IDX;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.DEFAULT_NTS_SHEET_NAME_IDX;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.DEFAULT_OBIT_IDX;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.DEFAULT_RANK_IDX;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.DEFAULT_RESIDENT_IDX;

public class SearchResultItemFragment extends Fragment {
    private static final String sPositionKey = "position_key";
    private static final String sCountKey = "count_key";

    public SearchResultItemFragment(){
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setHasOptionsMenu(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b) {
        View rootView = i.inflate(R.layout.list_item_search, c, false);

        Context context = getContext();
        if (context == null){
            return rootView;
        }

        Bundle arguments = getArguments();
        SearchResultItem dataObject = new SearchResultItem(arguments);
        final SearchResultItemFormatter formatter = new SearchResultItemFormatter(
                context,
                dataObject,
                arguments != null ? arguments.getInt(sPositionKey) : 0,
                arguments != null ? arguments.getInt(sCountKey) : 0
        );

        SearchResultItemFragmentHolder holder = new SearchResultItemFragmentHolder(rootView);
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
                isFavorite(context, formatter.getGeomemorialId())
                        ? context.getDrawable(R.drawable.ic_favorite_accent_24dp)
                        : context.getDrawable(R.drawable.ic_favorite_border_accent_24dp)
        );

        holder.favoriteButton.setOnClickListener(new FavoriteButtonClickListener(context, formatter));
        holder.shareButton.setOnClickListener(new ShareButtonClickListener(context, formatter));
        holder.placeButton.setOnClickListener(new PlaceButtonClickListener(context, formatter));

        return rootView;
    }

    @NonNull
    public static SearchResultItemFragment newInstance(@Nullable Cursor c, int position, int count){
        Bundle b = new Bundle();
        if (c != null) {
            b.putString(_ID, c.getString(DEFAULT_ID_IDX));
            b.putString(COL_GEOMEMORIAL, c.getString(DEFAULT_GEOMEMORIAL_IDX));
            b.putString(COL_LATITUDE, c.getString(DEFAULT_LATITUDE_IDX));
            b.putString(COL_LONGITUDE, c.getString(DEFAULT_LONGITUDE_IDX));
            b.putString(COL_RESIDENT, c.getString(DEFAULT_RESIDENT_IDX));
            b.putString(COL_HOMETOWN, c.getString(DEFAULT_HOMETOWN_IDX));
            b.putString(COL_RANK, c.getString(DEFAULT_RANK_IDX));
            b.putString(COL_OBIT, c.getString(DEFAULT_OBIT_IDX));
            b.putString(COL_LETTER, c.getString(DEFAULT_LETTER_ID_IDX));
            b.putString(COL_NTS_SHEET, c.getString(DEFAULT_NTS_SHEET_IDX));
            b.putString(COL_NTS_SHEET_NAME, c.getString(DEFAULT_NTS_SHEET_NAME_IDX));
        }
        b.putInt(sPositionKey, position);
        b.putInt(sCountKey, count);

        SearchResultItemFragment f = new SearchResultItemFragment();
        f.setArguments(b);
        return f;
    }
}
