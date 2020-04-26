package com.github.dstaflund.geomemorial.ui.fragment.about;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import android.view.View;

public interface AboutFragmentView {
    void setHasOptionsMenu(boolean b);
    void setRetainInstance(boolean b);
    void setRootView(@NonNull View value);

    Context getContext();
    NestedScrollView getNestedScrollView();
    View getRootView();
}
