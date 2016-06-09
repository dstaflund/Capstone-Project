package com.github.dstaflund.geomemorial.integration;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import java.util.Set;

/**
 *
 */
public final class GeomemorialDbContract {
    public static final String CONTENT_AUTHORITY = "com.github.dstaflund.geomemorial";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String DATABASE_NAME = "geomemorial.db";
    public static final int DATABASE_VERSION = 21;

    // Sort Order Qualifiers
    public static final String SORT_ASC = " ASC";

    /**
     *
     */
    public static final class Geomemorial implements BaseColumns {
        public static final String TABLE_NAME = "geomemorial";

        // Uris
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
            .buildUpon()
            .appendPath(TABLE_NAME)
            .build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // Builders
        public static final Uri buildFor(@NonNull String id){
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static String getSelection(@NonNull final Set<String> favoriteIds){
            final String unscrubbedSelection = new StringBuilder()
                .append(_ID + " IN (")
                .append(new String(new char[favoriteIds.size()]).replace("\0", "?,"))
                .append(")")
                .toString();
            return unscrubbedSelection.replace(",)", ")");
        }

        public static String[] getSelectionArgs(@NonNull Set<String> favoriteIds){
            final String[] selectionArgs = new String[favoriteIds.size()];
            favoriteIds.toArray(selectionArgs);
            return selectionArgs;
        }


        // Columns
        public static final String COL_GEOMEMORIAL = "geomemorial";
        public static final String COL_LATITUDE = "latitude";
        public static final String COL_LONGITUDE = "longitude";

        // Constraints
        public static final String CONSTRAIN_BY_ID = TABLE_NAME + "." + _ID + " = ?";

        // Default projection
        public static final String[] DEFAULT_PROJECTION = new String[]{
                _ID,
                COL_GEOMEMORIAL,
                COL_LATITUDE,
                COL_LONGITUDE
        };

        // Default projection order
        public static final int DEFAULT_ID_IDX = 0;
        public static final int DEFAULT_GEOMEMORIAL_IDX = 1;
        public static final int DEFAULT_LATITUDE_IDX = 2;
        public static final int DEFAULT_LONGITUDE_IDX = 3;

        // Sort orders
        public static final String FAVORITES_SORT_ORDER = COL_GEOMEMORIAL + SORT_ASC;
    }

    public static final class GeomemorialInfo implements BaseColumns {
        public static final String VIEW_NAME = "geomemorial_info";

        // Uris
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
            .buildUpon()
            .appendPath(VIEW_NAME)
            .build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + VIEW_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + VIEW_NAME;

        //  Columns
        public static final String COL_GEOMEMORIAL = "geomemorial";
        public static final String COL_LATITUDE = "latitude";
        public static final String COL_LONGITUDE = "longitude";
        public static final String COL_RESIDENT = "resident";
        public static final String COL_HOMETOWN = "hometown";
        public static final String COL_RANK = "rank";
        public static final String COL_OBIT = "obit";
        public static final String COL_LETTER = "letter";

        // Builders
        public static final Uri buildFor(@NonNull String id){
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        // Constraints
        public static final String CONSTRAIN_BY_ID = VIEW_NAME + "." + _ID + " = ?";
        public static final String CONTRAIN_BY_OBIT = VIEW_NAME + "." + COL_OBIT + " LIKE ?";

        //  Default projection
        public static final String[] DEFAULT_PROJECTION = new String[] {
            _ID,
            COL_GEOMEMORIAL,
            COL_LATITUDE,
            COL_LONGITUDE,
            COL_RESIDENT,
            COL_HOMETOWN,
            COL_RANK,
            COL_OBIT,
            COL_LETTER
        };

        //  Default project order
        public static final int IDX_ID = 0;
        public static final int IDX_GEOMEMORIAL = 1;
        public static final int IDX_LATITUDE = 2;
        public static final int IDX_LONGITUDE = 3;
        public static final int IDX_RESIDENT = 4;
        public static final int IDX_HOMETOWN = 5;
        public static final int IDX_RANK = 6;
        public static final int IDX_OBIT = 7;
        public static final int IDX_LETTER = 8;

        // Default ordering
        public static final String DEFAULT_ORDER = VIEW_NAME + "." + COL_RESIDENT + " ASC, " +
            VIEW_NAME + "." + COL_HOMETOWN + " ASC";
    }

    public static final class Hometown implements BaseColumns {
        public static final String TABLE_NAME = "hometown";

        // Uris
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
            .buildUpon()
            .appendPath(TABLE_NAME)
            .build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // Columns
        public static final String COL_HOMETOWN = "hometown";

        // Constraints
        public static final String CONSTRAIN_BY_ID = TABLE_NAME + "." + _ID + " = ?";

