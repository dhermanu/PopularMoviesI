package com.example.dhermanu.popularmoviesi.Interface;

import com.example.dhermanu.popularmoviesi.BuildConfig;
import com.example.dhermanu.popularmoviesi.Model.MovieData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by dhermanu on 5/22/16.
 */
public interface MovieAPI {
    @GET("movie/{sort}?api_key=" + BuildConfig.MOVIEDB_API_KEY)
    Call<MovieData> getSortMovies(@Path("sort") String sort_by);
}
