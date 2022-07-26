package com.orhunkolgeli.capstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.orhunkolgeli.capstone.databinding.ActivityLoginBinding;
import com.orhunkolgeli.capstone.models.User;
import com.orhunkolgeli.capstone.utils.CustomParsePushBroadcastReceiver;
import com.orhunkolgeli.capstone.viewmodel.LoginViewModel;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    public static final String DEVELOPER_ID = "developerId";
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.log_in_toolbar);

        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(loginViewModel);

        loginViewModel.getMessage().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer message) {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        loginViewModel.getUserType().observe(this, new Observer<LoginViewModel.UserType>() {
            @Override
            public void onChanged(LoginViewModel.UserType userType) {
                Intent intent = null;
                Bundle bundle = getIntent().getExtras();
                switch (userType) {
                    case DEVELOPER:
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        // Send any push data to MainActivity
                        if (bundle != null && bundle.getString("organizationId") != null) {
                            intent.putExtra("organizationId", bundle.getString("organizationId"));
                        }
                        break;
                    case ORGANIZATION:
                        intent = new Intent(LoginActivity.this, OrganizationActivity.class);
                        // Send any push data to OrganizationActivity
                        if (bundle != null && bundle.getString(DEVELOPER_ID) != null) {
                            intent.putExtra(DEVELOPER_ID, bundle.getString(DEVELOPER_ID));
                        }
                        break;
                }
                if (intent != null) {
                    startActivity(intent);
                    finish();
                }
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.updateUser(binding.etUsername.getText().toString(),
                                          binding.etPassword.getText().toString());
                loginViewModel.login();
            }
        });

        binding.tvDontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}