        // Default projection
        public static final String[] DEFAULT_PROJECTION = new String[]{
                _ID,
                COL_HOMETOWN,
        };

        // Default projection order
        public static final int DEFAULT_ID_IDX = 0;
        public static final int DEFAULT_HOMETOWN_IDX = 1;
    }

    public static final class Letter implements BaseColumns {
        public static final String TABLE_NAME = "letter";

        // Uris
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
            .buildUpon()
            .appendPath(TABLE_NAME)
            .build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // Columns
        public static final String COL_LETTER = "letter";

        // Constraints
        public static final String CONSTRAIN_BY_ID = TABLE_NAME + "." + _ID + " = ?";

        // Default projection
        public static final String[] DEFAULT_PROJECTION = new String[]{
                _ID,
                COL_LETTER,
        };

        // Default projection order
        public static final int DEFAULT_ID_IDX = 0;
        public static final int DEFAULT_LETTER_IDX = 1;
    }

    public static final class Obit implements BaseColumns {
        public static final String TABLE_NAME = "obit";

        // Uris
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
            .buildUpon()
            .appendPath(TABLE_NAME)
            .build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // Columns
        public static final String COL_OBIT = "obit";

        // Constraints
        public static final String CONSTRAIN_BY_ID = TABLE_NAME + "." + _ID + " = ?";

        // Default projection
        public static final String[] DEFAULT_PROJECTION = new String[]{
                _ID,
                COL_OBIT,
        };

        // Default projection order
        public static final int DEFAULT_ID_IDX = 0;
        public static final int DEFAULT_OBIT_IDX = 1;
    }

    public static final class Rank implements BaseColumns {
        public static final String TABLE_NAME = "rank";

        // Uris
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
            .buildUpon()
            .appendPath(TABLE_NAME)
            .build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // Columns
        public static final String COL_RANK = "rank";

        // Constraints
        public static final String CONSTRAIN_BY_ID = TABLE_NAME + "." + _ID + " = ?";

        // Default projection
        public static final String[] DEFAULT_PROJECTION = new String[]{
                _ID,
                COL_RANK,
        };

        // Default projection order
        public static final int DEFAULT_ID_IDX = 0;
        public static final int DEFAULT_RANK_IDX = 1;
    }

    public static final class Resident implements BaseColumns {
        public static final String TABLE_NAME = "resident";

        // Uris
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
            .buildUpon()
            .appendPath(TABLE_NAME)
            .build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // Columns
        public static final String COL_RESIDENT = "resident";
        public static final String COL_GEOMEMORIAL_ID = "geomemorial_id";
        public static final String COL_RANK_ID = "rank_id";
        public static final String COL_HOMETOWN_ID = "hometown_id";
        public static final String COL_OBIT_ID = "obit_id";
        public static final String COL_LETTER_ID = "letter_id";

        // Constraints
        public static final String CONSTRAIN_BY_ID = TABLE_NAME + "." + _ID + " = ?";

        // Default projection
        public static final String[] DEFAULT_PROJECTION = new String[]{
                _ID,
                COL_RESIDENT,
                COL_GEOMEMORIAL_ID,
                COL_RANK_ID,
                COL_HOMETOWN_ID,
                COL_OBIT_ID,
                COL_LETTER_ID
        };

        // Default projection order
        public static final int DEFAULT_ID_IDX = 0;
        public static final int DEFAULT_RESIDENT_IDX = 1;
        public static final int DEFAULT_GEOMEMORIAL_ID_IDX = 2;
        public static final int DEFAULT_RANK_ID_IDX = 3;
        public static final int DEFAULT_HOMETOWN_ID_IDX = 4;
        public static final int DEFAULT_OBIT_ID_IDX = 5;
        public static final int DEFAULT_LETTER_ID_IDX = 6;
    }

    public static final class NtsSeries implements BaseColumns {
        public static final String TABLE_NAME = "nts_series";

        // Uris
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
            .buildUpon()
            .appendPath(TABLE_NAME)
            .build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // Columns
        public static final String COL_NTS_SERIES_NUMBER = "nts_series_number";
        public static final String COL_NTS_SERIES_NAME = "nts_series_name";
        public static final String COL_NORTH_WEST_LAT = "north_west_lat";
        public static final String COL_NORTH_WEST_LNG = "north_west_lng";
        public static final String COL_SOUTH_EAST_LAT = "south_east_lat";
        public static final String COL_SOUTH_EAST_LNG = "south_east_lng";
        public static final String COL_MARKER_LAT = "marker_lat";
        public static final String COL_MARKER_LNG = "marker_lng";

        // Constraints
        public static final String CONSTRAIN_BY_ID = TABLE_NAME + "." + _ID + " = ?";

