package com.orhunkolgeli.capstone;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;
import java.util.Random;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {
    private Context context;
    private List<Project> projects;

    public ProjectAdapter(Context context, List<Project> projects) {
        this.context = context;
        this.projects = projects;
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProjectType = itemView.findViewById(R.id.tvProjectType);
            tvProjectDescription = itemView.findViewById(R.id.tvProjectDescription);
            ivProjectImage = itemView.findViewById(R.id.ivProjectImage);
            tvInitials = itemView.findViewById(R.id.tvInitials);
            tvUsername = itemView.findViewById(R.id.tvUsername);
        }

        public void bind(Project project) {
            ivProjectImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle result = new Bundle();
                    result.putParcelable("bundleKey", project);
                    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                    NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
                    NavController navController = navHostFragment.getNavController();
                    navController.navigate(R.id.action_ProjectSearchFragment_to_ProjectDetailFragment, result);



                }
            });
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
            // Pick a random color
            Random rnd = new Random();
            int color = Color.argb(255,
                    rnd.nextInt(256),
                    rnd.nextInt(256),
                    rnd.nextInt(256));
            // Set user's initial's background to random color
            tvInitials.setBackgroundTintList(ColorStateList.valueOf(color));
            // Load image describing the project from Parse server
            ParseFile image = project.getImage();
            if (image != null) {
                Glide.with(context)
                        .load(image.getUrl())
                        .into(ivProjectImage);
            }
        }
    }
}
