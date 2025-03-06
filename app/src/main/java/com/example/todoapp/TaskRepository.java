package com.example.todoapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {
    private TaskDao taskDao;
    private CommentDao commentDao;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Task>> allTasksAlphabetically;

    public TaskRepository(Application application) {
        TaskDatabase database = TaskDatabase.getInstance(application);
        taskDao = database.taskDao();
        commentDao = database.commentDao(); // Add this line
        allTasks = taskDao.getAllTasks();
        allTasksAlphabetically = taskDao.getAllTasksAlphabetically();
    }

    // Task operations
    public void insert(Task task) {
        new InsertTaskAsyncTask(taskDao).execute(task);
    }

    public void update(Task task) {
        new UpdateTaskAsyncTask(taskDao).execute(task);
    }

    public void delete(Task task) {
        new DeleteTaskAsyncTask(taskDao).execute(task);
    }

    public void deleteCompletedTasks() {
        new DeleteCompletedTasksAsyncTask(taskDao).execute();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<List<Task>> getAllTasksAlphabetically() {
        return allTasksAlphabetically;
    }

    // Comment operations
    public void insertComment(Comment comment) {
        new InsertCommentAsyncTask(commentDao).execute(comment);
    }

    public LiveData<List<Comment>> getCommentsForTask(int taskId) {
        return commentDao.getCommentsForTask(taskId);
    }

    // AsyncTask classes for tasks
    private static class InsertTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private InsertTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insert(tasks[0]);
            return null;
        }
    }

    private static class UpdateTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private UpdateTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.update(tasks[0]);
            return null;
        }
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private DeleteTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.delete(tasks[0]);
            return null;
        }
    }

    private static class DeleteCompletedTasksAsyncTask extends AsyncTask<Void, Void, Void> {
        private TaskDao taskDao;

        private DeleteCompletedTasksAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.deleteCompletedTasks();
            return null;
        }
    }

    // AsyncTask class for comments
    private static class InsertCommentAsyncTask extends AsyncTask<Comment, Void, Void> {
        private CommentDao commentDao;

        private InsertCommentAsyncTask(CommentDao commentDao) {
            this.commentDao = commentDao;
        }

        @Override
        protected Void doInBackground(Comment... comments) {
            commentDao.insert(comments[0]);
            return null;
        }
    }
}