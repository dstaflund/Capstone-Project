package com.github.dstaflund.geomemorial.ui.activity.main;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class SearchRequest implements Parcelable {
    public static final String ACTION = "action";
    public static final String URI = "uri";
    public static final String QUERY = SearchManager.QUERY;
    public static final String USER_QUERY = SearchManager.USER_QUERY;
    public static final String EXTRA_DATA_KEY = SearchManager.EXTRA_DATA_KEY;

    public static final Creator<SearchRequest> CREATOR =
        new Creator<SearchRequest>() {

            @Override
            public SearchRequest createFromParcel(Parcel in) {
                return new SearchRequest(in);
            }

            @Override
            public SearchRequest[] newArray(int size) {
                return new SearchRequest[size];
            }
        };

    private String mAction;
    private Uri mUri;
    private String mQuery;
    private String mUserQuery;
    private String mExtraDataKey;

    public Uri getUri() {
        return mUri;
    }

    public String getQuery() {
        return mQuery;
    }

    public String getExtraDataKey() {
        return mExtraDataKey;
    }

    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putString(ACTION, mAction);
        b.putParcelable(URI, mUri);
        b.putString(QUERY, mQuery);
        b.putString(USER_QUERY, mUserQuery);
        b.putString(EXTRA_DATA_KEY, mExtraDataKey);
        return b;
    }

    public Intent toIntent() {
        Intent i = new Intent(mAction);
        i.setData(mUri);
        i.putExtra(SearchManager.QUERY, mQuery);
        i.putExtra(SearchManager.USER_QUERY, mUserQuery);
        i.putExtra(SearchManager.EXTRA_DATA_KEY, mExtraDataKey);
        return i;
    }

    public SearchRequest(Parcel in) {
        super();
        mAction = in.readString();
        mUri = Uri.parse(in.readString());
        mQuery = in.readString();
        mUserQuery = in.readString();
        mExtraDataKey = in.readString();
    }

    public SearchRequest(Bundle in) {
        mAction = in.getString(ACTION);
        mUri = in.getParcelable(URI);
        mQuery = in.getString(QUERY);
        mUserQuery = in.getString(USER_QUERY);
        mExtraDataKey = in.getString(EXTRA_DATA_KEY);
    }

    public SearchRequest(Intent i) {
        mAction = i.getAction();
        mUri = i.getData();
        mQuery = i.getStringExtra(QUERY);
        mUserQuery = i.getStringExtra(USER_QUERY);
        mExtraDataKey = i.getStringExtra(EXTRA_DATA_KEY);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAction);
        dest.writeString(mUri == null ? null : mUri.toString());
        dest.writeString(mQuery);
        dest.writeString(mUserQuery);
        dest.writeString(mExtraDataKey);
    }
}
