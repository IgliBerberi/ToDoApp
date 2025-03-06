package com.example.todoapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {
    @Insert
    long insert(User user);

    @Update
    void update(User user);

    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    @Query("SELECT * FROM user_table WHERE id = :userId LIMIT 1")
    User getUserById(int userId);

    @Query("SELECT COUNT(*) FROM user_table")
    int getUserCount();
}