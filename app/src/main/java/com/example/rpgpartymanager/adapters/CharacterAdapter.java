package com.example.rpgpartymanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rpgpartymanager.R;
import com.example.rpgpartymanager.models.Character;

import java.util.ArrayList;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.ViewHolder> {

    public interface OnCharacterClick {
        void onClick(Character character);
    }

    public interface OnCharacterDelete {
        void onDelete(int position);
    }

    private ArrayList<Character> list;
    private OnCharacterClick clickListener;
    private OnCharacterDelete deleteListener;

    public CharacterAdapter(ArrayList<Character> list,
                            OnCharacterClick clickListener,
                            OnCharacterDelete deleteListener) {
        this.list = list;
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
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
        holder.hp.setText("HP: " + c.getHp());
        holder.mana.setText("Mana: " + c.getMana());

        // OPEN DETAILS
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onClick(c);
            }
        });

        // DELETE BUTTON
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                int pos = holder.getAdapterPosition();

                if (pos != RecyclerView.NO_POSITION) {
                    deleteListener.onDelete(pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, role, hp, mana;
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txtName);
            role = itemView.findViewById(R.id.txtRole);
            hp = itemView.findViewById(R.id.txtHp);
            mana = itemView.findViewById(R.id.txtMana);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
