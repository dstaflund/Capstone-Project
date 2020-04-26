package com.github.dstaflund.geomemorial.common.util;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.Toast;

public final class ToastManager {

    @NonNull
    public static Toast newToast(
        @NonNull Context context,
        @Nullable Toast currentToast,
        @NonNull String message,
        int duration
    ){
        if (currentToast != null){
            currentToast.cancel();
        }

        final Toast newToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        newToast.setText(message);
        newToast.setDuration(duration);
        newToast.show();

        return newToast;
    }

    private ToastManager(){
        super();
    }
}
