package com.github.dstaflund.geomemorial.ui.fragment.preferences;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.ui.fragment.preferences.PreferencesFragment.PreferencesViewHolder;

import static android.widget.Toast.LENGTH_SHORT;
import static com.github.dstaflund.geomemorial.common.util.PreferencesManager.setMapType;
import static com.github.dstaflund.geomemorial.common.util.ToastManager.newToast;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

public class PreferencesFragmentPresenterImpl
    implements PreferencesFragmentPresenter, RadioGroup.OnCheckedChangeListener {
    private static final String sXPosKey = "X_POS";
    private static final String sYPosKey = "Y_POST";

    private PreferencesFragmentView mView;
    private Toast mToast;

    public PreferencesFragmentPresenterImpl(@NonNull PreferencesFragmentView view){
        super();
        mView = view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        mView.setRetainInstance(true);
        mView.setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup vg, @Nullable Bundle b) {
        View v = i.inflate(R.layout.fragment_preferences, vg, false);

        NestedScrollView scrollView = (NestedScrollView) v.findViewById(R.id.fragment_preferences_scroll_view);
        mView.setNestedScrollView(scrollView);

        PreferencesViewHolder h = new PreferencesViewHolder(v).initialize(mView.getContext(), this);
        v.setTag(h);

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        NestedScrollView v = mView.getNestedScrollView();
        if (v != null) {
            outState.putInt(sXPosKey, v.getScrollX());
            outState.putInt(sYPosKey, v.getScrollY());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedState) {
        NestedScrollView v = mView.getNestedScrollView();
        if (savedState != null) {
            v.setScrollX(savedState.getInt(sXPosKey));
            v.setScrollY(savedState.getInt(sYPosKey));
        }

    }

    @Override
    public void onCheckedChanged(@NonNull RadioGroup group, int buttonId) {
        Context c = mView.getContext();
        switch(buttonId){
            case R.id.normal_radio_button:
                setMapType(c, MAP_TYPE_NORMAL);
                mToast = newToast(
                    c,
                    mToast,
                    c.getString(R.string.toast_map_type_normal),
                    LENGTH_SHORT
                );
                break;

            case R.id.terrain_radio_button:
                setMapType(c, MAP_TYPE_TERRAIN);
                mToast = newToast(
                    c,
                    mToast,
                    c.getString(R.string.toast_map_type_terrain),
                    LENGTH_SHORT
                );
                break;

            case R.id.satellite_radio_button:
                setMapType(c, MAP_TYPE_SATELLITE);
                mToast = newToast(
                    c,
                    mToast,
                    c.getString(R.string.toast_map_type_satellite),
                    LENGTH_SHORT
                );
                break;

            case R.id.hybrid_radio_button:
                setMapType(c, MAP_TYPE_HYBRID);
                mToast = newToast(
                    c,
                    mToast,
                    c.getString(R.string.toast_map_type_hybrid),
                    LENGTH_SHORT
                );
                break;
        }
    }
}
