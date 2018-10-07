package com.my.shishir.demoapp.api;

import com.my.shishir.demoapp.model.MoviesData;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

interface RetroInterface {

    @GET("3/movie/popular")
    Observable<MoviesData> getData(@Query("api_key") String apiKey,
                                   @Query("language") String language,
                                   @Query("page") int page);
}
