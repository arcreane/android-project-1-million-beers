package com.example.rpgpartymanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rpgpartymanager.R;
import com.example.rpgpartymanager.data.AppDatabase;
import com.example.rpgpartymanager.data.CharacterEntity;
import com.example.rpgpartymanager.fragments.StatsFragment;

public class CharacterDetailActivity extends AppCompatActivity {

    private AppDatabase db;
    private CharacterEntity entity;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_character_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = AppDatabase.getInstance(this);

        int id = getIntent().getIntExtra("id", -1);
        entity = db.characterDao().getById(id);

        setTitle(entity.name);

        refreshUI();

        findViewById(R.id.btnHpPlus).setOnClickListener(v -> {
            entity.hp += 10;
            save();
        });

        findViewById(R.id.btnHpMinus).setOnClickListener(v -> {
            entity.hp = Math.max(0, entity.hp - 10);
            save();
        });

        findViewById(R.id.btnManaPlus).setOnClickListener(v -> {
            entity.mana += 10;
            save();
        });

        findViewById(R.id.btnManaMinus).setOnClickListener(v -> {
            entity.mana = Math.max(0, entity.mana - 10);
            save();
        });
    }

    private void save() {
        db.characterDao().update(entity);
        refreshUI();
    }

    private void refreshUI() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.statsContainer,
                        StatsFragment.newInstance(entity.hp, entity.mana))
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.character_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_share) {
            shareCharacter();
            return true;
        }

        if (item.getItemId() == R.id.menu_reset) {
            // existing reset code
            return true;
        }

        if (item.getItemId() == R.id.menu_edit) {

            Intent i = new Intent(this, EditCharacterActivity.class);
            i.putExtra("id", entity.id);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareCharacter() {
        String summary = entity.name + "\n"
                + "Class: " + entity.role + "\n"
                + "HP: " + entity.hp + "\n"
                + "Mana: " + entity.mana;

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, entity.name + " character sheet");
        sendIntent.putExtra(Intent.EXTRA_TEXT, summary);

        Intent chooser = Intent.createChooser(sendIntent, "Share Character");
        startActivity(chooser);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
