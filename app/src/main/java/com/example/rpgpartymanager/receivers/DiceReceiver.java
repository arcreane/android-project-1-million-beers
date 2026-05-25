package com.example.rpgpartymanager.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.widget.Toast;

public class DiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int roll = intent.getIntExtra("roll", 0);
        int sides = intent.getIntExtra("sides", 20);
        boolean shouldVibrate = intent.getBooleanExtra("vibrate", true);

        if (shouldVibrate) {
            Vibrator vibrator = getVibrator(context);
            vibrate(vibrator, roll <= 2 ? 350 : 120);
        }

        if (roll == sides) {
            Toast.makeText(context,
                    "Critical Hit!",
                    Toast.LENGTH_SHORT).show();

        } else if (roll == 1) {
            Toast.makeText(context,
                    "Critical Failure!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private Vibrator getVibrator(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibratorManager manager =
                    (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            return manager == null ? null : manager.getDefaultVibrator();
        }

        return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    private void vibrate(Vibrator vibrator, long durationMs) {
        if (vibrator == null || !vibrator.hasVibrator()) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                    VibrationEffect.createOneShot(
                            durationMs,
                            VibrationEffect.DEFAULT_AMPLITUDE
                    )
            );
        } else {
            vibrator.vibrate(durationMs);
        }
    }
}
