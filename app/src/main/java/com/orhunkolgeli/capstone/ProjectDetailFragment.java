package com.orhunkolgeli.capstone;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.orhunkolgeli.capstone.databinding.FragmentProjectDetailBinding;
import com.orhunkolgeli.capstone.models.Developer;
import com.orhunkolgeli.capstone.models.Project;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class ProjectDetailFragment extends Fragment {

    private static final String TAG = "ProjectDetailFragment";
    public static final String OBJECT_ID = "objectId";
    public static final String USER = "user";
    public static final String ALERT = "alert";
    public static final String DEVELOPER_ID = "developerId";
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
        project.getApplicants().getQuery().whereEqualTo("user", ParseUser.getCurrentUser()).countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                binding.btnApply.setVisibility(View.VISIBLE);
                if (count == 0) {
                    binding.btnApply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClickApply(project);
                        }
                    });
                } else {
                    binding.btnApply.setText(R.string.you_already_applied);
                    binding.btnApply.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_grey, null)));
                    binding.btnApply.setTextColor(getResources().getColor(R.color.light_grey, null));
                }
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

    private void onClickApply(Project project) {
        binding.btnApply.setVisibility(View.GONE);
        binding.ivCheckMark.setVisibility(View.VISIBLE);
        ParseObject developerObject = ParseUser.getCurrentUser().getParseObject("developer");
        // If the user has no developer account, inform the user and return
        if (developerObject == null) {
            showNoDeveloperAccountAlert();
            return;
        }
        // Get the developer profile
        Developer developer = null;
        try {
            developer = (Developer) developerObject.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Send a push notification to the organization
        sendNotificationToOrganization(project, developer);
        // Add the developer to the pool of applicants
        project.addApplicant(developer);
        Toast.makeText(getContext(), R.string.application_received, Toast.LENGTH_SHORT).show();
    }

    private void sendNotificationToOrganization(@NonNull Project project, Developer developer) {
        ParseUser projectOwner = project.getUser();
        String owner_id = projectOwner.getObjectId();
        Log.i(TAG, "Sending notification to the user with the following id: " + owner_id);
        // Make a query where the user is the owner of the project
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo(OBJECT_ID, owner_id);

        // Find devices associated with this user
        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
        pushQuery.whereMatchesQuery(USER, userQuery);

        // Create JSONObject that contains push data
        JSONObject data = new JSONObject();
        try {
            data.put(ALERT, getString(R.string.you_have_an_application_from) +
                    ParseUser.getCurrentUser().getUsername());
            data.put(DEVELOPER_ID, developer.getObjectId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query
        push.setData(data);
        push.sendInBackground();
    }

    private void showNoDeveloperAccountAlert() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.dev_profile_needed_to_apply)
                .setMessage(R.string.set_up_profile_now)
                .setIcon(R.drawable.icon)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Navigation.findNavController(binding.getRoot())
                                .navigate(R.id.action_ProjectDetailFragment_to_setupProfileFragment);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }
}