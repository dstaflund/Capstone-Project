package com.github.dstaflund.geomemorial.ui.fragment.searchresultitem.listener;

import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;
import android.view.View;

import com.github.dstaflund.geomemorial.common.util.SharedIntentManager;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo;
import com.github.dstaflund.geomemorial.ui.fragment.searchresultitem.SearchResultItemFormatter;

import static com.github.dstaflund.geomemorial.common.util.SharedIntentManager.shareGeomemorial;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.buildFor;

public class ShareButtonClickListener implements View.OnClickListener {

    @NonNull
    private Context mContext;

    @NonNull
    private SearchResultItemFormatter mFormatter;

    public ShareButtonClickListener(
        @NonNull Context context,
        @NonNull SearchResultItemFormatter formatter
    ){
        super();
        mContext = context;
        mFormatter = formatter;
    }

    @Override
    public void onClick(@NonNull View v) {
        String geomemorialId = mFormatter.getGeomemorialId();
        Cursor c = mContext.getContentResolver().query(
            buildFor(geomemorialId),
            null,
            null,
            null,
            null
        );

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            SharedIntentManager.Payload payload = new SharedIntentManager.Payload
                .Builder(mContext)
                .geomemorial(c.getString(GeomemorialInfo.IDX_GEOMEMORIAL))
                .latitude(c.getString(GeomemorialInfo.IDX_LATITUDE))
                .longitude(c.getString(GeomemorialInfo.IDX_LONGITUDE))
                .resident(c.getString(GeomemorialInfo.IDX_RESIDENT))
                .hometown(c.getString(GeomemorialInfo.IDX_HOMETOWN))
                .rank(c.getString(GeomemorialInfo.IDX_RANK))
                .obit(c.getString(GeomemorialInfo.IDX_OBIT))
                .build();
            shareGeomemorial(payload);
            c.close();
        }
    }
}
