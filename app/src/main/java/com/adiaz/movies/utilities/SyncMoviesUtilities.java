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
			contentValuesTemp.put(MoviesContract.MovieEntity.COLUMN_TITLE, movie.getTitle());
			contentValuesTemp.put(MoviesContract.MovieEntity.COLUMN_POPULARITY, movie.getPopularity());
			contentValues[cont++] = contentValuesTemp;
		}
		return contentValues;
	}
}
