package com.orhunkolgeli.capstone;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostProjectFragment extends Fragment {

    private static final String TAG = "PostProjectFragment";
    public static final String PROJECT = "project";
    public static final int MAX_RESULTS = 5;
    public static final int ZIP_LENGTH = 5;
    private CustomGeoCoder geocoder;

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
        geocoder = new CustomGeoCoder(getContext());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_project, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get references to view objects
        EditText etProjectType = view.findViewById(R.id.etProjectType);
        EditText etProjectDescription = view.findViewById(R.id.etProjectDescription);
        AutoCompleteTextView tvZipCode = view.findViewById(R.id.tvZipCode);
        Button btnPost = view.findViewById(R.id.btnPost);

        // Set onClick for the Post button
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String projectType = etProjectType.getText().toString();
                String projectDescription = etProjectDescription.getText().toString();
                String addressText = tvZipCode.getText().toString();
                if (projectType.isEmpty() || projectDescription.isEmpty()) {
                    Toast.makeText(getContext(), R.string.fill_all_required_fields, Toast.LENGTH_SHORT).show();
                    return;
                }
                // Create new project and set its fields
                Project project = new Project();
                project.setType(projectType);
                project.setDescription(projectDescription);
                if (!addressText.isEmpty()) {
                    project.setLocation(geocoder.getGeoPointFromText(addressText));
                }
                // Assign saved project to the current user
                project.setUser(ParseUser.getCurrentUser());
                project.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) { // exception thrown
                            Log.e(TAG, "Error while saving the project", e);
                            return;
                        }
                        Toast.makeText(getActivity(), R.string.project_save_success, Toast.LENGTH_SHORT).show();
                        tieProjectToOrgAccount(project);
                    }
                });
                NavHostFragment.findNavController(PostProjectFragment.this)
                        .navigate(R.id.action_postProjectFragment_to_DeveloperSearchFragment);
            }
        });
        setUpAddressSuggestions(tvZipCode);
    }

    private void setUpAddressSuggestions(AutoCompleteTextView tvZipCode) {
        List<String> stringList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_list_item_1, stringList);
        tvZipCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (tvZipCode.getText().toString().length() == ZIP_LENGTH) {
                    String zipCode = tvZipCode.getText().toString();
                    List<Address> addressList = geocoder.getAddressListFromText(zipCode, MAX_RESULTS);
                    arrayAdapter.clear();
                    for (int i = 0; i < addressList.size(); i++) {
                        arrayAdapter.add(addressList.get(i).getAddressLine(0));
                    }
                    tvZipCode.setAdapter(arrayAdapter);
                    arrayAdapter.getFilter().filter(null);
                    tvZipCode.showDropDown();
                }
                return false;
            }
        });
    }

    private void tieProjectToOrgAccount(Project project) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put(PROJECT, project);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) { // exception thrown
                    Toast.makeText(getActivity(), R.string.error_tying_project_to_account, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), R.string.success_tying_project_to_account, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}