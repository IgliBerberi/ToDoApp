package com.example.todoapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CommentDao {
    @Insert
    void insert(Comment comment);

    @Update
    void update(Comment comment);

    @Delete
    void delete(Comment comment);

    @Query("SELECT * FROM comment_table WHERE taskId = :taskId ORDER BY timestamp DESC")
    LiveData<List<Comment>> getCommentsForTask(int taskId);

    @Query("DELETE FROM comment_table WHERE taskId = :taskId")
    void deleteAllCommentsForTask(int taskId);
}