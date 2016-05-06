package com.github.dstaflund.geomemorial.fragment.map;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.dstaflund.geomemorial.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback,
    GoogleMap.OnMapLoadedCallback,
    GoogleMap.OnCameraChangeListener,
    GoogleMap.OnMarkerClickListener {

    private static final String sLogTag = MapFragment.class.getSimpleName();
    private static final LatLng sSwCorner = new LatLng(49, -110);
    private static final LatLng sNeCorner = new LatLng(60, -101);
    private static final LatLngBounds sSkBounds = new LatLngBounds(sSwCorner, sNeCorner);

    private BitmapDescriptor mBitmapDescriptor;
    private GoogleMap mMap;
    private MapFragmentListener mMapFragmentListener;

    public MapFragment() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        if (context == null || ! (MapFragmentListener.class.isAssignableFrom(context.getClass()))){
            throw new IllegalArgumentException("Context hasn't implemented MapFragmentListener");
        }

        super.onAttach(context);
        mMapFragmentListener = (MapFragmentListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(
        LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        Log.i(sLogTag, "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        MapsInitializer.initialize(getContext());
        mBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.geomemorial_poppy);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(sLogTag, "onMapReady");

        AppCompatActivity activity = (AppCompatActivity) getContext();

        mMap = googleMap;
        mMap.setMapType(MAP_TYPE_NORMAL);
        mMap.setInfoWindowAdapter(new MapInfoWindowAdapter(activity.getLayoutInflater()));
        mMap.setOnMapLoadedCallback(this);
        mMap.setOnCameraChangeListener(this);
        mMap.setOnMarkerClickListener(this);

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setRotateGesturesEnabled(true);

        mMapFragmentListener.onMapReady(mMap);
    }

    @Override
    public void onMapLoaded() {
        Log.i(sLogTag, "onMapLoaded");
        mMap.animateCamera(newLatLngBounds(sSkBounds, 0));
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Log.i(sLogTag, "onCameraChange");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.i(sLogTag, "onMarkerClick");
        Log.d(sLogTag, "Click Coordinate = " + marker.getPosition());
        return false;
    }
}
