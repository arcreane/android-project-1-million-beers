package com.example.rpgpartymanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.SparseIntArray;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rpgpartymanager.R;
import com.example.rpgpartymanager.models.Character;

import java.util.ArrayList;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.ViewHolder> {

    public interface OnCharacterActions {
        void onOpen(Character character, View anchor);
    }

    public interface OnCharacterDelete {
        void onDelete(int position);
    }

    public interface OnStatChange {
        void onChange(int characterId, boolean health, int amount);
        void onReset(int characterId);
    }

    private ArrayList<Character> list;
    private OnCharacterActions actionsListener;
    private OnCharacterDelete deleteListener;
    private final OnStatChange statListener;
    private final SparseIntArray initialHp;
    private final SparseIntArray initialMana;

    public CharacterAdapter(ArrayList<Character> list,
                            OnCharacterActions actionsListener,
                            OnCharacterDelete deleteListener,
                            OnStatChange statListener,
                            SparseIntArray initialHp,
                            SparseIntArray initialMana) {
        this.list = list;
        this.actionsListener = actionsListener;
        this.deleteListener = deleteListener;
        this.statListener = statListener;
        this.initialHp = initialHp;
        this.initialMana = initialMana;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_character, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Character c = list.get(position);

        holder.name.setText(c.getName());
        holder.role.setText(c.getRole());
        holder.role.setVisibility(c.getRole() == null || c.getRole().isEmpty() ? View.GONE : View.VISIBLE);
        holder.hp.setText(String.valueOf(c.getHp()));
        holder.mana.setText(String.valueOf(c.getMana()));
        holder.hpBar.setProgress(percent(c.getHp(), initialHp.get(c.getId())));
        holder.manaBar.setProgress(percent(c.getMana(), initialMana.get(c.getId())));
        holder.hpBar.setContentDescription(holder.itemView.getContext().getString(R.string.party_hp_accessibility, c.getHp()));
        holder.manaBar.setContentDescription(holder.itemView.getContext().getString(R.string.party_mana_accessibility, c.getMana()));

        bindStatButton(holder, R.id.partyHpMinus3, c, true, -3);
        bindStatButton(holder, R.id.partyHpMinus1, c, true, -1);
        bindStatButton(holder, R.id.partyHpPlus1, c, true, 1);
        bindStatButton(holder, R.id.partyHpPlus3, c, true, 3);
        bindStatButton(holder, R.id.partyManaMinus3, c, false, -3);
        bindStatButton(holder, R.id.partyManaMinus1, c, false, -1);
        bindStatButton(holder, R.id.partyManaPlus1, c, false, 1);
        bindStatButton(holder, R.id.partyManaPlus3, c, false, 3);
        holder.itemView.findViewById(R.id.btnPartyReset).setOnClickListener(v -> statListener.onReset(c.getId()));

        View actions = holder.itemView.findViewById(R.id.btnPartyActions);
        actions.setContentDescription(holder.itemView.getContext().getString(R.string.party_actions_for, c.getName()));
        ViewCompat.setTooltipText(actions, holder.itemView.getContext().getString(R.string.party_actions));
        actions.setOnClickListener(v -> actionsListener.onOpen(c, v));

        // DELETE BUTTON
        holder.itemView.findViewById(R.id.btnDelete).setOnClickListener(v -> {
            if (deleteListener != null) {
                int pos = holder.getBindingAdapterPosition();

                if (pos != RecyclerView.NO_POSITION) {
                    deleteListener.onDelete(pos);
                }
            }
        });
    }

    private void bindStatButton(ViewHolder holder, int viewId, Character character, boolean health, int amount) {
        holder.itemView.findViewById(viewId).setOnClickListener(v ->
                statListener.onChange(character.getId(), health, amount));
    }

    private int percent(int value, int baseline) {
        return (int) Math.max(0, Math.min(100, Math.round(value * 100d / Math.max(1, baseline))));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, role, hp, mana;
        ProgressBar hpBar, manaBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txtName);
            role = itemView.findViewById(R.id.txtRole);
            hp = itemView.findViewById(R.id.txtHp);
            mana = itemView.findViewById(R.id.txtMana);
            hpBar = itemView.findViewById(R.id.partyHpBar);
            manaBar = itemView.findViewById(R.id.partyManaBar);
        }
    }
}
