package com.github.dstaflund.geomemorial;

import android.app.Application;

import com.github.dstaflund.geomemorial.common.util.PreferencesManager;

public class GeomemorialApplication extends Application {
    private static int sVisibleMapType;
    private static boolean sMapLoaded;

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

    @Override
    public void onCreate() {
        super.onCreate();
        sVisibleMapType = PreferencesManager.getMapType(this);
        sMapLoaded = false;
    }
}
