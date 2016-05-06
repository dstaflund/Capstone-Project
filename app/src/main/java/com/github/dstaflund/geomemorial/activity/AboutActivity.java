package com.github.dstaflund.geomemorial.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.dstaflund.geomemorial.R;

public class AboutActivity extends AppCompatActivity {
    private static final String sLogTag = AboutActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(sLogTag, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
