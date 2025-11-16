package com.example.cybersurvivaljava.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.cybersurvivaljava.database.CyberSurvivalDatabase;

@Entity(tableName = CyberSurvivalDatabase.USER_PROBLEMS_TABLE_NAME)
public class userProblems {
    private int userId;
    private int problemId;
    boolean isCorrect;

    userProblems(int userId, int problemId, boolean isCorrect){
        this.userId = userId;
        this.problemId = problemId;
        this.isCorrect = isCorrect;
    }
}
