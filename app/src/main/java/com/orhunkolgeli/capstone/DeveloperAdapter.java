package com.orhunkolgeli.capstone;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;
import java.util.Random;

public class DeveloperAdapter extends RecyclerView.Adapter<DeveloperAdapter.ViewHolder> {
    private Context context;
    private List<Developer> developers;
    Fragment fragment;

    public DeveloperAdapter(Context context, List<Developer> developers, Fragment fragment) {
        this.context = context;
        this.developers = developers;
        this.fragment = fragment;
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
        holder.bind(developer, position);
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
        ConstraintLayout clDeveloperItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSkills = itemView.findViewById(R.id.tvSkills);
            tvBio = itemView.findViewById(R.id.tvBio);
            tvGitHub = itemView.findViewById(R.id.tvGitHub);
            tvDevInitials = itemView.findViewById(R.id.tvDevInitials);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            clDeveloperItem = itemView.findViewById(R.id.clDeveloperItem);
        }

        public void bind(Developer developer, int position) {
            tvFullName.setText(developer.getFullName());
            tvSkills.setText(developer.getSkills());
            tvBio.setText(developer.getBio());
            tvGitHub.setText(developer.getGitHub());
            tvDevInitials.setText(developer.getFullName().substring(0,1));
            clDeveloperItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavHostFragment.findNavController(fragment)
                            .navigate(DeveloperSearchFragmentDirections
                                    .actionDeveloperSearchFragmentToDeveloperDetailFragment(
                                            developer, DeveloperDetailFragment.DeveloperCategory.DEVELOPER, position));
                }
            });
        }
    }
}
