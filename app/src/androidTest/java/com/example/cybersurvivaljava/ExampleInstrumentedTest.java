package com.example.cybersurvivaljava;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.cybersurvivaljava.database.CyberSurvivalDatabase;
import com.example.cybersurvivaljava.database.UserDAO;
import com.example.cybersurvivaljava.database.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private UserDAO userDao;
    private CyberSurvivalDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, CyberSurvivalDatabase.class)
                .allowMainThreadQueries()
                .build();
        userDao = db.userDAO();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void createUser() throws Exception {
        User user = new User("testuser", "password");
        userDao.insert(user);
        User byName = userDao.findUserByUsername_TEST("testuser");
        assertEquals(user.getUsername(), byName.getUsername());
    }

    @Test
    public void deleteUser() throws Exception {
        User user = new User("deleteMe", "password");
        userDao.insert(user);

        User insertedUser = userDao.findUserByUsername_TEST("deleteMe");
        assertNotNull(insertedUser);

        userDao.delete(insertedUser);

        User deletedUser = userDao.findUserByUsername_TEST("deleteMe");
        assertNull(deletedUser);
    }
}