        // Default projection
        public static final String[] DEFAULT_PROJECTION = new String[]{
                _ID,
                COL_NTS_SERIES_NUMBER,
                COL_NTS_SERIES_NAME,
                COL_NORTH_WEST_LAT,
                COL_NORTH_WEST_LNG,
                COL_SOUTH_EAST_LAT,
                COL_SOUTH_EAST_LNG,
                COL_MARKER_LAT,
                COL_MARKER_LNG
        };

        // Default projection order
        public static final int DEFAULT_ID_IDX = 0;
        public static final int DEFAULT_NTS_SERIES_NUMBER_IDX = 1;
        public static final int DEFAULT_NTS_SERIES_NAME_IDX = 2;
        public static final int DEFAULT_NORTH_WEST_LAT_IDX = 3;
        public static final int DEFAULT_NORTH_WEST_LNG_IDX = 4;
        public static final int DEFAULT_SOUTH_EAST_LAT_IDX = 5;
        public static final int DEFAULT_SOUTH_EAST_LNG_IDX = 6;
        public static final int DEFAULT_MARKER_LAT_IDX = 7;
        public static final int DEFAULT_MARKER_LNG_IDX = 8;
    }

    public static final class NtsArea implements BaseColumns {
        public static final String TABLE_NAME = "nts_area";

        // Uris
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
            .buildUpon()
            .appendPath(TABLE_NAME)
            .build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // Columns
        public static final String COL_NTS_AREA_LETTER = "nts_area_letter";
        public static final String COL_NTS_AREA_NAME = "nts_area_name";
        public static final String COL_NORTH_WEST_LAT = "north_west_lat";
        public static final String COL_NORTH_WEST_LNG = "north_west_lng";
        public static final String COL_SOUTH_EAST_LAT = "south_east_lat";
        public static final String COL_SOUTH_EAST_LNG = "south_east_lng";
        public static final String COL_NTS_SERIES_ID = "nts_series_id";
        public static final String COL_MARKER_LAT = "marker_lat";
        public static final String COL_MARKER_LNG = "marker_lng";

        // Constraints
        public static final String CONSTRAIN_BY_ID = TABLE_NAME + "." + _ID + " = ?";

        // Default projection
        public static final String[] DEFAULT_PROJECTION = new String[]{
                _ID,
                COL_NTS_AREA_LETTER,
                COL_NTS_AREA_NAME,
                COL_NORTH_WEST_LAT,
                COL_NORTH_WEST_LNG,
                COL_SOUTH_EAST_LAT,
                COL_SOUTH_EAST_LNG,
                COL_NTS_SERIES_ID,
                COL_MARKER_LAT,
                COL_MARKER_LNG
        };

        // Default projection order
        public static final int DEFAULT_ID_IDX = 0;
        public static final int DEFAULT_NTS_AREA_LETTER_IDX = 1;
        public static final int DEFAULT_NTS_AREA_NAME_IDX = 2;
        public static final int DEFAULT_NORTH_WEST_LAT_IDX = 3;
        public static final int DEFAULT_NORTH_WEST_LNG_IDX = 4;
        public static final int DEFAULT_SOUTH_EAST_LAT_IDX = 5;
        public static final int DEFAULT_SOUTH_EAST_LNG_IDX = 6;
        public static final int DEFAULT_NTS_SERIES_ID_IDX = 7;
        public static final int DEFAULT_MARKER_LAT_IDX = 8;
        public static final int DEFAULT_MARKER_LNG_IDX = 9;
    }

    public static final class NtsSheet implements BaseColumns {
        public static final String TABLE_NAME = "nts_sheet";

        // Uris
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
            .buildUpon()
            .appendPath(TABLE_NAME)
            .build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // Columns
        public static final String COL_NTS_SHEET_NUMBER = "nts_sheet_number";
        public static final String COL_NTS_SHEET_NAME = "nts_sheet_name";
        public static final String COL_NORTH_WEST_LAT = "north_west_lat";
        public static final String COL_NORTH_WEST_LNG = "north_west_lng";
        public static final String COL_SOUTH_EAST_LAT = "south_east_lat";
        public static final String COL_SOUTH_EAST_LNG = "south_east_lng";
        public static final String COL_NTS_AREA_ID = "nts_area_id";
        public static final String COL_MARKER_LAT = "marker_lat";
        public static final String COL_MARKER_LNG = "marker_lng";

        // Constraints
        public static final String CONSTRAIN_BY_ID = TABLE_NAME + "." + _ID + " = ?";

