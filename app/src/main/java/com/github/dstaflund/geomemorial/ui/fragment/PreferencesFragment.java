package com.github.dstaflund.geomemorial.ui.fragment;

import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.github.dstaflund.geomemorial.R;

public class PreferencesFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference, rootKey);

        ListPreference preference = findPreference("map_type");
        if (preference != null) {
            preference.setSummaryProvider(ListPreference.SimpleSummaryProvider.getInstance());
        }
    }
}
