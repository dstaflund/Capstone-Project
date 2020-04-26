package com.github.dstaflund.geomemorial.common.util;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds;

public final class CameraUpdateStrategy {

    public static void setMapLocation(
        @NonNull GoogleMap map,
        @NonNull FavoritesMarkerInfo data,
        boolean ignoreZoom
    ) {
        MarkerOptions options = new MarkerOptions();
        options.position(data.getLatLng());

        Marker marker = map.addMarker(options);
        MarkerMap markerMap = new MarkerMap();
        markerMap.put(Long.valueOf(data.geomemorialId).toString(), marker);

        updateCamera(map, markerMap, ignoreZoom);
    }

    public static void updateCamera(
        @NonNull GoogleMap map,
        @NonNull MarkerMap markers,
        boolean ignoreZoom
    ){
        if (markers.size() > 0) {
            Marker marker = markers.values().iterator().next();

            LatLngBounds bounds = markers.size() == 1
                ? getLatLngBoundsFor(marker)
                : getLatLngBoundsFor(markers);

            CameraUpdate update = newLatLngBounds(bounds, 0);

            if (ignoreZoom) {
                map.moveCamera(update);
            }
            else {
                map.animateCamera(update);
            }

            if (markers.size() == 1) {
                markers.bringMarkerToFront(marker.getPosition());
            }
        }
    }

    public static void zoomTo(
        @NonNull GoogleMap map,
        @NonNull MarkerMap markers,
        @NonNull LatLng latLng
    ){
        LatLngBounds bounds = getLatLngBoundsFor(latLng);
        CameraUpdate update = newLatLngBounds(bounds, 0);
        map.animateCamera(update);
        markers.bringMarkerToFront(latLng);
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
        for(Map.Entry<String, Marker> entry : markers.entrySet()){
            builder.include(entry.getValue().getPosition());
        }
        return builder.build();
    }

    private CameraUpdateStrategy(){
        super();
    }
}