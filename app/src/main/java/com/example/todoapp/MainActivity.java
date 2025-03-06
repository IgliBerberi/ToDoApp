package com.example.todoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskActionListener {

    private TaskViewModel taskViewModel;
    private TaskAdapter taskAdapter;
    private SessionManager sessionManager;
    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.d("MainActivity", "onCreate started");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // Initialize session manager
            sessionManager = new SessionManager(this);

            // Check if user is logged in
            if (!sessionManager.isLoggedIn()) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return;
            }

            // Set up toolbar - correctly handle the action bar
            Toolbar toolbar = findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("My Todo List");
                }
            } else {
                Log.e("MainActivity", "Toolbar not found in layout");
            }

            // Debug logging for session info
            String userName = sessionManager.getUserFullName();
            Log.d("MainActivity", "User full name from session: " + (userName != null ? userName : "null"));
            Log.d("MainActivity", "User ID from session: " + sessionManager.getUserId());

            // Welcome message with user's name
            welcomeTextView = findViewById(R.id.welcomeTextView);
            if (welcomeTextView != null) {
                String welcomeText = "Welcome back, " + sessionManager.getUserFullName();
                welcomeTextView.setText(welcomeText);
                welcomeTextView.setVisibility(View.VISIBLE);
                Log.d("MainActivity", "Welcome message set: " + welcomeText);
            } else {
                Log.e("MainActivity", "welcomeTextView not found in layout");
            }

            // Initialize RecyclerView
            RecyclerView tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
            if (tasksRecyclerView != null) {
                tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

                // Initialize adapter with empty list
                taskAdapter = new TaskAdapter(this, new ArrayList<>());
                tasksRecyclerView.setAdapter(taskAdapter);

                // Initialize ViewModel
                taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

                // Observe the tasks from database
                taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
                    @Override
                    public void onChanged(List<Task> tasks) {
                        // Update RecyclerView when data changes
                        taskAdapter.setTasks(tasks);
                        Log.d("MainActivity", "Task list updated, count: " + tasks.size());
                    }
                });

                Log.d("MainActivity", "RecyclerView and ViewModel setup completed");
            } else {
                Log.e("MainActivity", "tasksRecyclerView not found in layout");
            }

            // Set up Floating Action Button for adding tasks
            FloatingActionButton fab = findViewById(R.id.fab);
            if (fab != null) {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showAddTaskDialog();
                    }
                });
            } else {
                Log.e("MainActivity", "fab not found in layout");
            }

            Log.d("MainActivity", "onCreate completed successfully");
        } catch (Exception e) {
            Log.e("MainActivity", "Error in onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error starting app: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Add logout option
        menu.add(Menu.NONE, 101, Menu.NONE, "Logout");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_priority) {
            // Now using database for sorting
            taskViewModel.getAllTasks().observe(this, tasks -> taskAdapter.setTasks(tasks));
            return true;
        } else if (id == R.id.action_sort_alphabetical) {
            // Now using database for sorting
            taskViewModel.getAllTasksAlphabetically().observe(this, tasks -> taskAdapter.setTasks(tasks));
            return true;
        } else if (id == R.id.action_clear_completed) {
            // Now using database for deleting
            taskViewModel.deleteCompletedTasks();
            return true;
        } else if (id == 101) { // Logout
            sessionManager.logoutUser();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAddTaskDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_add_task, null);
            builder.setView(dialogView);

            final EditText taskTitleEditText = dialogView.findViewById(R.id.taskTitleEditText);
            final EditText taskDescriptionEditText = dialogView.findViewById(R.id.taskDescriptionEditText);
            final RadioGroup priorityRadioGroup = dialogView.findViewById(R.id.priorityRadioGroup);

            builder.setTitle("Add New Task");
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        String title = taskTitleEditText.getText().toString().trim();
                        String description = taskDescriptionEditText.getText().toString().trim();

                        int priority = 2; // Default is medium
                        int selectedRadioButtonId = priorityRadioGroup.getCheckedRadioButtonId();

                        if (selectedRadioButtonId == R.id.radioPriorityLow) {
                            priority = 1;
                        } else if (selectedRadioButtonId == R.id.radioPriorityHigh) {
                            priority = 3;
                        }

                        if (!title.isEmpty()) {
                            // Create new task and insert into database
                            Task newTask = new Task(title, description, priority);
                            taskViewModel.insert(newTask);
                            Toast.makeText(MainActivity.this, "Task added to database!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Task title cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("MainActivity", "Error adding task: " + e.getMessage(), e);
                        Toast.makeText(MainActivity.this, "Error adding task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("Cancel", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        } catch (Exception e) {
            Log.e("MainActivity", "Error showing add task dialog: " + e.getMessage(), e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Implement OnTaskActionListener
    @Override
    public void onDeleteTask(Task task) {
        try {
            // Delete from database
            taskViewModel.delete(task);
            Toast.makeText(this, "Task deleted from database", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("MainActivity", "Error deleting task: " + e.getMessage(), e);
            Toast.makeText(this, "Error deleting task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Implement the new method for updating tasks
    @Override
    public void updateTask(Task task) {
        try {
            taskViewModel.update(task);
            Log.d("MainActivity", "Task updated: " + task.getId() + ", Completed: " + task.isCompleted());
        } catch (Exception e) {
            Log.e("MainActivity", "Error updating task: " + e.getMessage(), e);
            Toast.makeText(this, "Error updating task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}