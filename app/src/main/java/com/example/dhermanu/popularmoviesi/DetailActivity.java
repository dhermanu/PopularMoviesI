package com.example.dhermanu.popularmoviesi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private String save_sort_state;
    public final static String EXTRA_STATE =
            "com.example.dhermanu.popularmoviesi.EXTRA_STATE";
    public final static String EXTRA_DATA =
            "com.example.dhermanu.popularmoviesi.EXTRA_DATA";

    public final static String EXTRA_TITLE =
            "com.example.dhermanu.popularmoviesi.EXTRA_TITLE";
    public final static String EXTRA_OVERVIEW =
            "com.example.dhermanu.popularmoviesi.EXTRA_OVERVIEW";
    public final static String EXTRA_POSTER =
            "com.example.dhermanu.popularmoviesi.EXTRA_POSTER";
    public final static String EXTRA_RATING =
            "com.example.dhermanu.popularmoviesi.EXTRA_RATING";
    public final static String EXTRA_RELEASEDATE =
            "com.example.dhermanu.popularmoviesi.EXTRA_RELEASEDATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent  = getIntent();
        Bundle extras = intent.getExtras();
        save_sort_state = extras.getString(EXTRA_STATE);

        setTitle("Popular Movies I");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //pass in state to the previous activity
            case android.R.id.home:
                Intent data = new Intent();
                data.putExtra(EXTRA_STATE, save_sort_state);
                finish();
                return(true);
        }

        return(super.onOptionsItemSelected(item));
    }

    public static class DetailActivityFragment extends Fragment {

        private String movieTitle, moviePoster, movieOverview, movieRelease;
        private int movieRating;

        private TextView movie_title, movie_overview, movie_release, movie_rating;
        private ImageView movie_poster;
        public DetailActivityFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            movie_title = (TextView) rootView.findViewById(R.id.movieTitle);
            movie_poster = (ImageView) rootView.findViewById(R.id.movieImage);
            movie_overview = (TextView) rootView.findViewById(R.id.movieOverview);
            movie_release = (TextView) rootView.findViewById(R.id.movieRelease);
            movie_rating = (TextView) rootView.findViewById(R.id.movieRating);

            Intent intent  = getActivity().getIntent();
            Movie movie = intent.getParcelableExtra(EXTRA_DATA);
            movieTitle = movie.getTitle();
            moviePoster = movie.getPoster();
            movieOverview = movie.getOverview();
            movieRelease = movie.getReleasedate();
            movieRating = movie.getRating();

            //update images and texts
            movie_title.setText(movieTitle);
            movie_overview.setText(movieOverview);
            movie_release.setText(movieRelease);
            movie_rating.setText(Integer.toString(movieRating));

            String imageURL = "http://image.tmdb.org/t/p/w185/" + moviePoster;

            Picasso.with(getActivity()).load(imageURL).fit()
                    .into(movie_poster);

            return rootView;
        }
    }
}
