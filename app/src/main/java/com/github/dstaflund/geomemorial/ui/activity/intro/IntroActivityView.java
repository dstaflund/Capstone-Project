package com.github.dstaflund.geomemorial.ui.activity.intro;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

public interface IntroActivityView {
    void addSlide(@NonNull Fragment fragment);
    void finish();
    void setBarColor(int color);
    void setProgressButtonEnabled(boolean b);
    void setTitle(int app_name);
    void showSkipButton(boolean b);

    Context getContext();

}
