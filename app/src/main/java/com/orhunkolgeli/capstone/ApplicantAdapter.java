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

public class ApplicantAdapter extends DeveloperAdapter {

    public ApplicantAdapter(Context context, List<Developer> developers, Fragment fragment) {
        super(context, developers, fragment);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applicant, parent, false);
        return new ApplicantAdapter.ViewHolder(view);
    }

    @Override
    public void setApplicantOnClickListeners(Button btnExtendOffer, Button btnRemoveApplicant, Developer applicant) {
        btnExtendOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "TODO", Toast.LENGTH_SHORT).show();
            }
        });
        btnRemoveApplicant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Project project = null;
                try {
                    project = ParseUser.getCurrentUser().getParseObject("project").fetchIfNeeded();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (project != null) {
                    project.removeApplicant(applicant);
                }
            }
        });
    }
}
