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

public class MarkerMap extends HashMap<String, Marker> {
    private static final DecimalFormat sDecimalFormat = new DecimalFormat("#.###");

    public MarkerMap() {
        super();
    }

    @Nullable
    public String getMarkerIdFor(@NonNull LatLng coord){
        for(Map.Entry<String, Marker> entry : entrySet()){
            sDecimalFormat.setRoundingMode(RoundingMode.UP);
            String roundedLat = sDecimalFormat.format(entry.getValue().getPosition().latitude);
            String roundedLng = sDecimalFormat.format(entry.getValue().getPosition().longitude);
            LatLng latLng = new LatLng(parseDouble(roundedLat), parseDouble(roundedLng));

            if (latLng.latitude <= parseDouble(sDecimalFormat.format(coord.latitude + .001)) &&
                latLng.latitude >= parseDouble(sDecimalFormat.format(coord.latitude - .001)) &&
                latLng.longitude <= parseDouble(sDecimalFormat.format(coord.longitude + .001)) &&
                latLng.longitude >= parseDouble(sDecimalFormat.format(coord.longitude - .001))){
                return entry.getKey();
            }
        }

        return null;
    }

    public void bringMarkerToFront(@NonNull LatLng latLng){
        String markerId = getMarkerIdFor(latLng);
        if (markerId != null) {
            bringMarkerToFront(markerId);
        }
    }

    public void bringMarkerToFront(@NonNull String markerId){
        for(Map.Entry<String, Marker> entry : entrySet()){
            if (markerId.equals(entry.getKey())){
                entry.getValue().showInfoWindow();
            }
            else {
                entry.getValue().hideInfoWindow();
            }
        }
    }
}
