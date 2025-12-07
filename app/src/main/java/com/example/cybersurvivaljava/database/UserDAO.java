package com.example.cybersurvivaljava.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.cybersurvivaljava.database.entities.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM " + CyberSurvivalDatabase.USER_TABLE_NAME)
    void deleteAll();

    @Query("SELECT * FROM " + CyberSurvivalDatabase.USER_TABLE_NAME + " WHERE username == :username")
    LiveData<User> getUserByUsername(String username);

    @Query("SELECT * FROM " + CyberSurvivalDatabase.USER_TABLE_NAME + " WHERE userId == :userId")
    LiveData<User> getUserById(int userId);

    // Method for testing purposes
    @Query("SELECT * FROM " + CyberSurvivalDatabase.USER_TABLE_NAME + " WHERE username == :username")
    User findUserByUsername_TEST(String username);
}
