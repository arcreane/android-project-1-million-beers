package com.example.rpgpartymanager.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rpgpartymanager.R;
import com.example.rpgpartymanager.data.AppDatabase;
import com.example.rpgpartymanager.data.CharacterEntity;

public class EditCharacterActivity extends AppCompatActivity {

    private AppDatabase db;
    private CharacterEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_character);

        setTitle("Editing Character");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = AppDatabase.getInstance(this);

        int id = getIntent().getIntExtra("id", -1);
        entity = db.characterDao().getById(id);

        EditText name = findViewById(R.id.inputName);
        EditText role = findViewById(R.id.inputClass);
        EditText hp = findViewById(R.id.inputHp);
        EditText mana = findViewById(R.id.inputMana);
        Button save = findViewById(R.id.btnSave);

        // PREFILL DATA
        name.setText(entity.name);
        role.setText(entity.role);
        hp.setText(String.valueOf(entity.hp));
        mana.setText(String.valueOf(entity.mana));

        save.setOnClickListener(v -> {

            entity.name = name.getText().toString();
            entity.role = role.getText().toString();
            entity.hp = Integer.parseInt(hp.getText().toString());
            entity.mana = Integer.parseInt(mana.getText().toString());

            db.characterDao().update(entity);

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