package com.github.dstaflund.geomemorial.common.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.dstaflund.geomemorial.R;

import java.util.Date;

public final class SharedIntentManager {

    @Nullable
    private static String toTitleCase(@NonNull Context context, @NonNull String value){
        String[] words = value.split(context.getString(R.string.misc_space_character));
        StringBuilder sb = new StringBuilder();
        if (words[0].length() > 0) {
            sb
                .append(Character.toUpperCase(words[0].charAt(0)))
                .append(words[0].subSequence(1, words[0].length()).toString().toLowerCase());
            for (int i = 1; i < words.length; i++) {
                sb
                    .append(context.getString(R.string.misc_space_character))
                    .append(Character.toUpperCase(words[i].charAt(0)))
                    .append(words[i].subSequence(1, words[i].length()).toString().toLowerCase());
            }
        }
        return sb.toString();
    }

    @Nullable
    private static String formatDate(@NonNull Context context, @NonNull String unformatted){
        Date date = DateUtil.toDate(context, unformatted);
        return date == null ? null : DateUtil.toString(date);
    }

    @NonNull
    private static String swapNames(@NonNull Context context, @NonNull String value){
        String[] values = value.split(context.getString(R.string.misc_comma_character));
        if (values.length != 2){
            return value;
        }
        return values[1].trim() + context.getString(R.string.misc_space_character) + values[0];
    }

    public static void shareGeomemorial(@NonNull Context context, @NonNull Payload payload){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType(context.getString(R.string.email_type));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        intent.putExtra(
            Intent.EXTRA_SUBJECT,
            String.format(
                context.getString(R.string.email_subject_pattern),
                toTitleCase(context, payload.getGeomemorial())
            )
        );
        intent.putExtra(
            Intent.EXTRA_TEXT,
            String.format(context.getString(R.string.email_body_pattern),
                toTitleCase(context, payload.getGeomemorial()),
                toTitleCase(context, swapNames(context, payload.getResident())),
                toTitleCase(context, payload.getHometown()),
                formatDate(context, payload.getObit()),
                payload.getUri().toString()
            )
        );

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.email_dialog_header)));
        }
    }

    private SharedIntentManager(){
        super();
    }

    public static class Payload {
        private Context mContext;
        private String mGeomemorial;
        private String mLatitude;
        private String mLongitude;
        private String mResident;
        private String mHometown;
        private String mObit;
        private String mRank;

        @NonNull
        public String getGeomemorial() {
            return mGeomemorial;
        }

        @NonNull
        public String getLatitude(){
            return mLatitude;
        }

        @NonNull
        public String getLongitude(){
            return mLongitude;
        }

        @NonNull
        public String getResident(){
            return mResident;
        }

        @NonNull
        public String getHometown(){
            return mHometown;
        }

        @NonNull
        public String getObit(){
            return mObit;
        }

        @NonNull
        public String getRank(){
            return mRank;
        }

        @NonNull
        public Uri getUri(){
            return Uri.parse(
                String.format(
                    mContext.getString(R.string.email_maps_url_pattern),
                    mLatitude,
                    mLongitude,
                    mLatitude,
                    mLongitude
                )
            );
        }

        private Payload(){
            super();
        }

        public static class Builder{
            private Context mContext;
            private String mGeomemorial;
            private String mLatitude;
            private String mLongitude;
            private String mResident;
            private String mHometown;
            private String mObit;
            private String mRank;

            public Builder(@NonNull Context context){
                super();
                mContext = context;
            }

            @NonNull
            public Builder geomemorial(@NonNull String value){
                mGeomemorial = value;
                return this;
            }

            @NonNull
            public Builder latitude(@NonNull String value){
                mLatitude = value;
                return this;
            }

            @NonNull
            public Builder longitude(@NonNull String value){
                mLongitude = value;
                return this;
            }

            @NonNull
            public Builder resident(@NonNull String value){
                mResident = value;
                return this;
            }

            @NonNull
            public Builder hometown(@NonNull String value){
                mHometown = value;
                return this;
            }

            @NonNull
            public Builder obit(@NonNull String value){
                mObit = value;
                return this;
            }

            @NonNull
            public Builder rank(@NonNull String value){
                mRank = value;
                return this;
            }

            @NonNull
            public Payload build(){
                final Payload p = new Payload();
                p.mContext = mContext;
                p.mGeomemorial = mGeomemorial;
                p.mLatitude = mLatitude;
                p.mLongitude = mLongitude;
                p.mResident = mResident;
                p.mHometown = mHometown;
                p.mObit = mObit;
                p.mRank = mRank;
                return p;
            }
        }
    }
}
