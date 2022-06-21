package com.orhunkolgeli.capstone;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private FragmentProjectSearchBinding binding;
    List<Project> allProjects;
    ProjectAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProjectSearchBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        // Get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rvProjects);
        // Set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Create an adapter
        allProjects = new ArrayList<>();
        adapter = new ProjectAdapter(getActivity(), allProjects);
        // Set adapter
        recyclerView.setAdapter(adapter);
        // Set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // Read in the projects from database
        readProjects();

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ProjectSearchFragment.this)
                        .navigate(R.id.action_ProjectSearchFragment_to_ProjectDetailFragment);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void readProjects() {
        ParseQuery<Project> query = ParseQuery.getQuery("Project");
        query.setLimit(20);
        query.addDescendingOrder("createdAt");
        // Search for ParseObject Project
        // Query will invoke the FindCallback with either the object or the exception thrown
        query.findInBackground(new FindCallback<Project>() {
            @Override
            public void done(List<Project> projects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error reading projects from database");
                    return;
                }
                for (Project project : projects) {
                    Log.i(TAG, "Project : " + project.getType() + ", " + project.getDescription());
                }
                allProjects.addAll(projects);
                adapter.notifyDataSetChanged();
            }
        });
    }
}