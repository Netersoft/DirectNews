package com.neteru.hermod.fragments.MainFragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
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

import java.util.Locale;

public class UneFragment extends Fragment {
    private NavigationView navigationView;
    private NewsLoader newsLoader;
    private Activity activity;
    private String sources;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_une, container, false);

        if (getActivity() != null){ activity = getActivity(); }

        SwipeRefreshLayout refreshLayout = root.findViewById(R.id.swipeRefresh);
        RecyclerView recyclerView = root.findViewById(R.id.recycler);
        DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
        ProgressBar loadMore = activity.findViewById(R.id.progress);
        navigationView = activity.findViewById(R.id.nav_view);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        switch (preferences.getString("lang", Locale.getDefault().getLanguage().equals("fr") ? "fr" : "us")){

            case "fr":
                sources = "les-echos,le-monde,liberation";
                break;

            case "us":
                sources = "new-york-magazine,time,usa-today,the-washington-post,the-washington-times," +
                          "the-verge,the-wall-street-journal,the-telegraph,the-new-york-times,the-huffington-post," +
                          /* "the-guardian-uk,the-economist,techradar,techcrunch,nbc-news,mtv-news,mtv-news-uk," + */
                          "mtv-news,national-geographic,google-news-uk,fox-news,financial-times,cnn,cbs-news,cnbc,daily-mail," +
                          "bbc-news,bbc-sport,bloomberg";
                break;

            case "de":
                sources = "breitbart-news,der-tagesspiegel,die-zeit,gruenderszene,handelsblatt,goteborgs-posten," +
                          "spiegel-online,wirtschafts-woche,aftenposten";
                break;

            case "es":
                sources = "cnn-es,el-mundo,globo,la-gaceta,la-nacion,marca,politico,google-news-it";
                break;

            case "pt":
                sources = "cnn-es,el-mundo,globo,la-gaceta,la-nacion,marca,politico,google-news-it";
                break;
        }

        newsLoader = new NewsLoader(getContext(), sources, 2, refreshLayout, recyclerView, drawer, loadMore);
        newsLoader.loadNews();

        return root;
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
