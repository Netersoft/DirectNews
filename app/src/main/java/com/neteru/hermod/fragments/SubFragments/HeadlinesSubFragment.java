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

public class HeadlinesSubFragment extends Fragment {
    private NavigationView navigationView;
    private NewsLoader newsLoader;
    private Activity activity;
    private int position;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_headlines_sub, container, false);

        if(getActivity() != null){ activity = getActivity(); }

        SwipeRefreshLayout refreshLayout = root.findViewById(R.id.swipeRefresh);
        RecyclerView recyclerView = root.findViewById(R.id.recycler);
        DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
        ProgressBar loadMore = activity.findViewById(R.id.progress);
        navigationView = activity.findViewById(R.id.nav_view);

        String keywords = EMPTY;
        switch (position){

            case 0:
                keywords = "general";
                break;

            case 1:
                keywords = "afrique OR africa";
                break;

            case 2:
                keywords = "europe OR allemagne OR autriche OR belgique OR bulgarie OR chypre OR croatie" +
                           " OR danemark OR espagne OR estonie OR finlande OR france OR grèce OR hongrie" +
                           " OR irlande OR italie OR lettonie OR lituanie OR luxembourg OR malte OR pays-bas" +
                           " OR pologne OR portugal OR république tchèque OR roumanie OR royaume-unie OR slovaquie" +
                           " OR slovénie OR suède OR europa OR germany OR belgium OR bulgaria OR croatia" +
                           " OR spain OR estonia OR finland OR france OR grece OR hongria" +
                           " OR irland OR italia OR lettonia OR lituania OR holland" +
                           " OR polonia OR roumania OR england OR slovenia OR cnrs";
                break;

            case 3:
                keywords = "amerique OR u.s.a OR trump OR maison blanche OR etats unis OR brésil OR argentine OR californie OR new york OR atlanta OR texas " +
                           "OR america OR white house OR united states OR brasilia OR argentina OR california OR silicon valley OR boston OR ohio OR alabama " +
                           "OR alaska OR cia OR nsa OR fbi OR nasa";
                break;

            case 4:
                keywords = "asie OR chine OR japon OR corée OR inde OR pakistan OR syrie OR iran OR russie OR vietnam OR afghanistan OR irak OR israël OR palestine OR thailande" +
                           " OR asia OR china OR japan OR korea OR india OR syria OR russia OR israel OR thailand OR iraq";
                break;

        }

        newsLoader = new NewsLoader(getContext(), keywords, position == 0 ? 0 : 1, refreshLayout, recyclerView, drawer, loadMore);
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
