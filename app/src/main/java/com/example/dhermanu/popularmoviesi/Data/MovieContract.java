package com.example.dhermanu.popularmoviesi.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dhermanu on 5/23/16.
 */
public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.dhermanu.popularmoviesi";
    public static final Uri  BASE_CONTENT_Uri = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_Uri.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
    }
}
