package com.example.dhermanu.popularmoviesi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dhermanu on 5/16/16.
 */
//create custom adapter for movie image
public class MovieAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;


    public MovieAdapter(Context context, ArrayList<Movie> imageURL) {
        super(context, R.layout.list_image_view,imageURL);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void addList(List<Movie> movieList){
        clear();
        for(Movie movies : movieList){
            add(movies);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.list_image_view, parent, false);
        }

        final Movie movie = (Movie) getItem(position);
        String image_url = "http://image.tmdb.org/t/p/w185" + movie.getPoster();

        Picasso
                .with(context)
                .load(image_url)
                .fit()
                .into((ImageView) convertView);

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }
}