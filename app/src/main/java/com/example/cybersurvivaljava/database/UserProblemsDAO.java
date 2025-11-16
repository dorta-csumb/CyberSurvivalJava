package com.example.cybersurvivaljava.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.example.cybersurvivaljava.database.entities.UserProblems;

@Dao
public interface UserProblemsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserProblems userProblems);
}
