package com.example.rpgpartymanager.fragments;

import android.os.Bundle;
import android.text.InputType;
import android.view.*;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.rpgpartymanager.R;

public class StatsFragment extends Fragment {

    public interface StatsListener {
        void onHpChanged(int amount);
        void onManaChanged(int amount);
    }

    public static StatsFragment newInstance(int hp, int mana) {
        StatsFragment f = new StatsFragment();
        Bundle b = new Bundle();
        b.putInt("hp", hp);
        b.putInt("mana", mana);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup c, Bundle b) {
        View v = i.inflate(R.layout.fragment_stats, c, false);

        TextView hp = v.findViewById(R.id.txtHp);
        TextView mana = v.findViewById(R.id.txtMana);
        ProgressBar hpBar = v.findViewById(R.id.progressHp);
        ProgressBar manaBar = v.findViewById(R.id.progressMana);

        Bundle args = getArguments();
        int hpValue = args.getInt("hp");
        int manaValue = args.getInt("mana");

        hp.setText("HP: " + hpValue);
        mana.setText("Mana: " + manaValue);
        hpBar.setProgress(Math.max(0, Math.min(100, hpValue)));
        manaBar.setProgress(Math.max(0, Math.min(100, manaValue)));

        StatsListener listener = (StatsListener) requireActivity();
        bindButton(v, R.id.btnHpMinus3, () -> listener.onHpChanged(-3));
        bindButton(v, R.id.btnHpMinus1, () -> listener.onHpChanged(-1));
        bindButton(v, R.id.btnHpCustom, () -> showCustomStatDialog("HP", listener::onHpChanged));
        bindButton(v, R.id.btnHpPlus1, () -> listener.onHpChanged(1));
        bindButton(v, R.id.btnHpPlus3, () -> listener.onHpChanged(3));

        bindButton(v, R.id.btnManaMinus3, () -> listener.onManaChanged(-3));
        bindButton(v, R.id.btnManaMinus1, () -> listener.onManaChanged(-1));
        bindButton(v, R.id.btnManaCustom, () -> showCustomStatDialog("Mana", listener::onManaChanged));
        bindButton(v, R.id.btnManaPlus1, () -> listener.onManaChanged(1));
        bindButton(v, R.id.btnManaPlus3, () -> listener.onManaChanged(3));

        return v;
    }

    private void bindButton(View view, int id, Runnable action) {
        Button button = view.findViewById(id);
        button.setOnClickListener(v -> action.run());
    }

    private void showCustomStatDialog(String statName, StatChangeHandler handler) {
        EditText input = new EditText(requireContext());
        input.setHint("Example: 5 or -5");
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

        new AlertDialog.Builder(requireContext())
                .setTitle("Change " + statName)
                .setView(input)
                .setPositiveButton("Apply", (dialog, which) -> {
                    String value = input.getText().toString().trim();
                    if (!value.isEmpty()) {
                        handler.onChange(Integer.parseInt(value));
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private interface StatChangeHandler {
        void onChange(int amount);
    }
}
