package com.github.dstaflund.geomemorial.ui.fragment.map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.CameraUpdateStrategy;
import com.github.dstaflund.geomemorial.common.util.MarkerMap;
import com.github.dstaflund.geomemorial.receiver.CursorFinishedReceiver;
import com.github.dstaflund.geomemorial.receiver.PlaceButtonClickedReceiver;
import com.github.dstaflund.geomemorial.receiver.RecordFinishedReceiver;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static com.github.dstaflund.geomemorial.GeomemorialApplication.isMapLoaded;
import static com.github.dstaflund.geomemorial.GeomemorialApplication.setMapLoaded;
import static com.github.dstaflund.geomemorial.common.util.PreferencesManager.getMapType;
import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds;
import static com.google.android.gms.maps.MapsInitializer.initialize;

public class MapFragmentPresenterImpl implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, MapFragmentPresenter {
    private static final LatLng sSwCorner = new LatLng(49, -110);
    private static final LatLng sNeCorner = new LatLng(60, -101);
    private static final LatLngBounds sSaskBounds = new LatLngBounds(sSwCorner, sNeCorner);

    // Bundle keys for storing state on orientation change
    private static final String sTargetKey = "target";
    private static final String sZoomKey = "zoom";

    private MapFragmentView mView;

    public MapFragmentPresenterImpl(@NonNull MapFragmentView view){
        super();
        mView = view;
    }

    @Override
    public void onCreate(Bundle savedState) {
        mView.setCursorFinishedReceiver(new CursorFinishedReceiver().setMapFragment((MapFragment) mView));
        mView.setPlaceButtonClickedReceiver(new PlaceButtonClickedReceiver().setMapFragment((MapFragment) mView));
        mView.setRecordFinishedReceiver(new RecordFinishedReceiver().setMapFragment((MapFragment) mView));
        mView.setCursorFinishedIntentFilter(CursorFinishedReceiver.getIntentFilter());
        mView.setPlaceButtonClickedIntentFilter(PlaceButtonClickedReceiver.getIntentFilter());
        mView.setRecordFinishedIntentFilter(RecordFinishedReceiver.getIntentFilter());

        if (savedState == null) {
            mView.setRetainInstance(false);
            mView.setHasOptionsMenu(false);
            setMapLoaded(false);
        }
        else {
            LatLng target = savedState.getParcelable(sTargetKey);
            mView.setRestoredTarget(target);
            mView.setRestoredZoom(savedState.getFloat(sZoomKey));
            setMapLoaded(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        View r = inflater.inflate(R.layout.fragment_map, container, false);

        initialize(mView.getContext());

        FragmentManager fm = mView.getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return r;
    }

    @Override
    public void onResume() {
        Activity a = mView.getActivity();
        if (a != null) {
            a.registerReceiver(mView.getCursorFinishedReceiver(), mView.getCursorFinishedIntentFilter());
            a.registerReceiver(mView.getPlaceButtonClickedReceiver(), mView.getPlaceButtonClickedIntentFilter());
            a.registerReceiver(mView.getRecordFinishedReceiver(), mView.getRecordFinishedIntentFilter());
        }
    }

    @Override
    public void onPause() {
        Activity a = mView.getActivity();
        if (a != null) {
            a.unregisterReceiver(mView.getCursorFinishedReceiver());
            a.unregisterReceiver(mView.getPlaceButtonClickedReceiver());
            a.unregisterReceiver(mView.getRecordFinishedReceiver());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        GoogleMap map = mView.getGoogleMap();
        if (map != null) {
            outState.putParcelable(sTargetKey, map.getCameraPosition().target);
            outState.putFloat(sZoomKey, map.getCameraPosition().zoom);
        }
    }

    @Override
    public void clearMap() {
        GoogleMap map = mView.getGoogleMap();
        if (map != null) {
            map.clear();
        }

        MarkerMap visibleMarkers = mView.getVisibleMarkers();
        if (visibleMarkers != null) {
            visibleMarkers.clear();
        }
    }

    @Override
    public void addMarker(MarkerOptions options) {
        GoogleMap map = mView.getGoogleMap();
        if (map != null) {
            Marker marker = map.addMarker(options);
            MarkerMap visibleMarkers = mView.getVisibleMarkers();
            if (visibleMarkers != null && marker != null) {
                visibleMarkers.put(marker.getId(), marker);
            }
        }
    }

    @Override
    public void updateCamera() {
        if (! mView.getIgnoreCameraZoom()) {
            CameraUpdateStrategy.updateCamera(mView.getGoogleMap(), mView.getVisibleMarkers(), false);
        }
    }

    @Override
    public void zoomInOn(LatLng latLng) {
        mView.setIgnoreCameraZoom(false);
        CameraUpdateStrategy.zoomTo(mView.getGoogleMap(), mView.getVisibleMarkers(), latLng);
    }

    @Override
    public void setMapType(int mapTypeId) {
        GoogleMap map = mView.getGoogleMap();
        if (map != null) {
            map.setMapType(mapTypeId);
        }
    }

    @Override
    public void onMapLoaded() {
        GoogleMap map = mView.getGoogleMap();
        if (map != null) {
            if (!isMapLoaded()) {
                map.animateCamera(newLatLngBounds(sSaskBounds, 0));
                setMapLoaded(true);
            }
            else {
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        mView.getRestoredTarget(),
                        mView.getRestoredZoom()
                    )
                );
                mView.setIgnoreCameraZoom(false);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Context c = mView.getContext();
        LayoutInflater li = mView.getLayoutInflater(null);

        googleMap.setMapType(getMapType(c));
        googleMap.setInfoWindowAdapter(new MapFragmentInfoWindow(c, li));
        googleMap.setOnMapLoadedCallback(this);
        googleMap.setMyLocationEnabled(false);

        UiSettings ui = googleMap.getUiSettings();
        ui.setZoomControlsEnabled(true);
        ui.setScrollGesturesEnabled(true);
        ui.setZoomGesturesEnabled(true);
        ui.setTiltGesturesEnabled(true);
        ui.setRotateGesturesEnabled(true);
        ui.setMyLocationButtonEnabled(false);

        if (checkSelfPermission(c, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
         || checkSelfPermission(c, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED) {

            googleMap.setMyLocationEnabled(true);
            ui.setMyLocationButtonEnabled(true);
        }

        mView.setGoogleMap(googleMap);
    }
}
