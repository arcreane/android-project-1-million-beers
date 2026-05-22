package com.example.rpgpartymanager.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rpgpartymanager.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnCharacters).setOnClickListener(v ->
                startActivity(new Intent(this, CharacterListActivity.class)));

        findViewById(R.id.btnDice).setOnClickListener(v ->
                startActivity(new Intent(this, DiceRollActivity.class)));
    }
}