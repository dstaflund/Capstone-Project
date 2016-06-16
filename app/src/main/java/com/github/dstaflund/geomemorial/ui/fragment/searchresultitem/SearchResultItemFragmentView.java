package com.github.dstaflund.geomemorial.ui.fragment.searchresultitem;

import android.content.Context;
import android.os.Bundle;

public interface SearchResultItemFragmentView {
    void setHasOptionsMenu(boolean b);

    Bundle getArguments();
    Context getContext();
}
