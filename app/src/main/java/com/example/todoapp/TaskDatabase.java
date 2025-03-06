package com.example.todoapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Task.class, Comment.class, User.class}, version = 4)
public abstract class TaskDatabase extends RoomDatabase {

    private static TaskDatabase instance;
    public abstract TaskDao taskDao();
    public abstract CommentDao commentDao();
    public abstract UserDao userDao();

    // Define migrations
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Create the user table if it doesn't exist already
            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS user_table (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "email TEXT, " +
                            "password TEXT, " +
                            "fullName TEXT)"
            );

            // Update comment table to include user information if needed
            database.execSQL("ALTER TABLE comment_table ADD COLUMN IF NOT EXISTS userId INTEGER");
            database.execSQL("ALTER TABLE comment_table ADD COLUMN IF NOT EXISTS userFullName TEXT");
        }
    };

    public static synchronized TaskDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            TaskDatabase.class, "task_database")
                    .addMigrations(MIGRATION_3_4)
                    .fallbackToDestructiveMigration() // As a last resort
                    .build();
        }
        return instance;
    }
}