package com.github.dstaflund.geomemorial.ui.activity.main.callback;

import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.Toast;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract;
import com.github.dstaflund.geomemorial.ui.activity.main.MainActivity;
import com.github.dstaflund.geomemorial.ui.activity.main.MainActivityView;
import com.github.dstaflund.geomemorial.ui.activity.main.SearchRequest;

import static com.github.dstaflund.geomemorial.common.util.ToastManager.newToast;

public class MainLoaderManagerCallbacks implements LoaderCallbacks<Cursor>{
    private MainActivityView mView;

    public MainLoaderManagerCallbacks(@NonNull MainActivityView view){
        super();
        mView = view;
    }

    @Override
    @Nullable
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle args) {
        switch (loaderId) {
            case MainActivity.EMPTY_SEARCH:
                return null;
            default:
                if (args == null) {
                    return null;
                }

                SearchRequest searchRequest = new SearchRequest(args);
                Uri uri = searchRequest.getUri();
                String query = searchRequest.getQuery();
                String extraDataKey = searchRequest.getExtraDataKey();

                //  Free-form search
                if (uri == null && query != null && extraDataKey == null){
                    return new CursorLoader(
                        mView.getContext(),
                        GeomemorialDbContract.MarkerInfo.CONTENT_URI,
                        GeomemorialDbContract.MarkerInfo.DEFAULT_PROJECTION,
                        GeomemorialDbContract.MarkerInfo.CONSTRAINT_BY_SEARCH_CRITERIA,
                        GeomemorialDbContract.MarkerInfo.getSelectionArgsFor(query),
                        GeomemorialDbContract.MarkerInfo.SORT_ORDER_RESIDENT
                    );
                }

                //  Suggestion Search
                else if (uri != null && query != null && extraDataKey != null){
                    return new CursorLoader(
                        mView.getContext(),
                        uri,
                        GeomemorialDbContract.MarkerInfo.DEFAULT_PROJECTION,
                        query,
                        GeomemorialDbContract.MarkerInfo.getSelectionArgsFor(extraDataKey),
                        GeomemorialDbContract.MarkerInfo.SORT_ORDER_RESIDENT
                    );
                }

                //  Previous Search
                else if (uri == null && query == null && extraDataKey != null){
                    return new CursorLoader(
                        mView.getContext(),
                        GeomemorialDbContract.MarkerInfo.CONTENT_URI,
                        GeomemorialDbContract.MarkerInfo.DEFAULT_PROJECTION,
                        GeomemorialDbContract.MarkerInfo.CONSTRAINT_BY_SEARCH_CRITERIA,
                        GeomemorialDbContract.MarkerInfo.getSelectionArgsFor(extraDataKey),
                        GeomemorialDbContract.MarkerInfo.SORT_ORDER_RESIDENT
                    );
                }
                else {
                    return null;
                }
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, @Nullable Cursor data) {
        switch (loader.getId()) {
            case MainActivity.EMPTY_SEARCH:
                mView.clearMap();
                mView.swapCursor(null);
                break;
            default:
                Resources r = mView.getContext().getResources();
                if (data == null || data.getCount() == 0) {
                    if (mView.isDisplayToast()) {
                        newToast(
                            mView.getContext(),
                            null,
                            mView.getContext().getString(R.string.toast_search_results_empty),
                            Toast.LENGTH_SHORT
                        );
                    }
                } else if (data.getCount() <= r.getInteger(R.integer.max_visible_memorials)) {
                    if (mView.isDisplayToast()) {
                        newToast(
                            mView.getContext(),
                            null,
                            mView.getContext().getString(
                                R.string.toast_search_results_normal,
                                data.getCount()
                            ),
                            Toast.LENGTH_SHORT
                        );
                    }
                } else {
                    if (mView.isDisplayToast()) {
                        newToast(
                            mView.getContext(),
                            null,
                            mView.getContext().getString(
                                R.string.toast_search_results_too_many,
                                r.getInteger(R.integer.max_visible_memorials)
                            ),
                            Toast.LENGTH_SHORT
                        );
                    }
                }
                mView.clearMap();
                mView.swapCursor(data);
                mView.setDisplayToast(true);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        try {
            mView.clearMap();
            mView.swapCursor(null);
        }

        catch(IllegalStateException e){
            // Eat it for now
        }
    }
}
