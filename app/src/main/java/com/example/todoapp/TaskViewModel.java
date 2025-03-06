package com.example.todoapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository repository;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> allTasksAlphabetically;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
        allTasksAlphabetically = repository.getAllTasksAlphabetically();
    }

    // Task methods
    public void insert(Task task) {
        repository.insert(task);
    }

    public void update(Task task) {
        repository.update(task);
    }

    public void delete(Task task) {
        repository.delete(task);
    }

    public void deleteCompletedTasks() {
        repository.deleteCompletedTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<List<Task>> getAllTasksAlphabetically() {
        return allTasksAlphabetically;
    }

    // Comment methods
    public void insertComment(Comment comment) {
        repository.insertComment(comment);
    }

    public LiveData<List<Comment>> getCommentsForTask(int taskId) {
        return repository.getCommentsForTask(taskId);
    }
}