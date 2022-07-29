package com.orhunkolgeli.capstone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhunkolgeli.capstone.databinding.FragmentDeveloperSearchBinding;
import com.orhunkolgeli.capstone.databinding.FragmentPostedProjectBinding;
import com.orhunkolgeli.capstone.databinding.FragmentProjectSearchBinding;
import com.orhunkolgeli.capstone.models.Project;
import com.orhunkolgeli.capstone.utils.EndlessRecyclerViewScrollListener;
import com.orhunkolgeli.capstone.utils.ProjectFilterValues;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class PostedProjectFragment extends Fragment {

    public static final String PROJECT = "project";
    public static final String CLASS_NAME_PROJECT = "Project";
    public static final String USER = "user";
    private Project project;
    private FragmentPostedProjectBinding binding;
    private ArrayList<Project> projects;
    private ProjectAdapter adapter;

    public PostedProjectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseObject projectObject = ParseUser.getCurrentUser().getParseObject(PROJECT);
        if (projectObject == null) {
            return;
        }
        try {
            projectObject = projectObject.fetchIfNeeded();
            project = (Project) projectObject;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPostedProjectBinding.inflate(inflater, container, false);

        // Set up the RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.rvProjects.setLayoutManager(linearLayoutManager);
        projects = new ArrayList<>();
        adapter = new ProjectAdapter(getActivity(), projects, PostedProjectFragment.this);
        binding.rvProjects.setAdapter(adapter);
        binding.rvProjects.setItemAnimator(new DefaultItemAnimator());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        projects.clear();
        // Find the projects owned by the current user
        ParseQuery<Project> parseQuery = new ParseQuery<Project>(CLASS_NAME_PROJECT);
        parseQuery.whereEqualTo(USER, ParseUser.getCurrentUser());
        parseQuery.findInBackground(new FindCallback<Project>() {
            @Override
            public void done(List<Project> projectList, ParseException e) {
                projects.addAll(projectList);
                adapter.notifyDataSetChanged();
            }
        });
    }
}