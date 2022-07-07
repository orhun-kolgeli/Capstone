package com.orhunkolgeli.capstone;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DeveloperSearchTabAdapter extends FragmentStateAdapter {
    public static final int ITEM_COUNT = 2;
    public DeveloperSearchTabAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new FindDeveloperFragment();
        } else {
            return new ReviewApplicationsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }
}
