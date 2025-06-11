package com.example.mobile.utils.helper;

import android.content.Context;
import android.os.Vibrator;

public class MotorHelper {
    private final Context ctx;

    public MotorHelper(final Context ctx) {
        this.ctx = ctx;
    }

    public void initiateVibration() {
        final Vibrator v = (Vibrator) this.ctx.getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null && v.hasVibrator()) {
            final long[] pattern = {0, 500, 500};
            v.vibrate(pattern, 0);
        }
    }

    public void stopVibration() {
        final Vibrator v = (Vibrator) this.ctx.getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null) {
            v.cancel();
        }
    }
}
