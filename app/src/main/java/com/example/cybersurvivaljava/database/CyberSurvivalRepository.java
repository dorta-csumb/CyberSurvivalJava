package com.example.cybersurvivaljava.database;

import android.app.Application;

import com.example.cybersurvivaljava.database.entities.Problems;
import com.example.cybersurvivaljava.database.entities.User;
import com.example.cybersurvivaljava.database.entities.UserProblems;

public class CyberSurvivalRepository {
    private final UserDAO userDAO;
    private final ProblemsDAO problemsDAO;
    private final UserProblemsDAO userProblemsDAO;

    private CyberSurvivalRepository(Application application) {
        CyberSurvivalDatabase db = CyberSurvivalDatabase.getDatabase(application);
        this.userDAO = db.userDAO();
        this.problemsDAO = db.problemsDAO();
        this.userProblemsDAO = db.userProblemsDAO();
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
}
