package com.github.dstaflund.geomemorial.ui.fragment.about;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface AboutFragmentPresenter {
    void onCreate(@Nullable Bundle savedState);
    void onSaveInstanceState(@NonNull Bundle outState);
    void onViewStateRestored(@Nullable Bundle savedState);

    @Nullable
    View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b);
}
