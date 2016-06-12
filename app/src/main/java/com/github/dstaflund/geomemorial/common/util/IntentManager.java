package com.github.dstaflund.geomemorial.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public final class IntentManager {

    private IntentManager(){
        super();
    }

    public static void startActivity(@NonNull Context ctx, @NonNull Class<? extends Activity> clz){
        Intent i = new Intent(ctx, clz);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);
    }
}
