package com.neteru.hermod.fragments.MainFragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.neteru.hermod.R;
import com.neteru.hermod.classes.HeadlinesViewPagerAdapter;
import com.neteru.hermod.fragments.SubFragments.HeadlinesSubFragment;

import static com.neteru.hermod.classes.Constants.EMPTY;

public class HeadlinesFragment extends Fragment {
    private Activity activity;
    private NavigationView navigationView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_headlines, container, false);

        if (getActivity() != null){ activity = getActivity(); }

        TabLayout tabLayout = root.findViewById(R.id.tabs);
        ViewPager viewPager = root.findViewById(R.id.viewpager);
        navigationView = activity.findViewById(R.id.nav_view);

        setViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        Bundle bundle = getArguments();
        String section;
        if (bundle != null){
            section = bundle.getString("rubrik",EMPTY);
        }else {
            section = EMPTY;
        }

        switch (section){
            case "general":
                viewPager.setCurrentItem(0, true);
                break;
            case "africa":
                viewPager.setCurrentItem(1, true);
                break;
            case "europa":
                viewPager.setCurrentItem(2, true);
                break;
            case "america":
                viewPager.setCurrentItem(3, true);
                break;
            case "asia":
                viewPager.setCurrentItem(4, true);
                break;
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position){
                    case 0:
                        uncheckedAllItem();
                        navigationView.getMenu().getItem(1).getSubMenu().getItem(0).setChecked(true);
                        break;
                    case 1:
                        uncheckedAllItem();
                        navigationView.getMenu().getItem(1).getSubMenu().getItem(1).setChecked(true);
                        break;
                    case 2:
                        uncheckedAllItem();
                        navigationView.getMenu().getItem(1).getSubMenu().getItem(2).setChecked(true);
                        break;
                    case 3:
                        uncheckedAllItem();
                        navigationView.getMenu().getItem(1).getSubMenu().getItem(3).setChecked(true);
                        break;
                    case 4:
                        uncheckedAllItem();
                        navigationView.getMenu().getItem(1).getSubMenu().getItem(4).setChecked(true);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return root;
    }

    private void uncheckedAllItem(){
        for(int i = 0;i < 5; i++ ){
            if (i == 0){

                navigationView.getMenu().getItem(i).setChecked(false);
                navigationView.getMenu().getItem(3).setChecked(false);
                navigationView.getMenu().getItem(4).setChecked(false);

            }else if (i == 1){

                for (int y = 0; y < 5; y++){
                    navigationView.getMenu().getItem(i).getSubMenu().getItem(y).setChecked(false);
                }

            }else if (i == 2){

                for (int y = 0; y < 6; y++){
                    navigationView.getMenu().getItem(i).getSubMenu().getItem(y).setChecked(false);
                }

            }
        }
    }

    private void setViewPager(ViewPager viewPager){
        HeadlinesViewPagerAdapter adapter = new HeadlinesViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new HeadlinesSubFragment(), getString(R.string.world_str), 0);
        adapter.addFragment(new HeadlinesSubFragment(), getString(R.string.africa_str), 1);
        adapter.addFragment(new HeadlinesSubFragment(), getString(R.string.europa_str), 2);
        adapter.addFragment(new HeadlinesSubFragment(), getString(R.string.america_str), 3);
        adapter.addFragment(new HeadlinesSubFragment(), getString(R.string.asia_str), 4);

        viewPager.setAdapter(adapter);

    }

}
