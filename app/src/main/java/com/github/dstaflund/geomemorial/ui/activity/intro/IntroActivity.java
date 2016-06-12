package com.github.dstaflund.geomemorial.ui.activity.intro;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.paolorotolo.appintro.AppIntro;

public class IntroActivity extends AppIntro implements IntroActivityView {
    private IntroActivityPresenter mPresenter;

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        mPresenter = new IntroActivityPresenterImpl(this);
        mPresenter.init(savedInstanceState);
    }

    @Override
    public void onSkipPressed() {
        if (mPresenter != null) {
            mPresenter.onSkipPressed();
        }
    }

    @Override
    public void onDonePressed() {
        if (mPresenter != null) {
            mPresenter.onDonePressed();
        }
    }

    @Override
    public void onSlideChanged() {
        if (mPresenter != null) {
            mPresenter.onSlideChanged();
        }
    }

    @Override
    public void onNextPressed() {
        if (mPresenter != null) {
            mPresenter.onNextPressed();
        }
    }

    @Override
    public Context getContext(){
        return getApplicationContext();
    }
}