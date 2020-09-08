package com.neteru.hermod.fragments.MainFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.neteru.hermod.R;
import com.neteru.hermod.activities.ui_utilities.ImageViewActivity;
import com.neteru.hermod.classes.adapters.NewsAdapter;
import com.neteru.hermod.classes.databases.DbManager;
import com.neteru.hermod.classes.models.Article;

import java.util.ArrayList;
import java.util.List;

import static com.neteru.hermod.activities.ui_utilities.ImageViewActivity.URL;
import static com.neteru.hermod.classes.AppUtilities.openWebview;

public class ReadlaterFragment extends Fragment {
    private List<Article> articleNews = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recycler;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_readlater, container, false);

        recycler = root.findViewById(R.id.recycler);
        LinearLayoutManager lmanager = new LinearLayoutManager(getContext());
        refreshLayout = root.findViewById(R.id.swipeRefresh);
        recycler.setLayoutManager(lmanager);

        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadSavedNews();
            }
        });

        loadSavedNews();

        return root;
    }

    private void loadSavedNews(){

        DbManager dbManager = new DbManager(getContext());

        articleNews.clear();
        articleNews = dbManager.db_getArticleByFav(true);

        if (articleNews == null || articleNews.isEmpty()){

            articleNews = new ArrayList<>();
            Toast.makeText(getContext(), R.string.no_fav_str, Toast.LENGTH_SHORT).show();
        }

        if (refreshLayout.isRefreshing()){refreshLayout.setRefreshing(false);}

        NewsAdapter adapter = new NewsAdapter(articleNews, getContext(), R.layout.news_model, new NewsAdapter.NewsAdapterListener() {
            @Override
            public void onNewsSelected(String source, String url) {
                openWebview(getContext(), url);
            }

            @Override
            public void onNewsPictureSelected(String title, String img) {
                startActivity(new Intent(getContext(), ImageViewActivity.class).putExtra(URL, img));
            }
        });

        recycler.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

}
