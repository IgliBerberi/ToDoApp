package com.example.todoapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProfileActivity extends AppCompatActivity {

    private TextInputEditText fullNameEditText, emailEditText, currentPasswordEditText, newPasswordEditText;
    private TextInputLayout currentPasswordLayout, newPasswordLayout;
    private Button saveChangesButton, changePasswordButton;
    private TaskDatabase database;
    private UserDao userDao;
    private SessionManager sessionManager;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        try {
            // Set up toolbar
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("My Profile");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            // Initialize database
            database = TaskDatabase.getInstance(this);
            userDao = database.userDao();

            // Initialize session manager
            sessionManager = new SessionManager(this);

            // Check if user is logged in
            if (!sessionManager.isLoggedIn()) {
                finish();
                return;
            }

            // Initialize views
            fullNameEditText = findViewById(R.id.fullNameEditText);
            emailEditText = findViewById(R.id.emailEditText);
            currentPasswordEditText = findViewById(R.id.currentPasswordEditText);
            newPasswordEditText = findViewById(R.id.newPasswordEditText);
            currentPasswordLayout = findViewById(R.id.currentPasswordLayout);
            newPasswordLayout = findViewById(R.id.newPasswordLayout);
            saveChangesButton = findViewById(R.id.saveChangesButton);
            changePasswordButton = findViewById(R.id.changePasswordButton);

            // Load user data
            loadUserData();

            // Set up button click listeners
            saveChangesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveProfileChanges();
                }
            });

            changePasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentPasswordLayout.getVisibility() == View.VISIBLE) {
                        // Password fields are visible, so try to change password
                        changePassword();
                    } else {
                        // Show password fields
                        currentPasswordLayout.setVisibility(View.VISIBLE);
                        newPasswordLayout.setVisibility(View.VISIBLE);
                        changePasswordButton.setText("Save New Password");
                    }
                }
            });

        } catch (Exception e) {
            Log.e("ProfileActivity", "Error in onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error initializing: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadUserData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final User user = userDao.getUserById(sessionManager.getUserId());
                    if (user != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fullNameEditText.setText(user.getFullName());
                                emailEditText.setText(user.getEmail());
                            }
                        });
                    }
                } catch (final Exception e) {
                    Log.e("ProfileActivity", "Error loading user data: " + e.getMessage(), e);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ProfileActivity.this, "Error loading profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void saveProfileChanges() {
        final String fullName = fullNameEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(fullName)) {
            fullNameEditText.setError("Full name is required");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final User existingUser = userDao.getUserByEmail(email);
                    final int userId = sessionManager.getUserId();

                    // Make sure we don't create a duplicate email (unless it's the user's current email)
                    if (existingUser != null && existingUser.getId() != userId) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                emailEditText.setError("Email already in use by another account");
                            }
                        });
                        return;
                    }

                    final User user = userDao.getUserById(userId);
                    if (user != null) {
                        user.setFullName(fullName);
                        user.setEmail(email);
                        userDao.update(user);

                        // Update session data
                        sessionManager.updateUserDetails(email, fullName);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (final Exception e) {
                    Log.e("ProfileActivity", "Error saving profile: " + e.getMessage(), e);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ProfileActivity.this, "Error updating profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void changePassword() {
        final String currentPassword = currentPasswordEditText.getText().toString().trim();
        final String newPassword = newPasswordEditText.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(currentPassword)) {
            currentPasswordEditText.setError("Current password is required");
            return;
        }

        if (TextUtils.isEmpty(newPassword)) {
            newPasswordEditText.setError("New password is required");
            return;
        }

        if (newPassword.length() < 6) {
            newPasswordEditText.setError("Password must be at least 6 characters");
            return;
        }

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final User user = userDao.getUserById(sessionManager.getUserId());

                    if (user != null) {
                        if (!user.getPassword().equals(currentPassword)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    currentPasswordEditText.setError("Current password is incorrect");
                                }
                            });
                            return;
                        }

                        user.setPassword(newPassword);
                        userDao.update(user);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ProfileActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                // Hide password fields and reset button text
                                currentPasswordLayout.setVisibility(View.GONE);
                                newPasswordLayout.setVisibility(View.GONE);
                                changePasswordButton.setText("Change Password");
                                // Clear password fields
                                currentPasswordEditText.setText("");
                                newPasswordEditText.setText("");
                            }
                        });
                    }
                } catch (final Exception e) {
                    Log.e("ProfileActivity", "Error changing password: " + e.getMessage(), e);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ProfileActivity.this, "Error changing password: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}