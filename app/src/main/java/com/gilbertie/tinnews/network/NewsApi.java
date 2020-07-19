package com.gilbertie.tinnews.network;

import com.gilbertie.tinnews.model.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// interface for your restful api service
// set request method
// set relative url
// set key-val pair in url
public interface NewsApi {

    @GET("top-headlines")
    Call<NewsResponse> getTopHeadlines(@Query("country") String country);

    @GET("everything")
    Call<NewsResponse> getEverything(@Query("q") String query, @Query("pageSize") int pageSize);
}