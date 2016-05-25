package com.example.dhermanu.popularmoviesi.Interface;

import com.example.dhermanu.popularmoviesi.BuildConfig;
import com.example.dhermanu.popularmoviesi.Model.MovieList;
import com.example.dhermanu.popularmoviesi.Model.ReviewList;
import com.example.dhermanu.popularmoviesi.Model.TrailerList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by dhermanu on 5/22/16.
 */
public interface MovieAPI {
    @GET("movie/{sort}?api_key=" + BuildConfig.MOVIEDB_API_KEY)
    Call<MovieList> getSortMovies(@Path("sort") String sort_by);

    @GET("movie/{id}/reviews?api_key=" + BuildConfig.MOVIEDB_API_KEY)
    Call<ReviewList> getReview(@Path("id") String id);

    @GET("movie/{id}/videos?api_key=" + BuildConfig.MOVIEDB_API_KEY)
    Call<TrailerList> getTrailer(@Path("id") String id);
}
