package com.example.cybersurvivaljava.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.cybersurvivaljava.database.CyberSurvivalDatabase;

import java.util.Objects;

@Entity(tableName = CyberSurvivalDatabase.USER_PROBLEMS_TABLE_NAME)
public class UserProblems {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private int problemId;
    boolean isCorrect;

    public UserProblems(int userId, int problemId, boolean isCorrect){
        this.userId = userId;
        this.problemId = problemId;
        this.isCorrect = isCorrect;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserProblems that = (UserProblems) o;
        return userId == that.userId && problemId == that.problemId && isCorrect == that.isCorrect;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, problemId, isCorrect);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
