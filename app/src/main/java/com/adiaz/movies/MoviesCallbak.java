package com.adiaz.movies;

import com.adiaz.movies.entities.DbMoviesQuery;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Created by toni on 06/02/2017. */

public class MoviesCallbak implements Callback<DbMoviesQuery> {

	private MoviesLoadListener mLoadMoviesListener;

	public MoviesCallbak(MoviesLoadListener mLoadMoviesListener) {
		this.mLoadMoviesListener = mLoadMoviesListener;
	}

	@Override
	public void onResponse(Call<DbMoviesQuery> call, Response<DbMoviesQuery> response) {
		if (response.isSuccessful() && response.body()!=null) {
			this.mLoadMoviesListener.onLoadFinished(response.body());
		} else {
			this.mLoadMoviesListener.onLoadFailed();
		}
	}

	@Override
	public void onFailure(Call<DbMoviesQuery> call, Throwable t) {
		mLoadMoviesListener.onLoadFailed();
	}

	public interface MoviesLoadListener {
		public void onLoadFinished(DbMoviesQuery dbMoviesQuery);
		public void onLoadFailed();
	}
}
