package com.example.cybersurvivaljava.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.cybersurvivaljava.HighScoreEntry;
import com.example.cybersurvivaljava.database.entities.UserProblems;

import java.util.List;

@Dao
public interface UserProblemsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserProblems userProblems);

    // REAL DATA CALCULATION:
    // 1. Join UserProblems with User table to get the name.
    // 2. Accuracy = (Correct Answers / Total Rows) * 100.
    // 3. Speed = Average of timeTaken (using COALESCE to handle nulls safely).
    // 4. Tasks = Sum of Correct Answers.
    @Query("SELECT " +
            "u.username AS userName, " +
            "(CAST(SUM(CASE WHEN up.isCorrect THEN 1 ELSE 0 END) AS INTEGER) * 100 / COUNT(*)) AS totalAccuracy, " +
            "CAST(AVG(COALESCE(up.timeTaken, 0)) AS INTEGER) AS totalSpeed, " +
            "CAST(SUM(CASE WHEN up.isCorrect THEN 1 ELSE 0 END) AS INTEGER) AS tasksCompleted " +
            "FROM " + CyberSurvivalDatabase.USER_PROBLEMS_TABLE_NAME + " up " +
            "JOIN " + CyberSurvivalDatabase.USER_TABLE_NAME + " u ON up.userId = u.userId " +
            "GROUP BY up.userId " +
            "ORDER BY tasksCompleted DESC")
    LiveData<List<HighScoreEntry>> getHighScores();
}

