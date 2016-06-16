package com.github.dstaflund.geomemorial.ui.fragment.about;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.dstaflund.geomemorial.R;

import java.util.List;

public class AboutFragmentPresenterImpl implements AboutFragmentPresenter {
    private static final String sXPosKey = "X_POS";
    private static final String sYPosKey = "Y_POS";

    private AboutFragmentView mView;

    public AboutFragmentPresenterImpl(@NonNull AboutFragmentView view){
        super();
        mView = view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        mView.setRetainInstance(true);
        mView.setHasOptionsMenu(false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        NestedScrollView nsv = mView.getNestedScrollView();
        if (nsv != null) {
            outState.putInt(sXPosKey, nsv.getScrollX());
            outState.putInt(sYPosKey, nsv.getScrollY());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedState) {
        if (savedState != null) {
            NestedScrollView nsv = mView.getNestedScrollView();
            if (nsv != null) {
                nsv.setScrollX(savedState.getInt(sXPosKey));
                nsv.setScrollY(savedState.getInt(sYPosKey));
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b) {
        View v = i.inflate(R.layout.fragment_about, c, false);
        if (v != null){
            NestedScrollView nsv = (NestedScrollView) v.findViewById(R.id.fragment_about_nested_scroll_view);
            mView.setNestedScrollView(nsv);
        }

        // Disable email link if it can't be handled
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:darryl.staflund@hushmail.com"));
        PackageManager manager = mView.getContext().getPackageManager();
        List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
        if (infos.size() <= 0) {
            TextView tv = (TextView) v.findViewById(R.id.fragment_about_contact_value);
            tv.setAutoLinkMask(0);
            tv.setLinksClickable(false);
        }

        return v;
    }
}
