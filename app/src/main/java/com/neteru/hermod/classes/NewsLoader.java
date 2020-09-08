package com.neteru.hermod.classes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.neteru.hermod.R;
import com.neteru.hermod.activities.ui_utilities.ImageViewActivity;
import com.neteru.hermod.classes.Interfaces.ApiInterface;
import com.neteru.hermod.classes.adapters.NewsAdapter;
import com.neteru.hermod.classes.models.News;
import com.neteru.hermod.classes.models.NewsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.neteru.hermod.activities.ui_utilities.ImageViewActivity.URL;
import static com.neteru.hermod.classes.AppUtilities.openWebview;

@SuppressWarnings("unused")
public class NewsLoader{
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar loadMore;
    private NewsAdapter adapter;
    private List<News> news = new ArrayList<>();
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://newsapi.org/v2/";
    private ApiInterface apiService;
    private final static String TAG = "NewsLoader";
    private String code, section;
    private int nb, indice;
    private Context context;
    private Call<NewsResponse> call;
    
    public NewsLoader(Context ctx, String s, int i, SwipeRefreshLayout srl, RecyclerView rv, DrawerLayout dl, ProgressBar pb){
        
        context = ctx;
        section = s;
        refreshLayout = srl;
        recyclerView = rv;
        loadMore = pb;
        indice = i;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        nb = Integer.valueOf(preferences.getString("nb","42"));
        code = preferences.getString("lang", Locale.getDefault().getLanguage().equals("fr") ? "fr" : "us");

        apiService = getClient().create(ApiInterface.class);

        LinearLayoutManager lmanager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(lmanager);

        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews();
            }
        });
        
    }

    public void loadNews() {
        loadMore.setVisibility(View.VISIBLE);

        if (!Connectivity.getInstance(context).isOnline()){
            Toast.makeText(context, R.string.error_connection, Toast.LENGTH_SHORT).show();
            loadMore.setVisibility(View.GONE);
        }

        if (refreshLayout.isRefreshing()){
            loadMore.setVisibility(View.GONE);
        }

        switch (indice){
            case 0:
                call = apiService.getNews(ApiKeySelector.getInstance().getKey(), code, section, nb);
                break;
            case 1:
                call = apiService.getArticles(ApiKeySelector.getInstance().getKey(), section, code.equals("us") ? "en" : code, "publishedAt", nb);
                break;
            case 2:
                call = apiService.getNews(ApiKeySelector.getInstance().getKey(), section, nb);
                break;
        }

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {

                if (response.body() == null) return;

                news = response.body().getArticles();

                if (news == null){ Toast.makeText(context, R.string.no_news_str, Toast.LENGTH_SHORT).show(); }

                adapter = new NewsAdapter(news, R.layout.news_model, context, new NewsAdapter.NewsAdapterListener() {
                    @Override
                    public void onNewsSelected(String source, String url) {

                        openWebview(context, url);

                    }

                    @Override
                    public void onNewsPictureSelected(String title, String img) {

                        context.startActivity(new Intent(context, ImageViewActivity.class).putExtra(URL, img));

                    }

                });

                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();

                loadMore.setVisibility(View.GONE);
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
                Log.d(TAG,code + section +">>> "+ news);
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {

                Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();

                loadMore.setVisibility(View.GONE);
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
            }
        });

    }

    private static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
