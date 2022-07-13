package com.orhunkolgeli.capstone;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.orhunkolgeli.capstone.databinding.ActivityMainBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public ProjectFilterListener projectFilterListener = null;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    public ProjectFilterValues projectFilterValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            ParseUser.logOutInBackground(e -> {
                if(e != null) {
                    Toast.makeText(MainActivity.this, R.string.error_signout, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                // Prevent user from using back button to go back to Main Activity after logout
                finish();
                Toast.makeText(MainActivity.this, R.string.logout_successful, Toast.LENGTH_SHORT).show();
            });
            return true;
        } else if (id == R.id.action_filter) {
            onClickActionFilter();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onClickActionFilter() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_filter, null);

        // Get references to filter dialog's view objects
        Spinner spinner = dialogView.findViewById(R.id.spinnerSortProjects);
        CheckBox checkBoxAndroid = dialogView.findViewById(R.id.checkBoxAndroid);
        CheckBox checkBoxiOS = dialogView.findViewById(R.id.checkBoxiOS);
        CheckBox checkBoxWeb = dialogView.findViewById(R.id.checkBoxWeb);
        EditText etDistance = dialogView.findViewById(R.id.etDistance);

        // Populate the dialog's fields with previous (or default) selection
        if (projectFilterValues == null) {
            projectFilterValues = new ProjectFilterValues();
        }
        spinner.setSelection(projectFilterValues.getSortBy());
        checkBoxAndroid.setChecked(projectFilterValues.isAndroidChecked());
        checkBoxiOS.setChecked(projectFilterValues.isiOSChecked());
        checkBoxWeb.setChecked(projectFilterValues.isWebChecked());
        etDistance.setText(String.valueOf(projectFilterValues.getDistance()));

        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.filter_projects);
        builder.setIcon(R.drawable.icon);
        builder.setView(dialogView);
        builder.setPositiveButton(R.string.filter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Communicate the new filter values to ProjectSearchFragment
                projectFilterValues = new ProjectFilterValues(); // just save this
                projectFilterValues.setSortBy(spinner.getSelectedItemPosition());
                projectFilterValues.setAndroidChecked(checkBoxAndroid.isChecked());
                projectFilterValues.setIsiOSChecked(checkBoxiOS.isChecked());
                projectFilterValues.setWebChecked(checkBoxWeb.isChecked());
                if (!etDistance.getText().toString().isEmpty()) {
                    projectFilterValues.setDistance(Integer.parseInt(etDistance.getText().toString()));
                }
                if (projectFilterListener != null) {
                    projectFilterListener.onActionFilterProjects(projectFilterValues);
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


}