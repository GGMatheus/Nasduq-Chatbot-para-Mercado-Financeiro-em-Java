package com.amazonaws.youruserpools.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amazonaws.youruserpools.Fragments.ChatFragment;
import com.amazonaws.youruserpools.Fragments.SettingsFragment;
import com.amazonaws.youruserpools.Fragments.WatchlistFragment;

/**
 * Created by Leandro on 3/9/2017.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    static final int NUM_ITEMS = 3;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                return new WatchlistFragment();
            case 1:
                return new ChatFragment();
            case 2:
                return new SettingsFragment();
            default:
                return null;
        }
    }
}
