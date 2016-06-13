package com.github.dstaflund.geomemorial.ui.fragment.preferences;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;

public interface PreferencesFragmentView {
    void setHasOptionsMenu(boolean b);
    void setNestedScrollView(@Nullable NestedScrollView value);
    void setRetainInstance(boolean b);

    Context getContext();
    NestedScrollView getNestedScrollView();
}
