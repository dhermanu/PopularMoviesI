package com.example.dhermanu.popularmoviesi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.dhermanu.popularmoviesi.Model.Movie;

public class MainActivity extends AppCompatActivity implements MovieFragment.CallbackTablet{

    private boolean mTwoPane;
    public final static String EXTRA_DATA =
            "com.example.dhermanu.popularmoviesi.EXTRA_DATA";
    public final static String EXTRA_STATE =
            "com.example.dhermanu.popularmoviesi.EXTRA_STATE";
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
//            if (savedInstanceState == null) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.weather_detail_container, new DetailActivityFragment(),
//                                DETAILFRAGMENT_TAG)
//                        .commit();
//            }
        }

        else {
            mTwoPane = false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Movie movie) {
        if(mTwoPane){
            Bundle args = new Bundle();
            args.putParcelable(EXTRA_DATA, movie);

            Log.v("Movie Title is", movie.getTitle());

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        }

        else{
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(EXTRA_DATA, movie);
            startActivity(intent);
        }
    }
}
