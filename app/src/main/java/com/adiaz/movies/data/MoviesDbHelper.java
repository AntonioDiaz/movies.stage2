package com.adiaz.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.adiaz.movies.data.MoviesContract.MovieEntity;

/** Created by toni on 08/02/2017. */

public class MoviesDbHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "movies.db";
	public static final int DATABASE_VERSION = 1;

	public MoviesDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		final String SQL_CREATE_TABLE =
				"CREATE TABLE " + MovieEntity.TABLE_NAME +
				"(" +
						MovieEntity._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
						MovieEntity.COLUMN_TITLE + " TEXT NOT NULL," +
						MovieEntity.COLUMN_POPULARITY + " TEXT NOT NULL" +
						")";
		sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntity.TABLE_NAME);
		onCreate(sqLiteDatabase);
	}
}