        // Default projection
        public static final String[] DEFAULT_PROJECTION = new String[]{
                _ID,
                COL_NTS_SHEET_NUMBER,
                COL_NTS_SHEET_NAME,
                COL_NORTH_WEST_LAT,
                COL_NORTH_WEST_LNG,
                COL_SOUTH_EAST_LAT,
                COL_SOUTH_EAST_LNG,
                COL_NTS_AREA_ID,
                COL_MARKER_LAT,
                COL_MARKER_LNG
        };

        // Default projection order
        public static final int DEFAULT_ID_IDX = 0;
        public static final int DEFAULT_NTS_SHEET_NUMBER_IDX = 1;
        public static final int DEFAULT_NTS_SHEET_NAME_IDX = 2;
        public static final int DEFAULT_NORTH_WEST_LAT_IDX = 3;
        public static final int DEFAULT_NORTH_WEST_LNG_IDX = 4;
        public static final int DEFAULT_SOUTH_EAST_LAT_IDX = 5;
        public static final int DEFAULT_SOUTH_EAST_LNG_IDX = 6;
        public static final int DEFAULT_NTS_AREA_ID_IDX = 7;
        public static final int DEFAULT_MARKER_LAT_IDX = 8;
        public static final int DEFAULT_MARKER_LNG_IDX = 9;
    }

    public static final class ProvincialCount implements BaseColumns {
        public static final String VIEW_NAME = "provincial_count";

        // Uris
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
            .buildUpon()
            .appendPath(VIEW_NAME)
            .build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + VIEW_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + VIEW_NAME;

        // Columns
        public static final String COL_MARKER_LAT = "marker_lat";
        public static final String COL_MARKER_LNG = "marker_lng";
        public static final String COL_GEOMEMORIAL_COUNT = "geomemorial_count";

        // Constraints
        public static final String CONSTRAIN_BY_VISIBLE_BOUNDARY =
            VIEW_NAME + "." + COL_MARKER_LAT + " <= ?"
                + " AND " + VIEW_NAME + "." + COL_MARKER_LAT + " >= ?"
                + " AND " + VIEW_NAME + "." + COL_MARKER_LNG + " >= ?"
                + " AND " + VIEW_NAME + "." + COL_MARKER_LNG + " <= ?";

        // Default projection
        public static final String[] DEFAULT_PROJECTION = new String[]{
                _ID,
                COL_MARKER_LAT,
                COL_MARKER_LNG,
                COL_GEOMEMORIAL_COUNT
        };

        // Default projection order
        public static final int DEFAULT_ID_IDX = 0;
        public static final int DEFAULT_MARKER_LAT_IDX = 1;
        public static final int DEFAULT_MARKER_LNG_IDX = 2;
        public static final int DEFAULT_GEOMEMORIAL_COUNT_IDX = 3;
    }

    public static final class NtsSeriesCounts implements BaseColumns {
        public static final String VIEW_NAME = "nts_series_counts";

        // Uris
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
            .buildUpon()
            .appendPath(VIEW_NAME)
            .build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + VIEW_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + VIEW_NAME;

        // Columns
        public static final String COL_NTS_SERIES_ID = "nts_series_id";
        public static final String COL_NTS_SERIES_NUMBER = NtsSeries.COL_NTS_SERIES_NUMBER;
        public static final String COL_NTS_SERIES_NAME = NtsSeries.COL_NTS_SERIES_NAME;
        public static final String COL_NORTH_WEST_LAT = NtsSeries.COL_NORTH_WEST_LAT;
        public static final String COL_NORTH_WEST_LNG = NtsSeries.COL_NORTH_WEST_LNG;
        public static final String COL_SOUTH_EAST_LAT = NtsSeries.COL_SOUTH_EAST_LAT;
        public static final String COL_SOUTH_EAST_LNG = NtsSeries.COL_SOUTH_EAST_LNG;
        public static final String COL_MARKER_LAT = NtsSeries.COL_MARKER_LAT;
        public static final String COL_MARKER_LNG = NtsSeries.COL_MARKER_LNG;
        public static final String COL_GEOMEMORIAL_COUNT = "geomemorial_count";

        // Constraints
        public static final String CONSTRAIN_BY_ID = VIEW_NAME + "." + _ID + " = ?";
        public static final String CONSTRAIN_BY_VISIBLE_BOUNDARY =
            VIEW_NAME + "." + COL_MARKER_LAT + " <= ?"
                + " AND " + VIEW_NAME + "." + COL_MARKER_LAT + " >= ?"
                + " AND " + VIEW_NAME + "." + COL_MARKER_LNG + " >= ?"
                + " AND " + VIEW_NAME + "." + COL_MARKER_LNG + " <= ?";

