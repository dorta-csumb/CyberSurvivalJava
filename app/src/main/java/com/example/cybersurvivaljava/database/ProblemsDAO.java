package com.example.cybersurvivaljava.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.example.cybersurvivaljava.database.entities.Problems;

@Dao
public interface ProblemsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Problems problem);
}
