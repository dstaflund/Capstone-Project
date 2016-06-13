package com.github.dstaflund.geomemorial.ui.fragment.preferences;

import android.content.Context;
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

import com.github.dstaflund.geomemorial.R;

import static com.github.dstaflund.geomemorial.common.util.PreferencesManager.isDefaultMapType;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

public class PreferencesFragment extends Fragment implements PreferencesFragmentView {
    private NestedScrollView mScrollView;
    private PreferencesFragmentPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        mPresenter = new PreferencesFragmentPresenterImpl(this);
        mPresenter.onCreate(savedState);
    }

    @Override
    @Nullable
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedState
    ) {
        return mPresenter != null
            ? mPresenter.onCreateView(inflater, container, savedState)
            : null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPresenter != null){
            mPresenter.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedState) {
        super.onViewStateRestored(savedState);
        if (mPresenter != null){
            mPresenter.onViewStateRestored(savedState);
        }
    }

    @Override
    public void setNestedScrollView(@Nullable NestedScrollView value) {
        mScrollView = value;
    }

    @Override
    public NestedScrollView getNestedScrollView() {
        return mScrollView;
    }

    protected static class PreferencesViewHolder {
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

        protected PreferencesViewHolder initialize(@NonNull Context c, @NonNull RadioGroup.OnCheckedChangeListener l) {
            normalButton.setChecked(isDefaultMapType(c, MAP_TYPE_NORMAL));
            terrainButton.setChecked(isDefaultMapType(c, MAP_TYPE_TERRAIN));
            satelliteButton.setChecked(isDefaultMapType(c, MAP_TYPE_SATELLITE));
            hybridButton.setChecked(isDefaultMapType(c, MAP_TYPE_HYBRID));
            radioGroup.setOnCheckedChangeListener(l);
            return this;
        }
    }
}
