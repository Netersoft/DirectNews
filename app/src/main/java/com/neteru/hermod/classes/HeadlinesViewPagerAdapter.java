package com.neteru.hermod.classes;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.neteru.hermod.fragments.SubFragments.HeadlinesSubFragment;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class HeadlinesViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public HeadlinesViewPagerAdapter(FragmentManager manager) { super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT); }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(HeadlinesSubFragment fragment, String title, int pos) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);

        fragment.setPosition(pos);
    }

    @Override
    public CharSequence getPageTitle(int position) { return mFragmentTitleList.get(position); }

}
