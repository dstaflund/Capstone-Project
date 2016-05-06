package com.github.dstaflund.geomemorial.activity;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.fragment.map.MapFragmentListener;
import com.google.android.gms.maps.GoogleMap;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

public class MainActivity extends AppCompatActivity implements MapFragmentListener {
    private static final String sLogTag = MainActivity.class.getSimpleName();

    private GoogleMap mMap;
    private LinearLayout mRootView;
    private SearchRecentSuggestions mSearchRecentSuggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(sLogTag, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRootView = (LinearLayout) findViewById(R.id.activity_main_layout);

//        mSearchRecentSuggestions = new SearchRecentSuggestions(
//            this,
//            GeomemorialDbProvider.AUTHORITY,
//            GeomemorialDbProvider.MODE
//        );
//
//        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(sLogTag, "onCreateOptionsMenu");

        getMenuInflater().inflate(R.menu.menu_main_activity, menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            final ComponentName componentName = getComponentName();
            final SearchableInfo searchableInfo = searchManager.getSearchableInfo(componentName);
            searchView.setSearchableInfo(searchableInfo);
            searchView.setIconifiedByDefault(false);
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(sLogTag, "onOptionsItemSelected");

        switch (item.getItemId()) {
            case R.id.menu_home_activity_layer_normal:
                if (mMap != null) {
                    mMap.setMapType(MAP_TYPE_NORMAL);
                }
                item.setChecked(true);
                return true;
            case R.id.menu_home_activity_layer_hybrid:
                if (mMap != null) {
                    mMap.setMapType(MAP_TYPE_HYBRID);
                }
                item.setChecked(true);
                return true;
            case R.id.menu_home_activity_layer_terrain:
                if (mMap != null) {
                    mMap.setMapType(MAP_TYPE_TERRAIN);
                }
                item.setChecked(true);
                return true;
            case R.id.menu_home_activity_layer_satellite:
                if (mMap != null) {
                    mMap.setMapType(MAP_TYPE_SATELLITE);
                }
                item.setChecked(true);
                return true;
            case R.id.menu_home_clear_search_history:
                mSearchRecentSuggestions.clearHistory();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        Log.i(sLogTag, "onMapReady");
        mMap = map;
    }

    //    @Override
//    protected void onNewIntent(Intent intent) {
//        Log.i(sLogTag, "onNewIntent");
//
//        handleIntent(intent);
//    }

//    private void handleIntent(Intent intent) {
//        if (intent == null){
//            return;
//        }
//
//        if (Intent.ACTION_VIEW.equals(intent.getAction())){
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            doSearch(null, null, query);
//            return;
//        }
//
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            Uri uri = intent.getData();
//            String selection = intent.getStringExtra(SearchManager.QUERY);
//            String selectionArg = intent.getStringExtra(SearchManager.EXTRA_DATA_KEY);
//            mSearchRecentSuggestions.saveRecentQuery(selectionArg, null);
//            doSearch(uri, selection, selectionArg);
//        }
//    }
//
//    private void doSearch(Uri uri, String selection, String selectionArg) {
//        Bundle args = new Bundle();
//        args.putParcelable(SearchLoaderCallbacks.URI_KEY, uri);
//        args.putString(SearchLoaderCallbacks.SELECTION_KEY, selection);
//        args.putString(SearchLoaderCallbacks.SELECTION_ARG_KEY, selectionArg);
//
//        LoaderManager loaderManager = getSupportLoaderManager();
//        Loader<Cursor> loader = loaderManager.getLoader(SearchLoaderCallbacks.RESIDENT_LOADER_ID);
//
//        if (loader != null && ! loader.isReset()){
//            loaderManager.restartLoader(SearchLoaderCallbacks.RESIDENT_LOADER_ID, args, mSearchLoaderCallbacks);
//        } else {
//            loaderManager.initLoader(SearchLoaderCallbacks.RESIDENT_LOADER_ID, args, mSearchLoaderCallbacks);
//        }
//    }
}
