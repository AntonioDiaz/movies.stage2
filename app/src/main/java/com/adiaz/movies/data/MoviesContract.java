package com.adiaz.movies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by toni on 08/02/2017.
 */

public class MoviesContract {

	public static final String AUTHORITY = "com.adiaz.movies";
	public static final Uri BASE_CONTENT = Uri.parse("content://" + AUTHORITY);
	public static final String PATH_MOVIES = "movies";

	public static final class MovieEntity implements BaseColumns {

		public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_MOVIES).build();
		public static final String TABLE_NAME = "movies";
		public static final String COLUMN_TITLE = "column_title";
		public static final String COLUMN_POPULARITY = "column_popularity";
	}
}