        // Default projection
        public static final String[] DEFAULT_PROJECTION = new String[]{
                _ID,
                COL_NTS_SERIES_ID,
                COL_NTS_SERIES_NUMBER,
                COL_NTS_SERIES_NAME,
                COL_NORTH_WEST_LAT,
                COL_NORTH_WEST_LNG,
                COL_SOUTH_EAST_LAT,
                COL_SOUTH_EAST_LNG,
                COL_MARKER_LAT,
                COL_MARKER_LNG,
                COL_GEOMEMORIAL_COUNT
        };

        // Default projection order
        public static final int DEFAULT_ID_IDX = 0;
        public static final int DEFAULT_NTS_SERIES_ID_IDX = 1;
        public static final int DEFAULT_NTS_SERIES_NUMBER_IDX = 2;
        public static final int DEFAULT_NTS_SERIES_NAME_IDX = 3;
        public static final int DEFAULT_NORTH_WEST_LAT_IDX = 4;
        public static final int DEFAULT_NORTH_WEST_LNG_IDX = 5;
        public static final int DEFAULT_SOUTH_EAST_LAT_IDX = 6;
        public static final int DEFAULT_SOUTH_EAST_LNG_IDX = 7;
        public static final int DEFAULT_MARKER_LAT_IDX = 8;
        public static final int DEFAULT_MARKER_LNG_IDX = 9;
        public static final int DEFAULT_GEOMEMORIAL_COUNT_IDX = 10;
    }

    public static final class NtsAreaCounts implements BaseColumns {
        public static final String VIEW_NAME = "nts_area_counts";

        // Uris
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
            .buildUpon()
            .appendPath(VIEW_NAME)
            .build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + VIEW_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + VIEW_NAME;

        // Columns
        public static final String COL_NTS_AREA_ID = "nts_area_id";
        public static final String COL_NTS_SERIES_NUMBER = NtsSeries.COL_NTS_SERIES_NUMBER;
        public static final String COL_NTS_SERIES_NAME = NtsSeries.COL_NTS_SERIES_NAME;
        public static final String COL_NTS_AREA_LETTER = NtsArea.COL_NTS_AREA_LETTER;
        public static final String COL_NTS_AREA_NAME = NtsArea.COL_NTS_AREA_NAME;
        public static final String COL_NORTH_WEST_LAT = NtsArea.COL_NORTH_WEST_LAT;
        public static final String COL_NORTH_WEST_LNG = NtsArea.COL_NORTH_WEST_LNG;
        public static final String COL_SOUTH_EAST_LAT = NtsArea.COL_SOUTH_EAST_LAT;
        public static final String COL_SOUTH_EAST_LNG = NtsArea.COL_SOUTH_EAST_LNG;
        public static final String COL_MARKER_LAT = NtsArea.COL_MARKER_LAT;
        public static final String COL_MARKER_LNG = NtsArea.COL_MARKER_LNG;
        public static final String COL_GEOMEMORIAL_COUNT = "geomemorial_count";

        // Constraints
        public static final String CONSTRAIN_BY_ID = VIEW_NAME + "." + _ID + " = ?";
        public static final String CONSTRAIN_BY_VISIBLE_BOUNDARY =
            VIEW_NAME + "." + COL_MARKER_LAT + " <= ?"
                + " AND " + VIEW_NAME + "." + COL_MARKER_LAT + " >= ?"
                + " AND " + VIEW_NAME + "." + COL_MARKER_LNG + " >= ?"
                + " AND " + VIEW_NAME + "." + COL_MARKER_LNG + " <= ?";

        // Default projection
        public static final String[] DEFAULT_PROJECTION = new String[]{
                _ID,
                COL_NTS_AREA_ID,
                COL_NTS_SERIES_NUMBER,
                COL_NTS_SERIES_NAME,
                COL_NTS_AREA_LETTER,
                COL_NTS_AREA_NAME,
                COL_NORTH_WEST_LAT,
                COL_NORTH_WEST_LNG,
                COL_SOUTH_EAST_LAT,
                COL_SOUTH_EAST_LNG,
                COL_MARKER_LAT,
                COL_MARKER_LNG,
                COL_GEOMEMORIAL_COUNT
        };

        // Default projection order
        public static final int DEFAULT_ID_IDX = 0;
        public static final int DEFAULT_NTS_AREA_ID_IDX = 1;
        public static final int DEFAULT_NTS_SERIES_NUMBER_IDX = 2;
        public static final int DEFAULT_NTS_SERIES_NAME_IDX = 3;
        public static final int DEFAULT_NTS_AREA_LETTER_IDX = 4;
        public static final int DEFAULT_NTS_AREA_NAME_IDX = 5;
        public static final int DEFAULT_NORTH_WEST_LAT_IDX = 6;
        public static final int DEFAULT_NORTH_WEST_LNG_IDX = 7;
        public static final int DEFAULT_SOUTH_EAST_LAT_IDX = 8;
        public static final int DEFAULT_SOUTH_EAST_LNG_IDX = 9;
        public static final int DEFAULT_MARKER_LAT_IDX = 10;
        public static final int DEFAULT_MARKER_LNG_IDX = 11;
        public static final int DEFAULT_GEOMEMORIAL_COUNT_IDX = 12;
    }

