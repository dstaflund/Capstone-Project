package com.github.dstaflund.geomemorial.ui.fragment.favorites.listener;

import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.SharedIntentManager;

import static com.github.dstaflund.geomemorial.common.util.SharedIntentManager.shareGeomemorial;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.IDX_GEOMEMORIAL;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.IDX_HOMETOWN;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.IDX_LATITUDE;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.IDX_LONGITUDE;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.IDX_OBIT;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.IDX_RANK;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.IDX_RESIDENT;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.buildFor;
import static com.github.dstaflund.geomemorial.ui.fragment.favorites.FavoritesFragment.GEOMEMORIAL_TAG_KEY;

public class ShareButtonClickListener implements View.OnClickListener {

    @NonNull
    private Context mContext;

    public ShareButtonClickListener(@NonNull Context context){
        super();
        mContext = context;
    }

    @Override
    public void onClick(@NonNull View v) {
        switch(v.getId()){
            case R.id.list_item_favorite_share_button:
                ImageButton button = (ImageButton) v;
                Long id = (Long) button.getTag(GEOMEMORIAL_TAG_KEY);
                Cursor c = mContext.getContentResolver().query(
                    buildFor(id.toString()),
                    null,
                    null,
                    null,
                    null
                );

                if (c != null && c.getCount() > 0) {
                    c.moveToFirst();
                    SharedIntentManager.Payload payload
                        = new SharedIntentManager.Payload.Builder(mContext)
                        .geomemorial(c.getString(IDX_GEOMEMORIAL))
                        .latitude(c.getString(IDX_LATITUDE))
                        .longitude(c.getString(IDX_LONGITUDE))
                        .resident(c.getString(IDX_RESIDENT))
                        .hometown(c.getString(IDX_HOMETOWN))
                        .rank(c.getString(IDX_RANK))
                        .obit(c.getString(IDX_OBIT))
                        .build();
                    shareGeomemorial(payload);
                    c.close();
                }
                break;
            default:
        }
    }
}
