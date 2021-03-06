package com.orhunkolgeli.capstone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orhunkolgeli.capstone.databinding.FragmentReviewApplicationsBinding;
import com.orhunkolgeli.capstone.models.Developer;
import com.orhunkolgeli.capstone.models.Project;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class ReviewApplicationsFragment extends Fragment {

    private FragmentReviewApplicationsBinding binding;
    List<Developer> applicants;
    ApplicantAdapter adapter;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView != null) {
            return rootView;
        }
        // Inflate the layout for this fragment
        binding = FragmentReviewApplicationsBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        // Get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rvApplicants);
        // Set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Create an adapter
        applicants = new ArrayList<Developer>();
        adapter = new ApplicantAdapter(getContext(), applicants, ReviewApplicationsFragment.this);
        // Set adapter
        recyclerView.setAdapter(adapter);
        // Set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return rootView;
    }

    private void setUpRefresh(Project project) {
        binding.swipeContainerApplicant.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                loadApplicants(project);
                // Signal refresh has finished
                binding.swipeContainerApplicant.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        binding.swipeContainerApplicant.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void loadApplicants(Project project) {
        if (project == null) {
            binding.pbLoadApplicants.setVisibility(View.GONE);
            binding.tvLoadingApplicants.setVisibility(View.GONE);
            return;
        }
        project.getApplicants().getQuery().findInBackground(new FindCallback<Developer>() {
            @Override
            public void done(List<Developer> developers, ParseException e) {
                if (e != null) {
                    Toast.makeText(getContext(), R.string.applicant_load_error, Toast.LENGTH_SHORT).show();
                }
                applicants.addAll(developers);
                adapter.notifyDataSetChanged();
                // Hide the progress bar
                binding.pbLoadApplicants.setVisibility(View.GONE);
                binding.tvLoadingApplicants.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        applicants.clear();
        Project project = null;
        try {
            if (ParseUser.getCurrentUser().getParseObject("project") != null) {
                project = (Project) ParseUser.getCurrentUser().getParseObject("project").fetchIfNeeded();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Read in the applicants from database
        loadApplicants(project);
        setUpRefresh(project);
    }
}