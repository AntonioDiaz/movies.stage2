package com.adiaz.movies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/** Created by toni on 08/02/2017. */

public class MoviesContract {

	public static final String AUTHORITY = "com.adiaz.movies";
	public static final Uri BASE_CONTENT = Uri.parse("content://" + AUTHORITY);
	public static final String PATH_MOVIES = "movies";

	public static final class MovieEntity implements BaseColumns {

		public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_MOVIES).build();

		public static final String TABLE_NAME = "movies";

		public static final String COLUMN_ID_ORIGINAL = "column_id_original";
		public static final String COLUMN_ORIGINAL_TITLE = "column_original_title";
		public static final String COLUMN_POSTER_PATH = "column_poster_path";
		public static final String COLUMN_OVERVIEW = "column_overview";
		public static final String COLUMN_USER_RATING = "column_user_rating";
		public static final String COLUMN_RELEASE_DATE = "column_release_date";
		public static final String COLUMN_IS_FAVORITE = "column_is_favorite";
		public static final String COLUMN_POPULARITY = "column_popularity";




	}
}
