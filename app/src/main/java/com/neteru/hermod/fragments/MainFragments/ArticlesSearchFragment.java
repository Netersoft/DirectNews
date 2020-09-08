package com.neteru.hermod.fragments.MainFragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.neteru.hermod.R;
import com.neteru.hermod.activities.ArticlesSettingsActivity;
import com.neteru.hermod.activities.ui_utilities.ImageViewActivity;
import com.neteru.hermod.classes.ApiKeySelector;
import com.neteru.hermod.classes.Interfaces.ApiInterface;
import com.neteru.hermod.classes.LoadingDialog;
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

public class ArticlesSearchFragment extends Fragment {
    private Activity activity;
    private RecyclerView recyclerView;
    private LoadingDialog loadingDialog;
    private List<News> articles = new ArrayList<>() ;
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://newsapi.org/v2/";
    private Call<NewsResponse> call;
    private NewsAdapter adapter;
    private String domains, from, to, language, sortBy;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_articles_search, container, false);

        if (getActivity() != null){ activity = getActivity(); }

        recyclerView = root.findViewById(R.id.recycler);
        loadingDialog = new LoadingDialog(getContext());

        LinearLayoutManager lmanager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lmanager);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        domains = preferences.getString("domains", "");
        from = preferences.getString("from", "");
        to = preferences.getString("to", "");
        language = preferences.getString("language", Locale.getDefault().getLanguage().equals("fr") ? "fr" : "us");
        sortBy = preferences.getString("sortBy", "relevancy");

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        activity.getMenuInflater().inflate(R.menu.articles_search_menu, menu);

        MenuItem mSearch = menu.findItem(R.id.search);

        SearchView search = (SearchView) mSearch.getActionView();
        search.setQueryHint(getString(R.string.tap_your_search_str));
        search.setIconified(false);
        search.setIconifiedByDefault(true);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                loadingDialog.show();

                ApiInterface apiService = getClient().create(ApiInterface.class);
                call = apiService.getArticles(ApiKeySelector.getInstance().getKey(),query,language,sortBy,domains,from,to);

                call.enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {

                        articles = response.body() != null ? response.body().getArticles() : new ArrayList<News>();

                        adapter = new NewsAdapter(articles, R.layout.news_model, getContext(),new NewsAdapter.NewsAdapterListener(){

                            @Override
                            public void onNewsSelected(String source,String url){

                                openWebview(getContext(), url);

                            }

                            @Override
                            public void onNewsPictureSelected(String title, String img) {

                                startActivity(new Intent(getContext(), ImageViewActivity.class).putExtra(URL, img));

                            }

                        });

                        recyclerView.setAdapter(adapter);

                        loadingDialog.dismiss();
                        if (articles.isEmpty()){
                            Toast.makeText(getContext(), getString(R.string.no_result_str, query), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {

                        Log.e("JSON", t.toString());
                        loadingDialog.dismiss();
                        Toast.makeText(getContext(),R.string.error,Toast.LENGTH_SHORT).show();

                    }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.articles_settings) {
            Intent i = new Intent(getContext(), ArticlesSettingsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
