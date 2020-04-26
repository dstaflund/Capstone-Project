package com.github.dstaflund.geomemorial.ui.fragment.searchexamples;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface SearchExamplesFragmentPresenter {
    void onCreate(@Nullable Bundle savedState);

    @Nullable
    View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b);
}
