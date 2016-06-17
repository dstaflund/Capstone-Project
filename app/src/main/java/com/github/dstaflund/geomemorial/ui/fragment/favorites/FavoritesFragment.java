package com.github.dstaflund.geomemorial.ui.fragment.favorites;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.FavoritesMarkerInfo;

public class FavoritesFragment extends Fragment implements FavoritesFragmentView {
    public static final Integer GEOMEMORIAL_TAG_KEY = R.integer.geomemorial_tag_key;

    private GridView mList;
    private ArrayAdapter<FavoritesMarkerInfo> mAdapter;

    private int mFirstVisiblePosition;
    private View mRoot;
    private FavoritesFragmentPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        mPresenter = new FavoritesFragmentPresenterImpl(this);
        mPresenter.onCreate(savedState);
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b) {
        if (mPresenter != null){
            return mPresenter.onCreateView(i, c, b);
        }
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPresenter != null){
            mPresenter.onSaveInstanceState(outState);
        }
    }

    @Override
    public void setAdapter(ArrayAdapter<FavoritesMarkerInfo> value){
        mAdapter = value;
    }

    @Override
    public GridView getGridView(){
        return mList;
    }

    @Override
    public GridView findGridView(){
        return (GridView) mRoot.findViewById(android.R.id.list);
    }

    @Override
    public View findEmptyView(){
        return mRoot.findViewById(R.id.empty_favorites);
    }

    @Override
    public void setViewRoot(View value) {
        mRoot = value;
    }

    @Override
    public void setFirstVisiblePosition(int value) {
        mFirstVisiblePosition = value;
    }

    @Override
    public int getFirstVisiblePosition(){
        return mFirstVisiblePosition;
    }

    @Override
    public void setGridView(GridView value){
        mList = value;
    }
}
