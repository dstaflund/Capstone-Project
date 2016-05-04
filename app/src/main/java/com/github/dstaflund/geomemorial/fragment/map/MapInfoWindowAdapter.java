package com.github.dstaflund.geomemorial.fragment.map;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.dstaflund.geomemorial.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private static final String sLogTag = MapInfoWindowAdapter.class.getSimpleName();

    @NonNull
    private final View mWindow;

    @NonNull
    private final TextView mTitle;

    @NonNull
    private final TextView mResident;

    @NonNull
    private final ForegroundColorSpan mColorSpan;

    public MapInfoWindowAdapter(@NonNull LayoutInflater inflater){
        super();
        Log.i(sLogTag, "<ctor>");
        mWindow = inflater.inflate(R.layout.info_window_map, null);
        mTitle = (TextView) mWindow.findViewById(R.id.info_window_map_title);
        mResident = (TextView) mWindow.findViewById(R.id.info_window_map_resident);
        mColorSpan = new ForegroundColorSpan(Color.RED);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        Log.i(sLogTag, "getInfoWindow");
        mTitle.setText("");
        mResident.setText("");

        final String title = marker.getTitle();
        if (title != null) {
            final SpannableString titleText = new SpannableString(title);
            titleText.setSpan(mColorSpan, 0, title.length(), 0);
            mTitle.setText(titleText);
        }

        final String resident = marker.getSnippet();
        if (resident != null) {
            mResident.setText(String.format("Named After:  %s", resident));
        }

        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        Log.i(sLogTag, "getInfoContents");
        return null;
    }
}
