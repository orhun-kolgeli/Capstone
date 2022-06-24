package com.orhunkolgeli.capstone;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.List;
import java.util.Random;

public class DeveloperAdapter extends RecyclerView.Adapter<DeveloperAdapter.ViewHolder> {
    private Context context;
    private List<Developer> developers;

    public DeveloperAdapter(Context context, List<Developer> developers) {
        this.context = context;
        this.developers = developers;
    }

    @NonNull
    @Override
    public DeveloperAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_developer, parent, false);
        return new DeveloperAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeveloperAdapter.ViewHolder holder, int position) {
        Developer developer = developers.get(position);
        // Bind the project data to the view elements
        holder.bind(developer);
    }

    @Override
    public int getItemCount() {
        return developers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSkills;
        TextView tvBio;
        TextView tvGitHub;
        TextView tvDevInitials;
        TextView tvFullName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSkills = itemView.findViewById(R.id.tvSkills);
            tvBio = itemView.findViewById(R.id.tvBio);
            tvGitHub = itemView.findViewById(R.id.tvGitHub);
            tvDevInitials = itemView.findViewById(R.id.tvDevInitials);
            tvFullName = itemView.findViewById(R.id.tvFullName);
        }

        public void bind(Developer developer) {
            tvSkills.setText(developer.getSkills());
            tvBio.setText(developer.getBio());
            tvGitHub.setText(developer.getGitHub());
            String full_name = "";
            try {
                full_name = developer.getUser().fetchIfNeeded().getString("name");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvDevInitials.setText(full_name.substring(0,1).toUpperCase());
            tvFullName.setText(full_name);
        }
    }
}
