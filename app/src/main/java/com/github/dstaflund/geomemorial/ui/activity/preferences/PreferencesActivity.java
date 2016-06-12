package com.github.dstaflund.geomemorial.ui.activity.preferences;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class PreferencesActivity extends AppCompatActivity implements PreferencesActivityView {
    private PreferencesActivityPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);

        mPresenter = new PreferencesActivityPresenterImpl(this);
        mPresenter.onCreate(savedState);
    }
}
