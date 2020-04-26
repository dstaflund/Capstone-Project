package com.github.dstaflund.geomemorial.ui.activity.intro;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.IntentManager;
import com.github.dstaflund.geomemorial.common.util.PreferencesManager;
import com.github.dstaflund.geomemorial.ui.activity.main.MainActivity;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivityPresenterImpl implements IntroActivityPresenter {
    private static final Integer[] sTitleIds = new Integer[]{
        R.string.intro_project_title,
        R.string.intro_app_title,
        R.string.intro_map_title,
        R.string.intro_search_title,
        R.string.intro_favorites_title,
        R.string.intro_share_title,
        R.string.intro_settings_title,
        R.string.intro_widget_title
    };
    private static final Integer[] sDescriptionIds = new Integer[]{
        R.string.intro_project_description,
        R.string.intro_app_description,
        R.string.intro_map_description,
        R.string.intro_search_description,
        R.string.intro_favorites_description,
        R.string.intro_share_description,
        R.string.intro_settings_description,
        R.string.intro_widget_description
    };
    private static final Integer[] sDrawableIds = new Integer[]{
        R.drawable.intro_1_image,           // CC0, https://commons.wikimedia.org/w/index.php?curid=435678
        R.drawable.intro_2_screenshot,
        R.drawable.intro_3_screenshot,
        R.drawable.intro_4_screenshot,
        R.drawable.intro_5_screenshot,
        R.drawable.intro_6_screenshot,
        R.drawable.intro_7_screenshot,
        R.drawable.intro_8_screenshot
    };
    private static final Integer[] sColorIds = new Integer[]{
        R.color.colorAccent1VeryLight,
        R.color.colorAccent1VeryLight,
        R.color.colorAccent1VeryLight,
        R.color.colorAccent1VeryLight,
        R.color.colorAccent1VeryLight,
        R.color.colorAccent1VeryLight,
        R.color.colorAccent1VeryLight,
        R.color.colorAccent1VeryLight
    };

    private IntroActivityView mView;

    public IntroActivityPresenterImpl(@NonNull IntroActivityView view){
        super();
        mView = view;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState){
        Context c = mView.getContext();
        c.getResources().getConfiguration();

        if (! PreferencesManager.isFirstTime(c)){
            endActivity();
        }

        for(int i = 0;  i < sTitleIds.length;  i++){
            mView.addSlide(AppIntroFragment.newInstance(
                c.getString(sTitleIds[i]),
                c.getString(sDescriptionIds[i]),
                sDrawableIds[i],
                sColorIds[i]
            ));
        }

        setBarColor();
        mView.showSkipButton(true);
        mView.setProgressButtonEnabled(true);
        mView.setTitle(R.string.app_name);
    }

    private void setBarColor(){
        Context c = mView.getContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mView.setBarColor(c.getColor(R.color.colorPrimaryDark));
        }
        else {
            //noinspection deprecation
            mView.setBarColor(c.getResources().getColor(R.color.colorPrimaryDark));
        }
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
    }

    @Override
    public void onNextPressed() {
    }

    private void endActivity(){
        Context c = mView.getContext();
        PreferencesManager.setFirstTime(c, false);
        IntentManager.startActivity(c, MainActivity.class);
        mView.finish();
    }
}
