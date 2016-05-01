package com.github.dstaflund.geomemorial.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.github.dstaflund.geomemorial.R;

import static com.github.dstaflund.geomemorial.fragment.FragmentFactory.MAP_FRAGMENT_TAG;
import static com.github.dstaflund.geomemorial.fragment.FragmentFactory.SEARCH_FRAGMENT_TAG;
import static com.github.dstaflund.geomemorial.fragment.FragmentFactory.newFragment;

public class MainActivity extends AppCompatActivity {
    private LinearLayout mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRootView = (LinearLayout) findViewById(R.id.activity_main_layout);

        //  Add fragments to root view if needed
        if (getSupportFragmentManager().getFragments().isEmpty()) {
            final Fragment mapFragment = newFragment(this, MAP_FRAGMENT_TAG);
            final Fragment searchFragment = newFragment(this, SEARCH_FRAGMENT_TAG);

            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main_layout, mapFragment, MAP_FRAGMENT_TAG)
                .add(R.id.activity_main_layout, searchFragment, SEARCH_FRAGMENT_TAG)
                .commit();
        }
    }
}
