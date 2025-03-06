package com.example.todoapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskDetailActivity extends AppCompatActivity {

    private TaskViewModel taskViewModel;
    private Task currentTask;
    private Button markCompleteButton;
    private int taskId;
    private EditText newCommentEditText;
    private CommentAdapter commentAdapter;
    private TaskDatabase taskDatabase;
    private CommentDao commentDao;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // Initialize database and DAOs
        taskDatabase = TaskDatabase.getInstance(this);
        commentDao = taskDatabase.commentDao();

        // Initialize session manager
        sessionManager = new SessionManager(this);

        // Initialize ViewModel
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Set up the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Task Details");
        }

        // Get views from layout
        TextView titleTextView = findViewById(R.id.detailTitleTextView);
        TextView descriptionTextView = findViewById(R.id.detailDescriptionTextView);
        markCompleteButton = findViewById(R.id.markCompleteButton);
        Button backButton = findViewById(R.id.backButton);

        RecyclerView commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        newCommentEditText = findViewById(R.id.newCommentEditText);
        Button addCommentButton = findViewById(R.id.addCommentButton);

        // Set up comments RecyclerView
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter();
        commentsRecyclerView.setAdapter(commentAdapter);

        // Get data from intent
        taskId = getIntent().getIntExtra("taskId", -1);
        String title = getIntent().getStringExtra("taskTitle");
        String description = getIntent().getStringExtra("taskDescription");
        boolean isCompleted = getIntent().getBooleanExtra("taskCompleted", false);
        int priority = getIntent().getIntExtra("taskPriority", 2);

        // Create task object
        currentTask = new Task(title, description, priority);
        currentTask.setId(taskId);
        currentTask.setCompleted(isCompleted);

        // Set data to views
        titleTextView.setText(title);
        descriptionTextView.setText(description);

        // Update button text based on completion status
        updateCompleteButtonText(isCompleted);

        // Load comments for this task
        if (taskId != -1) {
            taskDatabase.commentDao().getCommentsForTask(taskId).observe(this, new Observer<List<Comment>>() {
                @Override
                public void onChanged(List<Comment> comments) {
                    commentAdapter.setComments(comments);
                }
            });
        }

        // Mark complete button
        markCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean newStatus = !currentTask.isCompleted();
                currentTask.setCompleted(newStatus);
                updateCompleteButtonText(newStatus);

                // Save the updated status to database
                if (taskId != -1) {
                    taskViewModel.update(currentTask);
                }

                Toast.makeText(TaskDetailActivity.this,
                        newStatus ? "Task marked as complete" : "Task marked as incomplete",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Add comment button
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });

        // Back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateCompleteButtonText(boolean isCompleted) {
        if (isCompleted) {
            markCompleteButton.setText("Mark as Incomplete");
        } else {
            markCompleteButton.setText("Mark as Complete");
        }
    }

    private void addComment() {
        String commentText = newCommentEditText.getText().toString().trim();

        if (commentText.isEmpty()) {
            Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (taskId != -1) {
            // Create comment with user information
            Comment newComment = new Comment(
                    taskId,
                    sessionManager.getUserId(),
                    sessionManager.getUserFullName(),
                    commentText
            );

            // Insert comment in background thread
            new InsertCommentAsyncTask(commentDao).execute(newComment);

            // Clear the input field
            newCommentEditText.setText("");
            Toast.makeText(this, "Comment added", Toast.LENGTH_SHORT).show();
        }
    }

    // AsyncTask for inserting comments
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

    // Handle the up/back button in the action bar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}