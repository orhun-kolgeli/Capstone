package com.orhunkolgeli.capstone;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.orhunkolgeli.capstone.models.Project;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {
    public static final int DARK_COLOR_RGB_VALUE = 200;
    public static final int ALPHA = 255;
    public static final String PROJECT = "project";
    private final Context context;
    private final List<Project> projects;
    private final Fragment fragment;

    public ProjectAdapter(Context context, List<Project> projects, Fragment fragment) {
        this.context = context;
        this.projects = projects;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (fragment instanceof ProjectSearchFragment) {
            view = LayoutInflater.from(context).inflate(R.layout.item_project, parent, false);
        } else { // instanceof PostedProjectFragment
            view = LayoutInflater.from(context).inflate(R.layout.item_own_project, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Project project = projects.get(position);
        // Bind the project data to the view elements
        holder.bind(project);

    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProjectType;
        TextView tvUsername;
        TextView tvProjectDescription;
        ImageView ivProjectImage;
        TextView tvInitials;
        TextView tvApplied;
        Button btnSelect;
        ConstraintLayout clProjectItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProjectType = itemView.findViewById(R.id.tvProjectType);
            tvProjectDescription = itemView.findViewById(R.id.tvProjectDescription);
            ivProjectImage = itemView.findViewById(R.id.ivProjectImage);
            tvInitials = itemView.findViewById(R.id.tvInitials);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            clProjectItem = itemView.findViewById(R.id.clProjectItem);
            tvApplied = itemView.findViewById(R.id.tvApplied);
            btnSelect = itemView.findViewById(R.id.btnSelect);
        }

        public void bind(Project project) {
            if (fragment instanceof ProjectSearchFragment) {
                clProjectItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavHostFragment.findNavController(fragment).navigate(ProjectSearchFragmentDirections
                                .actionProjectSearchFragmentToProjectDetailFragment(project));
                    }
                });
            } else { // instanceof PostedProjectFragment
                if (project.getApplicantCount() == 1) {
                    // Only 1 applicant
                    tvApplied.setText(String.format(Locale.getDefault(), "%d %s",
                            project.getApplicantCount(),
                            context.getString(R.string.singular_already_applied)));
                } else {
                    // Many applicants
                    tvApplied.setText(String.format(Locale.getDefault(), "%d %s",
                            project.getApplicantCount(),
                            context.getString(R.string.already_applied)));
                }
                if (isPrimary(project)) {
                    // If this is the primary project, gray out the select button
                    btnSelect.setText(R.string.selected);
                    btnSelect.setBackgroundTintList(ColorStateList.valueOf(
                            context.getResources().getColor(R.color.dark_grey, null)));
                    btnSelect.setTextColor(
                            context.getResources().getColor(R.color.light_grey, null));
                } else {
                    // If not the primary project, allow user to make this project primary by
                    // clicking select
                    btnSelect.setText(R.string.select);
                    btnSelect.setBackgroundTintList(ColorStateList.valueOf(
                            context.getResources().getColor(R.color.color_primary, null)));
                    btnSelect.setTextColor(
                            context.getResources().getColor(R.color.white, null));
                }
                btnSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Make the clicked project the primary project and update the views
                        makePrimary(project);
                        btnSelect.setText(R.string.selected);
                        btnSelect.setBackgroundTintList(ColorStateList.valueOf(
                                context.getResources().getColor(R.color.dark_grey, null)));
                        btnSelect.setTextColor(
                                context.getResources().getColor(R.color.light_grey, null));
                        fragment.onResume();
                    }
                });
            }
            setProjectTexts(project);
            int darker_color = generateRandomDarkerColor();
            // Set user's initial's background to random color
            tvInitials.setBackgroundTintList(ColorStateList.valueOf(darker_color));
            // Load image describing the project from Parse server
            ParseFile image = project.getImage();
            if (image != null) {
                ivProjectImage.setContentDescription(project.getImageDescription());
                ivProjectImage.setTooltipText(project.getImageDescription());
                // Reset the size of ivProjectImage
                ivProjectImage.layout(0,0,0,0);
                // Load project image into ivProjectImage
                Glide.with(context)
                        .load(image.getUrl())
                        .into(ivProjectImage);
                ivProjectImage.setVisibility(View.VISIBLE);
            } else {
                // No project image to show
                ivProjectImage.setVisibility(View.GONE);
            }
        }

        private void setProjectTexts(Project project) {
            // Display project type
            tvProjectType.setText(project.getType());
            // Display project description
            tvProjectDescription.setText(project.getDescription());
            // Display user's initial
            String username = "";
            try {
                username = project.getUser().fetchIfNeeded().getString("name");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvUsername.setText(username);
            tvInitials.setText(username.substring(0,1).toUpperCase());
        }
    }

    private void makePrimary(Project project) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put(PROJECT, project);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(context,
                            R.string.error_selecting_project, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isPrimary(Project project) {
        // Return true if IDs match, false otherwise
        ParseObject parseObject = ParseUser.getCurrentUser().getParseObject("project");
        if (parseObject == null) {
            return false;
        }
        try {
            parseObject = parseObject.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Project givenProject = (Project) parseObject;
        return Objects.equals(project.getObjectId(), givenProject.getObjectId());
    }

    private int generateRandomDarkerColor() {
        if (fragment instanceof PostedProjectFragment) {
            return context.getColor(R.color.color_primary);
        }
        // Pick a random color
        Random rnd = new Random();
        int color = Color.argb(ALPHA,
                rnd.nextInt(DARK_COLOR_RGB_VALUE),
                rnd.nextInt(DARK_COLOR_RGB_VALUE),
                rnd.nextInt(DARK_COLOR_RGB_VALUE));
        return color;
    }

    public void clear() {
        int size = projects.size();
        projects.clear();
        notifyItemRangeRemoved(0, size);
    }
}
