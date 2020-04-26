package com.github.dstaflund.geomemorial.ui.activity.about;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.dstaflund.geomemorial.R;

public class AboutActivityPresenterImpl implements AboutActivityPresenter {
    private AboutActivityView mView;

    public AboutActivityPresenterImpl(@NonNull AboutActivityView view){
        super();
        mView = view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        mView.setContentView(R.layout.activity_about);
    }
}
