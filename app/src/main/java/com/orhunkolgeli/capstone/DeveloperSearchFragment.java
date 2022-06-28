package com.orhunkolgeli.capstone;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orhunkolgeli.capstone.databinding.FragmentDeveloperSearchBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class DeveloperSearchFragment extends Fragment {

    private FragmentDeveloperSearchBinding binding;
    private static final String TAG = "ProjectSearchFragment";
    List<Developer> allDevelopers;
    DeveloperAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDeveloperSearchBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        // Get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rvDevelopers);
        // Set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Create an adapter
        allDevelopers = new ArrayList<>();
        adapter = new DeveloperAdapter(getActivity(), allDevelopers, DeveloperSearchFragment.this);
        // Set adapter
        recyclerView.setAdapter(adapter);
        // Set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // Read in the developer profiles from database
        loadDeveloperProfiles();

        return rootView;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fabAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(DeveloperSearchFragment.this).navigate(
                        R.id.action_DeveloperSearchFragment_to_postProjectFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadDeveloperProfiles() {
        ParseQuery<Developer> query = ParseQuery.getQuery("Developer");
        query.setLimit(20);
        query.addDescendingOrder("createdAt");
        // Search for ParseObject Developer
        // Query will invoke the FindCallback with either the object or the exception thrown
        query.findInBackground(new FindCallback<Developer>() {
            @Override
            public void done(List<Developer> developers, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error reading in the developer profiles from database");
                    return;
                }
                for (Developer developer : developers) {
                    Log.i(TAG, "Developer GitHub: " + developer.getGitHub());
                }
                allDevelopers.addAll(developers);
                adapter.notifyDataSetChanged();
                // Hide the progress bar
                binding.pbLoadDevelopers.setVisibility(View.GONE);
                binding.tvLoadingDevs.setVisibility(View.GONE);
            }
        });
    }
}