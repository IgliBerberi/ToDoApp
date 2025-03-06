package com.example.todoapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM task_table WHERE isCompleted = 1")
    void deleteCompletedTasks();

    @Query("SELECT * FROM task_table ORDER BY priority DESC")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM task_table ORDER BY title ASC")
    LiveData<List<Task>> getAllTasksAlphabetically();
}