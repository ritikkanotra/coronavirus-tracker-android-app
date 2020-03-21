package com.appodex.coronavirustracker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class CategoryAdapter extends FragmentPagerAdapter {


    private Context mContext;

    public CategoryAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return new GlobalFragment();
        }
        else if(position == 1) {
            return new CountriesFragment();
        }
        else {
            return new MyCountryFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0) {
            return "Global";
        }
        else if(position == 1) {
            return "Countries";
        }
        else {
            return "India";
        }
    }
}
