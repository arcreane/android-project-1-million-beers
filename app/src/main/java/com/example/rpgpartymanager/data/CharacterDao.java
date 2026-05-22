package com.example.rpgpartymanager.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CharacterDao {

    @Query("SELECT * FROM CharacterEntity")
    List<CharacterEntity> getAll();

    @Query("SELECT * FROM CharacterEntity WHERE id = :id LIMIT 1")
    CharacterEntity getById(int id);

    @Insert
    void insert(CharacterEntity character);

    @Delete
    void delete(CharacterEntity character);

    @Update
    void update(CharacterEntity character);
}