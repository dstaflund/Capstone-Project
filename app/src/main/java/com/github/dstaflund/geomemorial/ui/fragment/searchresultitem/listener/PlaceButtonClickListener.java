package com.github.dstaflund.geomemorial.ui.fragment.searchresultitem.listener;

import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;
import android.view.View;

import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo;
import com.github.dstaflund.geomemorial.ui.fragment.searchresultitem.SearchResultItemFormatter;
import com.google.android.gms.maps.model.LatLng;

import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.IDX_LATITUDE;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.IDX_LONGITUDE;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.buildFor;
import static com.github.dstaflund.geomemorial.receiver.PlaceButtonClickedReceiver.sendBroadcast;

public class PlaceButtonClickListener implements View.OnClickListener {

    @NonNull
    private Context mContext;

    @NonNull
    private SearchResultItemFormatter mFormatter;

    public PlaceButtonClickListener(
        @NonNull Context context,
        @NonNull SearchResultItemFormatter formatter
    ){
        super();
        mContext = context;
        mFormatter = formatter;
    }

    @Override
    public void onClick(@NonNull View v) {
        String id = mFormatter.getGeomemorialId();

        Cursor c = mContext.getContentResolver().query(
            buildFor(id),
            null,
            null,
            null,
            null
        );

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            LatLng latLng = new LatLng(
                Double.parseDouble(c.getString(IDX_LATITUDE)),
                Double.parseDouble(c.getString(IDX_LONGITUDE))
            );
            sendBroadcast(mContext, latLng);
            c.close();
        }
    }
}
