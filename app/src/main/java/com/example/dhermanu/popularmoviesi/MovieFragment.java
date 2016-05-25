package com.example.dhermanu.popularmoviesi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.dhermanu.popularmoviesi.Interface.MovieAPI;
import com.example.dhermanu.popularmoviesi.Model.MovieList;
import com.example.dhermanu.popularmoviesi.Model.Movie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieFragment extends Fragment {

    private MovieAdapter movieListAdapter;

    private String POPULAR_MOVIES = "popular";
    private String TOP_RATED_MOVIES = "top_rated";
    private String SORT_MOVIES_BY;
    private MovieAPI movieAPI;

    private Menu optionsMenu;
    private ArrayList<Movie> movieListSaved = null;

    // intent extras to pass in to the next activity
    public final static String EXTRA_DATA =
            "com.example.dhermanu.popularmoviesi.EXTRA_DATA";
    public final static String EXTRA_STATE =
            "com.example.dhermanu.popularmoviesi.EXTRA_STATE";
    public final static String SAVED_STATE =
            "com.example.dhermanu.popularmoviesi.SAVED_STATE";
    public final static String SAVED_MOVIES =
            "com.example.dhermanu.popularmoviesi.SAVED_MOVIES";

    public MovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);

        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            SORT_MOVIES_BY = extras.getString(EXTRA_STATE);
        }
        else
            SORT_MOVIES_BY = POPULAR_MOVIES;

        GridView gridView = (GridView) rootview.findViewById(R.id.grid_view_movies);
        movieListAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());
        gridView.setAdapter(movieListAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movieSelected = (Movie) movieListAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(EXTRA_DATA, movieSelected);
                intent.putExtra(EXTRA_STATE, SORT_MOVIES_BY);
                startActivity(intent);
            }
        });

        //restore previous state
        if(savedInstanceState != null)
        {
            SORT_MOVIES_BY = savedInstanceState.getString(SAVED_STATE);
            movieListSaved = savedInstanceState.getParcelableArrayList(SAVED_MOVIES);
            movieListAdapter.addList(movieListSaved); //update movie list
            updateOptionsMenu(); //update the state
        }

        // execute network operation
        else
            updateMovies(SORT_MOVIES_BY);

        updateMovies(SORT_MOVIES_BY);

        return rootview;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(movieListSaved != null) {
            //save state and movie lists
            outState.putParcelableArrayList(SAVED_MOVIES,movieListSaved);
            outState.putString(SAVED_STATE, SORT_MOVIES_BY);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie_fragment, menu);
        optionsMenu = menu;
    }

    //helper function to update the option menu
    private void updateOptionsMenu(){
        if (optionsMenu != null) {
            onPrepareOptionsMenu(optionsMenu);
        }
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if(SORT_MOVIES_BY.equals(POPULAR_MOVIES))
           menu.findItem(R.id.action_popular).setChecked(true);
        else if(SORT_MOVIES_BY.equals(TOP_RATED_MOVIES))
            menu.findItem(R.id.action_toprated).setChecked(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_popular) {
            SORT_MOVIES_BY = POPULAR_MOVIES;
            updateMovies(POPULAR_MOVIES);
            item.setChecked(true);
            return true;
        }
        if (id == R.id.action_toprated) {
            SORT_MOVIES_BY = TOP_RATED_MOVIES;
            updateMovies(TOP_RATED_MOVIES);
            item.setChecked(true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // start connection
    private void updateMovies(String sortBy){
        final String BASE_URL = "http://api.themoviedb.org/3/";
        Gson gson =  new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        movieAPI = retrofit.create(MovieAPI.class);

        Call<MovieList> movieListCall = movieAPI.getSortMovies(sortBy);

        movieListCall.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                List<Movie> movieList = response.body().getResults();

                if(movieList != null)
                {
                    movieListSaved = new ArrayList<>();
                    movieListAdapter.clear();
                    for (Movie movie :  movieList) {
                        movieListAdapter.add(movie);
                        movieListSaved.add(movie);
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {

            }
        });
    }
}
