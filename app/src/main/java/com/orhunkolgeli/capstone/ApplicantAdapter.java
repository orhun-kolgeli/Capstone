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
import java.util.Locale;

public class ApplicantAdapter extends RecyclerView.Adapter<ApplicantAdapter.ViewHolder> {
    private Context context;
    private List<Developer> applicants;
    Fragment fragment;
    protected static RemoveCallback removeCallback;

    public ApplicantAdapter(Context context, List<Developer> applicants, Fragment fragment) {
        this.context = context;
        this.applicants = applicants;
        this.fragment = fragment;
        removeCallback = new RemoveCallback() {
            @Override
            public void onActionRemove(int position) {
                applicants.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());

            }
        };
    }
    
    @NonNull
    @Override
    public ApplicantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_applicant, parent, false);
        return new ApplicantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicantAdapter.ViewHolder holder, int position) {
        Developer developer = applicants.get(position);
        // Bind the project data to the view elements
        holder.bind(developer, position);
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
        ConstraintLayout clApplicant;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSkills = itemView.findViewById(R.id.tvSkills);
            tvBio = itemView.findViewById(R.id.tvBio);
            tvGitHub = itemView.findViewById(R.id.tvGitHub);
            tvDevInitials = itemView.findViewById(R.id.tvDevInitials);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            clApplicant = itemView.findViewById(R.id.clApplicant);
        }

        public void bind(Developer developer, int position) {
            tvFullName.setText(developer.getFullName());
            tvSkills.setText(developer.getSkills());
            tvBio.setText(developer.getBio());
            tvGitHub.setText(developer.getGitHub());
            tvDevInitials.setText(developer.getFullName().substring(0,1));
            clApplicant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavHostFragment.findNavController(fragment)
                            .navigate(DeveloperSearchFragmentDirections
                                    .actionDeveloperSearchFragmentToDeveloperDetailFragment(
                                            developer, DeveloperDetailFragment.DeveloperCategory.APPLICANT, position));
                }
            });
        }
    }
}
