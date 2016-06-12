package com.github.dstaflund.geomemorial.ui.fragment.about;

import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;

public interface AboutFragmentView {
    void setHasOptionsMenu(boolean b);
    void setNestedScrollView(@NonNull NestedScrollView v);
    void setRetainInstance(boolean b);

    NestedScrollView getNestedScrollView();

}
