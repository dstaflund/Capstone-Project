package com.github.dstaflund.geomemorial.ui.activity.intro;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public interface IntroActivityView {
    void addSlide(@NonNull Fragment fragment);
    void finish();
    void setBarColor(int color);
    void setProgressButtonEnabled(boolean b);
    void setTitle(int app_name);
    void showSkipButton(boolean b);

    @NonNull
    Context getContext();

}
