package com.adiaz.movies.loaders;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.adiaz.movies.BuildConfig;
import com.adiaz.movies.data.MoviesContract;
import com.adiaz.movies.entities.DbMoviesQuery;
import com.adiaz.movies.entities.Movie;
import com.adiaz.movies.utilities.MoviesConstants;
import com.adiaz.movies.utilities.PreferencesUtilities;
import com.adiaz.movies.utilities.SyncMoviesUtilities;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by toni on 09/02/2017.
 */

public class MoviesAsyncTask extends AsyncTask<Void, Void, Void> {

	private static final String TAG = MoviesAsyncTask.class.getSimpleName();
	private Context mContext;
	private MoviesAsyncTask.FetchMoviesListener mFetchMoviesListener;

	public MoviesAsyncTask(Context mContext, FetchMoviesListener mFetchMoviesListener) {
		this.mContext = mContext;
		this.mFetchMoviesListener = mFetchMoviesListener;
	}

	public interface FetchMoviesListener {
		public void onFetchMoviesStarted();
		public void onFetchMoviesFinished();
	}

	@Override
	protected void onPreExecute() {
		mFetchMoviesListener.onFetchMoviesStarted();
	}

	@Override
	protected void onPostExecute(Void aVoid) {
		mFetchMoviesListener.onFetchMoviesFinished();
	}

	@Override
	protected Void doInBackground(Void... voids) {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(MoviesConstants.BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		MoviesRestApi moviesRestApi = retrofit.create(MoviesRestApi.class);
		// TODO: 09/02/2017 delete all but favorites

		String selection = MoviesContract.MovieEntity.COLUMN_IS_FAVORITE + "!=?";
		String[] args = new String[]{MoviesConstants.FAVORITE_YES.toString()};
		mContext.getContentResolver().delete(MoviesContract.MovieEntity.CONTENT_URI, selection, args);
		try {
			int pages = PreferencesUtilities.getPagesToQuery(mContext);
			for (int i = 0; i < pages; i++) {
				int page = i + 1;
				Call<DbMoviesQuery> dbMoviesQueryCall;
				if (PreferencesUtilities.sortByPopularity(mContext)) {
					dbMoviesQueryCall = moviesRestApi.queryMoviesPopularity(BuildConfig.MOVIES_API_KEY, page);
				} else {
					dbMoviesQueryCall = moviesRestApi.queryMoviesTopRated(BuildConfig.MOVIES_API_KEY, page);
				}
				Response<DbMoviesQuery> response = dbMoviesQueryCall.execute();
				if (response.isSuccessful()) {
					List<Movie> movieList = response.body().getMovies();
					ContentValues[] moviesArray = SyncMoviesUtilities.moviesList2ContentValuesArray(movieList);
					mContext.getContentResolver().bulkInsert(MoviesContract.MovieEntity.CONTENT_URI, moviesArray);
				}
			}
			/* set last update data */
			PreferencesUtilities.shaveUpdateMoviesDate(mContext);
		} catch (IOException e) {
			Log.e(TAG, "loadInBackground: " + e.toString());
		}
		return null;
	}
}
