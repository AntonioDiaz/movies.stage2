package com.adiaz.movies.loaders;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.adiaz.movies.data.MoviesContract;
import com.adiaz.movies.utilities.MoviesConstants;

/**
 * Created by toni on 09/02/2017.
 */

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
		return new CursorLoader(mContext, uri, MoviesConstants.MAIN_MOVIES_PROJECTION, null, null, null);
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
