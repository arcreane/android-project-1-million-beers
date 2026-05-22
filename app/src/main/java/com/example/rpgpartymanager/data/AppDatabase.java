package com.example.rpgpartymanager.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CharacterEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CharacterDao characterDao();

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "rpg_db"
            ).allowMainThreadQueries().build();
        }
        return instance;
    }
}