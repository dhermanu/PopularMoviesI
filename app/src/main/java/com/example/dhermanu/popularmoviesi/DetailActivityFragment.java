package com.example.dhermanu.popularmoviesi;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dhermanu.popularmoviesi.Interface.MovieAPI;
import com.example.dhermanu.popularmoviesi.Model.Movie;
import com.example.dhermanu.popularmoviesi.Model.Review;
import com.example.dhermanu.popularmoviesi.Model.ReviewList;
import com.example.dhermanu.popularmoviesi.Model.Trailer;
import com.example.dhermanu.popularmoviesi.Model.TrailerList;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

    private YouTubePlayerFragment playerFragment;
    private ArrayList<Review> movieReviewSaved = null;
    private ArrayList<Trailer> movieTrailerSaved = null;

    private Trailer trailer;

    private String movieTitle, moviePoster, movieOverview, movieRelease;
    private Double movieRating;
    private MovieAPI movieAPI;

    private TextView movie_title, movie_overview, movie_release, movie_rating,
            movie_review, review_author;
    private ImageView movie_poster, movie_banner, movie_trailer;
    public TrailerAdapter trailerAdapter;
    public RecyclerView rvTrailer;
    ArrayAdapter<String> mMoviewAdapter;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_collapse, container, false);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsingtoolbar);

        Intent intent  = getActivity().getIntent();
        Movie movie = intent.getParcelableExtra(EXTRA_DATA);

        final CollapsingToolbarLayout templayout = collapsingToolbarLayout;
        setDetailLayout(movie, rootView);
        collapsingToolbarLayout = templayout;
        collapsingToolbarLayout.setTitle(movieTitle);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getReviewTrailer(TRAILER_TYPE, movie.getId().toString());
        getReviewTrailer(REVIEW_TYPE, movie.getId().toString());

        rvTrailer = (RecyclerView) rootView.findViewById(R.id.recycle_movie);
        rvTrailer.setLayoutManager(new LinearLayoutManager(getActivity()));


        return rootView;
    }


    public void setDetailLayout(Movie movie, View view){
        movie_title = (TextView) view.findViewById(R.id.movieTitle);
        movie_poster = (ImageView) view.findViewById(R.id.movieImage);
        movie_overview = (TextView) view.findViewById(R.id.movieOverview);
        movie_release = (TextView) view.findViewById(R.id.movieRelease);
        movie_rating = (TextView) view.findViewById(R.id.movieRating);
        movie_review = (TextView) view.findViewById(R.id.movieReview);
        review_author = (TextView) view.findViewById(R.id.reviewAuthor);

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
        movie_banner = (ImageView) view.findViewById(R.id.imageBanner);



        String imageURL = "http://image.tmdb.org/t/p/w185/" + moviePoster;
        String imageUR2L = "http://image.tmdb.org/t/p/w500/" + movie.getBackdropPath();

        Picasso.with(getActivity()).load(imageURL).fit()
                .into(movie_poster);
        Picasso.with(getActivity()).load(imageUR2L).fit()
                .into(movie_banner);
    }

    public void getReviewTrailer(String getType, String id){
        Gson gson =  new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        movieAPI = retrofit.create(MovieAPI.class);

        if(getType.equals(REVIEW_TYPE)){
            Call<ReviewList> reviewListCall = movieAPI.getReview(id);
            reviewListCall.enqueue(new Callback<ReviewList>() {
                @Override
                public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {
                    List<Review> reviewList = response.body().getResults();
                    if(reviewList.size() != 0) {
                        movie_review.setText((reviewList.get(0).getContent()));
                        review_author.setText(reviewList.get(0).getAuthor());
                    }
                    else
                        review_author.setText("No reviews yet for this movie.");


                }

                @Override
                public void onFailure(Call<ReviewList> call, Throwable t) {

                }
            });
        }

        else if (getType.equals(TRAILER_TYPE)){
            Call<TrailerList> trailerListCall = movieAPI.getTrailer(id);
            trailerListCall.enqueue(new Callback<TrailerList>() {
                @Override
                public void onResponse(Call<TrailerList> call, Response<TrailerList> response) {
                    List<Trailer> trailerList = response.body().getResults();
                    trailerAdapter = new TrailerAdapter(trailerList, getContext());
                    rvTrailer.setAdapter(trailerAdapter);
                }

                @Override
                public void onFailure(Call<TrailerList> call, Throwable t) {

                }
            });
        }


    }
}
