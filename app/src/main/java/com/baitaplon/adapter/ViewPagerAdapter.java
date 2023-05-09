package com.baitaplon.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.baitaplon.view.fragment.HomeFragment;
import com.baitaplon.view.fragment.NotiFragment;
import com.baitaplon.view.fragment.SearchFragment;
import com.baitaplon.view.fragment.UserFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new HomeFragment();
            case 1: return new SearchFragment();
            case 2: return  new NotiFragment();
            case 3: return new UserFragment();
            default: return new HomeFragment();
        }
    }


    @Override
    public int getCount() {
        return 4;
    }
}
