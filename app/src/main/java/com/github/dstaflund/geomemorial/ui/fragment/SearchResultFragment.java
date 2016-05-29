package com.github.dstaflund.geomemorial.ui.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.DateUtil;
import com.github.dstaflund.geomemorial.common.util.SharedIntentManager;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract;
import com.google.android.gms.maps.model.LatLng;

import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.addFavorite;
import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.isFavorite;
import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.removeFavorite;
import static com.github.dstaflund.geomemorial.common.util.SharedIntentManager.shareGeomemorial;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.buildFor;

public class SearchResultFragment extends Fragment {
    private static final String sLogTag = SearchResultFragment.class.getSimpleName();

    private OnChangeCursorListener mOnChangeCursorListener;
    private OnPlaceButtonClickedListener mOnPlaceButtonClickedListener;
    private CursorAdapter mSearchCursorAdapter;
    private View mRoot;

    public SearchResultFragment(){
        super();
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);

        try {
            mOnChangeCursorListener = (OnChangeCursorListener) context;
            mOnPlaceButtonClickedListener = (OnPlaceButtonClickedListener) context;
        }

        catch(Exception e){
            Log.e(sLogTag, context.getString(R.string.log_search_result_bad_activity));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setRetainInstance(true);
        setHasOptionsMenu(false);
    }
    @Override
    @NonNull
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedState
    ) {
        mRoot = inflater.inflate(R.layout.fragment_search_result, container, false);
        mSearchCursorAdapter = new SearchCursorAdapter(getContext(), null);

        GridView gridView = (GridView) mRoot.findViewById(R.id.fragment_search_result_listview);
        gridView.setAdapter(mSearchCursorAdapter);

        return mRoot;
    }



    public void changeCursor(@Nullable Cursor value){
        mSearchCursorAdapter.changeCursor(value);
    }

    public class SearchCursorAdapter extends CursorAdapter {
        private final Integer sViewHolderKey = R.integer.view_holder_id;
        private final Integer sGeomemorialTagKey = R.integer.geomemorial_tag_key;

        public SearchCursorAdapter(
            @NonNull Context context,
            @NonNull Cursor cursor
        ) {
            super(context, cursor, 0);
        }

        @Override
        public @NonNull
        View newView(
            @NonNull Context context,
            @NonNull Cursor cursor,
            @NonNull ViewGroup parent
        ) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_search, parent, false);
            view.setTag(sViewHolderKey, new ViewHolder(view));
            bindView(view, context, cursor);
            return view;
        }

        @Override
        public void changeCursor(@Nullable Cursor cursor) {
            int count = 0;
            if (cursor != null && cursor.getCount() > 0) {
                int position = cursor.getPosition();
                while (cursor.moveToNext()) {
                    count += 1;
                    if (count > getResources().getInteger(R.integer.max_visible_memorials)){
                        break;
                    }
                    DataObject dataObject = new DataObject(cursor);
                    DataFormatter dataFormatter = new DataFormatter(getContext(), dataObject);

                    mOnChangeCursorListener.recordFinished(dataFormatter);
                }

                mOnChangeCursorListener.cursorFinished();
                cursor.moveToPosition(position);
            }

            super.changeCursor(cursor);
        }

        /**
         * Binds a new cursor record to the given view
         *
         * @param view to bind cursor record to
         * @param context in which the binding is taking place
         * @param cursor containing the record to be bound
         */
        @Override
        public void bindView(
            @NonNull View view,
            @NonNull final Context context,
            @Nullable Cursor cursor
        ) {

            if (cursor != null) {
                DataObject dataObject = new DataObject(cursor);
                DataFormatter formatter = new DataFormatter(context, dataObject);

                ViewHolder holder = (ViewHolder) view.getTag(sViewHolderKey);
                holder.name.setText(formatter.getResident());
                holder.hometown.setText(formatter.getHometown());
                holder.rank.setText(formatter.getRank());
                holder.obit.setText(formatter.getObit());
                holder.geomemorial.setText(formatter.getGeomemorial());
                holder.ntsSheet.setText(formatter.getNtsSheet());
                holder.coordinate.setText(formatter.getCoordinate());

                holder.favoriteButton.setImageDrawable(
                    isFavorite(context, formatter.getGeomemorialId())
                        ? getContext().getResources().getDrawable(R.drawable.ic_favorite_accent_24dp)
                        : getContext().getResources().getDrawable(R.drawable.ic_favorite_border_accent_24dp)
                );
                holder.favoriteButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(@NonNull View v) {
                        ImageButton button = (ImageButton) v;
                        String id = (String) button.getTag(sGeomemorialTagKey);
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
                holder.favoriteButton.setTag(sGeomemorialTagKey, formatter.getGeomemorialId());
                holder.shareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(@NonNull View v) {
                        ImageButton button = (ImageButton) v;
                        String geomemorialId = (String) button.getTag(sGeomemorialTagKey);
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
                                .Builder(context)
                                .geomemorial(c.getString(GeomemorialDbContract.GeomemorialInfo.IDX_GEOMEMORIAL))
                                .latitude(c.getString(GeomemorialDbContract.GeomemorialInfo.IDX_LATITUDE))
                                .longitude(c.getString(GeomemorialDbContract.GeomemorialInfo.IDX_LONGITUDE))
                                .resident(c.getString(GeomemorialDbContract.GeomemorialInfo.IDX_RESIDENT))
                                .hometown(c.getString(GeomemorialDbContract.GeomemorialInfo.IDX_HOMETOWN))
                                .rank(c.getString(GeomemorialDbContract.GeomemorialInfo.IDX_RANK))
                                .obit(c.getString(GeomemorialDbContract.GeomemorialInfo.IDX_OBIT))
                                .build();
                            shareGeomemorial(getContext(), payload);
                        }
                    }
                });
                holder.shareButton.setTag(sGeomemorialTagKey, formatter.getGeomemorialId());
                holder.placeButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(@NonNull View v) {
                        ImageButton button = (ImageButton) v;
                        String id = (String) button.getTag(sGeomemorialTagKey);

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
                holder.placeButton.setTag(sGeomemorialTagKey, formatter.getGeomemorialId());
            }
        }
    }

    public static class ViewHolder {
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

        public float latitude;
        public float longitude;

        public DataObject(@NonNull Cursor c){
            geomemorial = c.getString(GeomemorialDbContract.MarkerInfo.DEFAULT_GEOMEMORIAL_IDX);
            geomemorialId = c.getString(GeomemorialDbContract.MarkerInfo.DEFAULT_GEOMEMORIAL_ID_IDX);
            hometown = c.getString(GeomemorialDbContract.MarkerInfo.DEFAULT_HOMETOWN_IDX);
            latitude = c.getFloat(GeomemorialDbContract.MarkerInfo.DEFAULT_LATITUDE_IDX);
            longitude = c.getFloat(GeomemorialDbContract.MarkerInfo.DEFAULT_LONGITUDE_IDX);
            ntsSheet = c.getString(GeomemorialDbContract.MarkerInfo.DEFAULT_NTS_SHEET_IDX);
            ntsSheetName = c.getString(GeomemorialDbContract.MarkerInfo.DEFAULT_NTS_SHEET_NAME_IDX);
            obit = c.getString(GeomemorialDbContract.MarkerInfo.DEFAULT_OBIT_IDX);
            resident = c.getString(GeomemorialDbContract.MarkerInfo.DEFAULT_RESIDENT_IDX);
            rank = c.getString(GeomemorialDbContract.MarkerInfo.DEFAULT_RANK_IDX);
        }
    }

    public static class DataFormatter {

        @NonNull
        private Context mContext;

        @NonNull
        private DataObject mDataObject;

        public DataFormatter(@NonNull Context context, @NonNull DataObject dataObject){
            super();
            mContext = context;
            mDataObject = dataObject;
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
            return DateUtil.toDisplayString(mContext, mDataObject.obit);
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

        @NonNull String getCoordinate(){
            return String.format(
                mContext.getString(R.string.string_format_lat_lng_pattern),
                Float.valueOf(mDataObject.latitude).toString(),
                Float.valueOf(mDataObject.longitude).toString()
            );
        }

        @NonNull
        public LatLng getLatLng(){
            return new LatLng(mDataObject.latitude, mDataObject.longitude);
        }

        @NonNull
        public String getGeomemorialId(){
            return mDataObject.geomemorialId;
        }
    }

    public interface OnChangeCursorListener {
        void recordFinished(@NonNull DataFormatter record);
        void cursorFinished();
    }

    public interface OnPlaceButtonClickedListener {
        void placeButtonClicked(@NonNull LatLng position);
    }
}
