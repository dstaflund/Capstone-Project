package com.github.dstaflund.geomemorial.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.dstaflund.geomemorial.R;

public class PreferencesActivity extends AppCompatActivity {
    private static final String sLogTag = PreferencesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
    }

    /**
     * Starts {@link PreferencesActivity}
     * @param context to work ith
     */
    public static void startActivity(final Context context){
        Log.d(sLogTag, String.format("Starting %s", sLogTag));
        final Intent intent = new Intent(context, PreferencesActivity.class);
        context.startActivity(intent);
    }
}
