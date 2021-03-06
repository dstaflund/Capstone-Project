package com.github.dstaflund.geomemorial.ui.activity.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;

public interface IntroActivityPresenter {
    void init(@Nullable Bundle savedInstanceState);
    void onDonePressed();
    void onNextPressed();
    void onSkipPressed();
    void onSlideChanged();
}
