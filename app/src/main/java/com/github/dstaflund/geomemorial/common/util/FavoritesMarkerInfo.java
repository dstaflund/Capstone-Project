package com.github.dstaflund.geomemorial.common.util;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.Geomemorial;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class FavoritesMarkerInfo {
    public long geomemorialId;
    public String geomemorial;
    public double latitude;
    public double longitude;

    public LatLng getLatLng(){
        return new LatLng(latitude, longitude);
    }

    public FavoritesMarkerInfo(@NonNull Cursor cursor){
        super();
        geomemorialId = cursor.getLong(Geomemorial.DEFAULT_ID_IDX);
        geomemorial = cursor.getString(Geomemorial.DEFAULT_GEOMEMORIAL_IDX);
        latitude = cursor.getDouble(Geomemorial.DEFAULT_LATITUDE_IDX);
        longitude = cursor.getDouble(Geomemorial.DEFAULT_LONGITUDE_IDX);
    }

    @NonNull
    public static List<FavoritesMarkerInfo> getDataFormatters(@Nullable Cursor cursor){
        final List<FavoritesMarkerInfo> formatters = new ArrayList<>();
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    formatters.add(new FavoritesMarkerInfo(cursor));
                }
            }
        }
        return formatters;
    }
}
