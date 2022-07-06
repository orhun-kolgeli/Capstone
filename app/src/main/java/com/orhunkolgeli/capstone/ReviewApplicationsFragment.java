package com.orhunkolgeli.capstone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orhunkolgeli.capstone.databinding.FragmentFindDeveloperBinding;
import com.orhunkolgeli.capstone.databinding.FragmentReviewApplicationsBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class ReviewApplicationsFragment extends Fragment {

    private FragmentReviewApplicationsBinding binding;
    List<Developer> applicants;
    ApplicantAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReviewApplicationsBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        // Get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rvApplicants);
        // Set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Create an adapter
        Project project = null;
        try {
            project = (Project) ParseUser.getCurrentUser().getParseObject("project").fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        applicants = new ArrayList<Developer>();
        adapter = new ApplicantAdapter(getContext(), applicants, getParentFragment());
        // Set adapter
        recyclerView.setAdapter(adapter);
        // Set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // Read in the applicants from database
        loadApplicants(project);
        return rootView;
    }

    private void loadApplicants(Project project) {
        project.getApplicants().getQuery().findInBackground(new FindCallback<Developer>() {
            @Override
            public void done(List<Developer> developers, ParseException e) {
                if (e != null) {
                    Toast.makeText(getContext(), "Error reading applicants from the database", Toast.LENGTH_SHORT).show();
                }
                applicants.addAll(developers);
                adapter.notifyDataSetChanged();
                // Hide the progress bar
                binding.pbLoadApplicants.setVisibility(View.GONE);
                binding.tvLoadingApplicants.setVisibility(View.GONE);
            }
        });
    }
}