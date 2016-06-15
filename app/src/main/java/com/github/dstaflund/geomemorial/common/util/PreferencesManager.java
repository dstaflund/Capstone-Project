package com.github.dstaflund.geomemorial.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;

public final class PreferencesManager {
    private static final String sFavoritesFilename = "geomemorial.prefs";
    private static final String sMapTypeKey = "map_type";
    private static final String sFirstTimeKey = "first_time";

    public static SharedPreferences getSharedPreferences(@NonNull Context context){
        return context.getSharedPreferences(sFavoritesFilename, Context.MODE_PRIVATE);
    }

    public static boolean isFirstTime(@NonNull Context context){
        return getSharedPreferences(context).getBoolean(sFirstTimeKey, true);
    }

    public static void setFirstTime(@NonNull Context context, boolean value){
        getSharedPreferences(context)
            .edit()
            .putBoolean(sFirstTimeKey, value)
            .commit();
    }

    public static boolean isDefaultMapType(@NonNull Context context, int mapTypeId){
        return mapTypeId == PreferencesManager.getMapType(context);
    }

    public static int getMapType(@NonNull Context context){
        return getSharedPreferences(context).getInt(sMapTypeKey, GoogleMap.MAP_TYPE_HYBRID);
    }

    public static boolean setMapType(@NonNull Context context, int mapType){
        return getSharedPreferences(context)
            .edit()
            .putInt(sMapTypeKey, mapType)
            .commit();
    }

    public static boolean setLastViewPageItem(@NonNull Context context, int lastViewPageItem) {
        return getSharedPreferences(context)
            .edit()
            .putInt("last_view_page_item", lastViewPageItem)
            .commit();
    }

    public static int getLastViewPageItem(@NonNull Context context){
        return getSharedPreferences(context).getInt("last_view_page_item", 0);
    }
}
