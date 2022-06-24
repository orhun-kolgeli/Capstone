package com.orhunkolgeli.capstone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.orhunkolgeli.capstone.databinding.FragmentProjectDetailBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class PostProjectFragment extends Fragment {

    private static final String TAG = "PostProjectFragment";

    public PostProjectFragment() {
        // Required empty public constructor
    }


    public static PostProjectFragment newInstance(String param1, String param2) {
        PostProjectFragment fragment = new PostProjectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_project, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get references to view objects
        EditText etProjectType = view.findViewById(R.id.etProjectType);
        EditText etProjectDescription = view.findViewById(R.id.etProjectDescription);
        Button btnPost = view.findViewById(R.id.btnPost);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Project project = new Project();
                project.setType(etProjectType.getText().toString());
                project.setDescription(etProjectDescription.getText().toString());
                // Assign saved profile to the current user
                project.setUser(ParseUser.getCurrentUser());
                project.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) { // exception thrown
                            Log.e(TAG, "Error while saving the project", e);
                            return;
                        }
                        Toast.makeText(getActivity(), "Project saved successfully",
                                Toast.LENGTH_SHORT).show();
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        currentUser.put("project", project);
                        currentUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) { // exception thrown
                                    Toast.makeText(getActivity(),
                                            "Error tying project to your organization account",
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Toast.makeText(getActivity(),
                                        "Your project has been tied to your organization account.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                NavHostFragment.findNavController(PostProjectFragment.this)
                        .navigate(R.id.action_postProjectFragment_to_DeveloperSearchFragment);
            }
        });
    }
}