package com.github.dstaflund.geomemorial.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.dstaflund.geomemorial.R;

import static android.widget.Toast.LENGTH_SHORT;
import static com.github.dstaflund.geomemorial.common.util.PreferencesManager.isDefaultMapType;
import static com.github.dstaflund.geomemorial.common.util.PreferencesManager.setMapType;
import static com.github.dstaflund.geomemorial.common.util.ToastManager.newToast;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

public class PreferencesFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private static final String X_POS_KEY = "X_POS";
    private static final String Y_POS_KEY = "Y_POST";

    private NestedScrollView mScrollView;
    private Toast mToast;

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setRetainInstance(true);
        setHasOptionsMenu(false);
    }

    @Override
    @NonNull
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedState
    ) {
        View mRoot = inflater.inflate(R.layout.fragment_preferences, container, false);
        mScrollView = (NestedScrollView) mRoot.findViewById(R.id.fragment_preferences_scroll_view);

        PreferencesViewHolder mHolder = new PreferencesViewHolder(mRoot);
        mHolder.normalButton.setChecked(isDefaultMapType(getContext(), MAP_TYPE_NORMAL));
        mHolder.terrainButton.setChecked(isDefaultMapType(getContext(), MAP_TYPE_TERRAIN));
        mHolder.satelliteButton.setChecked(isDefaultMapType(getContext(), MAP_TYPE_SATELLITE));
        mHolder.hybridButton.setChecked(isDefaultMapType(getContext(), MAP_TYPE_HYBRID));
        mHolder.radioGroup.setOnCheckedChangeListener(this);

        return mRoot;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mScrollView != null) {
            outState.putInt(X_POS_KEY, mScrollView.getScrollX());
            outState.putInt(Y_POS_KEY, mScrollView.getScrollY());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedState) {
        super.onViewStateRestored(savedState);
        if (savedState != null) {
            mScrollView.setScrollX(savedState.getInt(X_POS_KEY));
            mScrollView.setScrollY(savedState.getInt(Y_POS_KEY));
        }
    }

    @Override
    public void onCheckedChanged(@NonNull RadioGroup group, int buttonId) {
        switch(buttonId){
            case R.id.normal_radio_button:
                setMapType(getContext(), MAP_TYPE_NORMAL);
                mToast = newToast(
                    getContext(),
                    mToast,
                    getString(R.string.toast_map_type_normal),
                    LENGTH_SHORT
                );
                break;

            case R.id.terrain_radio_button:
                setMapType(getContext(), MAP_TYPE_TERRAIN);
                mToast = newToast(
                    getContext(),
                    mToast,
                    getString(R.string.toast_map_type_terrain),
                    LENGTH_SHORT
                );
                break;

            case R.id.satellite_radio_button:
                setMapType(getContext(), MAP_TYPE_SATELLITE);
                mToast = newToast(
                    getContext(),
                    mToast,
                    getString(R.string.toast_map_type_satellite),
                    LENGTH_SHORT
                );
                break;

            case R.id.hybrid_radio_button:
                setMapType(getContext(), MAP_TYPE_HYBRID);
                mToast = newToast(
                    getContext(),
                    mToast,
                    getString(R.string.toast_map_type_hybrid),
                    LENGTH_SHORT
                );
                break;
        }
    }

    private static class PreferencesViewHolder {
        @NonNull RadioGroup radioGroup;
        @NonNull RadioButton normalButton;
        @NonNull RadioButton terrainButton;
        @NonNull RadioButton satelliteButton;
        @NonNull RadioButton hybridButton;

        PreferencesViewHolder(@NonNull View view){
            radioGroup = (RadioGroup) view.findViewById(R.id.map_type_radio_group);
            normalButton = (RadioButton) radioGroup.findViewById(R.id.normal_radio_button);
            terrainButton = (RadioButton) radioGroup.findViewById(R.id.terrain_radio_button);
            satelliteButton = (RadioButton) radioGroup.findViewById(R.id.satellite_radio_button);
            hybridButton = (RadioButton) radioGroup.findViewById(R.id.hybrid_radio_button);
        }
    }
}
