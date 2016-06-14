package com.github.dstaflund.geomemorial.ui.fragment.map;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.dstaflund.geomemorial.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Darryl on 2016-06-13.
 */
public class MapFragmentInfoWindow implements GoogleMap.InfoWindowAdapter {

    @NonNull
    private final Context mContext;

    @NonNull
    private final ForegroundColorSpan mColorSpan;

    @NonNull
    private final TextView mTitle;

    @NonNull
    private final TextView mResident;

    @NonNull
    private final View mWindow;

    public MapFragmentInfoWindow(@NonNull Context context, @NonNull LayoutInflater li) {
        super();

        mContext = context;
        mWindow = li.inflate(R.layout.custom_info_window, null);
        mTitle = (TextView) mWindow.findViewById(R.id.title);
        mResident = (TextView) mWindow.findViewById(R.id.resident);
        mColorSpan = new ForegroundColorSpan(Color.RED);
    }

    @Override
    @NonNull
    public View getInfoWindow(@NonNull Marker marker) {
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
            mResident.setText(String.format(
                mContext.getString(R.string.string_format_name_pattern),
                resident
            ));
        }

        return mWindow;
    }

    @Nullable
    @Override
    public View getInfoContents(@Nullable Marker marker) {
        return null;
    }
}
