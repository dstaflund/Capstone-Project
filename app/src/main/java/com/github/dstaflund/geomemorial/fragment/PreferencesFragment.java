package com.github.dstaflund.geomemorial.fragment;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.github.dstaflund.geomemorial.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreferencesFragment extends PreferenceFragment {
    private static final String sLogTag = PreferencesFragment.class.getSimpleName();

    public PreferencesFragment() {
        super();
        Log.i(sLogTag, "<ctor>");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(sLogTag, "onCreate");

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
