package com.orhunkolgeli.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.orhunkolgeli.capstone.databinding.FragmentProjectDetailBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import java.util.List;
import java.util.Objects;

public class ProjectDetailFragment extends Fragment {

    private static final String TAG = "ProjectDetailFragment";
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
        binding.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                String email = project.getUser().getString("emailAddress");
                intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {email} );
                intent.putExtra(intent.EXTRA_SUBJECT, "Job application - " + project.getType() + " Developer");
                intent.putExtra(android.content.Intent.EXTRA_TEXT,
                        "Dear " + orgName + ",\n\n");
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    getActivity().startActivity(Intent.createChooser(intent, "Send Email using:"));
                }
                else {
                    Toast.makeText(getActivity(), "You don't have any email apps.", Toast.LENGTH_SHORT).show();
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
}