package com.github.dstaflund.geomemorial.ui.fragment.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public interface MapFragmentPresenter {
    void addMarker(MarkerOptions options);
    void clearMap();
    void onCreate(Bundle savedState);
    void onPause();
    void onResume();
    void onSaveInstanceState(Bundle outState);
    void setMapType(int mapTypeId);
    void updateCamera();
    void zoomInOn(LatLng latLng);

    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState);
}