    public static final class NtsSheetCounts implements BaseColumns {
        public static final String VIEW_NAME = "nts_sheet_counts";

        // Uris
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
            .buildUpon()
            .appendPath(VIEW_NAME)
            .build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + VIEW_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + VIEW_NAME;

        // Columns
        public static final String COL_NTS_SHEET_ID = "nts_sheet_id";
        public static final String COL_NTS_SERIES_NUMBER = NtsSeries.COL_NTS_SERIES_NUMBER;
        public static final String COL_NTS_SERIES_NAME = NtsSeries.COL_NTS_SERIES_NAME;
        public static final String COL_NTS_AREA_LETTER = NtsArea.COL_NTS_AREA_LETTER;
        public static final String COL_NTS_AREA_NAME = NtsArea.COL_NTS_AREA_NAME;
        public static final String COL_NTS_SHEET_NUMBER = NtsSheet.COL_NTS_SHEET_NUMBER;
        public static final String COL_NTS_SHEET_NAME = NtsSheet.COL_NTS_SHEET_NAME;
        public static final String COL_NORTH_WEST_LAT = NtsSheet.COL_NORTH_WEST_LAT;
        public static final String COL_NORTH_WEST_LNG = NtsSheet.COL_NORTH_WEST_LNG;
        public static final String COL_SOUTH_EAST_LAT = NtsSheet.COL_SOUTH_EAST_LAT;
        public static final String COL_SOUTH_EAST_LNG = NtsSheet.COL_SOUTH_EAST_LNG;
        public static final String COL_MARKER_LAT = NtsSheet.COL_MARKER_LAT;
        public static final String COL_MARKER_LNG = NtsSheet.COL_MARKER_LNG;
        public static final String COL_GEOMEMORIAL_COUNT = "geomemorial_count";

        // Constraints
        public static final String CONSTRAIN_BY_ID = VIEW_NAME + "." + _ID + " = ?";
        public static final String CONSTRAIN_BY_VISIBLE_BOUNDARY =
            VIEW_NAME + "." + COL_MARKER_LAT + " <= ?"
                + " AND " + VIEW_NAME + "." + COL_MARKER_LAT + " >= ?"
                + " AND " + VIEW_NAME + "." + COL_MARKER_LNG + " >= ?"
                + " AND " + VIEW_NAME + "." + COL_MARKER_LNG + " <= ?";

        // Default projection
        public static final String[] DEFAULT_PROJECTION = new String[]{
                _ID,
                COL_NTS_SHEET_ID,
                COL_NTS_SERIES_NUMBER,
                COL_NTS_SERIES_NAME,
                COL_NTS_AREA_LETTER,
                COL_NTS_AREA_NAME,
                COL_NTS_SHEET_NUMBER,
                COL_NTS_SHEET_NAME,
                COL_NORTH_WEST_LAT,
                COL_NORTH_WEST_LNG,
                COL_SOUTH_EAST_LAT,
                COL_SOUTH_EAST_LNG,
                COL_MARKER_LAT,
                COL_MARKER_LNG,
                COL_GEOMEMORIAL_COUNT
        };

        // Default projection order
        public static final int DEFAULT_ID_IDX = 0;
        public static final int DEFAULT_NTS_SHEET_ID_IDX = 1;
        public static final int DEFAULT_NTS_SERIES_NUMBER_IDX = 2;
        public static final int DEFAULT_NTS_SERIES_NAME_IDX = 3;
        public static final int DEFAULT_NTS_AREA_LETTER_IDX = 4;
        public static final int DEFAULT_NTS_AREA_NAME_IDX = 5;
        public static final int DEFAULT_NTS_SHEET_NUMBER_IDX = 6;
        public static final int DEFAULT_NTS_SHEET_NAME_IDX = 7;
        public static final int DEFAULT_NORTH_WEST_LAT_IDX = 8;
        public static final int DEFAULT_NORTH_WEST_LNG_IDX = 9;
        public static final int DEFAULT_SOUTH_EAST_LAT_IDX = 10;
        public static final int DEFAULT_SOUTH_EAST_LNG_IDX = 11;
        public static final int DEFAULT_MARKER_LAT_IDX = 12;
        public static final int DEFAULT_MARKER_LNG_IDX = 13;
        public static final int DEFAULT_GEOMEMORIAL_COUNT_IDX = 14;
    }

    public static final class MarkerInfo implements BaseColumns {
        public static final String VIRTUAL_TABLE_NAME = "marker_info";

