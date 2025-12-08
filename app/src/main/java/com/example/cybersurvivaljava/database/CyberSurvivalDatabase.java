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

@Database(entities = {User.class, Problems.class, UserProblems.class}, version = 2, exportSchema = false)
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
                UserDAO userDAO = INSTANCE.userDAO();
                userDAO.deleteAll();
                User admin = new User("admin2", "admin2");
                admin.setAdmin(true);
                userDAO.insert(admin);

                User testUser1 = new User("testuser1", "testuser1");
                userDAO.insert(testUser1);

                ProblemsDAO problemsDAO = INSTANCE.problemsDAO();
                Problems networkingProblem = new Problems(
                        Problems.CATEGORY_NETWORKING,
                        "Signal Check",
                        "The backup generators have restored power, but the network status is critical. You need to verify if this terminal can reach the campus emergency server at 192.168.1.1. Which command tests connectivity?",
                        "ping 192.168.1.1",
                        "ipconfig /all",
                        "tracert -d",
                        "rm -rf /network"
                );
                problemsDAO.insert(networkingProblem);

                Problems programmingProblem = new Problems(
                        Problems.CATEGORY_PROGRAMMING,
                        "Turret Script",
                        "You need to patch the automated turret's targeting script. It needs to keep firing as long as zombies are detected. Which loop is best?",
                        "while(zombies > 0)",
                        "if(zombies > 0)",
                        "for(i=0; i<1; i++)",
                        "try / catch"
                );
                problemsDAO.insert(programmingProblem);

                Problems cybersecurityProblem = new Problems(
                        Problems.CATEGORY_CYBERSECURITY,
                        "Supply Crate Access",
                        "You found a locked supply crate with a keypad. The hint says 'Standard Admin Privileges'. Which command usually grants superuser access on Linux systems?",
                        "sudo",
                        "admin",
                        "unlock",
                        "root -access"
                );
                problemsDAO.insert(cybersecurityProblem);

                Problems circuitryProblem = new Problems(
                        Problems.CATEGORY_CIRCUITRY,
                        "Radio Repair",
                        "The radio transmitter is dead. You found a blown component labeled 'R1'. What component restricts the flow of current and likely needs replacing?",
                        "Resistor",
                        "Capacitor",
                        "LED",
                        "Inductor"
                );
                problemsDAO.insert(circuitryProblem);
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