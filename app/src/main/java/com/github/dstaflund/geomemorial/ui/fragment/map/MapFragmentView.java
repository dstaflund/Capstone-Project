package com.github.dstaflund.geomemorial.ui.fragment.map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;

import com.github.dstaflund.geomemorial.common.util.MarkerMap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public interface MapFragmentView {
    void setCursorFinishedIntentFilter(IntentFilter value);
    void setCursorFinishedReceiver(BroadcastReceiver value);
    void setGoogleMap(GoogleMap googleMap);
    void setHasOptionsMenu(boolean b);
    void setIgnoreCameraZoom(boolean b);
    void setPlaceButtonClickedIntentFilter(IntentFilter value);
    void setPlaceButtonClickedReceiver(BroadcastReceiver value);
    void setRecordFinishedIntentFilter(IntentFilter value);
    void setRecordFinishedReceiver(BroadcastReceiver value);
    void setRestoredTarget(LatLng value);
    void setRestoredZoom(float value);
    void setRetainInstance(boolean b);

    boolean getIgnoreCameraZoom();
    float getRestoredZoom();

    Activity getActivity();
    BroadcastReceiver getCursorFinishedReceiver();
    BroadcastReceiver getPlaceButtonClickedReceiver();
    BroadcastReceiver getRecordFinishedReceiver();
    Context getContext();
    FragmentManager getChildFragmentManager();
    GoogleMap getGoogleMap();
    IntentFilter getCursorFinishedIntentFilter();
    IntentFilter getPlaceButtonClickedIntentFilter();
    IntentFilter getRecordFinishedIntentFilter();
    LatLng getRestoredTarget();
    LayoutInflater getLayoutInflater(Bundle b);
    MarkerMap getVisibleMarkers();
}
