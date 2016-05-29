package com.github.dstaflund.geomemorial.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.github.dstaflund.geomemorial.R;

public class GeomemorialWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(
        @NonNull Context context,
        @NonNull AppWidgetManager appWidgetManager,
        @NonNull int[] appWidgetIds
    ) {

        /**
         * For each Geomemorial widget on the homescreen...
         */
        for (int appWidgetId : appWidgetIds) {

            /*
             * Create the intent needed to invoke the service class.
             */
            Intent intent = new Intent(context, GeomemorialWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            /**
             * Set the remote adapter that will be used to update the widgets (needed since
             * we have developed a collection widget)
             */
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            rv.setRemoteAdapter(R.id.stack_view, intent);
            rv.setEmptyView(R.id.stack_view, R.id.empty_view);

            /**
             * And have the app widget manager update the widget for us.
             */
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }

        /**
         * Invoke the superclass method in case it has work to do.
         */
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}