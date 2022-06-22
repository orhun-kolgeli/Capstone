package com.orhunkolgeli.capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActvity";
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ParseUser currentUser = ParseUser.getCurrentUser();
        // Check if user is already logged in
        if (currentUser != null) {
            boolean isOrganization = currentUser.getBoolean("organization");
            if (isOrganization) {
                //launchOrganizationView();
            } else {
                launchDeveloperView();
            }
        }
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                logUserIn(username, password);
            }
        });
    }

    private void logUserIn(String username, String password) {
        Log.i(TAG, "Attempting to log in: " + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) { // exception thrown
                    Log.e(TAG, "Login error", e);
                    if (username.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Please enter your username",
                                Toast.LENGTH_SHORT).show();
                    } else if (password.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Please enter your password",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid username or password",
                                Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                launchDeveloperView();
                Toast.makeText(LoginActivity.this, "Welcome " + username + "!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void launchDeveloperView() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        // Prevent user from going back to LoginActivity with back button
        finish();
    }
}