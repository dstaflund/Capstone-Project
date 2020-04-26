package com.github.dstaflund.geomemorial.ui.activity.about;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity implements AboutActivityView {
    private AboutActivityPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);

        mPresenter = new AboutActivityPresenterImpl(this);
        mPresenter.onCreate(savedState);
    }

}
