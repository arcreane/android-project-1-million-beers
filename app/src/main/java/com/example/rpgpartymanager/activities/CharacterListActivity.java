package com.example.rpgpartymanager.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rpgpartymanager.R;
import com.example.rpgpartymanager.adapters.CharacterAdapter;
import com.example.rpgpartymanager.data.AppDatabase;
import com.example.rpgpartymanager.data.CharacterEntity;
import com.example.rpgpartymanager.models.Character;

import java.util.ArrayList;
import java.util.List;

public class CharacterListActivity extends AppCompatActivity {

    private ArrayList<Character> list;
    private CharacterAdapter adapter;
    private AppDatabase db;

    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_list);

        setTitle("Characters");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = AppDatabase.getInstance(this);

        RecyclerView rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        loadFromDb();

        adapter = new CharacterAdapter(
                list,
                character -> {
                    Intent i = new Intent(this, CharacterDetailActivity.class);
                    i.putExtra("id", character.getId());
                    startActivity(i);
                },
                position -> {

                    Character c = list.get(position);

                    CharacterEntity entity = db.characterDao().getById(c.getId());

                    if (entity != null) {
                        db.characterDao().delete(entity);
                    }

                    loadFromDb();
                    adapter.notifyDataSetChanged();
                }
        );

        rv.setAdapter(adapter);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadFromDb();
                        adapter.notifyDataSetChanged();
                    }
                }
        );

        findViewById(R.id.btnAdd).setOnClickListener(v -> {
            Intent i = new Intent(this, CreateCharacterActivity.class);
            launcher.launch(i);
        });
    }

    private void loadFromDb() {

        List<CharacterEntity> entities = db.characterDao().getAll();

        list.clear();

        for (CharacterEntity e : entities) {
            list.add(new Character(
                    e.id,
                    e.name,
                    e.role,
                    e.hp,
                    e.mana
            ));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}