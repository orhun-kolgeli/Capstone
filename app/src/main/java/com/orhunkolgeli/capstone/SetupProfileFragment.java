package com.orhunkolgeli.capstone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.orhunkolgeli.capstone.databinding.FragmentProjectDetailBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class SetupProfileFragment extends Fragment {
    // the fragment initialization parameters
    public static final String KEY_NAME = "name";
    public static final String TAG = "SetupProfileFragment";
    public static final String[] suggestedSkills = {
            "iOS", "Objective-C", "Swift", "Xcode", "Git", "GitHub",
            "Android", "Android Studio", "Java", "Kotlin", "C++", "Material Design", "Firebase", "XML"
    };


    public SetupProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of SetupProfileFragment using the provided parameters.
     *
     * @return A new instance of fragment SetupProfileFragment.
     */
    public static SetupProfileFragment newInstance() {
        SetupProfileFragment fragment = new SetupProfileFragment();
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
        return inflater.inflate(R.layout.fragment_setup_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get references to view objects
        MultiAutoCompleteTextView mactvSkills = view.findViewById(R.id.mactvSkills);
        EditText etBio = view.findViewById(R.id.etBio);
        EditText etGitHub = view.findViewById(R.id.etGitHub);
        Button btnSave = view.findViewById(R.id.btnSave);

        // Pre-fill the form, if applicable
        Developer existingProfile = (Developer) ParseUser.getCurrentUser().getParseObject("developer");
        if (existingProfile != null) {
            try {
                ((MainActivity) requireActivity()).getSupportActionBar().setTitle(R.string.update_profile);
                btnSave.setText(R.string.update);
                existingProfile = existingProfile.fetchIfNeeded();
                mactvSkills.setText(existingProfile.getSkills());
                etBio.setText(existingProfile.getBio());
                etGitHub.setText(existingProfile.getGitHub());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Set up multiAutoCompleteTextView
        mactvSkills.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, suggestedSkills);
        mactvSkills.setAdapter(arrayAdapter);

        Developer finalExistingProfile = existingProfile;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etBio.getText().toString().isEmpty() || etGitHub.getText().toString().isEmpty() || mactvSkills.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                    return;
                }
                // If new profile, create new Developer object and set its fields
                // Otherwise, update the existing fields
                Developer developer;
                if (finalExistingProfile == null) {
                    developer = new Developer();
                } else {
                    developer = finalExistingProfile;
                }
                developer.setBio(etBio.getText().toString());
                developer.setGitHub(etGitHub.getText().toString());
                developer.setSkills(mactvSkills.getText().toString());
                // Assign saved profile to the current user
                developer.setUser(ParseUser.getCurrentUser());
                developer.setFullName(ParseUser.getCurrentUser().getString(KEY_NAME));
                developer.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) { // exception thrown
                            Log.e(TAG, "Error while saving the profile", e);
                            return;
                        }
                        Toast.makeText(getActivity(), R.string.profile_save_success, Toast.LENGTH_SHORT).show();
                        tieDeveloperProfileToAccount(developer);
                    }
                });
                NavHostFragment.findNavController(SetupProfileFragment.this)
                        .navigate(R.id.action_setupProfileFragment_to_ProjectSearchFragment);
            }
        });
    }

    private void tieDeveloperProfileToAccount(Developer developer) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("developer", developer);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) { // exception thrown
                    Toast.makeText(getContext(), R.string.error_tying_profile_to_account, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}