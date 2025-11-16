package com.example.cybersurvivaljava.database.entities;

import androidx.room.Entity;

import com.example.cybersurvivaljava.database.CyberSurvivalDatabase;

@Entity(tableName = CyberSurvivalDatabase.USER_PROBLEMS_TABLE_NAME)
public class UserProblems {
    private int userId;
    private int problemId;
    boolean isCorrect;

    UserProblems(int userId, int problemId, boolean isCorrect){
        this.userId = userId;
        this.problemId = problemId;
        this.isCorrect = isCorrect;
    }
}
