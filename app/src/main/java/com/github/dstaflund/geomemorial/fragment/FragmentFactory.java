package com.github.dstaflund.geomemorial.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.github.dstaflund.geomemorial.fragment.map.MapFragment;

import java.util.HashMap;
import java.util.Map;

import static android.support.v4.app.Fragment.instantiate;
import static java.lang.String.format;

public final class FragmentFactory {
    public static final String ABOUT_FRAGMENT_TAG = AboutFragment.class.getSimpleName();
    public static final String FAVORITES_FRAGMENT_TAG = FavoritesFragment.class.getSimpleName();
    public static final String MAP_FRAGMENT_TAG = MapFragment.class.getSimpleName();
    public static final String NAVBAR_FRAGMENT_TAG = NavbarFragment.class.getSimpleName();
    public static final String PREFERENCES_FRAGMENT_TAG = PreferencesFragment.class.getSimpleName();
    public static final String SEARCH_FRAGMENT_TAG = SearchFragment.class.getSimpleName();

    private static final Map<String, String> sClassNames = new HashMap<String, String>(){{
        put(ABOUT_FRAGMENT_TAG, AboutFragment.class.getCanonicalName());
        put(FAVORITES_FRAGMENT_TAG, FavoritesFragment.class.getCanonicalName());
        put(MAP_FRAGMENT_TAG, MapFragment.class.getCanonicalName());
        put(NAVBAR_FRAGMENT_TAG, NavbarFragment.class.getCanonicalName());
        put(PREFERENCES_FRAGMENT_TAG, PreferencesFragment.class.getCanonicalName());
        put(SEARCH_FRAGMENT_TAG, SearchFragment.class.getCanonicalName());
    }};

    public static Fragment newFragment(
        @NonNull final AppCompatActivity activity,
        @NonNull final String fragmentTag
    ){
        return newFragment(activity, fragmentTag, null);
    }

    public static Fragment newFragment(
        @NonNull final AppCompatActivity activity,
        @NonNull final String fragmentTag,
        @Nullable Bundle bundle
    ){
        if (sClassNames.containsKey(fragmentTag)){
            return instantiate(activity, sClassNames.get(fragmentTag), bundle);
        }

        throw new IllegalArgumentException(format("%s is not a supported tag", fragmentTag));
    }

    private FragmentFactory(){
        super();
    }
}
