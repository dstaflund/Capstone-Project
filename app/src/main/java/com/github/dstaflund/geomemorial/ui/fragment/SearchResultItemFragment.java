package com.github.dstaflund.geomemorial.ui.fragment;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.dstaflund.geomemorial.GeomemorialApplication;
import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.DateUtil;
import com.github.dstaflund.geomemorial.common.util.SharedIntentManager;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.addFavorite;
import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.isFavorite;
import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.removeFavorite;
import static com.github.dstaflund.geomemorial.common.util.SharedIntentManager.shareGeomemorial;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.buildFor;

public class SearchResultItemFragment extends Fragment {
    private static final String POSITION_KEY = "position_key";
    private static final String COUNT_KEY = "count_key";
    private static final DecimalFormat sDecimalFormat = new DecimalFormat("#.##");


    private OnPlaceButtonClickedListener mOnPlaceButtonClickedListener;

    public SearchResultItemFragment(){
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mOnPlaceButtonClickedListener = (OnPlaceButtonClickedListener) context;
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

        ViewHolder holder = new ViewHolder(rootView);
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
                ? getContext().getResources().getDrawable(R.drawable.ic_favorite_accent_24dp)
                : getContext().getResources().getDrawable(R.drawable.ic_favorite_border_accent_24dp)
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
                        .geomemorial(c.getString(GeomemorialDbContract.GeomemorialInfo.IDX_GEOMEMORIAL))
                        .latitude(c.getString(GeomemorialDbContract.GeomemorialInfo.IDX_LATITUDE))
                        .longitude(c.getString(GeomemorialDbContract.GeomemorialInfo.IDX_LONGITUDE))
                        .resident(c.getString(GeomemorialDbContract.GeomemorialInfo.IDX_RESIDENT))
                        .hometown(c.getString(GeomemorialDbContract.GeomemorialInfo.IDX_HOMETOWN))
                        .rank(c.getString(GeomemorialDbContract.GeomemorialInfo.IDX_RANK))
                        .obit(c.getString(GeomemorialDbContract.GeomemorialInfo.IDX_OBIT))
                        .build();
                    shareGeomemorial(payload);
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
                        Double.valueOf(c.getString(GeomemorialDbContract.GeomemorialInfo.IDX_LATITUDE)),
                        Double.valueOf(c.getString(GeomemorialDbContract.GeomemorialInfo.IDX_LONGITUDE))
                    );
                    mOnPlaceButtonClickedListener.placeButtonClicked(latLng);
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
        b.putString(MarkerInfo.COL_NTS_SHEET_NAME, c.getString(MarkerInfo.DEFAULT_NTS_SHEET_NAME_IDX));
        b.putInt(POSITION_KEY, position);
        b.putInt(COUNT_KEY, count);

