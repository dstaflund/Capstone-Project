package com.github.dstaflund.geomemorial.ui.activity.favorites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class FavoritesActivity extends AppCompatActivity implements FavoritesActivityView {
    private FavoritesActivityPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);

        mPresenter = new FavoritesActivityPresenterImpl(this);
        mPresenter.onCreate(savedState);
    }
}
