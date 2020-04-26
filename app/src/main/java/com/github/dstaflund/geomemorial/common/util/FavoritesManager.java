package com.github.dstaflund.geomemorial.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.github.dstaflund.geomemorial.common.util.PreferencesManager.getSharedPreferences;

public final class FavoritesManager {
    private static final String sFavoriteKey = "favorites";

    @NonNull
    public static Set<String> getFavorites(@NonNull Context context){
        SharedPreferences prefs = getSharedPreferences(context);
        Set<String> favorites = prefs.getStringSet(sFavoriteKey, Collections.<String>emptySet());
        Set<String> scrubbed = new HashSet<>(favorites);
        scrubbed.remove(null);
        return scrubbed;
    }

    public static boolean isFavorite(@NonNull Context context, @NonNull String geomemorialId){
        return getFavorites(context).contains(geomemorialId);
    }

    public static boolean addFavorite(@NonNull Context context, @NonNull String geomemorialId){
        if (! isFavorite(context, geomemorialId)) {
            Set<String> favorites = getFavorites(context);
            favorites.add(geomemorialId);

            return getSharedPreferences(context)
                .edit()
                .putStringSet(sFavoriteKey, favorites)
                .commit();
        }
        return false;
    }

    public static boolean removeFavorite(@NonNull Context context, @NonNull String geomemorialId){
        if (isFavorite(context, geomemorialId)){
            Set<String> favorites = getFavorites(context);
            favorites.remove(geomemorialId);

            return getSharedPreferences(context)
                .edit()
                .putStringSet(sFavoriteKey, favorites)
                .commit();
        }
        return false;
    }

    private FavoritesManager(){
        super();
    }
}
