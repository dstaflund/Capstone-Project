package com.github.dstaflund.geomemorial.common.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.dstaflund.geomemorial.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Map of markers visible with the map bounds
 *
 * Since a geomemorial can be named for two or more residents, then two or more markers
 * for the same position (i.e. with the same latitude and longitude) can be shown on the
 * map at the same time.  As a result, I cannot use a marker's position as key for the
 * marker.
 */
public class MarkerMap extends HashMap<String, Marker> {
    private static final String sLogTag = MarkerMap.class.getSimpleName();
    private static final DecimalFormat sDecimalFormat = new DecimalFormat("#.###");

    /**
     * Default constructor
     */
    public MarkerMap() {
        super();
    }

    /**
     * Returns the marker id for a given latlng
     *
     * @param latLng to get id for
     * @return marker id or null if there isn't one
     */
    @Nullable
    public String getMarkerIdFor(@NonNull Context context, @NonNull LatLng latLng){
        for(final Map.Entry<String, Marker> markerEntry : entrySet()){
            sDecimalFormat.setRoundingMode(RoundingMode.UP);
            String roundedLat = sDecimalFormat.format(markerEntry.getValue().getPosition().latitude);
            String roundedLng = sDecimalFormat.format(markerEntry.getValue().getPosition().longitude);
            LatLng markerLatLng = new LatLng(
                Double.parseDouble(roundedLat),
                Double.parseDouble(roundedLng)
            );
            if (markerLatLng.latitude <= Double.parseDouble(sDecimalFormat.format(latLng.latitude + .001)) &&
                markerLatLng.latitude >= Double.parseDouble(sDecimalFormat.format(latLng.latitude - .001)) &&
                markerLatLng.longitude <= Double.parseDouble(sDecimalFormat.format(latLng.longitude + .001)) &&
                markerLatLng.longitude >= Double.parseDouble(sDecimalFormat.format(latLng.longitude - .001))){
                return markerEntry.getKey();
            }
        }

        Log.w(sLogTag, context.getString(R.string.log_marker_map_marker_not_found));
        return null;
    }

    /**
     * Moves markers with the given lat / lng to the front of the map
     *
     * @param latLng of markers in question
     */
    public void bringMarkerToFront(@NonNull Context context, @NonNull LatLng latLng){
        String markerId = getMarkerIdFor(context, latLng);
        if (markerId != null) {
            bringMarkerToFront(markerId);
        }
    }

    /**
     * Moves marker with the giben id to the front of the map
     *
     * @param markerId in question
     */
    public void bringMarkerToFront(@NonNull String markerId){
        for(final Map.Entry<String, Marker> markerEntry : entrySet()){
            if (markerId.equals(markerEntry.getKey())){
                markerEntry.getValue().showInfoWindow();
            } else {
                markerEntry.getValue().hideInfoWindow();
            }
        }
    }
}
