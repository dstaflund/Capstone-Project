package com.github.dstaflund.geomemorial.integration;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.dstaflund.geomemorial.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.DATABASE_NAME;
import static com.github.dstaflund.geomemorial.integration.GeomemorialDbContract.DATABASE_VERSION;

/**
 * Helper class used to create, initialize, and upgrade the Geomemorial database.
 *
 * @author Darryl Staflund
 */
public class GeomemorialDbHelper extends SQLiteOpenHelper {
    private static final String sLogTag = GeomemorialDbHelper.class.getSimpleName();
    private static final String sStatementDelimiter = ";";
    private Context mContext;

    /**
     * One-arg constructor
     *
     * @param context to work with
     */
    public GeomemorialDbHelper(@NonNull final Context context){
        this(context, null);
    }

    /**
     * Two-arg constructor
     *
     * @param context to work with
     * @param factory to work with
     */
    public GeomemorialDbHelper(
        @NonNull Context context,
        @Nullable SQLiteDatabase.CursorFactory factory
    ){
        this(context, factory, null);
    }

    /**
     * Three-arg constructor
     *
     * @param context to work with
     * @param factory to work with
     * @param errHandler to work with
     */
    public GeomemorialDbHelper(
        @NonNull Context context,
        @Nullable SQLiteDatabase.CursorFactory factory,
        @Nullable DatabaseErrorHandler errHandler
    ){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION, errHandler);
        mContext = context;
    }

    /**
     * Creates the geomemorial database
     *
     * @param db to create
     */
    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        applyVersion1Script(db);
    }

    /**
     * If the existing geomemorial exists and needs to be updated, updates it
     *
     * @param db to update if necessary
     * @param oldVersion to update from
     * @param newVersion to update to
     */
    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            applyVersionNScript(db);
        }
    }

    /**
     * Applies the initial (i.e. 1st. version) of the database creation script against the
     * given database
     *
     * @param db to apply script to
     */
    private void applyVersion1Script(@NonNull SQLiteDatabase db){
        applySqlScript(db, R.raw.v1_alter_script);
    }

    /**
     * Applies the latest (i.e. the nth version) of the database script against the given
     * database.
     *
     * At the present time, this script just drops everything in the database
     *
     * @param db to apply script to
     */
    private void applyVersionNScript(@NonNull SQLiteDatabase db){
        applySqlScript(db, R.raw.vn_alter_script);
        applyVersion1Script(db);
    }

    /**
     * Given a database and resource id to an SQL script, applies the script to the database.
     *
     * @param db to applu script to
     * @param rawResourceId of script to apply to database
     */
    private void applySqlScript(@NonNull SQLiteDatabase db, int rawResourceId){
        InputStream is = mContext.getResources().openRawResource(rawResourceId);
        StringBuilder queries = new StringBuilder();

        try {
            Reader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                queries.append(line);
            }
        }

        catch (IOException e) {
            Log.e(sLogTag, mContext.getString(R.string.log_helper_script_error));
            return;
        }

        for (String query : queries.toString().split(sStatementDelimiter)) {
            db.execSQL(query);
        }
    }
}
