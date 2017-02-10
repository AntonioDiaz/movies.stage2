package com.adiaz.movies.utilities;

import com.adiaz.movies.data.MoviesContract;

import java.util.concurrent.TimeUnit;

/**
 * Created by toni on 09/02/2017.
 */

public class MoviesConstants {

	public static final long MINUTE_IN_MILLIS = TimeUnit.MINUTES.toMillis(1);

	public static final String[] MAIN_MOVIES_PROJECTION = {
			MoviesContract.MovieEntity._ID,
			MoviesContract.MovieEntity.COLUMN_ORIGINAL_TITLE,
			MoviesContract.MovieEntity.COLUMN_POSTER_PATH,
			MoviesContract.MovieEntity.COLUMN_OVERVIEW,
			MoviesContract.MovieEntity.COLUMN_USER_RATING,
			MoviesContract.MovieEntity.COLUMN_RELEASE_DATE,
			MoviesContract.MovieEntity.COLUMN_IS_FAVORITE
	};

	public static final int INDEX_MOVIE_ID = 0;
	public static final int INDEX_MOVIE_ORIGINAL_TITLE = 1;
	public static final int INDEX_MOVIE_POSTER_PATH = 2;
	public static final int INDEX_MOVIE_OVERVIEW = 3;
	public static final int INDEX_MOVIE_USER_RATING = 4;
	public static final int INDEX_MOVIE_RELEASE_DATE = 5;
	public static final int INDEX_MOVIE_IS_FAVORITE = 6;

	public static final Integer FAVORITE_YES = 1;
	public static final Integer FAVORITE_NOT = 0;
}
