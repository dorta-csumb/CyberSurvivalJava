package com.example.cybersurvivaljava.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.cybersurvivaljava.database.entities.Problems;
import com.example.cybersurvivaljava.database.entities.User;
import com.example.cybersurvivaljava.database.entities.UserProblems;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CyberSurvivalRepository {
    private final UserDAO userDAO;
    private final ProblemsDAO problemsDAO;
    private final UserProblemsDAO userProblemsDAO;

    private static CyberSurvivalRepository repository;

    private CyberSurvivalRepository(Application application) {
        CyberSurvivalDatabase db = CyberSurvivalDatabase.getDatabase(application);
        this.userDAO = db.userDAO();
        this.problemsDAO = db.problemsDAO();
        this.userProblemsDAO = db.userProblemsDAO();
    }

    public static CyberSurvivalRepository getRepository(Application application) {
        if (repository != null) {
            return repository;
        }
        Future<CyberSurvivalRepository> future = CyberSurvivalDatabase.databaseWriteExecutor.submit(
                new Callable<CyberSurvivalRepository>() {
                    @Override
                    public CyberSurvivalRepository call() throws Exception {
                        return new CyberSurvivalRepository(application);
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d("CyberSurvivalRepository", "Error getting repository");
        }
        return null;
    }

    public void insertUser(User user) {
        CyberSurvivalDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.insert(user);
        });
    }

    public void insertProblem(Problems problem) {
        CyberSurvivalDatabase.databaseWriteExecutor.execute(() -> {
            problemsDAO.insert(problem);
        });
    }

    public void insertUserProblem(UserProblems userProblem) {
        CyberSurvivalDatabase.databaseWriteExecutor.execute(() -> {
            userProblemsDAO.insert(userProblem);
        });
    }

    public LiveData<User> getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    public LiveData<User> getUserById(int userId) {
        return userDAO.getUserById(userId);
    }
}
