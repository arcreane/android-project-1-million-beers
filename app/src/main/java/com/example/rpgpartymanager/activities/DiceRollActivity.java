package com.example.rpgpartymanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rpgpartymanager.R;

import java.util.Random;

public class DiceRollActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        setContentView(R.layout.activity_dice_roll);

        setTitle("Dice Roller");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView result = findViewById(R.id.txtResult);

        findViewById(R.id.btnRoll).setOnClickListener(v -> {

            int roll = new Random().nextInt(20) + 1;

            result.setText("Rolled: " + roll);

            Intent intent = new Intent(
                    this,
                    com.example.rpgpartymanager.receivers.DiceReceiver.class
            );

            intent.putExtra("roll", roll);

            sendBroadcast(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}