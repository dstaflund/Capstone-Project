package com.github.dstaflund.geomemorial.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.dstaflund.geomemorial.R;

public class AboutActivity extends AppCompatActivity {
    private static final String sLogTag = AboutActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public static void startActivity(final Context context){
        Log.d(sLogTag, String.format("Starting %s", sLogTag));
        final Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }
}
