package com.github.dstaflund.geomemorial.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.DateUtil;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo;

import java.util.Date;

import static android.os.Binder.clearCallingIdentity;
import static android.os.Binder.restoreCallingIdentity;

public class GeomemorialWidgetService extends RemoteViewsService {

    @Override
    @NonNull
    public RemoteViewsFactory onGetViewFactory(@NonNull Intent intent) {
        return new GeomemorialRemoteViewsFactory(getApplicationContext());
    }
}

class GeomemorialRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Cursor cursor;

    public GeomemorialRemoteViewsFactory(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = clearCallingIdentity();

        String today = DateUtil.DB_FORMAT.format(new Date());
        String matchingDate = '%' + today.substring(4);

        cursor = mContext.getContentResolver().query(
            GeomemorialInfo.CONTENT_URI,
            null,
            GeomemorialInfo.CONTRAIN_BY_OBIT,
            new String[] { matchingDate },
            GeomemorialInfo.DEFAULT_ORDER
        );

        restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
        }
        cursor = null;
    }

    @Override
    public int getCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    @Nullable
    public RemoteViews getViewAt(int position) {
        if (cursor == null || !cursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        rv.setTextViewText(
            R.id.widget_geomemorial,
            cursor.getString(GeomemorialInfo.IDX_GEOMEMORIAL)
        );
        rv.setTextViewText(R.id.widget_hometown, cursor.getString(GeomemorialInfo.IDX_HOMETOWN));
        rv.setTextViewText(R.id.widget_obit, cursor.getString(GeomemorialInfo.IDX_OBIT));
        rv.setTextViewText(R.id.widget_rank, cursor.getString(GeomemorialInfo.IDX_RANK));
        rv.setTextViewText(R.id.widget_resident, cursor.getString(GeomemorialInfo.IDX_RESIDENT));
        return rv;
    }

    @Override
    @Nullable
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if (cursor != null) {
            if (cursor.moveToPosition(position)) {
                return cursor.getLong(GeomemorialInfo.IDX_ID);
            }
        }
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}