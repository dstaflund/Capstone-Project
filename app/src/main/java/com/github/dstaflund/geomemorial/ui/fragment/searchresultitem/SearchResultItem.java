package com.github.dstaflund.geomemorial.ui.fragment.searchresultitem;

import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract;

import static android.provider.BaseColumns._ID;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.COL_GEOMEMORIAL;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.COL_HOMETOWN;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.COL_LATITUDE;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.COL_LONGITUDE;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.COL_NTS_SHEET;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.COL_NTS_SHEET_NAME;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.COL_OBIT;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.COL_RANK;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.COL_RESIDENT;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.DEFAULT_GEOMEMORIAL_IDX;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.DEFAULT_HOMETOWN_IDX;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.DEFAULT_ID_IDX;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.DEFAULT_LATITUDE_IDX;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.DEFAULT_LONGITUDE_IDX;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.DEFAULT_NTS_SHEET_IDX;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.DEFAULT_NTS_SHEET_NAME_IDX;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.DEFAULT_OBIT_IDX;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.DEFAULT_RANK_IDX;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.MarkerInfo.DEFAULT_RESIDENT_IDX;

public class SearchResultItem {

    @NonNull
    @SuppressWarnings("NullableProblems")
    public String geomemorial;

    @NonNull
    @SuppressWarnings("NullableProblems")
    String geomemorialId;

    @NonNull
    @SuppressWarnings("NullableProblems")
    public String hometown;

    @NonNull
    @SuppressWarnings("NullableProblems")
    String ntsSheet;

    @NonNull
    @SuppressWarnings("NullableProblems")
    String ntsSheetName;

    @NonNull
    @SuppressWarnings("NullableProblems")
    public String obit;

    @NonNull
    @SuppressWarnings("NullableProblems")
    public String rank;

    @NonNull
    @SuppressWarnings("NullableProblems")
    public String resident;

    public String latitude;
    public String longitude;

    SearchResultItem(@Nullable Bundle b) {
        if (b == null){
            initEmptyItem();
            return;
        }

        geomemorial = b.getString(COL_GEOMEMORIAL, "");
        geomemorialId = b.getString(_ID, "");
        hometown = b.getString(COL_HOMETOWN, "");
        latitude = b.getString(COL_LATITUDE, "");
        longitude = b.getString(COL_LONGITUDE, "");
        ntsSheet = b.getString(COL_NTS_SHEET, "");
        ntsSheetName = b.getString(COL_NTS_SHEET_NAME, "");
        obit = b.getString(COL_OBIT, "");
        resident = b.getString(COL_RESIDENT, "");
        rank = b.getString(COL_RANK, "");
    }

    public SearchResultItem(@Nullable Cursor c) {
        if (c == null){
            initEmptyItem();
            return;
        }

        geomemorial = c.getString(DEFAULT_GEOMEMORIAL_IDX);
        geomemorialId = c.getString(DEFAULT_ID_IDX);
        hometown = c.getString(DEFAULT_HOMETOWN_IDX);
        latitude = c.getString(DEFAULT_LATITUDE_IDX);
        longitude = c.getString(DEFAULT_LONGITUDE_IDX);
        ntsSheet = c.getString(DEFAULT_NTS_SHEET_IDX);
        ntsSheetName = c.getString(DEFAULT_NTS_SHEET_NAME_IDX);
        obit = c.getString(DEFAULT_OBIT_IDX);
        resident = c.getString(DEFAULT_RESIDENT_IDX);
        rank = c.getString(DEFAULT_RANK_IDX);
    }

    private void initEmptyItem(){
        geomemorial = "";
        geomemorialId = "";
        hometown = "";
        latitude = null;
        longitude = null;
        ntsSheet = "";
        ntsSheetName = "";
        obit = "";
        resident = "";
        rank = "";
    }

    Location getLocation() {
        Location l = new Location("");
        l.setLatitude(Double.parseDouble(latitude));
        l.setLongitude(Double.parseDouble(longitude));
        return l;
    }
}
