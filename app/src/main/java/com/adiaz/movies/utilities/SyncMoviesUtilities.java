package com.adiaz.movies.utilities;

import android.content.ContentValues;

import com.adiaz.movies.data.MoviesContract;
import com.adiaz.movies.entities.Movie;

import java.util.List;

/**
 * Created by toni on 09/02/2017.
 */

public class SyncMoviesUtilities {

	public static final ContentValues[] moviesList2ContentValuesArray(List<Movie> moviesList) {
		ContentValues[] contentValues = new ContentValues[moviesList.size()];
		int cont = 0;
		for (Movie movie : moviesList) {
			ContentValues contentValuesTemp = new ContentValues();
			contentValuesTemp.put(MoviesContract.MovieEntity.COLUMN_ID_ORIGINAL, movie.getId());
			contentValuesTemp.put(MoviesContract.MovieEntity.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
			contentValuesTemp.put(MoviesContract.MovieEntity.COLUMN_POSTER_PATH, movie.getPosterPath());
			contentValuesTemp.put(MoviesContract.MovieEntity.COLUMN_OVERVIEW, movie.getOverview());
			contentValuesTemp.put(MoviesContract.MovieEntity.COLUMN_USER_RATING, movie.getVoteAverage());
			contentValuesTemp.put(MoviesContract.MovieEntity.COLUMN_RELEASE_DATE, movie.getReleaseDate());
			contentValues[cont++] = contentValuesTemp;
		}
		return contentValues;
	}
}
