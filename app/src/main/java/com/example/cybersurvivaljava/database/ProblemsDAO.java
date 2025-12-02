package com.example.cybersurvivaljava.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.cybersurvivaljava.database.entities.Problems;

import java.util.List;

@Dao
public interface ProblemsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Problems problem);

    @Delete
    void delete(Problems problem);

    @Query("SELECT * FROM " + CyberSurvivalDatabase.PROBLEMS_TABLE_NAME + " ORDER BY problemName ASC")
    LiveData<List<Problems>> getAllProblems();

    @Query("SELECT COUNT(*) FROM " + CyberSurvivalDatabase.PROBLEMS_TABLE_NAME)
    int count();
}
