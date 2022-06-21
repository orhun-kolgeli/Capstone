package com.orhunkolgeli.capstone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

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
        TextView tvProjectDescription;
        ImageView ivProjectImage;
        TextView tvInitials;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProjectType = itemView.findViewById(R.id.tvProjectType);
            tvProjectDescription = itemView.findViewById(R.id.tvProjectDescription);
            ivProjectImage = itemView.findViewById(R.id.ivProjectImage);
            tvInitials = itemView.findViewById(R.id.tvInitials);

        }

        public void bind(Project project) {
            tvProjectType.setText(project.getType());
            tvProjectDescription.setText(project.getDescription());
            //tvInitials.setText(project.getUser().getUsername().substring(0,1).toUpperCase());
            ParseFile image = project.getImage();
            if (image != null) {
                Glide.with(context)
                        .load(image.getUrl())
                        .into(ivProjectImage);
            }

        }
    }
}
