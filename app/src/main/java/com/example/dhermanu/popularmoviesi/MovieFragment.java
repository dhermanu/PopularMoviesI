package com.example.dhermanu.popularmoviesi;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.dhermanu.popularmoviesi.Interface.MovieAPI;
import com.example.dhermanu.popularmoviesi.Model.MovieData;
import com.example.dhermanu.popularmoviesi.Model.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
        movieListAdapter = new MovieAdapter(getActivity(), new ArrayList<Result>());
        gridView.setAdapter(movieListAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               /* Result movieSelected = (Result) movieListAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(EXTRA_DATA, movieSelected);
                intent.putExtra(EXTRA_STATE, SORT_MOVIES_BY);
                startActivity(intent);*/
            }
        });



        /*Intent intent = getActivity().getIntent();
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
            updateMovies(SORT_MOVIES_BY);*/

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

        Call<MovieData> movieDataCall = movieAPI.getSortMovies(sortBy);

        movieDataCall.enqueue(new Callback<MovieData>() {
            @Override
            public void onResponse(Call<MovieData> call, Response<MovieData> response) {
                List<Result> test = response.body().getResults();

                if(test != null)
                {
                    movieListAdapter.clear();
                    for (Result res :  test) {
                        movieListAdapter.add(res);
                    }
                }

            }

            @Override
            public void onFailure(Call<MovieData> call, Throwable t) {
                Log.v("HELOOO", "UUUUUUH");
            }
        });
    }

    //fetch movies task
    public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        private List<Movie> getMoviesDataFromJson(String movieDataStr) throws JSONException{

            final String OWM_RESULT = "results";

            JSONObject jsonMovieObj = new JSONObject(movieDataStr);
            JSONArray jsonMovieArr = jsonMovieObj.getJSONArray(OWM_RESULT);

            List<Movie> listMovie = new ArrayList<>();
            for(int i = 0; i < jsonMovieArr.length(); i++){
                JSONObject getMovieDesc = jsonMovieArr.getJSONObject(i);
                Movie parseMovieModel = new Movie(getMovieDesc);
                listMovie.add(parseMovieModel);
            }

            return listMovie;
        }

        @Override
        protected List<Movie> doInBackground(String... voids) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String BASE_URL = "http://api.themoviedb.org/3/movie";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(voids[0])
                        .appendQueryParameter("api_key", BuildConfig.MOVIEDB_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
            }

            catch (IOException e) {
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            }

            finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }

            try {
                return getMoviesDataFromJson(movieJsonStr);
            }
            catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            Log.v(LOG_TAG, movieJsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            if (movies != null) {
                movieListAdapter.clear();
                movieListSaved = new ArrayList<>();
                if (movieListAdapter != null) {
                    for(Movie movie : movies) {
                        movieListAdapter.add(movie);
                       // movieListSaved.add(movie); // save movies to restore state
                    }
                }
            }

        }
    }
}
