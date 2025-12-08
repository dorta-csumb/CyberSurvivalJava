package com.example.cybersurvivaljava.database;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.cybersurvivaljava.database.entities.Problems;
import com.example.cybersurvivaljava.database.entities.User;
import com.example.cybersurvivaljava.database.entities.UserProblems;

import java.util.List;

public class CyberSurvivalRepository {
    private final UserDAO userDAO;
    private final ProblemsDAO problemsDAO;
    private final UserProblemsDAO userProblemsDAO;

    private static volatile CyberSurvivalRepository repository;

    private CyberSurvivalRepository(Application application) {
        CyberSurvivalDatabase db = CyberSurvivalDatabase.getDatabase(application);
        this.userDAO = db.userDAO();
        this.problemsDAO = db.problemsDAO();
        this.userProblemsDAO = db.userProblemsDAO();
    }

    public static CyberSurvivalRepository getRepository(Application application) {
        if (repository == null) {
            synchronized (CyberSurvivalRepository.class) {
                if (repository == null) {
                    repository = new CyberSurvivalRepository(application);
                }
            }
        }
        return repository;
    }

    // User Methods
    public void insertUser(User user) {
        CyberSurvivalDatabase.databaseWriteExecutor.execute(() -> userDAO.insert(user));
    }

    public LiveData<User> getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    public LiveData<User> getUserById(int userId) {
        return userDAO.getUserById(userId);
    }

    // Problems Methods
    public void insertProblem(Problems problem) {
        CyberSurvivalDatabase.databaseWriteExecutor.execute(() -> problemsDAO.insert(problem));
    }

    public void deleteProblem(Problems problem) {
        CyberSurvivalDatabase.databaseWriteExecutor.execute(() -> problemsDAO.delete(problem));
    }

    public LiveData<List<Problems>> getAllProblems() {
        return problemsDAO.getAllProblems();
    }

    // UserProblems Methods
    public void insertUserProblem(UserProblems userProblem) {
        CyberSurvivalDatabase.databaseWriteExecutor.execute(() -> userProblemsDAO.insert(userProblem));
    }


    public Problems getRandomProblemByCategory(int category) {
        return problemsDAO.getRandomProblemByCategory(category);
    }

    public LiveData<List<Problems>> getProblemsByCategory(int categoryId) {
        return problemsDAO.getProblemsByCategory(categoryId);
    }

    // High Score Integration
    public androidx.lifecycle.LiveData<java.util.List<com.example.cybersurvivaljava.HighScoreEntry>> getHighScores() {
        return userProblemsDAO.getHighScores();
    }
}
