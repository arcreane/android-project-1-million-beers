package com.example.rpgpartymanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
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
    private final SparseIntArray initialHp = new SparseIntArray();
    private final SparseIntArray initialMana = new SparseIntArray();
    private Bundle restoredBaselines;

    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_PartyBoard);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_list);
        restoredBaselines = savedInstanceState == null ? null : savedInstanceState.getBundle("party_baselines");
        setTitle(R.string.party_board_title);

        View board = findViewById(R.id.partyBoard);
        int padding = board.getPaddingLeft();
        ViewCompat.setOnApplyWindowInsetsListener(board, (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            view.setPadding(padding + bars.left, padding + bars.top, padding + bars.right, padding + bars.bottom);
            return insets;
        });
        ViewCompat.requestApplyInsets(board);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = AppDatabase.getInstance(this);

        RecyclerView rv = findViewById(R.id.recyclerView);
        int columns = getResources().getConfiguration().screenWidthDp >= 700 ? 2 : 1;
        rv.setLayoutManager(new GridLayoutManager(this, columns));

        list = new ArrayList<>();
        loadFromDb();

        adapter = new CharacterAdapter(
                list,
                this::showCharacterActions,
                position -> {

                    Character c = list.get(position);
                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.party_delete_title, c.getName()))
                            .setMessage(R.string.party_delete_message)
                            .setNegativeButton(android.R.string.cancel, null)
                            .setPositiveButton(R.string.party_delete, (dialog, which) -> {
                                CharacterEntity entity = db.characterDao().getById(c.getId());
                                if (entity != null) db.characterDao().delete(entity);
                                initialHp.delete(c.getId());
                                initialMana.delete(c.getId());
                                refreshList();
                            }).show();
                },
                new CharacterAdapter.OnStatChange() {
                    @Override
                    public void onChange(int characterId, boolean health, int amount) {
                        changeStat(characterId, health, amount);
                    }

                    @Override
                    public void onReset(int characterId) {
                        CharacterEntity entity = db.characterDao().getById(characterId);
                        if (entity == null) return;
                        entity.hp = initialHp.get(characterId, entity.hp);
                        entity.mana = initialMana.get(characterId, entity.mana);
                        db.characterDao().update(entity);
                        refreshList();
                    }
                },
                initialHp,
                initialMana
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
        findViewById(R.id.btnPartyDice).setOnClickListener(v ->
                startActivity(new Intent(this, DiceRollActivity.class)));
    }

    private void showCharacterActions(Character character, View anchor) {
        PopupMenu menu = new PopupMenu(new ContextThemeWrapper(this, R.style.ThemeOverlay_PartyMenu), anchor);
        menu.inflate(R.menu.party_character_actions);
        menu.setOnMenuItemClickListener(item -> {
            int action = item.getItemId();
            if (action == R.id.party_action_edit) {
                Intent intent = new Intent(this, EditCharacterActivity.class);
                intent.putExtra("id", character.getId());
                startActivity(intent);
            } else if (action == R.id.party_action_share) {
                shareCharacter(character.getId());
            } else if (action == R.id.party_action_hp || action == R.id.party_action_mana) {
                showStatAdjustment(character.getId(), action == R.id.party_action_hp);
            } else {
                return false;
            }
            return true;
        });
        menu.show();
    }

    private void shareCharacter(int characterId) {
        CharacterEntity entity = db.characterDao().getById(characterId);
        if (entity == null) return;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.party_share_subject, entity.name));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.party_share_summary,
                entity.name, entity.role == null ? "" : entity.role, entity.hp, entity.mana));
        startActivity(Intent.createChooser(intent, getString(R.string.party_share)));
    }

    private void showStatAdjustment(int characterId, boolean health) {
        EditText input = new EditText(this);
        input.setHint(R.string.party_adjust_hint);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(health ? R.string.party_adjust_hp : R.string.party_adjust_mana)
                .setView(input)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.party_apply, null)
                .create();
        dialog.setOnShowListener(ignored -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            try {
                int amount = Integer.parseInt(input.getText().toString().trim());
                changeStat(characterId, health, amount);
                dialog.dismiss();
            } catch (NumberFormatException exception) {
                input.setError(getString(R.string.party_invalid_amount));
            }
        }));
        dialog.show();
        input.requestFocus();
    }

    private void changeStat(int characterId, boolean health, int amount) {
        CharacterEntity entity = db.characterDao().getById(characterId);
        if (entity == null) return;
        int current = health ? entity.hp : entity.mana;
        int next = (int) Math.max(0L, Math.min(Integer.MAX_VALUE, (long) current + amount));
        if (health) entity.hp = next;
        else entity.mana = next;
        db.characterDao().update(entity);
        refreshList();
    }

    private void loadFromDb() {

        List<CharacterEntity> entities = db.characterDao().getAll();

        list.clear();

        for (CharacterEntity e : entities) {
            if (initialHp.indexOfKey(e.id) < 0) {
                int[] restored = restoredBaselines == null ? null : restoredBaselines.getIntArray(String.valueOf(e.id));
                initialHp.put(e.id, restored == null ? e.hp : restored[0]);
                initialMana.put(e.id, restored == null ? e.mana : restored[1]);
            }
            list.add(new Character(
                    e.id,
                    e.name,
                    e.role,
                    e.hp,
                    e.mana
            ));
        }
        TextView count = findViewById(R.id.txtPartyCount);
        count.setText(getResources().getQuantityString(R.plurals.party_character_count, list.size(), list.size()));
        findViewById(R.id.txtPartyEmpty).setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void refreshList() {
        loadFromDb();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Bundle baselines = new Bundle();
        for (int index = 0; index < initialHp.size(); index++) {
            int id = initialHp.keyAt(index);
            baselines.putIntArray(String.valueOf(id), new int[]{initialHp.get(id), initialMana.get(id)});
        }
        outState.putBundle("party_baselines", baselines);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (adapter != null) {
            loadFromDb();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
