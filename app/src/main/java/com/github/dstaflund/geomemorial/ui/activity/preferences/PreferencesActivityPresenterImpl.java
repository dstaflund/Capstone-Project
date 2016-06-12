package com.github.dstaflund.geomemorial.ui.activity.preferences;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.dstaflund.geomemorial.R;

public class PreferencesActivityPresenterImpl implements PreferencesActivityPresenter {
    private PreferencesActivityView mView;

    public PreferencesActivityPresenterImpl(@NonNull PreferencesActivityView view){
        super();
        mView = view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        mView.setContentView(R.layout.activity_preferences);
    }
}
