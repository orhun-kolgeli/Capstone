package com.orhunkolgeli.capstone;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.orhunkolgeli.capstone.databinding.ActivityOrganizationBinding;
import com.orhunkolgeli.capstone.models.Developer;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class OrganizationActivity extends AppCompatActivity {

    public static final String OBJECT_ID = "objectId";
    public static final String DEVELOPER_ID = "developerId";
    public static final String DEVELOPER = "Developer";
    private AppBarConfiguration appBarConfiguration;
    private ActivityOrganizationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOrganizationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_organization);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Redirect user to the detail screen of the user that sent the push notification
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString(DEVELOPER_ID) != null) {
            // Find the developer with the given ID
            ParseQuery<Developer> parseQuery = new ParseQuery<Developer>(DEVELOPER);
            parseQuery.whereEqualTo(OBJECT_ID, bundle.getString(DEVELOPER_ID));
            parseQuery.findInBackground(new FindCallback<Developer>() {
                @Override
                public void done(List<Developer> developers, ParseException e) {
                    // Navigate to the applicant's detail screen, starting from the search screen
                    if (!developers.isEmpty()) {
                        navController.navigate(DeveloperSearchFragmentDirections
                                .actionDeveloperSearchFragmentToDeveloperDetailFragment(
                                        developers.get(0),
                                        DeveloperDetailFragment.DeveloperCategory.APPLICANT_PUSH, 0
                                )
                        );
                    }
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_organization);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_organization, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            ParseUser.logOutInBackground(e -> {
                if(e != null) {
                    Toast.makeText(OrganizationActivity.this, R.string.error_signout, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(OrganizationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(OrganizationActivity.this, R.string.logout_successful, Toast.LENGTH_SHORT).show();
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}