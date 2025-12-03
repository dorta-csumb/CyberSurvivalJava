package com.example.cybersurvivaljava.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.example.cybersurvivaljava.database.entities.Problems;

import java.util.List;

@Dao
public interface ProblemsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Problems problem);

    @Query("SELECT * FROM Problems WHERE category = :category ORDER BY RANDOM() LIMIT 1")
    Problems getRandomProblemByCategory(int category);

    @Query("SELECT * FROM " + CyberSurvivalDatabase.PROBLEMS_TABLE_NAME + " WHERE category = :categoryId")
    LiveData<List<Problems>> getProblemsByCategory(int categoryId);

}
