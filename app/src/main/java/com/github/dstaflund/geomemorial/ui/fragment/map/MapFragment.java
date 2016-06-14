package com.github.dstaflund.geomemorial.ui.fragment.map;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
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
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.github.dstaflund.geomemorial.GeomemorialApplication.isMapLoaded;
import static com.github.dstaflund.geomemorial.GeomemorialApplication.setMapLoaded;
import static com.github.dstaflund.geomemorial.common.util.PreferencesManager.getMapType;
import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds;
import static com.google.android.gms.maps.MapsInitializer.initialize;

public class MapFragment extends Fragment implements OnMapReadyCallback, OnMapLoadedCallback{
    private static final LatLng sSwCorner = new LatLng(49, -110);
    private static final LatLng sNeCorner = new LatLng(60, -101);
    private static final LatLngBounds sSaskBounds = new LatLngBounds(sSwCorner, sNeCorner);

    // Bundle keys for storing state on orientation change
    private static final String sTargetKey = "target";
    private static final String sZoomKey = "zoom";

    private GoogleMap mMap;
    private MarkerMap mVisibleMarkers = new MarkerMap();

    // Restored camera location
    private LatLng mRestoredTarget;
    private float mRestoredZoom;
    private boolean mIgnoreCameraZoom;
    private BroadcastReceiver mCursorFinishedReceiver;
    private BroadcastReceiver mPlaceButtonClickedReceiver;
    private BroadcastReceiver mRecordFinishedReceiver;
    private IntentFilter mCursorFinishedIntentFilter;
    private IntentFilter mPlaceButtonClickedIntentFilter;
    private IntentFilter mRecordFinishedIntentFilter;

    public MapFragment(){
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);

        mCursorFinishedReceiver = new CursorFinishedReceiver().setMapFragment(this);
        mPlaceButtonClickedReceiver = new PlaceButtonClickedReceiver().setMapFragment(this);
        mRecordFinishedReceiver = new RecordFinishedReceiver().setMapFragment(this);
        mCursorFinishedIntentFilter = CursorFinishedReceiver.getIntentFilter();
        mPlaceButtonClickedIntentFilter = PlaceButtonClickedReceiver.getIntentFilter();
        mRecordFinishedIntentFilter = RecordFinishedReceiver.getIntentFilter();

        if (savedState == null) {
            setRetainInstance(false);
            setHasOptionsMenu(false);
            setMapLoaded(false);
        }
        else {
            mRestoredTarget = savedState.getParcelable(sTargetKey);
            mRestoredZoom = savedState.getFloat(sZoomKey);
            setMapLoaded(true);
        }
    }

    @Override
    @NonNull
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedState
    ) {
        View mRoot = inflater.inflate(R.layout.fragment_map, container, false);

        initialize(getContext());

        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return mRoot;
    }

    @Override
    public void onResume() {
        Log.d("MapFragment", "onResume");
        super.onResume();
        getActivity().registerReceiver(mCursorFinishedReceiver, mCursorFinishedIntentFilter);
        getActivity().registerReceiver(mPlaceButtonClickedReceiver, mPlaceButtonClickedIntentFilter);
        getActivity().registerReceiver(mRecordFinishedReceiver, mRecordFinishedIntentFilter);
    }

    @Override
    public void onPause() {
        Log.d("MapFragment", "onPause");
        super.onPause();
        getActivity().unregisterReceiver(mCursorFinishedReceiver);
        getActivity().unregisterReceiver(mPlaceButtonClickedReceiver);
        getActivity().unregisterReceiver(mRecordFinishedReceiver);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setMapType(getMapType(getContext()));
        googleMap.setInfoWindowAdapter(
            new MapFragmentInfoWindow(getContext(), super.getLayoutInflater(null))
        );
        googleMap.setOnMapLoadedCallback(this);
        googleMap.setMyLocationEnabled(false);

        UiSettings ui = googleMap.getUiSettings();
        ui.setZoomControlsEnabled(true);
        ui.setScrollGesturesEnabled(true);
        ui.setZoomGesturesEnabled(true);
        ui.setTiltGesturesEnabled(true);
        ui.setRotateGesturesEnabled(true);
        ui.setMyLocationButtonEnabled(false);

        if (ActivityCompat.checkSelfPermission(
            getContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
            getContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) {

            googleMap.setMyLocationEnabled(true);
            ui.setMyLocationButtonEnabled(true);
        }

        mMap = googleMap;
    }


    /**
     * IGNORE ZOOM CANDIDATE
     */
    @Override
    public void onMapLoaded() {
        if (! isMapLoaded()) {
            mMap.animateCamera(newLatLngBounds(sSaskBounds, 0));
            setMapLoaded(true);
        }
        else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mRestoredTarget, mRestoredZoom));
            mIgnoreCameraZoom = false;
        }
    }

    //  Adaptation of Google's SaveStateDemoActivity
    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        if (mMap != null) {
            outState.putParcelable(sTargetKey, mMap.getCameraPosition().target);
            outState.putFloat(sZoomKey, mMap.getCameraPosition().zoom);
        }
    }

    public void ignoreCameraZoom(boolean value){
        mIgnoreCameraZoom = value;
    }

    public void clearMap(){
        mMap.clear();
        mVisibleMarkers.clear();
    }

    public void addMarker(@NonNull MarkerOptions options){
        Marker marker = mMap.addMarker(options);
        mVisibleMarkers.put(marker.getId(), marker);
    }

    /**
     * IGNORE ZOOM CANDIDATE
     */

    public void updateCamera(){
        if (! mIgnoreCameraZoom) {
            CameraUpdateStrategy.updateCamera(
                mMap,
                mVisibleMarkers,
                false
            );
        }
    }

    /**
     * IGNORE ZOOM CANDIDATE
     */
    public void zoomInOn(@NonNull LatLng latLng){
        if (! mIgnoreCameraZoom){
            CameraUpdateStrategy.zoomTo( mMap, mVisibleMarkers, latLng);
        }
    }

    public void setMapType(int mapTypeId) {
        mMap.setMapType(mapTypeId);
    }
}
