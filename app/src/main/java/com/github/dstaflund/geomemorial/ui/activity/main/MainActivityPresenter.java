package com.github.dstaflund.geomemorial.ui.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;

public interface MainActivityPresenter {
    void onCreate(@Nullable Bundle savedInstanceState);
    void onNewIntent(@NonNull Intent intent);
    void onSaveInstanceState(@NonNull Bundle outState);
    void onStart();

    boolean onBackPressed();
    boolean onCreateOptionsMenu(@NonNull Menu menu);
}
