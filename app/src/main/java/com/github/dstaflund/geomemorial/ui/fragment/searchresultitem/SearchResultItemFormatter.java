package com.github.dstaflund.geomemorial.ui.fragment.searchresultitem;

import android.content.Context;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.dstaflund.geomemorial.GeomemorialApplication;
import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.DateUtil;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

public class SearchResultItemFormatter {
    private static final DecimalFormat sDecimalFormat = new DecimalFormat("#.##");

    private int mPosition;
    private int mCount;

    @NonNull
    private Context mContext;

    @NonNull
    private SearchResultItem mDataObject;

    public SearchResultItemFormatter(
        @NonNull Context context,
        @NonNull SearchResultItem dataObject,
        int position,
        int count
    ) {
        super();
        mContext = context;
        mDataObject = dataObject;
        mPosition = position;
        mCount = count;
    }

    @NonNull
    public String getResident() {
        return mDataObject.resident.toUpperCase();
    }

    @NonNull
    public String getHometown() {
        return mDataObject.hometown;
    }

    @NonNull
    public String getRank() {
        return mDataObject.rank;
    }

    @Nullable
    public String getObit() {
        return DateUtil.toDisplayString(mDataObject.obit);
    }

    @NonNull
    public String getGeomemorial() {
        return String.format(
            mContext.getString(R.string.string_format_coordinate_pattern),
            mDataObject.geomemorial.toUpperCase()
        );
    }

    @NonNull
    public String getNtsSheet() {
        return String.format(
            mContext.getString(R.string.string_format_nts_sheet_pattern),
            mDataObject.ntsSheet,
            mDataObject.ntsSheetName
        );
    }

    @NonNull
    String getCoordinate() {
        return String.format(
            mContext.getString(R.string.string_format_lat_lng_pattern),
            Float.valueOf(mDataObject.latitude).toString(),
            Float.valueOf(mDataObject.longitude).toString()
        );
    }

    @NonNull
    public LatLng getLatLng() {
        return new LatLng(
            Double.parseDouble(mDataObject.latitude),
            Double.parseDouble(mDataObject.longitude)
        );
    }

    @NonNull
    public String getGeomemorialId() {
        return mDataObject.geomemorialId;
    }

    @NonNull
    public String getRecordCount() {
        int maxVisible = mContext.getResources().getInteger(R.integer.max_visible_memorials);
        int countval = mCount <= maxVisible ? mCount : maxVisible;
        return (mPosition + 1) + " of " + countval + " memorials";
    }

    @Nullable
    public String getDistance() {
        Location currentLocation = GeomemorialApplication.getLastLocation();
        if (currentLocation == null) {
            return null;
        }

        Location memorialLocation = mDataObject.getLocation();
        float distanceInMeters = currentLocation.distanceTo(memorialLocation);
        float distanceInKilometers = distanceInMeters / 1000;
        String distanceString = sDecimalFormat.format(distanceInKilometers);
        return distanceString + " km away";
    }
}
