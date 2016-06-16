package com.github.dstaflund.geomemorial.ui.fragment.map;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.dstaflund.geomemorial.common.util.MarkerMap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements MapFragmentView {
    private GoogleMap mMap;
    private MarkerMap mVisibleMarkers = new MarkerMap();

    private boolean mIgnoreCameraZoom;
    private BroadcastReceiver mCursorFinishedReceiver;
    private BroadcastReceiver mPlaceButtonClickedReceiver;
    private BroadcastReceiver mRecordFinishedReceiver;
    private IntentFilter mCursorFinishedIntentFilter;
    private IntentFilter mPlaceButtonClickedIntentFilter;
    private IntentFilter mRecordFinishedIntentFilter;
    private MapFragmentPresenter mPresenter;
    private LatLng mRestoredTarget;
    private float mRestoredZoom;

    public MapFragment(){
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        mPresenter = new MapFragmentPresenterImpl(this);
        mPresenter.onCreate(savedState);
    }

    @Override
    @NonNull
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b) {
        if (mPresenter != null){
            return mPresenter.onCreateView(i, c, b);
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null){
            mPresenter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null){
            mPresenter.onPause();
        }
    }

    //  Adaptation of Google's SaveStateDemoActivity
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPresenter != null){
            mPresenter.onSaveInstanceState(outState);
        }
    }

    public void ignoreCameraZoom(boolean value){
        mIgnoreCameraZoom = value;
    }

    public void clearMap(){
        if (mPresenter != null) {
            mPresenter.clearMap();
        }
    }

    public void addMarker(@NonNull MarkerOptions options){
        if (mPresenter != null){
            mPresenter.addMarker(options);
        }
    }

    /**
     * IGNORE ZOOM CANDIDATE
     */
    public void updateCamera(){
        if (mPresenter != null){
            mPresenter.updateCamera();
        }
    }

    /**
     * IGNORE ZOOM CANDIDATE
     */
    public void zoomInOn(@NonNull LatLng latLng){
        if (mPresenter != null){
            mPresenter.zoomInOn(latLng);
        }
    }

    public void setMapType(int mapTypeId) {
        if (mPresenter != null){
            mPresenter.setMapType(mapTypeId);
        }
    }

    @Override
    public void setCursorFinishedReceiver(BroadcastReceiver value) {
        mCursorFinishedReceiver = value;
    }

    @Override
    public void setPlaceButtonClickedReceiver(BroadcastReceiver value) {
        mPlaceButtonClickedReceiver = value;
    }

    @Override
    public void setRecordFinishedReceiver(BroadcastReceiver value) {
        mRecordFinishedReceiver = value;
    }

    @Override
    public void setCursorFinishedIntentFilter(IntentFilter value) {
        mCursorFinishedIntentFilter = value;
    }

    @Override
    public void setPlaceButtonClickedIntentFilter(IntentFilter value) {
        mPlaceButtonClickedIntentFilter = value;
    }

    @Override
    public void setRecordFinishedIntentFilter(IntentFilter value) {
        mRecordFinishedIntentFilter = value;
    }

    @Override
    public void setRestoredTarget(LatLng value) {
        mRestoredTarget = value;
    }

    @Override
    public void setRestoredZoom(float value) {
        mRestoredZoom = value;
    }

    @Override
    public GoogleMap getGoogleMap(){
        return mMap;
    }

    @Override
    public LatLng getRestoredTarget() {
        return mRestoredTarget;
    }

    @Override
    public float getRestoredZoom() {
        return mRestoredZoom;
    }

    @Override
    public void setIgnoreCameraZoom(boolean value){
        mIgnoreCameraZoom = value;
    }

    @Override
    public LayoutInflater getLayoutInflater(Bundle b){
        return super.getLayoutInflater(b);
    }

    @Override
    public void setGoogleMap(GoogleMap value){
        mMap = value;
    }

    @Override
    public BroadcastReceiver getCursorFinishedReceiver() {
        return mCursorFinishedReceiver;
    }

    @Override
    public BroadcastReceiver getPlaceButtonClickedReceiver() {
        return mPlaceButtonClickedReceiver;
    }

    @Override
    public BroadcastReceiver getRecordFinishedReceiver() {
        return mRecordFinishedReceiver;
    }

    @Override
    public IntentFilter getCursorFinishedIntentFilter() {
        return mCursorFinishedIntentFilter;
    }

    @Override
    public IntentFilter getPlaceButtonClickedIntentFilter() {
        return mPlaceButtonClickedIntentFilter;
    }

    @Override
    public IntentFilter getRecordFinishedIntentFilter() {
        return mRecordFinishedIntentFilter;
    }

    @Override
    public MarkerMap getVisibleMarkers() {
        return mVisibleMarkers;
    }

    @Override
    public boolean getIgnoreCameraZoom() {
        return mIgnoreCameraZoom;
    }
}
