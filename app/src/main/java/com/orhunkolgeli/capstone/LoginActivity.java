package com.orhunkolgeli.capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActvity";
    public static final String KEY_ORGANIZATION = "organization";
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvDontHaveAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle(R.string.log_in_toolbar);
        ParseUser currentUser = ParseUser.getCurrentUser();
        // Check if user is already logged in
        if (currentUser != null) {
            boolean isOrganization = currentUser.getBoolean(KEY_ORGANIZATION);
            if (isOrganization) {
                launchOrganizationView();
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
        tvDontHaveAccount = findViewById(R.id.tvDontHaveAccount);
        tvDontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
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
                        Toast.makeText(LoginActivity.this, R.string.enter_username,
                                Toast.LENGTH_SHORT).show();
                    } else if (password.isEmpty()) {
                        Toast.makeText(LoginActivity.this, R.string.enter_password,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.invalid_username_password,
                                Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                Toast.makeText(LoginActivity.this, getString(R.string.welcome) + username + "!",
                        Toast.LENGTH_SHORT).show();
                boolean isOrganization = user.getBoolean(KEY_ORGANIZATION);
                if (isOrganization) {
                    launchOrganizationView();
                } else {
                    launchDeveloperView();
                }
            }
        });
    }

    private void launchDeveloperView() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        // Prevent user from going back to LoginActivity with back button
        finish();
    }

    private void launchOrganizationView() {
        Intent intent = new Intent(this, OrganizationActivity.class);
        startActivity(intent);
        // Prevent user from going back to LoginActivity with back button
        finish();
    }
}