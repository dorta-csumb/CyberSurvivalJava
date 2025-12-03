package com.example.cybersurvivaljava.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.example.cybersurvivaljava.database.entities.Problems;

@Dao
public interface ProblemsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Problems problem);

    @Query("SELECT * FROM Problems WHERE category = :category ORDER BY RANDOM() LIMIT 1")
    Problems getRandomProblemByCategory(int category);

}
