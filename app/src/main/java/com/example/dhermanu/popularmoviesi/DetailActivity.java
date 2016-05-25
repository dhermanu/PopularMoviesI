package com.example.dhermanu.popularmoviesi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class DetailActivity extends AppCompatActivity {

    private String save_sort_state;
    public final static String EXTRA_STATE =
            "com.example.dhermanu.popularmoviesi.EXTRA_STATE";

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
}
