package com.adiaz.movies;

/* Created by toni on 08/02/2017. */

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.adiaz.movies.data.MoviesContract;
import com.adiaz.movies.data.MoviesDbHelper;

public class MoviesContentProvider extends ContentProvider {

	private MoviesDbHelper mMoviesDbHelper;
	private static final UriMatcher sUriMatcher = buildUriMatcher();

	private static final int MOVIES = 100;
	private static final int MOVIES_WITH_ID = 101;


	private static UriMatcher buildUriMatcher() {
		UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES, MOVIES);
		uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES + "/#", MOVIES_WITH_ID);
		return uriMatcher;
	}

	@Override
	public boolean onCreate() {
		mMoviesDbHelper = new MoviesDbHelper(getContext());
		return true;
	}

	@Nullable
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		SQLiteDatabase readableDatabase = mMoviesDbHelper.getReadableDatabase();
		switch (sUriMatcher.match(uri)) {
			case MOVIES:
				cursor = readableDatabase.query(
						MoviesContract.MovieEntity.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
				break;
			case MOVIES_WITH_ID:
				String id = uri.getPathSegments().get(1);
				String mSelection = "_id =?";
				String[] mSelectionArgs = new String[]{id};
				cursor = readableDatabase.query(
						MoviesContract.MovieEntity.TABLE_NAME, projection, mSelection, mSelectionArgs, null, null, sortOrder);
				break;
			default:
				String strError = getContext().getString(R.string.operation_forbidden) + " " + uri;
				throw new UnsupportedOperationException(strError);
		}
		if (cursor!=null){
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
		}
		return cursor;
	}

	@Nullable
	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Nullable
	@Override
	public Uri insert(Uri uri, ContentValues contentValues) {
		Uri uriReturned = null;
		switch (sUriMatcher.match(uri)) {
			case MOVIES:
				SQLiteDatabase writableDatabase = mMoviesDbHelper.getWritableDatabase();
				long idNew = writableDatabase.insert(MoviesContract.MovieEntity.TABLE_NAME, null, contentValues);
				if (idNew>0) {
					Uri.Builder builder = MoviesContract.MovieEntity.CONTENT_URI.buildUpon();
					uriReturned = ContentUris.appendId(builder, idNew).build();
				}
				break;
			default:
				String strError = getContext().getString(R.string.operation_forbidden) + " " + uri;
				throw new UnsupportedOperationException(strError);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return uriReturned;
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		switch (sUriMatcher.match(uri)) {
			case MOVIES:
				final SQLiteDatabase writableDatabase = mMoviesDbHelper.getWritableDatabase();
				int recordsInsert = 0;
				try {
					writableDatabase.beginTransaction();
					for (ContentValues contentValues : values) {
						long idInserted = writableDatabase.insert(MoviesContract.MovieEntity.TABLE_NAME, null, contentValues);
						if (idInserted>0) {
							recordsInsert++;
						}
					}
					writableDatabase.setTransactionSuccessful();
				} finally {
					writableDatabase.endTransaction();
				}
				return recordsInsert;
			default:
				return super.bulkInsert(uri, values);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int recordsDeleted = 0;
		SQLiteDatabase writableDatabase = mMoviesDbHelper.getWritableDatabase();
		switch (sUriMatcher.match(uri)) {
			case MOVIES:
				if (selection==null) {
					selection = "1";
				}
				recordsDeleted = writableDatabase.delete(MoviesContract.MovieEntity.TABLE_NAME, selection, selectionArgs);
				break;
			case MOVIES_WITH_ID:
				String id = uri.getPathSegments().get(1);
				String mSelection = "_id =?";
				String[] mSelectionArgs = new String[]{id};
				recordsDeleted = writableDatabase.delete(MoviesContract.MovieEntity.TABLE_NAME, mSelection, mSelectionArgs);
				break;
			default:
				String strError = getContext().getString(R.string.operation_forbidden) + " " + uri;
				throw new UnsupportedOperationException(strError);
		}
		if (recordsDeleted>0){
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return recordsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
		return 0;
	}

	/**
	 * You do not need to call this method. This is a method specifically to assist the testing
	 * framework in running smoothly. You can read more at:
	 * http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
	 */
	@Override
	@TargetApi(11)
	public void shutdown() {
		mMoviesDbHelper.close();
		super.shutdown();
	}

}
