package com.github.dstaflund.geomemorial.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.dstaflund.geomemorial.R;

public class PreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_preferences);
    }

    public static void startActivity(@NonNull Context context){
        context.startActivity(new Intent(context, PreferencesActivity.class));
    }

}
