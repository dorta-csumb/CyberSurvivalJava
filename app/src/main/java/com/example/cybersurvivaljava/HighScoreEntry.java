package com.example.cybersurvivaljava;

public class HighScoreEntry { // data for HighScore that tells us the username, accuracy, speed, and tasks completed by the player.
    private String userName;
    private int totalAccuracy;
    private long totalSpeed;
    private int tasksCompleted;

    public HighScoreEntry(String userName, int totalAccuracy, long totalSpeed, int tasksCompleted) {
        this.userName = userName;
        this.totalAccuracy = totalAccuracy;
        this.totalSpeed = totalSpeed;
        this.tasksCompleted = tasksCompleted;
    }

    // Getters are our public "doorway" to the private data
    // The Adapter is going to call the getter methods to get the username, accuracy, etc for each row in the RecyclerView
    public String getUserName() {
        return userName;
    }

    public int getTotalAccuracy() {
        return totalAccuracy;
    }

    public long getTotalSpeed() {
        return totalSpeed;
    }

    public int getTasksCompleted() {
        return tasksCompleted;
    }



}
