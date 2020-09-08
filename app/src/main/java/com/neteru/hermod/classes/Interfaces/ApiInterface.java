package com.neteru.hermod.classes.Interfaces;

import com.neteru.hermod.classes.models.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("everything")
    Call<NewsResponse> getArticles(@Query("apiKey") String apiKey,
                                   @Query("q") String q,
                                   @Query("language") String language,
                                   @Query("sortBy") String sortBy,
                                   @Query("domains") String domains,
                                   @Query("from") String from,
                                   @Query("to") String to);

    @GET("everything")
    Call<NewsResponse> getArticles(@Query("apiKey") String apiKey,
                                   @Query("q") String q,
                                   @Query("language") String language,
                                   @Query("sortBy") String sortBy,
                                   @Query("pageSize") int size);

    @GET("top-headlines")
    Call<NewsResponse> getNews(@Query("apiKey") String apiKey,
                               @Query("sources") String sources,
                               @Query("pageSize") int size);

    @GET("top-headlines")
    Call<NewsResponse> getNews(@Query("apiKey") String apiKey,
                               @Query("country") String country,
                               @Query("category") String category,
                               @Query("pageSize") int size);

}
