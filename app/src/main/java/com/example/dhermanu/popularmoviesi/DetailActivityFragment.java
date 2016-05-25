package com.example.dhermanu.popularmoviesi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dhermanu.popularmoviesi.Interface.MovieAPI;
import com.example.dhermanu.popularmoviesi.Model.Movie;
import com.example.dhermanu.popularmoviesi.Model.ReviewList;
import com.example.dhermanu.popularmoviesi.Model.TrailerList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dhermanu on 5/24/16.
 */

public class DetailActivityFragment extends Fragment {

    public final static String EXTRA_DATA =
            "com.example.dhermanu.popularmoviesi.EXTRA_DATA";
    public final static String BASE_URL = "http://api.themoviedb.org/3/";
    public final static String TRAILER_TYPE = "videos";
    public final static String REVIEW_TYPE = "review";

    private String movieTitle, moviePoster, movieOverview, movieRelease;
    private Double movieRating;
    private MovieAPI movieAPI;

    private TextView movie_title, movie_overview, movie_release, movie_rating;
    private ImageView movie_poster;
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent  = getActivity().getIntent();
        Movie movie = intent.getParcelableExtra(EXTRA_DATA);
        setDetailLayout(movie, rootView);

        return rootView;
    }

    public void setDetailLayout(Movie movie, View view){
        movie_title = (TextView) view.findViewById(R.id.movieTitle);
        movie_poster = (ImageView) view.findViewById(R.id.movieImage);
        movie_overview = (TextView) view.findViewById(R.id.movieOverview);
        movie_release = (TextView) view.findViewById(R.id.movieRelease);
        movie_rating = (TextView) view.findViewById(R.id.movieRating);

        movieTitle = movie.getTitle();
        moviePoster = movie.getPosterPath();
        movieOverview = movie.getOverview();
        movieRelease = movie.getReleaseDate();
        movieRating = movie.getVoteAverage();

        //update images and texts
        movie_title.setText(movieTitle);
        movie_overview.setText(movieOverview);
        movie_release.setText(movieRelease);
        movie_rating.setText(Double.toString(movieRating) + "/10");

        String imageURL = "http://image.tmdb.org/t/p/w185/" + moviePoster;

        Picasso.with(getActivity()).load(imageURL).fit()
                .into(movie_poster);
    }

    public void getReviewTrailer(String getType){
        Gson gson =  new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        movieAPI = retrofit.create(MovieAPI.class);

        if(getType.equals(REVIEW_TYPE)){
            Call<ReviewList> reviewListCall = movieAPI.getReview(getType);
        }

        else if (getType.equals(TRAILER_TYPE)){
            Call<TrailerList> trailerListCall = movieAPI.getTrailer(getType);
        }


    }
}
