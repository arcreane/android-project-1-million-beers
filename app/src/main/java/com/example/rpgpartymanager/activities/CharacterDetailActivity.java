package com.example.rpgpartymanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rpgpartymanager.R;
import com.example.rpgpartymanager.data.AppDatabase;
import com.example.rpgpartymanager.data.CharacterEntity;
import com.example.rpgpartymanager.fragments.StatsFragment;

public class CharacterDetailActivity extends AppCompatActivity implements StatsFragment.StatsListener {

    private static final String KEY_INITIAL_HP = "initial_hp";
    private static final String KEY_INITIAL_MANA = "initial_mana";

    private AppDatabase db;
    private CharacterEntity entity;
    private int initialHp;
    private int initialMana;

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
        if (b == null) {
            initialHp = entity.hp;
            initialMana = entity.mana;
        } else {
            initialHp = b.getInt(KEY_INITIAL_HP, entity.hp);
            initialMana = b.getInt(KEY_INITIAL_MANA, entity.mana);
        }

        setTitle(entity.name);

        refreshUI();

    }

    @Override
    public void onHpChanged(int amount) {
        entity.hp = Math.max(0, entity.hp + amount);
        save();
    }

    @Override
    public void onManaChanged(int amount) {
        entity.mana = Math.max(0, entity.mana + amount);
        save();
    }

    private void save() {
        db.characterDao().update(entity);
        refreshUI();
    }

    private void refreshUI() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.statsContainer,
                        StatsFragment.newInstance(entity.hp, entity.mana, initialHp, initialMana))
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
            resetStats();
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

    private void resetStats() {
        entity.hp = initialHp;
        entity.mana = initialMana;
        save();
        Toast.makeText(this, "Stats reset", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_INITIAL_HP, initialHp);
        outState.putInt(KEY_INITIAL_MANA, initialMana);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
