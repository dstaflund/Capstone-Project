package com.github.dstaflund.geomemorial.integration;

import android.app.SearchManager;
import android.content.SearchRecentSuggestionsProvider;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.SparseArray;

import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.Geomemorial;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.GeomemorialInfo;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.Hometown;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.Letter;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.NtsArea;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.NtsAreaCounts;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.NtsSeries;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.NtsSeriesCounts;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.NtsSheet;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.NtsSheetCounts;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.Obit;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.ProvincialCount;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.Rank;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.Resident;
import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.SearchSuggestions;

import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.CONTENT_AUTHORITY;

/**
 * Content provider of the Geomemorial application.
 *
 * <p>This content provider serves two pro=imary roles:</p>
 *
 * <ul>
 * <li>It handles user searches against the Geomemorial database</li>
 * <li>It offers search suggestions while the user types</li>
 * <li>It displays past searches that the user might be interested in again</li>
 * </ul>
 *
 * @author Darryl Staflund
 */
public class GeomemorialDbProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = GeomemorialDbContract.CONTENT_AUTHORITY;
    public static final int MODE = DATABASE_MODE_QUERIES;

    /**
     * Match codes assigned for the different search queries
     */
    public static class MatchCodes {
        public static final int RESIDENT = 0;
        public static final int RESIDENTS = 1;
        public static final int RANK = 2;
        public static final int RANKS = 3;
        public static final int OBIT = 4;
        public static final int OBITS = 5;
        public static final int NTS_SHEET = 6;
        public static final int NTS_SHEETS = 7;
        public static final int NTS_AREA = 8;
        public static final int NTS_AREAS = 9;
        public static final int NTS_SERIES_SG = 10;
        public static final int NTS_SERIES_PL = 11;
        public static final int LETTER = 12;
        public static final int LETTERS = 13;
        public static final int HOMETOWN = 14;
        public static final int HOMETOWNS = 15;
        public static final int GEOMEMORIAL = 16;
        public static final int GEOMEMORIALS = 17;
        public static final int PROVINCIAL_COUNT = 18;
        public static final int NTS_SERIES_COUNT = 19;
        public static final int NTS_SERIES_COUNTS = 20;
        public static final int NTS_AREA_COUNT = 21;
        public static final int NTS_AREA_COUNTS = 22;
        public static final int NTS_SHEET_COUNT = 23;
        public static final int NTS_SHEET_COUNTS = 24;
        public static final int MARKER_INFO = 25;
        public static final int MARKER_INFOS = 26;
        public static final int SEARCH_SUGGESTIONS = 27;
        public static final int GEOMEMORIAL_INFO = 28;
        public static final int GEOMEMORIALS_INFO = 29;
    }

    /**
     * Match paths identifying the different search queries
     */
    public static class MatchPaths {
        public static final String RESIDENT = Resident.TABLE_NAME + "/#";
        public static final String RESIDENTS = Resident.TABLE_NAME;
        public static final String RANK = Rank.TABLE_NAME + "/#";
        public static final String RANKS = Rank.TABLE_NAME;
        public static final String OBIT = Obit.TABLE_NAME + "/#";
        public static final String OBITS = Obit.TABLE_NAME;
        public static final String NTS_SHEET = NtsSheet.TABLE_NAME + "/#";
        public static final String NTS_SHEETS = NtsSheet.TABLE_NAME;
        public static final String NTS_AREA = NtsArea.TABLE_NAME + "/#";
        public static final String NTS_AREAS = NtsArea.TABLE_NAME;
        public static final String NTS_SERIES_SG = NtsSeries.TABLE_NAME + "/#";
        public static final String NTS_SERIES_PL = NtsSeries.TABLE_NAME;
        public static final String LETTER = Letter.TABLE_NAME + "/#";
        public static final String LETTERS = Letter.TABLE_NAME;
        public static final String HOMETOWN = Hometown.TABLE_NAME + "/#";
        public static final String HOMETOWNS = Hometown.TABLE_NAME;
        public static final String GEOMEMORIAL = Geomemorial.TABLE_NAME + "/#";
        public static final String GEOMEMORIALS = Geomemorial.TABLE_NAME;
        public static final String PROVINCIAL_COUNT = ProvincialCount.VIEW_NAME;
        public static final String NTS_SERIES_COUNT = NtsSeriesCounts.VIEW_NAME + "/#";
        public static final String NTS_SERIES_COUNTS = NtsSeriesCounts.VIEW_NAME;
        public static final String NTS_AREA_COUNT = NtsAreaCounts.VIEW_NAME + "/#";
        public static final String NTS_AREA_COUNTS = NtsAreaCounts.VIEW_NAME;
        public static final String NTS_SHEET_COUNT = NtsSheetCounts.VIEW_NAME + "/#";
        public static final String NTS_SHEET_COUNTS = NtsSheetCounts.VIEW_NAME;
        public static final String MARKER_INFO = MarkerInfo.VIRTUAL_TABLE_NAME + "/#";
        public static final String MARKER_INFOS = MarkerInfo.VIRTUAL_TABLE_NAME;
        public static final String SEARCH_SUGGESTIONS = SearchManager.SUGGEST_URI_PATH_QUERY;
        public static final String GEOMEMORIAL_INFO = GeomemorialInfo.VIEW_NAME + "/#";
        public static final String GEOMEMORIALS_INFO = GeomemorialInfo.VIEW_NAME;
    }

    /**
     * The Uri Match implementation that will be used to identify the types of uris supposed by
     * this content provider, and the queries, etc. that will be used to satisfy them.
     */
    public static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH){
        {
            addURI(CONTENT_AUTHORITY, MatchPaths.RESIDENT, MatchCodes.RESIDENT);
            addURI(CONTENT_AUTHORITY, MatchPaths.RESIDENTS, MatchCodes.RESIDENTS);
            addURI(CONTENT_AUTHORITY, MatchPaths.RANK, MatchCodes.RANK);
            addURI(CONTENT_AUTHORITY, MatchPaths.RANKS, MatchCodes.RANKS);
            addURI(CONTENT_AUTHORITY, MatchPaths.OBIT, MatchCodes.OBIT);
            addURI(CONTENT_AUTHORITY, MatchPaths.OBITS, MatchCodes.OBITS);
            addURI(CONTENT_AUTHORITY, MatchPaths.NTS_SHEET, MatchCodes.NTS_SHEET);
            addURI(CONTENT_AUTHORITY, MatchPaths.NTS_SHEETS, MatchCodes.NTS_SHEETS);
            addURI(CONTENT_AUTHORITY, MatchPaths.NTS_AREA, MatchCodes.NTS_AREA);
            addURI(CONTENT_AUTHORITY, MatchPaths.NTS_AREAS, MatchCodes.NTS_AREAS);
            addURI(CONTENT_AUTHORITY, MatchPaths.NTS_SERIES_SG, MatchCodes.NTS_SERIES_SG);
            addURI(CONTENT_AUTHORITY, MatchPaths.NTS_SERIES_PL, MatchCodes.NTS_SERIES_PL);
            addURI(CONTENT_AUTHORITY, MatchPaths.LETTER, MatchCodes.LETTER);
            addURI(CONTENT_AUTHORITY, MatchPaths.LETTERS, MatchCodes.LETTERS);
            addURI(CONTENT_AUTHORITY, MatchPaths.HOMETOWN, MatchCodes.HOMETOWN);
            addURI(CONTENT_AUTHORITY, MatchPaths.HOMETOWNS, MatchCodes.HOMETOWNS);
            addURI(CONTENT_AUTHORITY, MatchPaths.GEOMEMORIAL, MatchCodes.GEOMEMORIAL);
            addURI(CONTENT_AUTHORITY, MatchPaths.GEOMEMORIALS, MatchCodes.GEOMEMORIALS);
            addURI(CONTENT_AUTHORITY, MatchPaths.PROVINCIAL_COUNT, MatchCodes.PROVINCIAL_COUNT);
            addURI(CONTENT_AUTHORITY, MatchPaths.NTS_SERIES_COUNT, MatchCodes.NTS_SERIES_COUNT);
            addURI(CONTENT_AUTHORITY, MatchPaths.NTS_SERIES_COUNTS, MatchCodes.NTS_SERIES_COUNTS);
            addURI(CONTENT_AUTHORITY, MatchPaths.NTS_AREA_COUNT, MatchCodes.NTS_AREA_COUNT);
            addURI(CONTENT_AUTHORITY, MatchPaths.NTS_AREA_COUNTS, MatchCodes.NTS_AREA_COUNTS);
            addURI(CONTENT_AUTHORITY, MatchPaths.NTS_SHEET_COUNT, MatchCodes.NTS_SHEET_COUNT);
            addURI(CONTENT_AUTHORITY, MatchPaths.NTS_SHEET_COUNTS, MatchCodes.NTS_SHEET_COUNTS);
            addURI(CONTENT_AUTHORITY, MatchPaths.MARKER_INFO, MatchCodes.MARKER_INFO);
            addURI(CONTENT_AUTHORITY, MatchPaths.MARKER_INFOS, MatchCodes.MARKER_INFOS);
            addURI(CONTENT_AUTHORITY, MatchPaths.SEARCH_SUGGESTIONS, MatchCodes.SEARCH_SUGGESTIONS);
            addURI(CONTENT_AUTHORITY, MatchPaths.GEOMEMORIAL_INFO, MatchCodes.GEOMEMORIAL_INFO);
            addURI(CONTENT_AUTHORITY, MatchPaths.GEOMEMORIALS_INFO, MatchCodes.GEOMEMORIALS_INFO);
        }
    };

    /**
     * An array that match codes with the type of content item to be returned by them
     */
    public static final SparseArray<String> RETURN_TYPES = new SparseArray<String>(){
        {
            put(MatchCodes.RESIDENT, Resident.CONTENT_ITEM_TYPE);
            put(MatchCodes.RESIDENTS, Resident.CONTENT_TYPE);
            put(MatchCodes.RANK, Rank.CONTENT_ITEM_TYPE);
            put(MatchCodes.RANKS, Rank.CONTENT_TYPE);
            put(MatchCodes.OBIT, Obit.CONTENT_ITEM_TYPE);
            put(MatchCodes.OBITS, Obit.CONTENT_TYPE);
            put(MatchCodes.NTS_SHEET, NtsSheet.CONTENT_ITEM_TYPE);
            put(MatchCodes.NTS_SHEETS, NtsSheet.CONTENT_TYPE);
            put(MatchCodes.NTS_AREA, NtsArea.CONTENT_ITEM_TYPE);
            put(MatchCodes.NTS_AREAS, NtsArea.CONTENT_TYPE);
            put(MatchCodes.NTS_SERIES_SG, NtsSeries.CONTENT_ITEM_TYPE);
            put(MatchCodes.NTS_SERIES_PL, NtsSeries.CONTENT_TYPE);
            put(MatchCodes.LETTER, Letter.CONTENT_ITEM_TYPE);
            put(MatchCodes.LETTERS, Letter.CONTENT_TYPE);
            put(MatchCodes.HOMETOWN, Hometown.CONTENT_ITEM_TYPE);
            put(MatchCodes.HOMETOWNS, Hometown.CONTENT_TYPE);
            put(MatchCodes.GEOMEMORIAL, Geomemorial.CONTENT_ITEM_TYPE);
            put(MatchCodes.GEOMEMORIAL, Geomemorial.CONTENT_TYPE);
            put(MatchCodes.PROVINCIAL_COUNT, ProvincialCount.CONTENT_ITEM_TYPE);
            put(MatchCodes.NTS_SERIES_COUNT, NtsSeriesCounts.CONTENT_ITEM_TYPE);
            put(MatchCodes.NTS_SERIES_COUNTS, NtsSeriesCounts.CONTENT_TYPE);
            put(MatchCodes.NTS_AREA_COUNT, NtsAreaCounts.CONTENT_ITEM_TYPE);
            put(MatchCodes.NTS_AREA_COUNTS, NtsAreaCounts.CONTENT_TYPE);
            put(MatchCodes.NTS_SHEET_COUNT, NtsSheetCounts.CONTENT_ITEM_TYPE);
            put(MatchCodes.NTS_SHEET_COUNTS, NtsSheetCounts.CONTENT_TYPE);
            put(MatchCodes.MARKER_INFO, MarkerInfo.CONTENT_ITEM_TYPE);
            put(MatchCodes.MARKER_INFOS, MarkerInfo.CONTENT_TYPE);
            put(MatchCodes.SEARCH_SUGGESTIONS, SearchSuggestions.CONTENT_TYPE);
            put(MatchCodes.GEOMEMORIAL_INFO, GeomemorialInfo.CONTENT_ITEM_TYPE);
            put(MatchCodes.GEOMEMORIALS_INFO, GeomemorialInfo.CONTENT_TYPE);
        }
    };

    /**
     * Reference to the database helper class
     */
    private SQLiteOpenHelper mHelper;

    /**
     * Default constructor
     */
    public GeomemorialDbProvider() {
        super.setupSuggestions(AUTHORITY, MODE);
    }

    /**
     * Called when the content provider is first created.
     *
     * <p>Is responsible for initializing the database helper</p>
     *
     * @return true after the database has been created
     */
    @Override
    public boolean onCreate() {
        super.onCreate();
        if (getContext() != null) {
            mHelper = GeomemorialDbHelper.getInstance(getContext());
        }
        return true;
    }

    /**
     * Returns the content type of a given Uri.
     *
     * @param uri to get type of
     * @return uri's return type
     */
    @Nullable
    @Override
    public String getType(@NonNull final Uri uri) {
        final int matchCode = URI_MATCHER.match(uri);
        if (RETURN_TYPES.indexOfKey(matchCode) >= 0){
            return RETURN_TYPES.get(matchCode);
        }

        return super.getType(uri);
    }

    @Nullable
    @Override
    public Cursor query(
            @NonNull final Uri uri,
            @Nullable final String[] projection,
            @Nullable final String selection,
            @Nullable final String[] selectionArgs,
            @Nullable final String sortOrder
    ) {
        Cursor retCursor;
        final SQLiteDatabase db = mHelper.getReadableDatabase();

        switch (URI_MATCHER.match(uri)){
            case MatchCodes.RESIDENT:
                retCursor = db.query(
                        Resident.TABLE_NAME,
                        projection,
                        Resident.CONSTRAIN_BY_ID,
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        null
                );
                break;

            case MatchCodes.RESIDENTS:
                retCursor = db.query(
                        Resident.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MatchCodes.RANK:
                retCursor = db.query(
                        Rank.TABLE_NAME,
                        projection,
                        Rank.CONSTRAIN_BY_ID,
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        null
                );
                break;

            case MatchCodes.RANKS:
                retCursor = db.query(
                        Rank.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MatchCodes.OBIT:
                retCursor = db.query(
                        Obit.TABLE_NAME,
                        projection,
                        Obit.CONSTRAIN_BY_ID,
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        null
                );
                break;

            case MatchCodes.OBITS:
                retCursor = db.query(
                        Obit.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MatchCodes.NTS_SHEET:
                retCursor = db.query(
                        NtsSheet.TABLE_NAME,
                        projection,
                        NtsSheet.CONSTRAIN_BY_ID,
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        null
                );
                break;

            case MatchCodes.NTS_SHEETS:
                retCursor = db.query(
                        NtsSheet.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MatchCodes.NTS_AREA:
                retCursor = db.query(
                        NtsArea.TABLE_NAME,
                        projection,
                        NtsArea.CONSTRAIN_BY_ID,
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        null
                );
                break;

            case MatchCodes.NTS_AREAS:
                retCursor = db.query(
                        NtsArea.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MatchCodes.NTS_SERIES_SG:
                retCursor = db.query(
                        NtsSeries.TABLE_NAME,
                        projection,
                        NtsSeries.CONSTRAIN_BY_ID,
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        null
                );
                break;

            case MatchCodes.NTS_SERIES_PL:
                retCursor = db.query(
                        NtsSeries.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MatchCodes.LETTER:
                retCursor = db.query(
                        Letter.TABLE_NAME,
                        projection,
                        Letter.CONSTRAIN_BY_ID,
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        null
                );
                break;

            case MatchCodes.LETTERS:
                retCursor = db.query(
                        Letter.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MatchCodes.HOMETOWN:
                retCursor = db.query(
                        Hometown.TABLE_NAME,
                        projection,
                        Hometown.CONSTRAIN_BY_ID,
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        null
                );
                break;

            case MatchCodes.HOMETOWNS:
                retCursor = db.query(
                        Hometown.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MatchCodes.GEOMEMORIAL:
                retCursor = db.query(
                        Geomemorial.TABLE_NAME,
                        projection,
                        Geomemorial.CONSTRAIN_BY_ID,
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        null
                );
                break;

            case MatchCodes.GEOMEMORIALS:
                retCursor = db.query(
                        Geomemorial.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MatchCodes.PROVINCIAL_COUNT:
                retCursor = db.query(
                        ProvincialCount.VIEW_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        null
                );
                break;

            case MatchCodes.NTS_SERIES_COUNT:
                retCursor = db.query(
                        NtsSeriesCounts.VIEW_NAME,
                        projection,
                        NtsSeriesCounts.CONSTRAIN_BY_ID,
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        null
                );
                break;

            case MatchCodes.NTS_SERIES_COUNTS:
                retCursor = db.query(
                        NtsSeriesCounts.VIEW_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MatchCodes.NTS_AREA_COUNT:
                retCursor = db.query(
                        NtsAreaCounts.VIEW_NAME,
                        projection,
                        NtsAreaCounts.CONSTRAIN_BY_ID,
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        null
                );
                break;

            case MatchCodes.NTS_AREA_COUNTS:
                retCursor = db.query(
                        NtsAreaCounts.VIEW_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MatchCodes.NTS_SHEET_COUNT:
                retCursor = db.query(
                        NtsSheetCounts.VIEW_NAME,
                        projection,
                        NtsSheetCounts.CONSTRAIN_BY_ID,
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        null
                );
                break;

            case MatchCodes.NTS_SHEET_COUNTS:
                retCursor = db.query(
                        NtsSheetCounts.VIEW_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MatchCodes.MARKER_INFO:
                retCursor = db.query(
                        MarkerInfo.VIRTUAL_TABLE_NAME,
                        projection,
                        MarkerInfo.CONSTRAIN_BY_ID,
                        new String[]{uri.getLastPathSegment()},
                        null,
                        null,
                        null
                );
                break;

            case MatchCodes.MARKER_INFOS:
                retCursor = db.query(
                        MarkerInfo.VIRTUAL_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MatchCodes.SEARCH_SUGGESTIONS:
                if (selectionArgs == null
                    || selectionArgs.length == 0
                    || selectionArgs[0] == null
                    || selectionArgs[0].trim().length() == 0){
                    retCursor = super.query(uri, projection, selection, selectionArgs, sortOrder);
                } else {
                    retCursor = db.query(
                        SearchSuggestions.TABLE_NAME,
                        SearchSuggestions.DEFAULT_PROJECTION,
                        selection,
                        SearchSuggestions.getSelectionArgsFor(selectionArgs[0]),
                        null,
                        null,
                        SearchSuggestions.SORT_ORDER_SUGGEST_COLUMN_TEXT_1
                    );
                }
                break;

            case MatchCodes.GEOMEMORIAL_INFO:
                retCursor = db.query(
                    GeomemorialInfo.VIEW_NAME,
                    projection,
                    GeomemorialInfo.CONSTRAIN_BY_ID,
                    new String[]{uri.getLastPathSegment()},
                    null,
                    null,
                    null
                );
                break;

            case MatchCodes.GEOMEMORIALS_INFO:
                retCursor = db.query(
                    GeomemorialInfo.VIEW_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                );
                break;

            default:
                db.close();
                retCursor = null;
        }

        if (retCursor != null && getContext() != null) {
            retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return retCursor;
    }
}
