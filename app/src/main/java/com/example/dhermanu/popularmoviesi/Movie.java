package com.example.dhermanu.popularmoviesi;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dhermanu on 5/11/16.
 */
public class Movie implements Parcelable{

    private final String JSON_ID = "id";
    private final String JSON_TITLE = "original_title";
    private final String JSON_POSTER = "poster_path";
    private final String JSON_OVERVIEW = "overview";
    private final String JSON_RATING = "vote_average";
    private final String JSON_RELEASEDATE = "release_date";

    private int id;
    private String title;
    private String poster;
    private String overview;
    private String releasedate;
    private int rating;

    public Movie() {

    }

    public Movie(JSONObject movieList) throws JSONException{
        this.id = movieList.getInt(JSON_ID);
        this.title = movieList.getString(JSON_TITLE);
        this.poster = movieList.getString(JSON_POSTER);
        this.overview = movieList.getString(JSON_OVERVIEW);
        this.releasedate = movieList.getString(JSON_RELEASEDATE);
        this.rating = movieList.getInt(JSON_RATING);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public int getRating() {
        return rating;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        poster = in.readString();
        overview = in.readString();
        releasedate = in.readString();
        rating = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(poster);
        parcel.writeString(overview);
        parcel.writeString(releasedate);
        parcel.writeInt(rating);
    }
}
