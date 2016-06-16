package com.github.dstaflund.geomemorial.ui.fragment.searchresultitem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface SearchResultItemFragmentPresenter {
    void onCreate(@Nullable Bundle savedState);

    @NonNull
    View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b);
}
