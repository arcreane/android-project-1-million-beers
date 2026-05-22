package com.example.rpgpartymanager.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CharacterEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String role;
    public int hp;
    public int mana;
}
