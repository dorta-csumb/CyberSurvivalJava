package com.example.cybersurvivaljava.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.cybersurvivaljava.database.CyberSurvivalDatabase;

import java.util.Objects;

@Entity(tableName = CyberSurvivalDatabase.PROBLEMS_TABLE_NAME)
public class Problems {
    @PrimaryKey(autoGenerate = true)
    private int problemId;
    private int category;
    private String problemName;
    private String problemDescription;
    private String problemSolution;
    private String wrongSolution1;
    private String wrongSolution2;
    private String wrongSolution3;

    public Problems(int category, String problemName, String problemDescription, String problemSolution, String wrongSolution1, String wrongSolution2, String wrongSolution3) {
        this.category = category;
        this.problemName = problemName;
        this.problemDescription = problemDescription;
        this.problemSolution = problemSolution;
        this.wrongSolution1 = wrongSolution1;
        this.wrongSolution2 = wrongSolution2;
        this.wrongSolution3 = wrongSolution3;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Problems problems = (Problems) o;
        return problemId == problems.problemId && category == problems.category && Objects.equals(problemName, problems.problemName) && Objects.equals(problemDescription, problems.problemDescription) && Objects.equals(problemSolution, problems.problemSolution) && Objects.equals(wrongSolution1, problems.wrongSolution1) && Objects.equals(wrongSolution2, problems.wrongSolution2) && Objects.equals(wrongSolution3, problems.wrongSolution3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(problemId, category, problemName, problemDescription, problemSolution, wrongSolution1, wrongSolution2, wrongSolution3);
    }

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public String getProblemSolution() {
        return problemSolution;
    }

    public void setProblemSolution(String problemSolution) {
        this.problemSolution = problemSolution;
    }

    public String getWrongSolution1() {
        return wrongSolution1;
    }

    public void setWrongSolution1(String wrongSolution1) {
        this.wrongSolution1 = wrongSolution1;
    }

    public String getWrongSolution2() {
        return wrongSolution2;
    }

    public void setWrongSolution2(String wrongSolution2) {
        this.wrongSolution2 = wrongSolution2;
    }

    public String getWrongSolution3() {
        return wrongSolution3;
    }

    public void setWrongSolution3(String wrongSolution3) {
        this.wrongSolution3 = wrongSolution3;
    }
}
