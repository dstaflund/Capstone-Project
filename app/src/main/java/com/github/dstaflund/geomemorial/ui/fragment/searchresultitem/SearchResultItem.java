package com.github.dstaflund.geomemorial.ui.fragment.searchresultitem;

import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.github.dstaflund.geomemorial.integration.GeomemorialDbContract;

public class SearchResultItem {

    @NonNull
    public String geomemorial;

    @NonNull
    public String geomemorialId;

    @NonNull
    public String hometown;

    @NonNull
    public String ntsSheet;

    @NonNull
    public String ntsSheetName;

    @NonNull
    public String obit;

    @NonNull
    public String rank;

    @NonNull
    public String resident;

    public String latitude;
    public String longitude;

    public SearchResultItem(@NonNull Bundle b) {
        geomemorial = b.getString(GeomemorialDbContract.MarkerInfo.COL_GEOMEMORIAL, "");
        geomemorialId = b.getString(GeomemorialDbContract.MarkerInfo._ID, "");
        hometown = b.getString(GeomemorialDbContract.MarkerInfo.COL_HOMETOWN, "");
        latitude = b.getString(GeomemorialDbContract.MarkerInfo.COL_LATITUDE);
        longitude = b.getString(GeomemorialDbContract.MarkerInfo.COL_LONGITUDE);
        ntsSheet = b.getString(GeomemorialDbContract.MarkerInfo.COL_NTS_SHEET, "");
        ntsSheetName = b.getString(GeomemorialDbContract.MarkerInfo.COL_NTS_SHEET_NAME, "");
        obit = b.getString(GeomemorialDbContract.MarkerInfo.COL_OBIT, "");
        resident = b.getString(GeomemorialDbContract.MarkerInfo.COL_RESIDENT, "");
        rank = b.getString(GeomemorialDbContract.MarkerInfo.COL_RANK, "");
    }

    public SearchResultItem(@NonNull Cursor c) {
        geomemorial = c.getString(GeomemorialDbContract.MarkerInfo.DEFAULT_GEOMEMORIAL_IDX);
        geomemorialId = c.getString(GeomemorialDbContract.MarkerInfo.DEFAULT_ID_IDX);
        hometown = c.getString(GeomemorialDbContract.MarkerInfo.DEFAULT_HOMETOWN_IDX);
        latitude = c.getString(GeomemorialDbContract.MarkerInfo.DEFAULT_LATITUDE_IDX);
        longitude = c.getString(GeomemorialDbContract.MarkerInfo.DEFAULT_LONGITUDE_IDX);
        ntsSheet = c.getString(GeomemorialDbContract.MarkerInfo.DEFAULT_NTS_SHEET_IDX);
        ntsSheetName = c.getString(GeomemorialDbContract.MarkerInfo.DEFAULT_NTS_SHEET_NAME_IDX);
        obit = c.getString(GeomemorialDbContract.MarkerInfo.DEFAULT_OBIT_IDX);
        resident = c.getString(GeomemorialDbContract.MarkerInfo.DEFAULT_RESIDENT_IDX);
        rank = c.getString(GeomemorialDbContract.MarkerInfo.DEFAULT_RANK_IDX);
    }

    public Location getLocation() {
        Location l = new Location("");
        l.setLatitude(Double.parseDouble(latitude));
        l.setLongitude(Double.parseDouble(longitude));
        return l;
    }
}
