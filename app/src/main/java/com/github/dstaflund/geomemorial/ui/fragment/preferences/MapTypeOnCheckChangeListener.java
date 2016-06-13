package com.github.dstaflund.geomemorial.ui.fragment.preferences;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.dstaflund.geomemorial.R;

import static android.widget.Toast.LENGTH_SHORT;
import static com.github.dstaflund.geomemorial.common.util.PreferencesManager.setMapType;
import static com.github.dstaflund.geomemorial.common.util.ToastManager.newToast;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

class MapTypeOnCheckChangeListener implements RadioGroup.OnCheckedChangeListener {
    private Toast mToast;
    private PreferencesFragmentView mView;

    MapTypeOnCheckChangeListener(@NonNull PreferencesFragmentView view){
        super();
        mView = view;
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
