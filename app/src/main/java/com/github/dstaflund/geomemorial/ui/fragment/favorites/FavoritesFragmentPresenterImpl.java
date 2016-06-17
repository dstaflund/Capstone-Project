package com.github.dstaflund.geomemorial.ui.fragment.favorites;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.ui.fragment.favorites.task.FavoritesFragmentAsyncTask;

public class FavoritesFragmentPresenterImpl implements FavoritesFragmentPresenter {
    private FavoritesFragmentView mView;

    public FavoritesFragmentPresenterImpl(@NonNull FavoritesFragmentView view){
        super();
        mView = view;
    }

    @Override
    public void onCreate(Bundle savedState) {
        mView.setRetainInstance(false);
        mView.setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup c, Bundle b) {
        View root = i.inflate(R.layout.fragment_favorites, c, false);

        mView.setFirstVisiblePosition(b == null ? -1 : b.getInt("first_visible_position"));
        mView.setViewRoot(root);

        new FavoritesFragmentAsyncTask(mView).execute();

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        GridView gridView = mView.getGridView();
        if (gridView != null) {
            outState.putInt("first_visible_position", gridView.getFirstVisiblePosition());
        }
    }
}
