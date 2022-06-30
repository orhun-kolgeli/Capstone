package com.orhunkolgeli.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.orhunkolgeli.capstone.databinding.FragmentProjectDetailBinding;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class ProjectDetailFragment extends Fragment {

    private static final String TAG = "ProjectDetailFragment";
    private FragmentProjectDetailBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProjectDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Project project = getArguments().getParcelable("project");
        String orgName = project.getUser().getString("name");
        String type = project.getType();
        String description = project.getDescription();
        binding.tvPostedBy.setText(orgName);
        binding.tvTypeofProject.setText(type);
        binding.tvDescription.setText(description);
        ParseFile image = project.getImage();
        if (image != null) {
            Glide.with(requireActivity())
                    .load(image.getUrl())
                    .into(binding.ivProjectImg);
        }
        binding.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendApplicationViaEmail(project)
                // Pull out old code into another function for possible future use
                sendNotification(project);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void sendNotification(@NonNull Project project) {
        ParseUser projectOwner = project.getUser();
        String owner_id = projectOwner.getObjectId();
        // For development purposes
        Log.i(TAG, "Sending notification to the user with the following id: " + owner_id);
        // Make a query where the user is the owner of the project
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo("objectId", owner_id);

        // Find devices associated with this user
        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
        pushQuery.whereMatchesQuery("user", userQuery);

        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query
        push.setMessage(ParseUser.getCurrentUser().getUsername() + " is interested in your " +
                project.getType() + " project!");
        push.sendInBackground();
    }

    private void sendApplicationViaEmail(Project project) {
        // Old code
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        String email = project.getUser().getString("emailAddress");
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {email} );
        intent.putExtra(Intent.EXTRA_SUBJECT, "Job application - " + project.getType() + " Developer");
        intent.putExtra(android.content.Intent.EXTRA_TEXT,
                "Dear " + "\n\n");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            getActivity().startActivity(Intent.createChooser(intent, "Send Email using:"));
        }
        else {
            Toast.makeText(getActivity(), "You don't have any email apps.", Toast.LENGTH_SHORT).show();
        }
    }
}