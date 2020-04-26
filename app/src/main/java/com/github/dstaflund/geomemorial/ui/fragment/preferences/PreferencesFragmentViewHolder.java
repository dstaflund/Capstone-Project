package com.github.dstaflund.geomemorial.ui.fragment.preferences;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.github.dstaflund.geomemorial.R;

import static com.github.dstaflund.geomemorial.common.util.PreferencesManager.isDefaultMapType;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

class PreferencesFragmentViewHolder {

    @NonNull
    RadioGroup radioGroup;

    @NonNull
    RadioButton normalButton;

    @NonNull
    RadioButton terrainButton;

    @NonNull
    RadioButton satelliteButton;

    @NonNull
    RadioButton hybridButton;

    PreferencesFragmentViewHolder(@NonNull View view) {
        radioGroup = (RadioGroup) view.findViewById(R.id.map_type_radio_group);
        normalButton = (RadioButton) radioGroup.findViewById(R.id.normal_radio_button);
        terrainButton = (RadioButton) radioGroup.findViewById(R.id.terrain_radio_button);
        satelliteButton = (RadioButton) radioGroup.findViewById(R.id.satellite_radio_button);
        hybridButton = (RadioButton) radioGroup.findViewById(R.id.hybrid_radio_button);
    }

    protected PreferencesFragmentViewHolder initialize(@NonNull Context c, @NonNull RadioGroup.OnCheckedChangeListener l) {
        normalButton.setChecked(isDefaultMapType(c, MAP_TYPE_NORMAL));
        terrainButton.setChecked(isDefaultMapType(c, MAP_TYPE_TERRAIN));
        satelliteButton.setChecked(isDefaultMapType(c, MAP_TYPE_SATELLITE));
        hybridButton.setChecked(isDefaultMapType(c, MAP_TYPE_HYBRID));
        radioGroup.setOnCheckedChangeListener(l);
        return this;
    }
}
