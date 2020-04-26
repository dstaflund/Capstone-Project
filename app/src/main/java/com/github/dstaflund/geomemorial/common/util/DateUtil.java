package com.github.dstaflund.geomemorial.common.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Custom library of date / time utility methods.
 *
 * @author Darryl Staflund
 */
public final class DateUtil {
    private static final String sLogTag = DateUtil.class.getSimpleName();
    private static final Locale sLocale = Locale.getDefault();
    private static final String sDbTmpl = "yyyy-MM-dd";
    private static final String sDisplayTmpl = "MMMM d, yyyy";
    private static final DateFormat sDisplayFormat = new SimpleDateFormat(sDisplayTmpl, sLocale);
    public static final DateFormat DB_FORMAT = new SimpleDateFormat(sDbTmpl, sLocale);

    @Nullable
    public static Date toDate(@NonNull final String dateString){
        try {
            return DB_FORMAT.parse(dateString);
        }

        catch (ParseException e) {
            Log.e(sLogTag, e.toString());
            return null;
        }
    }

    @NonNull
    public static String toString(@NonNull final Date date){
        return sDisplayFormat.format(date);
    }

    @Nullable
    public static String toDisplayString(@NonNull final String dateString){
        final Date date = toDate(dateString);
        return date == null ? null : toString(date).toUpperCase();
    }

    private DateUtil(){
        super();
    }
}
