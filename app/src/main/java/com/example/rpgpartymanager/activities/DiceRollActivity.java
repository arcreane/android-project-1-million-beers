package com.example.rpgpartymanager.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rpgpartymanager.R;
import com.example.rpgpartymanager.utils.DiceManager;

public class DiceRollActivity extends AppCompatActivity {

    private int selectedSides = 20;
    private MediaPlayer rollPlayer;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        setContentView(R.layout.activity_dice_roll);

        setTitle("Dice Roller");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Spinner diceSpinner = findViewById(R.id.spinnerDice);
        TextView diceVisual = findViewById(R.id.txtDiceVisual);
        TextView result = findViewById(R.id.txtResult);

        diceSpinner.setSelection(5);
        diceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSides = parseSides(parent.getItemAtPosition(position).toString());
                result.setText("Ready to roll a D" + selectedSides + ".");
                diceVisual.setText("-");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSides = 20;
            }
        });

        prepareRollSound();

        findViewById(R.id.btnRoll).setOnClickListener(v -> {
            int roll = DiceManager.roll(selectedSides);

            diceVisual.setText(String.valueOf(roll));
            result.setText("Rolled D" + selectedSides + ": " + roll);
            playRollSound();

            Intent intent = new Intent(
                    this,
                    com.example.rpgpartymanager.receivers.DiceReceiver.class
            );

            intent.putExtra("roll", roll);
            intent.putExtra("sides", selectedSides);

            sendBroadcast(intent);
        });
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
