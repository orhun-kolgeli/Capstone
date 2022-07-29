package com.orhunkolgeli.capstone;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DeveloperSearchTabAdapter extends FragmentStateAdapter {
    public static final int ITEM_COUNT = 3;
    private FindDeveloperFragment findDeveloperFragment;
    private ReviewApplicationsFragment reviewApplicationsFragment;
    private PostedProjectFragment postedProjectFragment;

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
        } else if (position == 1) {
            if (reviewApplicationsFragment == null) {
                reviewApplicationsFragment = new ReviewApplicationsFragment();
            }
            return reviewApplicationsFragment;
        } else {
            if (postedProjectFragment == null) {
                postedProjectFragment = new PostedProjectFragment();
            }
            return postedProjectFragment;
        }
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }
}
