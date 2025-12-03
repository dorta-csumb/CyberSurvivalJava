package com.example.cybersurvivaljava.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.cybersurvivaljava.database.entities.Problems;
import com.example.cybersurvivaljava.database.entities.User;
import com.example.cybersurvivaljava.database.entities.UserProblems;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Problems.class, UserProblems.class}, version = 1, exportSchema = false)
public abstract class CyberSurvivalDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "CyberSurvivalDatabase";
    public static final String USER_TABLE_NAME = "usertable";
    public static final String PROBLEMS_TABLE_NAME = "problems";
    public static final String USER_PROBLEMS_TABLE_NAME = "userproblems";

    private static volatile CyberSurvivalDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static CyberSurvivalDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CyberSurvivalDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            CyberSurvivalDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.i("CyberSurvivalDatabase", "DB CREATED - Seeding initial users.");
            databaseWriteExecutor.execute(() -> {
                UserDAO dao = INSTANCE.userDAO();
                dao.deleteAll();
                User admin = new User("admin2", "admin2");
                admin.setAdmin(true);
                dao.insert(admin);

                User testUser1 = new User("testuser1", "testuser1");
                dao.insert(testUser1);
            });
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            Log.i("CyberSurvivalDatabase", "DB OPENED - Checking for problems data.");
            databaseWriteExecutor.execute(() -> {
                ProblemsDAO dao = INSTANCE.problemsDAO();
                if (dao.count() == 0) {
                    Log.i("CyberSurvivalDatabase", "Problems table is empty, seeding data.");
                    Problems problem1 = new Problems(2, "Phishing", "What is phishing?", "A fraudulent attempt to obtain sensitive information", "A type of fishing", "A computer virus", "A social media platform");
                    dao.insert(problem1);
                }
            });
        }
    };

    public abstract UserDAO userDAO();
    public abstract ProblemsDAO problemsDAO();
    public abstract UserProblemsDAO userProblemsDAO();
}