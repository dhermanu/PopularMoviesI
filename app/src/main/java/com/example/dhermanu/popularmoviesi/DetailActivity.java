package com.example.dhermanu.popularmoviesi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        save_sort_state = intent.getStringExtra(EXTRA_STATE);
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
