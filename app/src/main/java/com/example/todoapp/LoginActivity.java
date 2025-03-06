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

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private TaskDatabase database;
    private UserDao userDao;
    private SessionManager sessionManager;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            // Initialize database
            database = TaskDatabase.getInstance(this);
            userDao = database.userDao();

            // Initialize session manager
            sessionManager = new SessionManager(this);

            // Check if user is already logged in
            if (sessionManager.isLoggedIn()) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                return;
            }

            // Initialize views
            emailEditText = findViewById(R.id.emailEditText);
            passwordEditText = findViewById(R.id.passwordEditText);
            loginButton = findViewById(R.id.loginButton);
            registerTextView = findViewById(R.id.registerTextView);

            // Login button click listener
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });

            // Register text click listener
            registerTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            Log.e("LoginActivity", "Error in onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error initializing: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void login() {
        try {
            final String email = emailEditText.getText().toString().trim();
            final String password = passwordEditText.getText().toString().trim();

            // Validate input
            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("Email is required");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                passwordEditText.setError("Password is required");
                return;
            }

            // Show processing message
            Toast.makeText(this, "Processing login...", Toast.LENGTH_SHORT).show();
            Log.d("LoginActivity", "Starting login process");

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        final User user = userDao.getUserByEmail(email);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (user != null && user.getPassword().equals(password)) {
                                    Log.d("LoginActivity", "Login successful for: " + email);

                                    // Create session
                                    sessionManager.createLoginSession(user.getId(), user.getEmail(), user.getFullName());

                                    // Start main activity
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (final Exception e) {
                        Log.e("LoginActivity", "Error during login: " + e.getMessage(), e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            Log.e("LoginActivity", "Error in login method: " + e.getMessage(), e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}