        SearchResultItemFragment f = new SearchResultItemFragment();
        f.setArguments(b);
        return f;
    }

    public static class ViewHolder {
        @NonNull public TextView recordCount;
        @NonNull public TextView distance;
        @NonNull public TextView coordinate;
        @NonNull public TextView geomemorial;
        @NonNull public TextView hometown;
        @NonNull public TextView name;
        @NonNull public TextView ntsSheet;
        @NonNull public TextView obit;
        @NonNull public TextView rank;
        @NonNull public ImageButton favoriteButton;
        @NonNull public ImageButton shareButton;
        @NonNull public ImageButton placeButton;

        public ViewHolder(@NonNull View v){
            recordCount = (TextView) v.findViewById(R.id.record_count);
            distance = (TextView) v.findViewById(R.id.distance);
            coordinate = (TextView) v.findViewById(R.id.list_item_common_coordinate);
            geomemorial = (TextView) v.findViewById(R.id.list_item_common_geomemorial);
            hometown = (TextView) v.findViewById(R.id.list_item_common_hometown);
            name = (TextView) v.findViewById(R.id.list_item_common_name);
            ntsSheet = (TextView) v.findViewById(R.id.list_item_common_nts_sheet);
            obit = (TextView) v.findViewById(R.id.list_item_common_obit);
            rank = (TextView) v.findViewById(R.id.list_item_common_rank);
            favoriteButton = (ImageButton) v.findViewById(R.id.favorite_button);
            shareButton = (ImageButton) v.findViewById(R.id.share_button);
            placeButton = (ImageButton) v.findViewById(R.id.place_button);
        }
    }

    public static class DataObject {
        @NonNull public String geomemorial;
        @NonNull public String geomemorialId;
        @NonNull public String hometown;
        @NonNull public String ntsSheet;
        @NonNull public String ntsSheetName;
        @NonNull public String obit;
        @NonNull public String rank;
        @NonNull public String resident;

        public String latitude;
        public String longitude;

        public DataObject(@NonNull Bundle b){
            geomemorial = b.getString(MarkerInfo.COL_GEOMEMORIAL, "");
            geomemorialId = b.getString(MarkerInfo._ID, "");
            hometown = b.getString(MarkerInfo.COL_HOMETOWN, "");
            latitude = b.getString(MarkerInfo.COL_LATITUDE);
            longitude = b.getString(MarkerInfo.COL_LONGITUDE);
            ntsSheet = b.getString(MarkerInfo.COL_NTS_SHEET, "");
            ntsSheetName = b.getString(MarkerInfo.COL_NTS_SHEET_NAME, "");
            obit = b.getString(MarkerInfo.COL_OBIT, "");
            resident = b.getString(MarkerInfo.COL_RESIDENT, "");
            rank = b.getString(MarkerInfo.COL_RANK, "");
        }

        public DataObject(@NonNull Cursor c){
            geomemorial = c.getString(MarkerInfo.DEFAULT_GEOMEMORIAL_IDX);
            geomemorialId = c.getString(MarkerInfo.DEFAULT_ID_IDX);
            hometown = c.getString(MarkerInfo.DEFAULT_HOMETOWN_IDX);
            latitude = c.getString(MarkerInfo.DEFAULT_LATITUDE_IDX);
            longitude = c.getString(MarkerInfo.DEFAULT_LONGITUDE_IDX);
            ntsSheet = c.getString(MarkerInfo.DEFAULT_NTS_SHEET_IDX);
            ntsSheetName = c.getString(MarkerInfo.DEFAULT_NTS_SHEET_NAME_IDX);
            obit = c.getString(MarkerInfo.DEFAULT_OBIT_IDX);
            resident = c.getString(MarkerInfo.DEFAULT_RESIDENT_IDX);
            rank = c.getString(MarkerInfo.DEFAULT_RANK_IDX);
        }

        public Location getLocation(){
            Location l = new Location("");
            l.setLatitude(Double.parseDouble(latitude));
            l.setLongitude(Double.parseDouble(longitude));
            return l;
        }
    }

    public static class DataFormatter {
        private int mPosition;
        private int mCount;

        @NonNull
        private Context mContext;

        @NonNull
        private DataObject mDataObject;

        public DataFormatter(@NonNull Context context, @NonNull DataObject dataObject, int position, int count){
            super();
            mContext = context;
            mDataObject = dataObject;
            mPosition = position;
            mCount = count;
        }

        @NonNull
        public String getResident(){
            return mDataObject.resident.toUpperCase();
        }

        @NonNull
        public String getHometown(){
            return mDataObject.hometown;
        }

        @NonNull
        public String getRank(){
            return mDataObject.rank;
        }

        @NonNull
        public String getObit(){
            return DateUtil.toDisplayString(mDataObject.obit);
        }

        @NonNull
        public String getGeomemorial(){
            return String.format(
                mContext.getString(R.string.string_format_coordinate_pattern),
                mDataObject.geomemorial.toUpperCase()
            );
        }

        @NonNull
        public String getNtsSheet(){
            return String.format(
                mContext.getString(R.string.string_format_nts_sheet_pattern),
                mDataObject.ntsSheet,
                mDataObject.ntsSheetName
            );
        }

        @NonNull
        String getCoordinate(){
            return String.format(
                mContext.getString(R.string.string_format_lat_lng_pattern),
                Float.valueOf(mDataObject.latitude).toString(),
                Float.valueOf(mDataObject.longitude).toString()
            );
        }

        @NonNull
        public LatLng getLatLng(){
            return new LatLng(
                Double.parseDouble(mDataObject.latitude),
                Double.parseDouble(mDataObject.longitude)
            );
        }

        @NonNull
        public String getGeomemorialId(){
            return mDataObject.geomemorialId;
        }

        @NonNull
        public String getRecordCount(){
            int maxVisible = mContext.getResources().getInteger(R.integer.max_visible_memorials);
            int countval = mCount <= maxVisible ? mCount : maxVisible;
            return (mPosition + 1) + " of " + countval + " memorials";
        }

        @Nullable
        public String getDistance(){
            Location currentLocation = GeomemorialApplication.getLastLocation();
            if (currentLocation == null){
                return null;
            }

            Location memorialLocation = mDataObject.getLocation();
            float distanceInMeters = currentLocation.distanceTo(memorialLocation);
            float distanceInKilometers = distanceInMeters / 1000;
            String distanceString = sDecimalFormat.format(distanceInKilometers);
            return distanceString + " km away";
        }
    }

    public interface OnPlaceButtonClickedListener {
        void placeButtonClicked(@NonNull LatLng position);
    }
}
