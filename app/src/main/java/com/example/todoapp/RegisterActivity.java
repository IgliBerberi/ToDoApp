package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText fullNameEditText, emailEditText, passwordEditText;
    private Button registerButton;
    private TextView loginTextView;
    private TaskDatabase database;
    private UserDao userDao;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        try {
            // Initialize database
            database = TaskDatabase.getInstance(this);
            userDao = database.userDao();

            // Initialize views
            fullNameEditText = findViewById(R.id.fullNameEditText);
            emailEditText = findViewById(R.id.emailEditText);
            passwordEditText = findViewById(R.id.passwordEditText);
            registerButton = findViewById(R.id.registerButton);
            loginTextView = findViewById(R.id.loginTextView);

            // Register button click listener
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    register();
                }
            });

            // Login text click listener
            loginTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                }
            });
        } catch (Exception e) {
            Log.e("RegisterActivity", "Error in onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error initializing: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void register() {
        try {
            final String fullName = fullNameEditText.getText().toString().trim();
            final String email = emailEditText.getText().toString().trim();
            final String password = passwordEditText.getText().toString().trim();

            // Validate input
            if (TextUtils.isEmpty(fullName)) {
                fullNameEditText.setError("Full name is required");
                return;
            }

            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("Email is required");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                passwordEditText.setError("Password is required");
                return;
            }

            if (password.length() < 6) {
                passwordEditText.setError("Password must be at least 6 characters");
                return;
            }

            // Show processing message
            Toast.makeText(RegisterActivity.this, "Processing registration...", Toast.LENGTH_SHORT).show();

            // Create user in background using Executor instead of AsyncTask
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Check if email already exists
                        final User existingUser = userDao.getUserByEmail(email);

                        if (existingUser != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, "Email already registered", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }

                        // Create new user
                        User newUser = new User(email, password, fullName);
                        final long userId = userDao.insert(newUser);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (userId > 0) {
                                    Toast.makeText(RegisterActivity.this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();

                                    // Explicitly navigate to Login screen
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (final Exception e) {
                        Log.e("RegisterActivity", "Error registering user: " + e.getMessage(), e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            Log.e("RegisterActivity", "Error in register method: " + e.getMessage(), e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}