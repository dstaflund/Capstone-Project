package com.github.dstaflund.geomemorial.ui.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;
import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.IntentManager;
import com.github.dstaflund.geomemorial.common.util.PreferencesManager;
import com.github.dstaflund.geomemorial.ui.activity.main.MainActivity;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context c = getApplicationContext();
        c.getResources().getConfiguration();

        if (! PreferencesManager.isFirstTime(c)){
            endActivity();
            return;
        }

        addSlide(
            AppIntroFragment.newInstance(
                getString(R.string.intro_project_title),
                getString(R.string.intro_project_description),
                R.drawable.intro_1_image,           // CC0, https://commons.wikimedia.org/w/index.php?curid=435678
                R.color.colorAccent1VeryLight
            )
        );
        addSlide(
            AppIntroFragment.newInstance(
                getString(R.string.intro_app_title),
                getString(R.string.intro_app_description),
                R.drawable.intro_2_screenshot,
                R.color.colorAccent1VeryLight
            )
        );
        addSlide(
            AppIntroFragment.newInstance(
                getString(R.string.intro_map_title),
                getString(R.string.intro_map_description),
                R.drawable.intro_3_screenshot,
                R.color.colorAccent1VeryLight
            )
        );
        addSlide(
            AppIntroFragment.newInstance(
                getString(R.string.intro_search_title),
                getString(R.string.intro_search_description),
                R.drawable.intro_4_screenshot,
                R.color.colorAccent1VeryLight
            )
        );
        addSlide(
            AppIntroFragment.newInstance(
                getString(R.string.intro_favorites_title),
                getString(R.string.intro_favorites_description),
                R.drawable.intro_5_screenshot,
                R.color.colorAccent1VeryLight
            )
        );
        addSlide(
            AppIntroFragment.newInstance(
                getString(R.string.intro_share_title),
                getString(R.string.intro_share_description),
                R.drawable.intro_6_screenshot,
                R.color.colorAccent1VeryLight
            )
        );
        addSlide(
            AppIntroFragment.newInstance(
                getString(R.string.intro_settings_title),
                getString(R.string.intro_settings_description),
                R.drawable.intro_7_screenshot,
                R.color.colorAccent1VeryLight
            )
        );
        addSlide(
            AppIntroFragment.newInstance(
                getString(R.string.intro_widget_title),
                getString(R.string.intro_widget_description),
                R.drawable.intro_8_screenshot,
                R.color.colorAccent1VeryLight
            )
        );

        setBarColor();
        setSkipButtonEnabled(true);
        setProgressIndicator();
        setTitle(R.string.app_name);
    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        endActivity();
    }

    @Override
    protected void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        endActivity();
    }

    private void endActivity(){
        Context c = getApplicationContext();
        PreferencesManager.setFirstTime(c, false);
        IntentManager.startActivity(c, MainActivity.class);
        finish();
    }

    private void setBarColor(){
        Context c = getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setBarColor(c.getColor(R.color.colorPrimaryDark));
        }
        else {
            //noinspection deprecation
            setBarColor(c.getResources().getColor(R.color.colorPrimaryDark));
        }
    }
}