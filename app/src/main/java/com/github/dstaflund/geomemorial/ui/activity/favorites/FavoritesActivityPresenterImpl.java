package com.github.dstaflund.geomemorial.ui.activity.favorites;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.dstaflund.geomemorial.R;

public class FavoritesActivityPresenterImpl implements FavoritesActivityPresenter {
    private FavoritesActivityView mView;

    public FavoritesActivityPresenterImpl(@NonNull FavoritesActivityView view){
        super();
        mView = view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        mView.setContentView(R.layout.activity_favorites);
    }
}
