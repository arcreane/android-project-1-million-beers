package com.example.rpgpartymanager.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rpgpartymanager.R;
import com.example.rpgpartymanager.adapters.RollHistoryAdapter;
import com.example.rpgpartymanager.utils.DiceManager;

import java.util.ArrayList;
import java.util.List;

public class DiceRollActivity extends AppCompatActivity {

    private static final long ROLL_ANIMATION_STEP_MS = 70;
    private static final int ROLL_ANIMATION_STEPS = 14;
    private static final int SHAKE_ANIMATION_STEPS = 8;
    private static final int MAX_HISTORY_ITEMS = 10;

    private int selectedSides = 20;
    private MediaPlayer rollPlayer;
    private Handler rollHandler;
    private boolean isRolling;
    private TextView diceVisual;
    private TextView result;
    private EditText customSidesInput;
    private final List<String> rollHistory = new ArrayList<>();
    private RollHistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        setContentView(R.layout.activity_dice_roll);

        setTitle("Dice Roller");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Spinner diceSpinner = findViewById(R.id.spinnerDice);
        diceVisual = findViewById(R.id.txtDiceVisual);
        result = findViewById(R.id.txtResult);
        customSidesInput = findViewById(R.id.inputCustomSides);
        TextView vibrationStatus = findViewById(R.id.txtVibrationStatus);
        RecyclerView historyRecycler = findViewById(R.id.rvRollHistory);
        rollHandler = new Handler(Looper.getMainLooper());

        vibrationStatus.setText(hasVibrator()
                ? "Vibration available: rolls will buzz."
                : "Vibration unavailable on this device.");

        historyAdapter = new RollHistoryAdapter(rollHistory);
        historyRecycler.setLayoutManager(new LinearLayoutManager(this));
        historyRecycler.setAdapter(historyAdapter);

        diceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                customSidesInput.setVisibility("Custom".equals(selected) ? View.VISIBLE : View.GONE);
                selectedSides = "Custom".equals(selected) ? getCustomSidesOrDefault() : parseSides(selected);
                updateDiceStyle(selectedSides);
                result.setText("Ready to roll a D" + selectedSides + ".");
                diceVisual.setText("-");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSides = 20;
            }
        });
        diceSpinner.setSelection(5);
        customSidesInput.setVisibility(View.GONE);
        updateDiceStyle(selectedSides);

        prepareRollSound();

        findViewById(R.id.btnRoll).setOnClickListener(v -> {
            if (isRolling) {
                return;
            }

            selectedSides = getSelectedSides(diceSpinner);
            if (selectedSides < 2) {
                Toast.makeText(this, "Dice must have at least 2 sides.", Toast.LENGTH_SHORT).show();
                return;
            }

            int roll = DiceManager.roll(selectedSides);

            result.setText("Rolling D" + selectedSides + "...");
            playRollSound();
            startShakeAnimation();
            animateRoll(diceVisual, result, roll);

            Intent intent = new Intent(
                    this,
                    com.example.rpgpartymanager.receivers.DiceReceiver.class
            );

            intent.putExtra("roll", roll);
            intent.putExtra("sides", selectedSides);

            sendBroadcast(intent);
        });

        findViewById(R.id.btnClearHistory).setOnClickListener(v -> {
            rollHistory.clear();
            historyAdapter.notifyDataSetChanged();
        });
    }

    private void animateRoll(TextView diceVisual, TextView result, int finalRoll) {
        isRolling = true;
        updateDiceStyle(selectedSides);

        for (int step = 0; step < ROLL_ANIMATION_STEPS; step++) {
            int currentStep = step;
            rollHandler.postDelayed(() -> {
                int previewRoll = DiceManager.roll(selectedSides);
                diceVisual.setText(String.valueOf(previewRoll));

                if (currentStep == ROLL_ANIMATION_STEPS - 1) {
                    diceVisual.setText(String.valueOf(finalRoll));
                    result.setText("Rolled D" + selectedSides + ": " + finalRoll);
                    addHistoryItem("D" + selectedSides + " -> " + finalRoll);
                    isRolling = false;
                }
            }, currentStep * ROLL_ANIMATION_STEP_MS);
        }
    }

    private int getSelectedSides(Spinner diceSpinner) {
        String selected = diceSpinner.getSelectedItem().toString();
        return "Custom".equals(selected) ? getCustomSidesOrDefault() : parseSides(selected);
    }

    private int getCustomSidesOrDefault() {
        String value = customSidesInput.getText().toString().trim();
        if (value.isEmpty()) {
            return 20;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 20;
        }
    }

    private void addHistoryItem(String historyItem) {
        rollHistory.add(0, historyItem);
        if (rollHistory.size() > MAX_HISTORY_ITEMS) {
            rollHistory.remove(rollHistory.size() - 1);
        }
        historyAdapter.notifyDataSetChanged();
    }

    private void startShakeAnimation() {
        Animation shake = new TranslateAnimation(-12, 12, 0, 0);
        shake.setDuration(ROLL_ANIMATION_STEP_MS);
        shake.setRepeatCount(SHAKE_ANIMATION_STEPS - 1);
        shake.setRepeatMode(Animation.REVERSE);
        diceVisual.startAnimation(shake);
    }

    private void updateDiceStyle(int sides) {
        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setColor(Color.parseColor("#F7F1E3"));
        background.setCornerRadius(dpToPx(16));
        background.setStroke(dpToPx(3), getDiceBorderColor(sides));
        diceVisual.setBackground(background);
    }

    private int getDiceBorderColor(int sides) {
        if (sides <= 4) {
            return Color.parseColor("#9C27B0");
        } else if (sides <= 6) {
            return Color.parseColor("#2E7D32");
        } else if (sides <= 10) {
            return Color.parseColor("#0277BD");
        } else if (sides <= 12) {
            return Color.parseColor("#EF6C00");
        } else if (sides <= 20) {
            return Color.parseColor("#C62828");
        }
        return Color.parseColor("#37474F");
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    private boolean hasVibrator() {
        Vibrator vibrator;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibratorManager manager =
                    (VibratorManager) getSystemService(VIBRATOR_MANAGER_SERVICE);
            vibrator = manager == null ? null : manager.getDefaultVibrator();
        } else {
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        }

        return vibrator != null && vibrator.hasVibrator();
    }

    private int parseSides(String diceLabel) {
        return Integer.parseInt(diceLabel.replace("D", ""));
    }

    private void prepareRollSound() {
        int soundId = getResources().getIdentifier("roll", "raw", getPackageName());
        if (soundId != 0) {
            rollPlayer = MediaPlayer.create(this, soundId);
        }
    }

    private void playRollSound() {
        if (rollPlayer == null) {
            return;
        }

        rollPlayer.seekTo(0);
        rollPlayer.start();
    }

    @Override
    protected void onDestroy() {
        if (rollHandler != null) {
            rollHandler.removeCallbacksAndMessages(null);
        }

        if (rollPlayer != null) {
            rollPlayer.release();
            rollPlayer = null;
        }

        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
