package com.orhunkolgeli.capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etName;
    private EditText etEmailAddress;
    private EditText etPassword;
    private CheckBox checkBoxOrg;
    private Button btnRegister;
    private TextView tvHaveAccount;
    private boolean isOrganization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle(R.string.register);
        initializeViews();
        setOnClickListeners();
    }

    private void initializeViews() {
        etUsername = findViewById(R.id.etUsername);
        etName = findViewById(R.id.etName);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etPassword = findViewById(R.id.etPassword);
        checkBoxOrg = findViewById(R.id.checkBoxOrg);
        tvHaveAccount = findViewById(R.id.tvHaveAccount);
        isOrganization = false;
    }

    private void setOnClickListeners() {
        // Handle checkbox clicks
        checkBoxOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify the boolean that the box is checked or unchecked
                isOrganization = ((CheckBox) v).isChecked();
                if (v.getId() == R.id.checkBoxOrg) {
                    if (isOrganization) {
                        btnRegister.setText(R.string.register_org);
                    } else {
                        btnRegister.setText(R.string.register_dev);
                    }
                }
            }
        });

        // Handle register button click
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Handle "already have an account" click
        tvHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to login page
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser() {
        String username = etUsername.getText().toString();
        String name = etName.getText().toString();
        String email = etEmailAddress.getText().toString();
        String password = etPassword.getText().toString();
        if (username.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.fill_in_required_fields, Toast.LENGTH_SHORT).show();
            return;
        }
        ParseUser user = new ParseUser();
        // Set the User object's fields
        user.setUsername(username);
        user.setPassword(password);
        user.put("name", name);
        user.put("emailAddress", email);
        user.put("organization", isOrganization);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(RegisterActivity.this, "Sign up successful!",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    ParseUser.logOut();
                    Toast.makeText(RegisterActivity.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}