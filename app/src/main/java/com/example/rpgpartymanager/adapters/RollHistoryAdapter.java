package com.example.rpgpartymanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rpgpartymanager.R;

import java.util.List;

public class RollHistoryAdapter extends RecyclerView.Adapter<RollHistoryAdapter.RollViewHolder> {

    private final List<String> rolls;

    public RollHistoryAdapter(List<String> rolls) {
        this.rolls = rolls;
    }

    @NonNull
    @Override
    public RollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_roll_history, parent, false);
        return new RollViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RollViewHolder holder, int position) {
        holder.historyItem.setText(rolls.get(position));
    }

    @Override
    public int getItemCount() {
        return rolls.size();
    }

    static class RollViewHolder extends RecyclerView.ViewHolder {
        private final TextView historyItem;

        RollViewHolder(@NonNull View itemView) {
            super(itemView);
            historyItem = itemView.findViewById(R.id.txtHistoryItem);
        }
    }
}
