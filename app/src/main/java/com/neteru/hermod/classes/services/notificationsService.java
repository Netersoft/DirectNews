package com.neteru.hermod.classes.services;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.neteru.hermod.R;
import com.neteru.hermod.classes.ApiKeySelector;
import com.neteru.hermod.classes.Interfaces.ApiInterface;
import com.neteru.hermod.classes.models.News;
import com.neteru.hermod.classes.models.NewsResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.neteru.hermod.classes.services.NotificationUtilities.showNotification;

public class notificationsService extends Worker {
    private Context globalContext;
    private SharedPreferences preferences;
    private static Retrofit retrofit = null;
    private SharedPreferences.Editor editor;
    private NotificationManager notificationManager;
    private List<News> notifNews = new ArrayList<>();
    private static final String BASE_URL = "https://newsapi.org/v2/";

    public notificationsService(@NonNull Context context, @NonNull WorkerParameters workerParams){
        super(context, workerParams);

        globalContext = getApplicationContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(globalContext);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.d("NOTIF-SERVICE","Démarrage du service de notifications...");


        if (preferences.getBoolean("notif", true)){

            showRandomNotification();

        }

        return Result.success();
    }

    private void showRandomNotification(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String currentDay = simpleDateFormat.format(date),
               lastNotifDay = preferences.getString("notificationCurrentDate", "1970-01-01");

        if (!currentDay.equals(lastNotifDay)) {

            editor = preferences.edit();
            editor.putString("notificationCurrentDate", currentDay);
            editor.apply();

            if ((new Random().nextInt(7) + 1) == 7) {

                showNotification(globalContext, notificationManager, globalContext.getResources().getString(R.string.app_name), globalContext.getString(R.string.follow_the_main_facts_str), null);

            } else {
                final int notifNb = Integer.valueOf(preferences.getString("notifNb", "3")) <= 10 ? Integer.valueOf(preferences.getString("notifNb", "3")) : 3;

                ApiInterface apiService = getClient().create(ApiInterface.class);
                Call<NewsResponse> call = apiService.getNews(ApiKeySelector.getInstance().getKey(), preferences.getString("lang",
                        Locale.getDefault().getLanguage().equals("fr") ? "fr" : "us"), "general",
                        notifNb);

                call.enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {

                        if (response.body() == null) return;

                        notifNews = response.body().getArticles();

                        Log.d("JSON", "notifNews >>> " + notifNews);

                        String frequency = preferences.getString("notifFrequency", "0");
                        int notifCursor = preferences.getInt("notifCursor", 0);

                        switch (frequency) {
                            case "0":
                                for (int i = 0; i < notifNb; i++) {
                                    showNotification(globalContext, notificationManager, notifNews.get(i).getTitle(), notifNews.get(i).getDescription(), notifNews.get(i).getUrl());
                                }
                                break;

                            case "1":
                                if (notifCursor == 2) {

                                    for (int i = 0; i < notifNb; i++) {
                                        showNotification(globalContext, notificationManager, notifNews.get(i).getTitle(), notifNews.get(i).getDescription(), notifNews.get(i).getUrl());
                                    }
                                    editor.putInt("notifCursor", 0);

                                } else {
                                    editor.putInt("notifCursor", notifCursor + 1);
                                }
                                editor.apply();
                                break;

                            case "2":
                                if (notifCursor == 7) {

                                    for (int i = 0; i < notifNb; i++) {
                                        showNotification(globalContext, notificationManager, notifNews.get(i).getTitle(), notifNews.get(i).getDescription(), notifNews.get(i).getUrl());
                                    }
                                    editor.putInt("notifCursor", 0);

                                } else {
                                    editor.putInt("notifCursor", notifCursor + 1);
                                }
                                editor.apply();
                                break;
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                        // Log error here since request failed
                        Log.e("JSON", t.toString());
                    }
                });

            }
        }
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
