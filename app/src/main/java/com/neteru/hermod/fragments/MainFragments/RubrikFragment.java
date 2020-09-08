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
import com.neteru.hermod.classes.RubrikViewPagerAdapter;
import com.neteru.hermod.fragments.SubFragments.RubrikSubFragment;

import static com.neteru.hermod.classes.Constants.EMPTY;

public class RubrikFragment extends Fragment {
    private Activity activity;
    private NavigationView navigationView;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_rubrik, container, false);

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
            case "business":
                viewPager.setCurrentItem(0, true);
                break;

            case "science":
                viewPager.setCurrentItem(1, true);
                break;

            case "health":
                viewPager.setCurrentItem(2, true);
                break;

            case "technology":
                viewPager.setCurrentItem(3, true);
                break;

            case "entertainment":
                viewPager.setCurrentItem(4, true);
                break;

            case "sports":
                viewPager.setCurrentItem(5, true);
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
                        navigationView.getMenu().getItem(2).getSubMenu().getItem(0).setChecked(true);
                        break;
                    case 1:
                        uncheckedAllItem();
                        navigationView.getMenu().getItem(2).getSubMenu().getItem(1).setChecked(true);
                        break;
                    case 2:
                        uncheckedAllItem();
                        navigationView.getMenu().getItem(2).getSubMenu().getItem(2).setChecked(true);
                        break;
                    case 3:
                        uncheckedAllItem();
                        navigationView.getMenu().getItem(2).getSubMenu().getItem(3).setChecked(true);
                        break;
                    case 4:
                        uncheckedAllItem();
                        navigationView.getMenu().getItem(2).getSubMenu().getItem(4).setChecked(true);
                        break;
                    case 5:
                        uncheckedAllItem();
                        navigationView.getMenu().getItem(2).getSubMenu().getItem(5).setChecked(true);
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
        RubrikViewPagerAdapter adapter = new RubrikViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new RubrikSubFragment(), getString(R.string.economy_str), 0);
        adapter.addFragment(new RubrikSubFragment(), getString(R.string.science_str), 1);
        adapter.addFragment(new RubrikSubFragment(), getString(R.string.health_str), 2);
        adapter.addFragment(new RubrikSubFragment(), getString(R.string.technology_str), 3);
        adapter.addFragment(new RubrikSubFragment(), getString(R.string.culture_str), 4);
        adapter.addFragment(new RubrikSubFragment(), getString(R.string.sport_str), 5);

        viewPager.setAdapter(adapter);

    }

}
