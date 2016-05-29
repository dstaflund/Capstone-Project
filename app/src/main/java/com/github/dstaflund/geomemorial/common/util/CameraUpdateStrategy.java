package com.github.dstaflund.geomemorial.common.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

public final class CameraUpdateStrategy {

    public static void setMapLocation(@NonNull Context context, @NonNull GoogleMap map, @NonNull FavoritesMarkerInfo data) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(data.getLatLng());
//        markerOptions.icon(fromResource(R.mipmap.geomemorial_poppy));

        Marker marker = map.addMarker(markerOptions);
        MarkerMap markerMap = new MarkerMap();
        markerMap.put(Long.valueOf(data.geomemorialId).toString(), marker);

        CameraUpdateStrategy.updateCamera(context, map, markerMap);
    }

    public static void updateCamera(@NonNull Context context, @NonNull GoogleMap map, @NonNull MarkerMap markers){
        Marker firstMarker = markers.values().iterator().next();

        LatLngBounds bounds = markers.size() == 1
            ? getLatLngBoundsFor(firstMarker)
            : getLatLngBoundsFor(markers);

        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, 0);
        map.animateCamera(update);

        if (markers.size() == 1){
            markers.bringMarkerToFront(context, firstMarker.getPosition());
        }
    }

    public static void zoomTo(@NonNull Context context, @NonNull GoogleMap map, @NonNull MarkerMap markers, @NonNull LatLng latLng){
        LatLngBounds bounds = getLatLngBoundsFor(latLng);
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, 0);
        map.animateCamera(update);
        markers.bringMarkerToFront(context, latLng);
    }

    @NonNull
    public static LatLngBounds getLatLngBoundsFor(@NonNull Marker marker){
        return getLatLngBoundsFor(marker.getPosition());
    }

    @NonNull
    public static LatLngBounds getLatLngBoundsFor(@NonNull LatLng position){
        LatLng swCorner = new LatLng(position.latitude - 0.0625, position.longitude - 0.125);
        LatLng neCorner = new LatLng(position.latitude + 0.0625, position.longitude + 0.125);
        return new LatLngBounds(swCorner, neCorner);
    }

    @NonNull
    public static LatLngBounds getLatLngBoundsFor(@NonNull final MarkerMap markers){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(Map.Entry<String, Marker> markerEntry : markers.entrySet()){
            builder.include(markerEntry.getValue().getPosition());
        }
        return builder.build();
    }

    private CameraUpdateStrategy(){
        super();
    }
}