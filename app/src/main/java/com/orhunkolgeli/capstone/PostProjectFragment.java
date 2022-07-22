package com.orhunkolgeli.capstone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.orhunkolgeli.capstone.models.Project;
import com.orhunkolgeli.capstone.utils.GetFileFromUri;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;

public class PostProjectFragment extends Fragment {

    private static final String TAG = "PostProjectFragment";
    public static final String PROJECT = "project";
    public static final String LOCATION = "location";
    private ActivityResultLauncher<Intent> launcher;
    private File photoFile;
    private ImageView ivProjectPic;

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
        initializeLauncher();
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
        EditText etImageDescription = view.findViewById(R.id.etImageDescription);
        Button btnPost = view.findViewById(R.id.btnPost);
        Button btnUploadImage = view.findViewById(R.id.btnUploadProjectImage);
        ivProjectPic = view.findViewById(R.id.ivProjectPic);

        // Set onClick for image selection
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                launcher.launch(i);
            }
        });

        // Set onClick for the Post button
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String projectType = etProjectType.getText().toString();
                String projectDescription = etProjectDescription.getText().toString();
                String imageDescription = etImageDescription.getText().toString();

                if (projectType.isEmpty() || projectDescription.isEmpty() || imageDescription.isEmpty()) {
                    Toast.makeText(getContext(), R.string.fill_all_required_fields, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (photoFile == null) {
                    Toast.makeText(getContext(), R.string.upload_descriptive_image, Toast.LENGTH_SHORT).show();
                    return;
                }
                // Create new project and set its fields
                Project project = new Project();
                project.setImage(new ParseFile(photoFile));
                project.setType(projectType);
                project.setDescription(projectDescription);
                project.setImageDescription(imageDescription);
                project.setLocation(ParseUser.getCurrentUser().getParseGeoPoint(LOCATION));
                // Assign saved project to the current user
                project.setUser(ParseUser.getCurrentUser());
                project.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) { // exception thrown
                            Log.e(TAG, "Error while saving the project", e);
                            return;
                        }
                        Toast.makeText(getContext(), R.string.project_save_success, Toast.LENGTH_SHORT).show();
                    }
                });
                NavHostFragment.findNavController(PostProjectFragment.this)
                        .navigate(R.id.action_postProjectFragment_to_DeveloperSearchFragment);
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

    private void initializeLauncher() {
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            // Store the file using Uri
                            Uri selectedImageUri = data.getData();
                            photoFile = GetFileFromUri.getFile(requireContext(), selectedImageUri);
                            try {
                                // Display the selected image to the user
                                Bitmap selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                                        requireContext().getContentResolver(), selectedImageUri);
                                ivProjectPic.setImageBitmap(selectedImageBitmap);
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}