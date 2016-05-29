package com.github.dstaflund.geomemorial.ui.activity;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dstaflund.geomemorial.GeomemorialApplication;
import com.github.dstaflund.geomemorial.R;
import com.github.dstaflund.geomemorial.common.util.DateUtil;
import com.github.dstaflund.geomemorial.common.util.SharedIntentManager;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbProvider;
import com.github.dstaflund.geomemorial.ui.fragment.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.github.dstaflund.geomemorial.GeomemorialApplication.setVisibleMapType;
import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.addFavorite;
import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.isFavorite;
import static com.github.dstaflund.geomemorial.common.util.FavoritesManager.removeFavorite;
import static com.github.dstaflund.geomemorial.common.util.PreferencesManager.isDefaultMapType;
import static com.github.dstaflund.geomemorial.common.util.SharedIntentManager.shareGeomemorial;
import static com.github.dstaflund.geomemorial.common.util.ToastManager.newToast;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo.buildFor;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.fromResource;

public class MainActivity
    extends AppCompatActivity
    implements
    LoaderManager.LoaderCallbacks<Cursor>,
    NavigationView.OnNavigationItemSelectedListener{
    private static final String sLogTag = MainActivity.class.getSimpleName();

    public static final int EMPTY_SEARCH = -1;
    public static final int RESIDENT_LOADER_ID = 0;
    public static final String URI_KEY = "uri";
    public static final String SELECTION_KEY = "selection";
    public static final String SELECTION_ARG_KEY = "selectionArg";


    private CursorAdapter mSearchCursorAdapter;
    private SearchRecentSuggestions mSearchRecentSuggestions;
    private NavigationView mNavigationView;
    private MapFragment mMapFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        checkInitialMapType();

        mSearchCursorAdapter = new SearchCursorAdapter(this, null);
        mSearchRecentSuggestions = new SearchRecentSuggestions(
            this,
            GeomemorialDbProvider.AUTHORITY,
            GeomemorialDbProvider.MODE
        );

        GridView gridView = (GridView) findViewById(R.id.activity_search_listview);
        gridView.setAdapter(mSearchCursorAdapter);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Cursor> loader = loaderManager.getLoader(EMPTY_SEARCH);
        if (loader != null && ! loader.isReset()){
            loaderManager.restartLoader(EMPTY_SEARCH, null, this);
        } else {
            loaderManager.initLoader(EMPTY_SEARCH, null, this);
        }

        handleIntent(getIntent());
    }

    private void checkInitialMapType(){
        switch (GeomemorialApplication.getVisibleMapType()){
            case GoogleMap.MAP_TYPE_NORMAL:
                mNavigationView.getMenu().findItem(R.id.nav_layer_normal).setChecked(true);
                break;

            case GoogleMap.MAP_TYPE_TERRAIN:
                mNavigationView.getMenu().findItem(R.id.nav_layer_terrain).setChecked(true);
                break;

            case GoogleMap.MAP_TYPE_SATELLITE:
                mNavigationView.getMenu().findItem(R.id.nav_layer_satellite).setChecked(true);
                break;

            case GoogleMap.MAP_TYPE_HYBRID:
                mNavigationView.getMenu().findItem(R.id.nav_layer_hybrid).setChecked(true);
                break;
        }
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(@Nullable Intent intent) {
        if (intent == null){
            return;
        }

        if (Intent.ACTION_VIEW.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(null, null, query);
            return;
        }

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Uri uri = intent.getData();
            String selection = intent.getStringExtra(SearchManager.QUERY);
            String selectionArg = intent.getStringExtra(SearchManager.EXTRA_DATA_KEY);
            mSearchRecentSuggestions.saveRecentQuery(selectionArg, null);
            doSearch(uri, selection, selectionArg);
        }
    }

    private void doSearch(@NonNull Uri uri, @Nullable String selection, @Nullable String selectionArg) {
        Bundle args = new Bundle();
        args.putParcelable(URI_KEY, uri);
        args.putString(SELECTION_KEY, selection);
        args.putString(SELECTION_ARG_KEY, selectionArg);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Cursor> loader = loaderManager.getLoader(RESIDENT_LOADER_ID);

        if (loader != null && ! loader.isReset()){
            loaderManager.restartLoader(RESIDENT_LOADER_ID, args, this);
        } else {
            loaderManager.initLoader(RESIDENT_LOADER_ID, args, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            ComponentName componentName = getComponentName();
            SearchableInfo searchableInfo = searchManager.getSearchableInfo(componentName);
            searchView.setSearchableInfo(searchableInfo);
            searchView.setIconifiedByDefault(false);
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setMapType(int mapTypeId){
        mMapFragment.setMapType(mapTypeId);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean result;

        switch(item.getItemId()){
            case R.id.nav_camera:
                FavoritesActivity.startActivity(this);
                result = true;
                break;

            case R.id.nav_layer_normal:
                setMapType(MAP_TYPE_NORMAL);
                item.setChecked(isDefaultMapType(this, MAP_TYPE_NORMAL));
                mNavigationView.getMenu().findItem(R.id.nav_layer_terrain).setChecked(false);
                mNavigationView.getMenu().findItem(R.id.nav_layer_satellite).setChecked(false);
                mNavigationView.getMenu().findItem(R.id.nav_layer_hybrid).setChecked(false);
                setVisibleMapType(MAP_TYPE_NORMAL);
                result = true;
                break;

            case R.id.nav_layer_hybrid:
                setMapType(MAP_TYPE_HYBRID);
                item.setChecked(isDefaultMapType(this, MAP_TYPE_HYBRID));
                mNavigationView.getMenu().findItem(R.id.nav_layer_normal).setChecked(false);
                mNavigationView.getMenu().findItem(R.id.nav_layer_terrain).setChecked(false);
                mNavigationView.getMenu().findItem(R.id.nav_layer_satellite).setChecked(false);
                setVisibleMapType(MAP_TYPE_HYBRID);
                result = true;
                break;

            case R.id.nav_layer_terrain:
                setMapType(MAP_TYPE_TERRAIN);
                item.setChecked(isDefaultMapType(this, MAP_TYPE_TERRAIN));
                mNavigationView.getMenu().findItem(R.id.nav_layer_normal).setChecked(false);
                mNavigationView.getMenu().findItem(R.id.nav_layer_satellite).setChecked(false);
                mNavigationView.getMenu().findItem(R.id.nav_layer_hybrid).setChecked(false);
                setVisibleMapType(MAP_TYPE_TERRAIN);
                result = true;
                break;

            case R.id.nav_layer_satellite:
                setMapType(MAP_TYPE_SATELLITE);
                item.setChecked(isDefaultMapType(this, MAP_TYPE_SATELLITE));
                mNavigationView.getMenu().findItem(R.id.nav_layer_normal).setChecked(false);
                mNavigationView.getMenu().findItem(R.id.nav_layer_terrain).setChecked(false);
                mNavigationView.getMenu().findItem(R.id.nav_layer_hybrid).setChecked(false);
                setVisibleMapType(MAP_TYPE_SATELLITE);
                result = true;
                break;

            case R.id.nav_clear_search_history:
                mSearchRecentSuggestions.clearHistory();
                newToast(this, null, getString(R.string.toast_search_history_cleared), Toast.LENGTH_SHORT);
                result = true;
                break;


            case R.id.nav_manage:
                PreferencesActivity.startActivity(this);
                result = true;
                break;

            case R.id.nav_send:
                AboutActivity.startActivity(this);
                result = true;
                break;

            default:
                result = false;
        }

        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (dl != null) {
            dl.closeDrawer(GravityCompat.START);
        }

        return result;
    }

    @Override
    @Nullable
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle args) {
        switch (loaderId) {
            case EMPTY_SEARCH:
                return null;
            default:
                if (args == null){
                    Log.e(sLogTag, getString(R.string.log_main_loader_missing_arguments));
                    return null;
                }

                Uri uri = args.getParcelable(URI_KEY);
                String selection = args.getString(SELECTION_KEY);
                String selectionArg = args.getString(SELECTION_ARG_KEY);
                return new CursorLoader(
                    getApplicationContext(),
                    uri == null ? MarkerInfo.CONTENT_URI : uri,
                    MarkerInfo.DEFAULT_PROJECTION,
                    MarkerInfo.CONSTRAINT_BY_SEARCH_CRITERIA,
                    selectionArg == null
                        ? MarkerInfo.getSelectionArgsFor(selection)
                        : MarkerInfo.getSelectionArgsFor(selectionArg),
                    MarkerInfo.SORT_ORDER_RESIDENT
                );
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, @Nullable Cursor data) {
        switch (loader.getId()){
            case EMPTY_SEARCH:
                mSearchCursorAdapter.changeCursor(null);
                break;
            default:
                if (data == null || data.getCount() == 0){
                    Log.d(sLogTag, getResources().getString(R.string.log_search_too_few));
                    newToast(
                        this,
                        null,
                        getString(R.string.toast_search_results_empty),
                        Toast.LENGTH_SHORT
                    );
                }
                else if (data.getCount() <= getResources().getInteger(R.integer.max_visible_memorials)){
                    Log.d(sLogTag, getResources().getString(R.string.log_search_just_right));
                    newToast(
                        this,
                        null,
                        getString(
                            R.string.toast_search_results_normal,
                            data.getCount()
                        ),
                        Toast.LENGTH_SHORT
                    );
                }
                else {
                    Log.d(sLogTag, getResources().getString(R.string.log_search_too_many));
                    newToast(
                        this,
                        null,
                        getString(
                            R.string.toast_search_results_too_many,
                            getResources().getInteger(R.integer.max_visible_memorials)
                        ),
                        Toast.LENGTH_SHORT
                    );
                }
                mSearchCursorAdapter.changeCursor(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mSearchCursorAdapter.changeCursor(null);
    }

    public class SearchCursorAdapter extends CursorAdapter {
        private final Integer sViewHolderKey = R.integer.view_holder_id;
        private final Integer sGeomemorialTagKey = R.integer.geomemorial_tag_key;
        private final BitmapDescriptor sDescriptor = fromResource(R.mipmap.geomemorial_poppy);

        /**
         * Default constructor
         *
         * @param context of the adapter
         * @param cursor being used
         */
        public SearchCursorAdapter(
            @NonNull Context context,
            @NonNull Cursor cursor
        ) {
            super(context, cursor, 0);
        }

        /**
         * Creates and binds a new view for the adapter.
         *
         * @param context of the operator
         * @param cursor containing the view's initial data record
         * @param parent that will display the view
         * @return newly created view
         */
        @Override
        public @NonNull
        View newView(
            @NonNull Context context,
            @NonNull Cursor cursor,
            @NonNull ViewGroup parent
        ) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_search, parent, false);
            view.setTag(sViewHolderKey, new ViewHolder(view));
            bindView(view, context, cursor);
            return view;
        }

        @Override
        public void changeCursor(@Nullable Cursor cursor) {
            mMapFragment.clearMap();

            int count = 0;
            if (cursor != null && cursor.getCount() > 0) {
                int position = cursor.getPosition();
                while (cursor.moveToNext()) {
                    count += 1;
                    if (count > getResources().getInteger(R.integer.max_visible_memorials)){
                        break;
                    }
                    DataObject dataObject = new DataObject(cursor);
                    DataFormatter dataFormatter = new DataFormatter(getApplicationContext(), dataObject);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(dataFormatter.getLatLng());
                    markerOptions.icon(sDescriptor);
                    markerOptions.title(dataFormatter.getGeomemorial());
                    markerOptions.snippet(dataFormatter.getResident());

                    mMapFragment.addMarker(markerOptions);
                }

                mMapFragment.updateCamera();
                cursor.moveToPosition(position);
            }

            super.changeCursor(cursor);
        }

        /**
         * Binds a new cursor record to the given view
         *
         * @param view to bind cursor record to
         * @param context in which the binding is taking place
         * @param cursor containing the record to be bound
         */
        @Override
        public void bindView(
            @NonNull View view,
            @NonNull final Context context,
            @Nullable Cursor cursor
        ) {

            if (cursor != null) {
                DataObject dataObject = new DataObject(cursor);
                DataFormatter formatter = new DataFormatter(context, dataObject);

                ViewHolder holder = (ViewHolder) view.getTag(sViewHolderKey);
                holder.name.setText(formatter.getResident());
                holder.hometown.setText(formatter.getHometown());
                holder.rank.setText(formatter.getRank());
                holder.obit.setText(formatter.getObit());
                holder.geomemorial.setText(formatter.getGeomemorial());
                holder.ntsSheet.setText(formatter.getNtsSheet());
                holder.coordinate.setText(formatter.getCoordinate());

                holder.favoriteButton.setImageDrawable(
                    isFavorite(context, formatter.getGeomemorialId())
                        ? getApplicationContext().getResources().getDrawable(R.drawable.ic_favorite_black_24dp)
                        : getApplicationContext().getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp)
                );
                holder.favoriteButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(@NonNull View v) {
                        ImageButton button = (ImageButton) v;
                        String id = (String) button.getTag(sGeomemorialTagKey);
                        boolean isChecked = ! isFavorite(getApplicationContext(), id);

                        if (isChecked){
                            button.setImageResource(R.drawable.ic_favorite_black_24dp);
                            addFavorite(getApplicationContext(), id);
                        }
                        else {
                            button.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            removeFavorite(getApplicationContext(), id);
                        }
                    }
                });
                holder.favoriteButton.setTag(sGeomemorialTagKey, formatter.getGeomemorialId());
                holder.shareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(@NonNull View v) {
                        ImageButton button = (ImageButton) v;
                        String geomemorialId = (String) button.getTag(sGeomemorialTagKey);
                        Cursor c = getApplicationContext().getContentResolver().query(
                            buildFor(geomemorialId),
                            null,
                            null,
                            null,
                            null
                        );

                        if (c != null && c.getCount() > 0) {
                            c.moveToFirst();
                            SharedIntentManager.Payload payload = new SharedIntentManager.Payload
                                .Builder(context)
                                .geomemorial(c.getString(GeomemorialInfo.IDX_GEOMEMORIAL))
                                .latitude(c.getString(GeomemorialInfo.IDX_LATITUDE))
                                .longitude(c.getString(GeomemorialInfo.IDX_LONGITUDE))
                                .resident(c.getString(GeomemorialInfo.IDX_RESIDENT))
                                .hometown(c.getString(GeomemorialInfo.IDX_HOMETOWN))
                                .rank(c.getString(GeomemorialInfo.IDX_RANK))
                                .obit(c.getString(GeomemorialInfo.IDX_OBIT))
                                .build();
                            shareGeomemorial(getApplicationContext(), payload);
                        }
                    }
                });
                holder.shareButton.setTag(sGeomemorialTagKey, formatter.getGeomemorialId());
                holder.placeButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(@NonNull View v) {
                        ImageButton button = (ImageButton) v;
                        String id = (String) button.getTag(sGeomemorialTagKey);

                        Cursor c = getApplicationContext().getContentResolver().query(
                            buildFor(id),
                            null,
                            null,
                            null,
                            null
                        );

                        if (c != null && c.getCount() > 0) {
                            c.moveToFirst();
                            LatLng latLng = new LatLng(
                                Double.valueOf(c.getString(GeomemorialInfo.IDX_LATITUDE)),
                                Double.valueOf(c.getString(GeomemorialInfo.IDX_LONGITUDE))
                            );
                            mMapFragment.zoomInOn(latLng);
                        }
                    }
                });
                holder.placeButton.setTag(sGeomemorialTagKey, formatter.getGeomemorialId());
            }
        }
    }

    public static class ViewHolder {
        @NonNull public TextView coordinate;
        @NonNull public TextView geomemorial;
        @NonNull public TextView hometown;
        @NonNull public TextView name;
        @NonNull public TextView ntsSheet;
        @NonNull public TextView obit;
        @NonNull public TextView rank;
        @NonNull public ImageButton favoriteButton;
        @NonNull public ImageButton shareButton;
        @NonNull public ImageButton placeButton;

        public ViewHolder(@NonNull View v){
            coordinate = (TextView) v.findViewById(R.id.list_item_common_coordinate);
            geomemorial = (TextView) v.findViewById(R.id.list_item_common_geomemorial);
            hometown = (TextView) v.findViewById(R.id.list_item_common_hometown);
            name = (TextView) v.findViewById(R.id.list_item_common_name);
            ntsSheet = (TextView) v.findViewById(R.id.list_item_common_nts_sheet);
            obit = (TextView) v.findViewById(R.id.list_item_common_obit);
            rank = (TextView) v.findViewById(R.id.list_item_common_rank);
            favoriteButton = (ImageButton) v.findViewById(R.id.favorite_button);
            shareButton = (ImageButton) v.findViewById(R.id.share_button);
            placeButton = (ImageButton) v.findViewById(R.id.place_button);
        }
    }

    public static class DataObject {
        @NonNull public String geomemorial;
        @NonNull public String geomemorialId;
        @NonNull public String hometown;
        @NonNull public String ntsSheet;
        @NonNull public String ntsSheetName;
        @NonNull public String obit;
        @NonNull public String rank;
        @NonNull public String resident;

        public float latitude;
        public float longitude;

        public DataObject(@NonNull Cursor c){
            geomemorial = c.getString(MarkerInfo.DEFAULT_GEOMEMORIAL_IDX);
            geomemorialId = c.getString(MarkerInfo.DEFAULT_GEOMEMORIAL_ID_IDX);
            hometown = c.getString(MarkerInfo.DEFAULT_HOMETOWN_IDX);
            latitude = c.getFloat(MarkerInfo.DEFAULT_LATITUDE_IDX);
            longitude = c.getFloat(MarkerInfo.DEFAULT_LONGITUDE_IDX);
            ntsSheet = c.getString(MarkerInfo.DEFAULT_NTS_SHEET_IDX);
            ntsSheetName = c.getString(MarkerInfo.DEFAULT_NTS_SHEET_NAME_IDX);
            obit = c.getString(MarkerInfo.DEFAULT_OBIT_IDX);
            resident = c.getString(MarkerInfo.DEFAULT_RESIDENT_IDX);
            rank = c.getString(MarkerInfo.DEFAULT_RANK_IDX);
        }
    }

    public static class DataFormatter {

        @NonNull
        private Context mContext;

        @NonNull
        private DataObject mDataObject;

        public DataFormatter(@NonNull Context context, @NonNull DataObject dataObject){
            super();
            mContext = context;
            mDataObject = dataObject;
        }

        @NonNull
        public String getResident(){
            return mDataObject.resident.toUpperCase();
        }

        @NonNull
        public String getHometown(){
            return mDataObject.hometown;
        }

        @NonNull
        public String getRank(){
            return mDataObject.rank;
        }

        @NonNull
        public String getObit(){
            return DateUtil.toDisplayString(mContext, mDataObject.obit);
        }

        @NonNull
        public String getGeomemorial(){
            return String.format(
                mContext.getString(R.string.string_format_coordinate_pattern),
                mDataObject.geomemorial.toUpperCase()
            );
        }

        @NonNull
        public String getNtsSheet(){
            return String.format(
                mContext.getString(R.string.string_format_nts_sheet_pattern),
                mDataObject.ntsSheet,
                mDataObject.ntsSheetName
            );
        }

        @NonNull String getCoordinate(){
            return String.format(
                mContext.getString(R.string.string_format_lat_lng_pattern),
                Float.valueOf(mDataObject.latitude).toString(),
                Float.valueOf(mDataObject.longitude).toString()
            );
        }

        @NonNull
        public LatLng getLatLng(){
            return new LatLng(mDataObject.latitude, mDataObject.longitude);
        }

        @NonNull
        public String getGeomemorialId(){
            return mDataObject.geomemorialId;
        }
    }

    public static void startActivity(Context context){
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
    }
}
