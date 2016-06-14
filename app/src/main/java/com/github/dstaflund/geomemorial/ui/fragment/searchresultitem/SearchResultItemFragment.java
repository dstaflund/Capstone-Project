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
import android.widget.ImageButton;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.SharedIntentManager;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo;
import com.github.dstaflund.geomemorial.receiver.PlaceButtonClickedReceiver;
import com.google.android.gms.maps.model.LatLng;

import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.addFavorite;
import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.isFavorite;
import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.removeFavorite;
import static com.github.dstaflund.geomemorial.common.util.SharedIntentManager.shareGeomemorial;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.buildFor;

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

        DataObject dataObject = new DataObject(getArguments());
        final DataFormatter formatter = new DataFormatter(
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
        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(@NonNull View v) {
                ImageButton button = (ImageButton) v;
                String id = formatter.getGeomemorialId();
                boolean isChecked = ! isFavorite(getContext(), id);

                if (isChecked){
                    button.setImageResource(R.drawable.ic_favorite_accent_24dp);
                    addFavorite(getContext(), id);
                }
                else {
                    button.setImageResource(R.drawable.ic_favorite_border_accent_24dp);
                    removeFavorite(getContext(), id);
                }
            }
        });
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                String geomemorialId = formatter.getGeomemorialId();
                Cursor c = getContext().getContentResolver().query(
                    buildFor(geomemorialId),
                    null,
                    null,
                    null,
                    null
                );

                if (c != null && c.getCount() > 0) {
                    c.moveToFirst();
                    SharedIntentManager.Payload payload = new SharedIntentManager.Payload
                        .Builder(getContext())
                        .geomemorial(c.getString(GeomemorialInfo.IDX_GEOMEMORIAL))
                        .latitude(c.getString(GeomemorialInfo.IDX_LATITUDE))
                        .longitude(c.getString(GeomemorialInfo.IDX_LONGITUDE))
                        .resident(c.getString(GeomemorialInfo.IDX_RESIDENT))
                        .hometown(c.getString(GeomemorialInfo.IDX_HOMETOWN))
                        .rank(c.getString(GeomemorialInfo.IDX_RANK))
                        .obit(c.getString(GeomemorialInfo.IDX_OBIT))
                        .build();
                    shareGeomemorial(payload);
                    c.close();
                }
            }
        });
        holder.placeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(@NonNull View v) {
                String id = formatter.getGeomemorialId();

                Cursor c = getContext().getContentResolver().query(
                    buildFor(id),
                    null,
                    null,
                    null,
                    null
                );

                if (c != null && c.getCount() > 0) {
                    c.moveToFirst();
                    LatLng latLng = new LatLng(
                        Double.valueOf(c.getString(GeomemorialInfo.IDX_LATITUDE)),
                        Double.valueOf(c.getString(GeomemorialInfo.IDX_LONGITUDE))
                    );
                    PlaceButtonClickedReceiver.sendBroadcast(getContext(), latLng);
                    c.close();
                }
            }
        });

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
