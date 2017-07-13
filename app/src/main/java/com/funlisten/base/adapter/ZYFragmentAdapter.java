package com.funlisten.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 17/3/15.
 */

public class ZYFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private List<String> mTitles = new ArrayList<>();

    public ZYFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position < mTitles.size()) {
            return mTitles.get(position);
        } else {
            return super.getPageTitle(position);
        }
    }
}

