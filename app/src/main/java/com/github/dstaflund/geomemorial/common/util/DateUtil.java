package com.github.dstaflund.geomemorial.common.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
    private static final Locale sLocale = Locale.getDefault();
    public static final String DB_TMPL = "yyyy-MM-dd";
    public static final String DISPLAY_TMPL = "MMMM d, yyyy";
    public static final DateFormat DB_FORMAT = new SimpleDateFormat(DB_TMPL, sLocale);
    public static final DateFormat DISPLAY_FORMAT = new SimpleDateFormat(DISPLAY_TMPL, sLocale);

    /**
      Given a string representing a date in {@link #DB_TMPL} format, returns the
     * corresponding Date object.
     *
     * @param dateString to convert
     * @return date object
     */
    @Nullable
    public static Date toDate(@NonNull final String dateString){
        try {
            return DB_FORMAT.parse(dateString);
        }

        catch (ParseException e) {
            return null;
        }
    }

    /**
     * Given a Date object, returns its string value in {@link #DISPLAY_FORMAT}
     * format.
     *
     * @param date to get standardized string representation for
     * @return standardized string representation
     */
    @NonNull
    public static String toString(@NonNull final Date date){
        return DISPLAY_FORMAT.format(date);
    }

    /**
     * Given a date in {@link #DB_FORMAT} format, returns the date in
     * {@link #DISPLAY_FORMAT} format.
     *
     * @param dateString the date string to be formatted
     * @return formatted date string
     */
    @Nullable
    public static String toDisplayString(@NonNull final String dateString){
        final Date date = toDate(dateString);
        return date == null ? null : toString(date).toUpperCase();
    }

    /**
     * Default constructor.
     */
    private DateUtil(){
        super();
    }
}