        // Uris
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
            .buildUpon()
            .appendPath(VIRTUAL_TABLE_NAME)
            .build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + VIRTUAL_TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + VIRTUAL_TABLE_NAME;

        // Columns
        public static final String COL_RESIDENT_ID = "resident_id";
        public static final String COL_GEOMEMORIAL_ID = "geomemorial_id";
        public static final String COL_RANK_ID = "rank_id";
        public static final String COL_HOMETOWN_ID = "hometown_id";
        public static final String COL_OBIT_ID = "obit_id";
        public static final String COL_LETTER_ID = "letter_id";
        public static final String COL_RESIDENT = Resident.COL_RESIDENT;
        public static final String COL_GEOMEMORIAL = Geomemorial.COL_GEOMEMORIAL;
        public static final String COL_LATITUDE = Geomemorial.COL_LATITUDE;
        public static final String COL_LONGITUDE = Geomemorial.COL_LONGITUDE;
        public static final String COL_RANK = Rank.COL_RANK;
        public static final String COL_HOMETOWN = Hometown.COL_HOMETOWN;
        public static final String COL_OBIT = Obit.COL_OBIT;
        public static final String COL_LETTER = Letter.COL_LETTER;
        public static final String COL_NTS_SERIES = NtsSheetCounts.COL_NTS_SERIES_NUMBER;
        public static final String COL_NTS_SERIES_NAME = NtsSheetCounts.COL_NTS_SERIES_NAME;
        public static final String COL_NTS_AREA = NtsSheetCounts.COL_NTS_AREA_LETTER;
        public static final String COL_NTS_AREA_NAME = NtsSheetCounts.COL_NTS_AREA_NAME;
        public static final String COL_NTS_SHEET = NtsSheetCounts.COL_NTS_SHEET_NUMBER;
        public static final String COL_NTS_SHEET_NAME = NtsSheetCounts.COL_NTS_SHEET_NAME;

        // Constraints
        public static final String CONSTRAIN_BY_ID
            = VIRTUAL_TABLE_NAME + "." + _ID + " = ?";
        public static final String CONSTRAIN_BY_RANK_ID
            = VIRTUAL_TABLE_NAME + "." + COL_RANK_ID + " = ?";
        public static final String CONSTRAIN_BY_HOMETOWN_ID
            = VIRTUAL_TABLE_NAME + "." + COL_HOMETOWN_ID + " = ?";
        public static final String CONSTRAIN_BY_OBIT_ID
            = VIRTUAL_TABLE_NAME + "." + COL_OBIT_ID + " = ?";
        public static final String CONSTRAIN_BY_LETTER_ID
            = VIRTUAL_TABLE_NAME + "." + COL_LETTER_ID + " = ?";
        public static final String CONSTRAIN_BY_VISIBLE_BOUNDARY
            = VIRTUAL_TABLE_NAME + "." + COL_LATITUDE + " <= ?"
                + " AND " + VIRTUAL_TABLE_NAME + "." + COL_LATITUDE + " >= ?"
                + " AND " + VIRTUAL_TABLE_NAME + "." + COL_LONGITUDE + " >= ?"
                + " AND " + VIRTUAL_TABLE_NAME + "." + COL_LONGITUDE + " <= ?";
        public static final String CONSTRAINT_BY_SEARCH_CRITERIA = " marker_info match ?";

        // Selection argument constructors
        public static String[] getSelectionArgsFor(String query) {
            final String value = query == null ? "''" : query;
            return new String[]{value};
        }


        // Default projection
        public static final String[] DEFAULT_PROJECTION = new String[]{
                _ID,
                COL_RESIDENT_ID,
                COL_GEOMEMORIAL_ID,
                COL_RANK_ID,
                COL_HOMETOWN_ID,
                COL_OBIT_ID,
                COL_LETTER_ID,
                COL_RESIDENT,
                COL_GEOMEMORIAL,
                COL_LATITUDE,
                COL_LONGITUDE,
                COL_RANK,
                COL_HOMETOWN,
                COL_OBIT,
                COL_LETTER,
                COL_NTS_SERIES,
                COL_NTS_SERIES_NAME,
                COL_NTS_AREA,
                COL_NTS_AREA_NAME,
                COL_NTS_SHEET,
                COL_NTS_SHEET_NAME
        };

