package com.orhunkolgeli.capstone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.orhunkolgeli.capstone.databinding.FragmentDeveloperSearchBinding;
import com.orhunkolgeli.capstone.utils.ZoomOutPageTransformer;

public class DeveloperSearchFragment extends Fragment {

    private FragmentDeveloperSearchBinding binding;
    DeveloperSearchTabAdapter developerSearchTabAdapter;
    ViewPager2 viewPager;
    TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDeveloperSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tabLayout);
        developerSearchTabAdapter = new DeveloperSearchTabAdapter(this);
        viewPager.setAdapter(developerSearchTabAdapter);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
        int[] tabTitles = {
                R.string.invite,
                R.string.review,
                R.string.projects
        };
        int[] tabIcons = {
                R.drawable.searchperson,
                R.drawable.review,
                R.drawable.project
        };
        // Create a TabLayoutMediator to link the TabLayout to the ViewPager2 and attach it
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(tabTitles[position]);
            tab.setIcon(tabIcons[position]);
        }
        ).attach();
        binding.fabAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(DeveloperSearchFragment.this).navigate(
                        R.id.action_DeveloperSearchFragment_to_postProjectFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}