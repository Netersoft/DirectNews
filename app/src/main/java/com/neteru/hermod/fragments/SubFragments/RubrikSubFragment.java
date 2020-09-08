package com.neteru.hermod.fragments.SubFragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.navigation.NavigationView;
import com.neteru.hermod.R;
import com.neteru.hermod.activities.MainActivity;
import com.neteru.hermod.classes.NewsLoader;
import com.neteru.hermod.fragments.MainFragments.ArticlesSearchFragment;
import com.neteru.hermod.fragments.MainFragments.ReadlaterFragment;

import static com.neteru.hermod.classes.Constants.EMPTY;

public class RubrikSubFragment extends Fragment {
    private NavigationView navigationView;
    private NewsLoader newsLoader;
    private Activity activity;
    private int position;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_rubrik_sub, container, false);

        if (getActivity() != null){ activity = getActivity(); }

        SwipeRefreshLayout refreshLayout = root.findViewById(R.id.swipeRefresh);
        RecyclerView recyclerView = root.findViewById(R.id.recycler);
        DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
        ProgressBar loadMore = activity.findViewById(R.id.progress);
        navigationView = activity.findViewById(R.id.nav_view);

        String keywords = EMPTY;
        switch (position){
            case 0:
                keywords = "business";
                break;

            case 1:
                keywords = "science";
                break;

            case 2:
                keywords = "health";
                break;

            case 3:
                keywords = "technology";
                break;

            case 4:
                keywords = "entertainment";
                break;

            case 5:
                keywords = "sports";
                break;
        }

        newsLoader = new NewsLoader(getContext(), keywords, 0, refreshLayout, recyclerView, drawer, loadMore);
        newsLoader.loadNews();

        return root;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        activity.getMenuInflater().inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        MainActivity mainActivity = (MainActivity) activity;

        mainActivity.setItemId(item.getItemId());

        switch (item.getItemId()){

            case R.id.action_refresh:
                newsLoader.loadNews();
                break;

            case R.id.action_fav:
                mainActivity.loadFragment(new ReadlaterFragment(), null);
                navigationView.getMenu().getItem(4).setChecked(true);
                mainActivity.setTitle(getString(R.string.read_later_str));
                break;

            case R.id.action_search:
                mainActivity.loadFragment(new ArticlesSearchFragment(), null);
                navigationView.getMenu().getItem(3).setChecked(true);
                mainActivity.setTitle(getString(R.string.articles_search_str));
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
