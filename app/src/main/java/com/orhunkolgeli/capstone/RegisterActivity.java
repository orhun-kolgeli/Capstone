package com.orhunkolgeli.capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhunkolgeli.capstone.utils.CustomGeoCoder;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    public static final String NAME = "name";
    public static final String EMAIL_ADDRESS = "emailAddress";
    public static final String ORGANIZATION = "organization";
    public static final String LOCATION = "location";
    private EditText etUsername;
    private EditText etName;
    private EditText etEmailAddress;
    private EditText etPassword;
    private CheckBox checkBoxOrg;
    private Button btnRegister;
    private TextView tvHaveAccount;
    private boolean isOrganization;
    private AutoCompleteTextView tvZipCode;
    public static final int MAX_RESULTS = 5;
    public static final int ZIP_LENGTH = 5;
    private CustomGeoCoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle(R.string.register);
        initializeViews();
        setOnClickListeners();
        setAddressSuggestions(tvZipCode);
        geocoder = new CustomGeoCoder(this);
    }

    private void setAddressSuggestions(AutoCompleteTextView tvZipCode) {
        List<String> stringList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, stringList);
        tvZipCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (tvZipCode.getText().toString().length() == ZIP_LENGTH) {
                    String zipCode = tvZipCode.getText().toString();
                    List<Address> addressList = geocoder.getAddressListFromText(zipCode, MAX_RESULTS);
                    arrayAdapter.clear();
                    for (int i = 0; i < addressList.size(); i++) {
                        arrayAdapter.add(addressList.get(i).getAddressLine(0));
                    }
                    tvZipCode.setAdapter(arrayAdapter);
                    arrayAdapter.getFilter().filter(null);
                    tvZipCode.showDropDown();
                }
                return false;
            }
        });
    }

    private void initializeViews() {
        tvZipCode = findViewById(R.id.tvZipCode);
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
        String addressText = tvZipCode.getText().toString();
        ParseGeoPoint userLocation = geocoder.getGeoPointFromText(addressText);
        if (userLocation == null) {
            Toast.makeText(this, R.string.provide_zip_code, Toast.LENGTH_SHORT).show();
            return;
        }
        if (username.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.fill_in_required_fields, Toast.LENGTH_SHORT).show();
            return;
        }
        ParseUser user = new ParseUser();
        // Set the User object's fields
        user.setUsername(username);
        user.setPassword(password);
        user.put(NAME, name);
        user.put(EMAIL_ADDRESS, email);
        user.put(ORGANIZATION, isOrganization);
        user.put(LOCATION, userLocation);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(RegisterActivity.this, R.string.signup_success,
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