package com.github.dstaflund.geomemorial.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import com.github.dstaflund.geomemorial.R;

public class MainActivity extends AppCompatActivity {
    private static final String sLogTag = MainActivity.class.getSimpleName();

    private LinearLayout mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(sLogTag, "Entering onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRootView = (LinearLayout) findViewById(R.id.activity_main_layout);
    }
}
