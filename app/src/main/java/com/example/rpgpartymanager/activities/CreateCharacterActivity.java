package com.example.rpgpartymanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rpgpartymanager.R;
import com.example.rpgpartymanager.data.AppDatabase;
import com.example.rpgpartymanager.data.CharacterEntity;

public class CreateCharacterActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_character);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = AppDatabase.getInstance(this);

        EditText name = findViewById(R.id.inputName);
        EditText role = findViewById(R.id.inputClass);
        EditText hp = findViewById(R.id.inputHp);
        EditText mana = findViewById(R.id.inputMana);

        Button btn = findViewById(R.id.btnCreate);

        btn.setOnClickListener(v -> {

            CharacterEntity e = new CharacterEntity();

            e.name = name.getText().toString();
            e.role = role.getText().toString();
            e.hp = Integer.parseInt(hp.getText().toString());
            e.mana = Integer.parseInt(mana.getText().toString());

            db.characterDao().insert(e);

            setResult(RESULT_OK);
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}