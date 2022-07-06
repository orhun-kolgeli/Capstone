package com.orhunkolgeli.capstone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orhunkolgeli.capstone.databinding.FragmentDeveloperSearchBinding;
import com.orhunkolgeli.capstone.databinding.FragmentFindDeveloperBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class FindDeveloperFragment extends Fragment {

    private FragmentFindDeveloperBinding binding;
    List<Developer> allDevelopers;
    DeveloperAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFindDeveloperBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        // Get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rvDevelopers);
        // Set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Create an adapter
        allDevelopers = new ArrayList<>();
        adapter = new DeveloperAdapter(getActivity(), allDevelopers, FindDeveloperFragment.this);
        // Set adapter
        recyclerView.setAdapter(adapter);
        // Set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // Read in the developer profiles from database
        loadDeveloperProfiles();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadDeveloperProfiles() {
        ParseQuery<Developer> query = ParseQuery.getQuery("Developer");
        query.setLimit(5);
        query.addDescendingOrder("createdAt");
        // Search for ParseObject Developer
        // Query will invoke the FindCallback with either the object or the exception thrown
        query.findInBackground(new FindCallback<Developer>() {
            @Override
            public void done(List<Developer> developers, ParseException e) {
                if (e != null) {
                    Toast.makeText(getContext(), "Error reading developer profiles from the database",
                            Toast.LENGTH_SHORT).show();
                    return;
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