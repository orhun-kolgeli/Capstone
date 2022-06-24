package com.orhunkolgeli.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.orhunkolgeli.capstone.databinding.FragmentDeveloperDetailBinding;

public class DeveloperDetailFragment extends Fragment {

    private FragmentDeveloperDetailBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDeveloperDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Developer developer = getArguments().getParcelable("developer");
        binding.tvBio2.setText(developer.getBio());
        String full_name = developer.getUser().getString("name");
        binding.tvFullName2.setText(full_name);
        binding.tvDevInitials2.setText(full_name.substring(0,1).toUpperCase());
        binding.tvSkills2.setText(developer.getSkills());
        binding.tvGitHub2.setText(developer.getGitHub());
        binding.btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                String email = developer.getUser().getString("emailAddress");
                intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {email} );
                intent.putExtra(intent.EXTRA_SUBJECT, "Invitation to Interview");
                intent.putExtra(android.content.Intent.EXTRA_TEXT,
                        "Hi " + full_name + ",\n\n");
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

}