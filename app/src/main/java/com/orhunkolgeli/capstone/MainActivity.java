package com.orhunkolgeli.capstone;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.orhunkolgeli.capstone.databinding.ActivityMainBinding;
import com.orhunkolgeli.capstone.interfaces.ProjectFilterListener;
import com.orhunkolgeli.capstone.models.Developer;
import com.orhunkolgeli.capstone.utils.FilterProjectsOnClick;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final String ORGANIZATION_ID = "organizationId";
    public ProjectFilterListener projectFilterListener = null;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private FilterProjectsOnClick filterProjectsOnClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        filterProjectsOnClick = new FilterProjectsOnClick(this);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Redirect user to the detail screen of the user that sent the push notification
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString(ORGANIZATION_ID) != null) {
            if (projectFilterListener != null) {
                projectFilterListener.onPushOpen(bundle.getString(ORGANIZATION_ID));
            }
        }
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
            filterProjectsOnClick
                    .getReferences(getLayoutInflater())
                    .populateDialog()
                    .showDialog(projectFilterListener);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation
                .findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}