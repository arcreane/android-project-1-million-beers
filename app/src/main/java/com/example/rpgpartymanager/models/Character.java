package com.example.rpgpartymanager.models;

public class Character {

    private int id;
    private String name;
    private String role;
    private int hp;
    private int mana;

    public Character(int id, String name, String role, int hp, int mana) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.hp = hp;
        this.mana = mana;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public int getHp() { return hp; }
    public int getMana() { return mana; }
}