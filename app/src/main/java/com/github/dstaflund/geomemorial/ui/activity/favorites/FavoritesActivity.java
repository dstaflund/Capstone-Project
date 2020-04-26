package com.github.dstaflund.geomemorial.ui.activity.favorites;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FavoritesActivity extends AppCompatActivity implements FavoritesActivityView {
    private FavoritesActivityPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);

        mPresenter = new FavoritesActivityPresenterImpl(this);
        mPresenter.onCreate(savedState);
    }
}
