package com.orhunkolgeli.capstone;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DeveloperSearchTabAdapter extends FragmentStateAdapter {
    public static final int ITEM_COUNT = 2;
    private FindDeveloperFragment findDeveloperFragment;
    private ReviewApplicationsFragment reviewApplicationsFragment;

    public DeveloperSearchTabAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            if (findDeveloperFragment == null) {
                findDeveloperFragment = new FindDeveloperFragment();
            }
            return findDeveloperFragment;
        } else {
            if (reviewApplicationsFragment == null) {
                reviewApplicationsFragment = new ReviewApplicationsFragment();
            }
            return reviewApplicationsFragment;
        }
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }
}
