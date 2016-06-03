package com.github.dstaflund.geomemorial;

import android.app.Application;
import android.location.Location;
import android.support.annotation.Nullable;

import com.github.dstaflund.geomemorial.common.util.PreferencesManager;

public class GeomemorialApplication extends Application {
    private static int sVisibleMapType;
    private static boolean sMapLoaded;
    private static Location sLastLocation;

    public static int getVisibleMapType(){
        return sVisibleMapType;
    }

    public static void setVisibleMapType(final int value){
        sVisibleMapType = value;
    }

    public static boolean isMapLoaded(){
        return sMapLoaded;
    }

    public static void setMapLoaded(boolean value){
        sMapLoaded = value;
    }

    public static Location getLastLocation(){
        return sLastLocation;
    }

    public static void setLastLocation(@Nullable Location value){
        sLastLocation = value;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sVisibleMapType = PreferencesManager.getMapType(this);
        sMapLoaded = false;
    }
}
