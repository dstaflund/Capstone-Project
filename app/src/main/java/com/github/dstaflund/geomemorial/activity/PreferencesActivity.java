package com.github.dstaflund.geomemorial.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.fragment.PreferencesFragment;

public class PreferencesActivity extends Activity {
    private static final String sLogTag = PreferencesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(sLogTag, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        Fragment preferencesFragment = new PreferencesFragment();
        getFragmentManager()
            .beginTransaction()
            .add(R.id.activity_preferences_frame_layout, preferencesFragment)
            .commit();
    }
}
