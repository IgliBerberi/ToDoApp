package com.example.todoapp;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "comment_table",
        foreignKeys = {
                @ForeignKey(entity = Task.class,
                        parentColumns = "id",
                        childColumns = "taskId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.SET_NULL)
        })
public class Comment {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int taskId;
    private Integer userId; // Can be null
    private String userFullName; // Store full name for display
    private String text;
    private long timestamp;

    public Comment(int taskId, Integer userId, String userFullName, String text) {
        this.taskId = taskId;
        this.userId = userId;
        this.userFullName = userFullName;
        this.text = text;
        this.timestamp = System.currentTimeMillis();
    }

    // Original getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    // New user-related getters and setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    // Existing getters and setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFormattedTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}