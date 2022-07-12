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
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ProjectSearchFragment extends Fragment {

    private static final String TAG = "ProjectSearchFragment";
    public static final int LIMIT = 10;
    public static final String TYPE = "type";
    public static final String PROJECT = "Project";
    private FragmentProjectSearchBinding binding;
    List<Project> allProjects;
    ProjectAdapter adapter;
    protected static ProjectFilterListener projectFilterListener;
    protected static ProjectFilterValues projectFilterValues;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProjectSearchBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        // Get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rvDevelopers);
        // Set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        loadProjects();

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
        projectFilterListener = new ProjectFilterListener() {
            @Override
            public void onActionFilterProjects(ProjectFilterValues newFilterValues) {
                projectFilterValues = newFilterValues;
                loadProjects();
            }
        };
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


    public void loadProjects() {
        // Clear results from the previous filter, if any
        if (adapter.getItemCount() != 0) {
            adapter.clear();
            // Show the progress bar
            binding.pbReadProjects.setVisibility(View.VISIBLE);
            binding.tvLoadingProjects.setVisibility(View.VISIBLE);
        }
        ParseQuery<Project> query = ParseQuery.getQuery(PROJECT);
        query.setLimit(LIMIT);
        projectFilterValues.addSortingToQuery(query);
        query.whereContainedIn(TYPE, projectFilterValues.selectedProjectTypes());
        query.findInBackground(new FindCallback<Project>() {
            @Override
            public void done(List<Project> projects, ParseException e) {
                if (e != null) {
                    return;
                }
                allProjects.addAll(projects);
                adapter.notifyItemRangeInserted(0, LIMIT);
                // Hide the progress bar
                binding.pbReadProjects.setVisibility(View.GONE);
                binding.tvLoadingProjects.setVisibility(View.GONE);
            }
        });
    }
}