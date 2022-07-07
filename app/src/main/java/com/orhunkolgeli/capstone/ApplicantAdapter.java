package com.orhunkolgeli.capstone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

public class ApplicantAdapter extends RecyclerView.Adapter<ApplicantAdapter.ViewHolder> {
    private Context context;
    private List<Developer> applicants;

    public ApplicantAdapter(Context context, List<Developer> applicants) {
        this.context = context;
        this.applicants = applicants;
    }
    
    @NonNull
    @Override
    public ApplicantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_developer, parent, false);
        return new ApplicantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicantAdapter.ViewHolder holder, int position) {
        Developer developer = applicants.get(position);
        // Bind the project data to the view elements
        holder.bind(developer);
    }

    @Override
    public int getItemCount() {
        return applicants.size();
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
