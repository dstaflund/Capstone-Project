package com.github.dstaflund.geomemorial.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import com.github.dstaflund.geomemorial.ui.fragment.MapFragment;

public class CursorFinishedReceiver extends BroadcastReceiver {
    public static final String ACTION
        = CursorFinishedReceiver.class.getCanonicalName() + ".CURSOR_FINISHED";

    private MapFragment mMapFragment;

    public CursorFinishedReceiver setMapFragment(@NonNull MapFragment value){
        mMapFragment = value;
        return this;
    }

    public CursorFinishedReceiver(){
        super();
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        if (mMapFragment != null) {
            mMapFragment.updateCamera();
        }
    }

    public static IntentFilter getIntentFilter(){
        return new IntentFilter(ACTION);
    }

    public static void sendBroadcast(@NonNull Context context){
        Intent i = new Intent(ACTION);
        context.sendBroadcast(i);
    }
}
