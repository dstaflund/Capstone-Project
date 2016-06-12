package com.github.dstaflund.geomemorial.ui.activity.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity implements AboutActivityView {
    private AboutActivityPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);

        mPresenter = new AboutActivityPresenterImpl(this);
        mPresenter.onCreate(savedState);
    }

}