        // Default projection order
        public static final int DEFAULT_ID_IDX = 0;
        public static final int DEFAULT_RESIDENT_ID_IDX = 1;
        public static final int DEFAULT_GEOMEMORIAL_ID_IDX = 2;
        public static final int DEFAULT_RANK_ID_IDX = 3;
        public static final int DEFAULT_HOMETOWN_ID_IDX = 4;
        public static final int DEFAULT_OBIT_ID_IDX = 5;
        public static final int DEFAULT_LETTER_ID_IDX = 6;
        public static final int DEFAULT_RESIDENT_IDX = 7;
        public static final int DEFAULT_GEOMEMORIAL_IDX = 8;
        public static final int DEFAULT_LATITUDE_IDX = 9;
        public static final int DEFAULT_LONGITUDE_IDX = 10;
        public static final int DEFAULT_RANK_IDX = 11;
        public static final int DEFAULT_HOMETOWN_IDX = 12;
        public static final int DEFAULT_OBIT_IDX = 13;
        public static final int DEFAULT_LETTER_IDX = 14;
        public static final int DEFAULT_NTS_SERIES_IDX = 15;
        public static final int DEFAULT_NTS_SERIES_NAME_IDX = 16;
        public static final int DEFAULT_NTS_AREA_IDX = 17;
        public static final int DEFAULT_NTS_AREA_NAME_IDX = 18;
        public static final int DEFAULT_NTS_SHEET_IDX = 19;
        public static final int DEFAULT_NTS_SHEET_NAME_IDX = 20;

        // Sort orders
        public static final String SORT_ORDER_RESIDENT
            = COL_RESIDENT + SORT_ASC + ", " + COL_HOMETOWN_ID + SORT_ASC;
    }

    public static final class SearchSuggestions implements BaseColumns {
        public static final String TABLE_NAME = "search_suggestions";

        // Uris
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
            .buildUpon()
            .appendPath(TABLE_NAME)
            .build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // Columns
        public static final String COL_SUGGEST_COLUMN_TEXT_1 = SearchManager.SUGGEST_COLUMN_TEXT_1;
        public static final String COL_SUGGEST_COLUMN_TEXT_2 = SearchManager.SUGGEST_COLUMN_TEXT_2;
        public static final String COL_SUGGEST_COLUMN_INTENT_ACTION
            = SearchManager.SUGGEST_COLUMN_INTENT_ACTION;
        public static final String COL_SUGGEST_COLUMN_INTENT_DATA
            = SearchManager.SUGGEST_COLUMN_INTENT_DATA;
        public static final String COL_SUGGEST_COLUMN_INTENT_DATA_ID
            = SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID;
        public static final String COL_SUGGEST_COLUMN_QUERY = SearchManager.SUGGEST_COLUMN_QUERY;
        public static final String COL_SUGGEST_COLUMN_INTENT_EXTRA_DATA
            = SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA;
        public static final String COL_RESIDENT_ID = "resident_id";
        public static final String COL_HOMETOWN_ID = "hometown_id";
        public static final String COL_OBIT_ID = "obit_id";
        public static final String COL_RANK_ID = "rank_id";
        public static final String GEOMEMORIAL_ID = "geomemorial_id";
        public static final String NTS_SERIES_ID = "nts_series_id";
        public static final String NTS_AREA_ID = "nts_area_id";
        public static final String NTS_SHEET_ID = "nts_sheet_id";

        // Selection argument constructors
        public static String[] getSelectionArgsFor(String query) {
            final String value = query == null ? "" : query.toUpperCase() + "%";
            return new String[]{value};
        }


        // Default projection
        public static final String[] DEFAULT_PROJECTION = new String[]{
                _ID,
                COL_SUGGEST_COLUMN_TEXT_1,
                COL_SUGGEST_COLUMN_TEXT_2,
                COL_SUGGEST_COLUMN_INTENT_ACTION,
                COL_SUGGEST_COLUMN_INTENT_DATA,
                COL_SUGGEST_COLUMN_QUERY,
                COL_SUGGEST_COLUMN_INTENT_EXTRA_DATA
        };

        // Default projection order
        public static final int DEFAULT_ID_IDX = 0;
        public static final int DEFAULT_SUGGEST_COLUMN_TEXT_1_IDX = 1;
        public static final int DEFAULT_SUGGEST_COLUMN_TEXT_2_IDX = 2;
        public static final int DEFAULT_SUGGEST_COLUMN_INTENT_ACTION_IDX = 3;
        public static final int DEFAULT_SUGGEST_COLUMN_INTENT_DATA_IDX = 4;
        public static final int DEFAULT_SUGGEST_COLUMN_QUERY_IDX = 5;
        public static final int DEFAULT_SUGGEST_COLUMN_INTENT_EXTRA_DATA_IDX = 6;

        // Sort orders
        public static final String SORT_ORDER_SUGGEST_COLUMN_TEXT_1
            = COL_SUGGEST_COLUMN_TEXT_1 + SORT_ASC + ", " + COL_SUGGEST_COLUMN_TEXT_2 + SORT_ASC;
    }
}
