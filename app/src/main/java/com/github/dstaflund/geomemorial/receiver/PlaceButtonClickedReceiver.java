package com.github.dstaflund.geomemorial.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import com.github.dstaflund.geomemorial.ui.fragment.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class PlaceButtonClickedReceiver extends BroadcastReceiver {
    public static final String ACTION
        = PlaceButtonClickedReceiver.class.getCanonicalName() + ".PLACE_BUTTON_CLICKED";
    private static final String sPositionKey = "position_key";

    private MapFragment mMapFragment;

    public PlaceButtonClickedReceiver setMapFragment(@NonNull MapFragment value){
        mMapFragment = value;
        return this;
    }

    public PlaceButtonClickedReceiver(){
        super();
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        LatLng position = intent.getParcelableExtra(sPositionKey);
        mMapFragment.zoomInOn(position);
    }

    public static IntentFilter getIntentFilter(){
        return new IntentFilter(ACTION);
    }

    public static void sendBroadcast(@NonNull Context context, @NonNull LatLng position){
        Intent i = new Intent(ACTION);
        i.putExtra(sPositionKey, position);
        context.sendBroadcast(i);
    }
}
