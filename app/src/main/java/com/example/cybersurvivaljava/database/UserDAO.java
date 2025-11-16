package com.example.cybersurvivaljava.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.cybersurvivaljava.database.entities.User;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Query("DELETE FROM " + CyberSurvivalDatabase.USER_TABLE_NAME)
    void deleteAll();
}
