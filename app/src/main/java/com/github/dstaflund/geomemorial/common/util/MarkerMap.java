package com.github.dstaflund.geomemorial.common.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Double.parseDouble;

/**
 * Map of markers visible with the map bounds
 *
 * Since a geomemorial can be named for two or more residents, then two or more markers
 * for the same position (i.e. with the same latitude and longitude) can be shown on the
 * map at the same time.  As a result, I cannot use a marker's position as key for the
 * marker.
 */
public class MarkerMap extends HashMap<String, Marker> {
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
     * @param coord to get id for
     * @return marker id or null if there isn't one
     */
    @Nullable
    public String getMarkerIdFor(@NonNull LatLng coord){
        for(final Map.Entry<String, Marker> entry : entrySet()){
            sDecimalFormat.setRoundingMode(RoundingMode.UP);
            String roundedLat = sDecimalFormat.format(entry.getValue().getPosition().latitude);
            String roundedLng = sDecimalFormat.format(entry.getValue().getPosition().longitude);
            LatLng latLng = new LatLng(
                parseDouble(roundedLat),
                parseDouble(roundedLng)
            );
            if (latLng.latitude <= parseDouble(sDecimalFormat.format(coord.latitude + .001)) &&
                latLng.latitude >= parseDouble(sDecimalFormat.format(coord.latitude - .001)) &&
                latLng.longitude <= parseDouble(sDecimalFormat.format(coord.longitude + .001)) &&
                latLng.longitude >= parseDouble(sDecimalFormat.format(coord.longitude - .001))){
                return entry.getKey();
            }
        }

        return null;
    }

    /**
     * Moves markers with the given lat / lng to the front of the map
     *
     * @param latLng of markers in question
     */
    public void bringMarkerToFront(@NonNull LatLng latLng){
        String markerId = getMarkerIdFor(latLng);
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
