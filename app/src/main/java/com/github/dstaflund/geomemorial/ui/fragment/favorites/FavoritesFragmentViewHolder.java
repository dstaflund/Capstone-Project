package com.github.dstaflund.geomemorial.ui.fragment.favorites;


import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.dstaflund.geomemorial.GeomemorialApplication;
import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.CameraUpdateStrategy;
import com.github.dstaflund.geomemorial.common.util.FavoritesMarkerInfo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import static com.google.android.gms.maps.MapsInitializer.initialize;

public class FavoritesFragmentViewHolder implements OnMapReadyCallback {
    public MapView mapView;
    public TextView title;
    public GoogleMap map;
    public ImageButton favoritesButton;
    public ImageButton shareButton;

    @NonNull
    private Context mContext;

    public FavoritesFragmentViewHolder(@NonNull Context context, @NonNull View view){
        super();
        mContext = context;
        mapView = view.findViewById(R.id.lite_listrow_map);
        title = view.findViewById(R.id.lite_listrow_text);
        favoritesButton = view.findViewById(R.id.list_item_favorite_favorite_button);
        shareButton = view.findViewById(R.id.list_item_favorite_share_button);
    }

    public void initializeMapView() {
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.getMapAsync(this);
        }
    }

    /**
     * Once I overlaid the map with a scrim, the map's visible areas disappear when
     * scrolling off and then on the screen.  So I call this method to refresh the map but
     * skip the create step in order to avoid flickering.
     */
    public void refreshMapView(){
        mapView.onCreate(null);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        initialize(mContext);

        map = googleMap;
        map.getUiSettings().setAllGesturesEnabled(false);
        map.getUiSettings().setCompassEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setIndoorLevelPickerEnabled(false);
        map.getUiSettings().setRotateGesturesEnabled(false);
        map.getUiSettings().setScrollGesturesEnabled(false);
        map.getUiSettings().setTiltGesturesEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setZoomGesturesEnabled(false);
        map.setMapType(GeomemorialApplication.getVisibleMapType());

        FavoritesMarkerInfo data = (FavoritesMarkerInfo) mapView.getTag();
        if (data != null) {
            CameraUpdateStrategy.setMapLocation(map, data, true);
        }
    }
}
