package com.github.dstaflund.geomemorial.ui.activity.main.listener;

import android.provider.SearchRecentSuggestions;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.IntentManager;
import com.github.dstaflund.geomemorial.ui.activity.about.AboutActivity;
import com.github.dstaflund.geomemorial.ui.activity.favorites.FavoritesActivity;
import com.github.dstaflund.geomemorial.ui.activity.main.MainActivityView;
import com.github.dstaflund.geomemorial.ui.activity.preferences.PreferencesActivity;

import static com.github.dstaflund.geomemorial.GeomemorialApplication.setVisibleMapType;
import static com.github.dstaflund.geomemorial.common.util.PreferencesManager.isDefaultMapType;
import static com.github.dstaflund.geomemorial.common.util.ToastManager.newToast;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

public class NavigationItemSelectedListener implements OnNavigationItemSelectedListener {
    private MainActivityView mView;

    public NavigationItemSelectedListener(@NonNull MainActivityView view){
        super();
        mView = view;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean result;
        NavigationView navigationView;

        switch (item.getItemId()) {
            case R.id.nav_camera:
                IntentManager.startActivity(mView.getContext(), FavoritesActivity.class);
                result = true;
                break;

            case R.id.nav_layer_normal:
                mView.setMapType(MAP_TYPE_NORMAL);
                item.setChecked(isDefaultMapType(mView.getContext(), MAP_TYPE_NORMAL));

                navigationView = mView.getNavigationView();
                if (navigationView != null) {
                    navigationView.getMenu().findItem(R.id.nav_layer_terrain).setChecked(false);
                    navigationView.getMenu().findItem(R.id.nav_layer_satellite).setChecked(false);
                    navigationView.getMenu().findItem(R.id.nav_layer_hybrid).setChecked(false);
                }

                setVisibleMapType(MAP_TYPE_NORMAL);
                result = true;
                break;

            case R.id.nav_layer_hybrid:
                mView.setMapType(MAP_TYPE_HYBRID);
                item.setChecked(isDefaultMapType(mView.getContext(), MAP_TYPE_HYBRID));

                navigationView = mView.getNavigationView();
                if (navigationView != null) {
                    navigationView.getMenu().findItem(R.id.nav_layer_normal).setChecked(false);
                    navigationView.getMenu().findItem(R.id.nav_layer_terrain).setChecked(false);
                    navigationView.getMenu().findItem(R.id.nav_layer_satellite).setChecked(false);
                }

                setVisibleMapType(MAP_TYPE_HYBRID);
                result = true;
                break;

            case R.id.nav_layer_terrain:
                mView.setMapType(MAP_TYPE_TERRAIN);
                item.setChecked(isDefaultMapType(mView.getContext(), MAP_TYPE_TERRAIN));

                navigationView = mView.getNavigationView();
                if (navigationView != null) {
                    navigationView.getMenu().findItem(R.id.nav_layer_normal).setChecked(false);
                    navigationView.getMenu().findItem(R.id.nav_layer_satellite).setChecked(false);
                    navigationView.getMenu().findItem(R.id.nav_layer_hybrid).setChecked(false);
                }

                setVisibleMapType(MAP_TYPE_TERRAIN);
                result = true;
                break;

            case R.id.nav_layer_satellite:
                mView.setMapType(MAP_TYPE_SATELLITE);
                item.setChecked(isDefaultMapType(mView.getContext(), MAP_TYPE_SATELLITE));

                navigationView = mView.getNavigationView();
                if (navigationView != null) {
                    navigationView.getMenu().findItem(R.id.nav_layer_normal).setChecked(false);
                    navigationView.getMenu().findItem(R.id.nav_layer_terrain).setChecked(false);
                    navigationView.getMenu().findItem(R.id.nav_layer_hybrid).setChecked(false);
                }

                setVisibleMapType(MAP_TYPE_SATELLITE);
                result = true;
                break;

            case R.id.nav_clear_search_history:
                SearchRecentSuggestions suggestions = mView.getSearchRecentSuggestions();
                if (suggestions != null) {
                    suggestions.clearHistory();
                }
                newToast(
                    mView.getContext(),
                    null,
                    mView.getContext().getString(R.string.toast_search_history_cleared),
                    Toast.LENGTH_SHORT
                );
                result = true;
                break;


            case R.id.nav_manage:
                IntentManager.startActivity(mView.getContext(), PreferencesActivity.class);
                result = true;
                break;

            case R.id.nav_send:
                IntentManager.startActivity(mView.getContext(), AboutActivity.class);
                result = true;
                break;

            default:
                result = false;
        }

        DrawerLayout dl = mView.getDrawerLayout();
        if (dl != null) {
            dl.closeDrawer(GravityCompat.START);
        }

        return result;
    }
}
