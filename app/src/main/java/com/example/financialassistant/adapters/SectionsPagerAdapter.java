package com.example.financialassistant.adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.financialassistant.R;
import com.example.financialassistant.ui.main.Accounts_Fragment;
import com.example.financialassistant.ui.main.Converter_Fragment;
import com.example.financialassistant.ui.main.Main_Fragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2,
    R.string.tab_text_3};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new Main_Fragment();
            case 1: return new Accounts_Fragment();
            case 2: return new Converter_Fragment();
            default: return new Main_Fragment();
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
            title = mContext.getResources().getString(R.string.tab_text_1);
        else if (position == 1)
            title = mContext.getResources().getString(R.string.tab_text_2);
        else title = mContext.getResources().getString(R.string.tab_text_3);
        return title;
    }
}