package com.github.dstaflund.geomemorial.ui.activity.main.callback;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.dstaflund.geomemorial.ui.activity.main.MainActivityView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static com.github.dstaflund.geomemorial.GeomemorialApplication.setLastLocation;
import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class MainConnectionCallbacks implements ConnectionCallbacks {
    private MainActivityView mView;

    public MainConnectionCallbacks(@NonNull MainActivityView view){
        mView = view;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Context context = mView.getContext();
        if (checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
         || checkSelfPermission(context, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED) {

            GoogleApiClient googleApiClient = mView.getGoogleApiClient();
            setLastLocation(FusedLocationApi.getLastLocation(googleApiClient));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }
}
