package com.github.dstaflund.geomemorial.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.PreferencesManager;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        getResources().getConfiguration();

        if (! PreferencesManager.isFirstTime(this)){
            endActivity();
        }

        // CC0, https://commons.wikimedia.org/w/index.php?curid=435678
        addSlide(AppIntroFragment.newInstance(
            getString(R.string.intro_project_title),
            getString(R.string.intro_project_description),
            R.drawable.intro_1_image,
            R.color.colorAccent1VeryLight
        ));
        addSlide(AppIntroFragment.newInstance(
            getString(R.string.intro_app_title),
            getString(R.string.intro_app_description),
            R.drawable.intro_2_screenshot,
            R.color.colorAccent1VeryLight
        ));
        addSlide(AppIntroFragment.newInstance(
            getString(R.string.intro_map_title),
            getString(R.string.intro_map_description),
            R.drawable.intro_3_screenshot,
            R.color.colorAccent1VeryLight
        ));
        addSlide(AppIntroFragment.newInstance(
            getString(R.string.intro_search_title),
            getString(R.string.intro_search_description),
            R.drawable.intro_4_screenshot,
            R.color.colorAccent1VeryLight
        ));
        addSlide(AppIntroFragment.newInstance(
            getString(R.string.intro_favorites_title),
            getString(R.string.intro_favorites_description),
            R.drawable.intro_5_screenshot,
            R.color.colorAccent1VeryLight
        ));
        addSlide(AppIntroFragment.newInstance(
            getString(R.string.intro_share_title),
            getString(R.string.intro_share_description),
            R.drawable.intro_6_screenshot,
            R.color.colorAccent1VeryLight
        ));
        addSlide(AppIntroFragment.newInstance(
            getString(R.string.intro_settings_title),
            getString(R.string.intro_settings_description),
            R.drawable.intro_7_screenshot,
            R.color.colorAccent1VeryLight
        ));
        addSlide(AppIntroFragment.newInstance(
            getString(R.string.intro_widget_title),
            getString(R.string.intro_widget_description),
            R.drawable.intro_8_screenshot,
            R.color.colorAccent1VeryLight
        ));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setBarColor(getColor(R.color.colorPrimaryDark));
        } else {
            //noinspection deprecation
            setBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        showSkipButton(true);
        setProgressButtonEnabled(true);

        this.setTitle(R.string.app_name);
    }

    @Override
    public void onSkipPressed() {
        endActivity();
    }

    @Override
    public void onDonePressed() {
        endActivity();
    }

    @Override
    public void onSlideChanged() {
        //  Do nothing.  We just need this in order to implement the base class.
    }

    @Override
    public void onNextPressed() {
        //  Do nothing.  We just need this in order to implement the base class.
    }

    private void endActivity(){
        PreferencesManager.setFirstTime(this, false);
        MainActivity.startActivity(this);
        finish();
    }
}