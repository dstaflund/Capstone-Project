package com.github.dstaflund.geomemorial.ui.activity.preferences;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PreferencesActivity extends AppCompatActivity implements PreferencesActivityView {
    private PreferencesActivityPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);

        mPresenter = new PreferencesActivityPresenterImpl(this);
        mPresenter.onCreate(savedState);
    }
}
