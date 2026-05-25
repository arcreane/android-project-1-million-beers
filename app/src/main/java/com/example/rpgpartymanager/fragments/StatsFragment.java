package com.example.rpgpartymanager.fragments;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.rpgpartymanager.R;

public class StatsFragment extends Fragment {

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

        Bundle args = getArguments();

        hp.setText("HP: " + args.getInt("hp"));
        mana.setText("Mana: " + args.getInt("mana"));

        return v;
    }
}