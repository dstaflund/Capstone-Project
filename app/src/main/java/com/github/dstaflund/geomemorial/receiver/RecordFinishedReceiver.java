package com.github.dstaflund.geomemorial.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.NonNull;

import com.github.dstaflund.geomemorial.ui.fragment.map.MapFragment;
import com.github.dstaflund.geomemorial.ui.fragment.searchresultitem.SearchResultItemFormatter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RecordFinishedReceiver extends BroadcastReceiver {
    public static final String ACTION
        = RecordFinishedReceiver.class.getCanonicalName() + ".RECORD_FINISHED";

    private static final String sPositionKey = "position_key";
    private static final String sTitleKey = "title_key";
    private static final String sSnippetKey = "snippet_key";

    private MapFragment mMapFragment;

    public RecordFinishedReceiver setMapFragment(@NonNull MapFragment value){
        mMapFragment = value;
        return this;
    }

    public RecordFinishedReceiver(){
        super();
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        LatLng position = intent.getParcelableExtra(sPositionKey);

        MarkerOptions options = new MarkerOptions();
        options.position(position);
        options.title(intent.getStringExtra(sTitleKey));
        options.snippet(intent.getStringExtra(sSnippetKey));

        if (mMapFragment != null) {
            mMapFragment.addMarker(options);
        }
    }

    public static IntentFilter getIntentFilter(){
        return new IntentFilter(ACTION);
    }

    public static void sendBroadcast(
        @NonNull Context context,
        @NonNull SearchResultItemFormatter record
    ){
        Intent i = new Intent(ACTION);
        i.putExtra(sPositionKey, record.getLatLng());
        i.putExtra(sTitleKey, record.getGeomemorial());
        i.putExtra(sSnippetKey, record.getResident());
        context.sendBroadcast(i);
    }
}
