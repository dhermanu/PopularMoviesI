package com.example.dhermanu.popularmoviesi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

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
    public final static String EXTRA_STATE =
            "com.example.dhermanu.popularmoviesi.EXTRA_STATE";

    private String save_sort_state;

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
    private boolean mFavMovie = false;

    private FloatingActionButton fab;

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
        Bundle args = getArguments();

        if(rootView.findViewById(R.id.nested_view) != null){
            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        final Movie movie = args.getParcelable(EXTRA_DATA);

        final CollapsingToolbarLayout templayout = collapsingToolbarLayout;
        save_sort_state = intent.getStringExtra(EXTRA_STATE);

        setDetailLayout(movie, rootView);
        collapsingToolbarLayout = templayout;
        collapsingToolbarLayout.setTitle(movieTitle);

        fab = (FloatingActionButton) rootView.findViewById(R.id.favorite_fab);


        getReviewTrailer(TRAILER_TYPE, movie.getId().toString());
        getReviewTrailer(REVIEW_TYPE, movie.getId().toString());

        rvTrailer = (RecyclerView) rootView.findViewById(R.id.recycle_movie);
        rvTrailer.setLayoutManager(new LinearLayoutManager(getActivity()));

        SharedPreferences sharedPreferences
                = getActivity().getSharedPreferences("CheckFav", Context.MODE_PRIVATE);

        if(sharedPreferences.contains(movie.getId().toString())){
            fab.setImageResource(R.drawable.ic_favorite_white_24dp);
            mFavMovie = true;
         }

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(mFavMovie){
                    fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                    Toast.makeText
                            (getActivity(), "Movie removed from favorites", Toast.LENGTH_SHORT)
                            .show();
                    removeFavorite(movie);
                    mFavMovie = false;
                }

                else{
                    fab.setImageResource(R.drawable.ic_favorite_white_24dp);
                    Toast.makeText
                            (getActivity(), "Movie saved to favorites", Toast.LENGTH_SHORT)
                            .show();
                    saveFavorite(movie);
                    mFavMovie = true;
                }
            }
        });

        return rootView;
    }

    public void saveFavorite(Movie movie){
        SharedPreferences sharedPreferences
                = getActivity().getSharedPreferences("FavMovie", Context.MODE_PRIVATE);
        SharedPreferences pref
                = getActivity().getSharedPreferences("CheckFav", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(movie);

        editor.putString(movie.getId().toString(), json);
        editor.commit();

        SharedPreferences.Editor editList = pref.edit();
        editList.putBoolean(movie.getId().toString(), true);
        editList.commit();
    }

    public void removeFavorite(Movie movie){
        SharedPreferences sharedPreferences
                = getActivity().getSharedPreferences("FavMovie", Context.MODE_PRIVATE);
        SharedPreferences pref
                = getActivity().getSharedPreferences("CheckFav", Context.MODE_PRIVATE);

        String key = movie.getId().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();

        SharedPreferences.Editor editList = pref.edit();
        editList.remove(key);
        editList.commit();
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
