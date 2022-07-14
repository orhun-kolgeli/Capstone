package com.orhunkolgeli.capstone;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orhunkolgeli.capstone.databinding.FragmentProjectSearchBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProjectSearchFragment extends Fragment implements ProjectFilterListener {

    private static final String TAG = "ProjectSearchFragment";
    public static final int LIMIT = 10;
    public static final int START_PAGE = 0;
    public static final String TYPE = "type";
    public static final String PROJECT = "Project";
    public static final String LOCATION = "location";
    private FragmentProjectSearchBinding binding;
    private EndlessRecyclerViewScrollListener scrollListener;
    List<Project> allProjects;
    ProjectAdapter adapter;
    private ProjectFilterValues projectFilterValues;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProjectSearchBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        // Set the projectFilterListener, which is held by MainActivity
        ((MainActivity) requireActivity()).projectFilterListener = this;
        // Get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rvDevelopers);
        // Set layoutManger
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadProjects(page);
            }
        };
        // Add the scroll listener to the recyclerView
        recyclerView.addOnScrollListener(scrollListener);
        // Create an adapter
        allProjects = new ArrayList<>();
        adapter = new ProjectAdapter(getActivity(), allProjects, ProjectSearchFragment.this);
        // Set adapter
        recyclerView.setAdapter(adapter);
        // Set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // Initialize project filter values
        if (projectFilterValues == null) {
            projectFilterValues = new ProjectFilterValues();
        }
        setHasOptionsMenu(true);
        // Read in the projects from database
        loadProjects(START_PAGE);
        return rootView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ProjectSearchFragment.this)
                        .navigate(R.id.action_ProjectSearchFragment_to_setupProfileFragment);
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_filter);
        if (menuItem != null)
            menuItem.setVisible(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void loadProjects(int page) {
        // Clear the existing items when performing a new search
        if (page == START_PAGE && adapter.getItemCount() > 0) {
            adapter.clear();
            // Reset endless scroll listener
            scrollListener.resetState();
            // Show the progress bar
            binding.pbReadProjects.setVisibility(View.VISIBLE);
            binding.tvLoadingProjects.setVisibility(View.VISIBLE);
        }
        ParseQuery<Project> query = ParseQuery.getQuery(PROJECT);
        // Skip the projects that have already been shown
        query.setSkip(page*LIMIT);
        // Limit query to as many items as LIMIT
        query.setLimit(LIMIT);
        // Filter the query result by project type
        query.whereContainedIn(TYPE, projectFilterValues.selectedProjectTypes());
        // Filter the query result by distance
        query.whereWithinMiles(LOCATION, ParseUser.getCurrentUser().getParseGeoPoint(LOCATION), projectFilterValues.getDistance());
        // Sort the query results
        projectFilterValues.addSortingToQuery(query);
        // Start an asynchronous call to retrieve projects from database
        query.findInBackground(new FindCallback<Project>() {
            @Override
            public void done(List<Project> projects, ParseException e) {
                if (e != null) {
                    Toast.makeText(getContext(), R.string.error_loading_projects, Toast.LENGTH_SHORT).show();
                    return;
                }
                allProjects.addAll(projects);
                adapter.notifyItemRangeInserted(page*LIMIT, LIMIT);
                // Hide the progress bar
                binding.pbReadProjects.setVisibility(View.GONE);
                binding.tvLoadingProjects.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onActionFilterProjects(ProjectFilterValues projectFilterValues) {
        this.projectFilterValues = projectFilterValues;
        loadProjects(START_PAGE);
    }
}