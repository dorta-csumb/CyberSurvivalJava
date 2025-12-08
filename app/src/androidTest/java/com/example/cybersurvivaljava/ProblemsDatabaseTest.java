package com.example.cybersurvivaljava;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.cybersurvivaljava.database.CyberSurvivalDatabase;
import com.example.cybersurvivaljava.database.ProblemsDAO;
import com.example.cybersurvivaljava.database.entities.Problems;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ProblemsDatabaseTest {

    private CyberSurvivalDatabase db;
    private ProblemsDAO dao;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, CyberSurvivalDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.problemsDAO();
    }

    @After
    public void teardown() {
        db.close();
    }

    @Test
    public void insertAndGetProblem() {
        assertEquals(0, dao.count());
        Problems problem = new Problems(Problems.CATEGORY_PROGRAMMING, "Test Problem", "Desc", "Sol", "W1", "W2", "W3");
        dao.insert(problem);
        assertEquals(1, dao.count());
    }

    @Test
    public void deleteProblem() {
        Problems problem = new Problems(Problems.CATEGORY_PROGRAMMING, "Test Problem", "Desc", "Sol", "W1", "W2", "W3");
        dao.insert(problem);
        assertEquals(1, dao.count());
        // We need to get the auto-generated ID first
        dao.delete(dao.getRandomProblemByCategory(Problems.CATEGORY_PROGRAMMING));
        assertEquals(0, dao.count());
    }

    @Test
    public void deleteAllProblems() {
        Problems problem1 = new Problems(Problems.CATEGORY_PROGRAMMING, "Test Problem 1", "Desc", "Sol", "W1", "W2", "W3");
        Problems problem2 = new Problems(Problems.CATEGORY_CYBERSECURITY, "Test Problem 2", "Desc", "Sol", "W1", "W2", "W3");
        dao.insert(problem1);
        dao.insert(problem2);
        assertEquals(2, dao.count());
        dao.deleteAll();
        assertEquals(0, dao.count());
    }
}
