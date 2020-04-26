package com.github.dstaflund.geomemorial.ui.fragment.preferences;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public interface PreferencesFragmentView {
    void setHasOptionsMenu(boolean b);
    void setNestedScrollView(@Nullable NestedScrollView value);
    void setRetainInstance(boolean b);

    Context getContext();
    NestedScrollView getNestedScrollView();
}
