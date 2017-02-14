package com.adiaz.movies.loaders;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.adiaz.movies.data.MoviesContract;
import com.adiaz.movies.utilities.MoviesConstants;
import com.adiaz.movies.utilities.PreferencesUtilities;

import static android.content.ContentValues.TAG;

/* Created by toni on 09/02/2017. */

public class MoviesCursorLoader implements LoaderManager.LoaderCallbacks <Cursor> {

	private Context mContext;
	private LoadCursorListener mLoadCursorListener;

	public MoviesCursorLoader(Context mContext, LoadCursorListener mLoadCursorListener) {
		this.mContext = mContext;
		this.mLoadCursorListener = mLoadCursorListener;
	}

	public interface LoadCursorListener {
		public void onLoadCursorFinished(Cursor cursor);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri uri = MoviesContract.MovieEntity.CONTENT_URI;
		String selection = null;
		String[] selectionArgs = null;
		if (PreferencesUtilities.isFavoritesOnlyChecked(mContext)) {
			selection = MoviesContract.MovieEntity.COLUMN_IS_FAVORITE + "=?";
			selectionArgs = new String[]{"1"};
		}
		String sortBy = "";
		if (PreferencesUtilities.sortByPopularity(mContext)) {
			sortBy += MoviesContract.MovieEntity.COLUMN_POPULARITY;
		} else {
			sortBy += MoviesContract.MovieEntity.COLUMN_USER_RATING;
		}
		sortBy += " DESC ";
		Log.d(TAG, "onCreateLoader: " + sortBy);
		return new CursorLoader(mContext, uri, MoviesConstants.MAIN_MOVIES_PROJECTION, selection, selectionArgs, sortBy);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mLoadCursorListener.onLoadCursorFinished(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mLoadCursorListener.onLoadCursorFinished(null);
	}
}
