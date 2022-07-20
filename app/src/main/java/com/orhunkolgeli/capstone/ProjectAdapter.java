package com.orhunkolgeli.capstone;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;
import java.util.Random;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {
    public static final int DARK_COLOR_RGB_VALUE = 200;
    public static final int ALPHA = 255;
    private Context context;
    private List<Project> projects;
    private Fragment fragment;

    public ProjectAdapter(Context context, List<Project> projects, Fragment fragment) {
        this.context = context;
        this.projects = projects;
        this.fragment = fragment;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_project, parent, false);
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
        ConstraintLayout clProjectItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProjectType = itemView.findViewById(R.id.tvProjectType);
            tvProjectDescription = itemView.findViewById(R.id.tvProjectDescription);
            ivProjectImage = itemView.findViewById(R.id.ivProjectImage);
            tvInitials = itemView.findViewById(R.id.tvInitials);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            clProjectItem = itemView.findViewById(R.id.clProjectItem);
        }

        public void bind(Project project) {
            clProjectItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavHostFragment.findNavController(fragment).navigate(ProjectSearchFragmentDirections
                                    .actionProjectSearchFragmentToProjectDetailFragment(project));
                }
            });
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

    private int generateRandomDarkerColor() {
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
