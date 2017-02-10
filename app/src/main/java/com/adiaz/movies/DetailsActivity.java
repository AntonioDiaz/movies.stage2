package com.adiaz.movies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.adiaz.movies.data.MoviesContract;
import com.adiaz.movies.utilities.MoviesConstants;

public class DetailsActivity extends AppCompatActivity {

	// TODO: 10/02/2017 use ButterKnife for access to Views.
	private TextView mTvTitle;
	private TextView mTvPopularity;
	private int mIdMovie;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		mTvTitle = (TextView) findViewById(R.id.tv_detail_title);
		mTvPopularity = (TextView) findViewById(R.id.tv_detail_popularity);
		Uri uriMovie = getIntent().getData();
		if (uriMovie!=null) {
			Cursor cursor = null;
			try {
				cursor = getContentResolver().query(uriMovie, null, null, null, null);
				cursor.moveToFirst();
				mTvTitle.setText(cursor.getString(MoviesConstants.INDEX_MOVIE_ORIGINAL_TITLE));
				mTvPopularity.setText(cursor.getString(MoviesConstants.INDEX_MOVIE_RELEASE_DATE));
				int isFavorite = cursor.getInt(MoviesConstants.INDEX_MOVIE_IS_FAVORITE);
				mIdMovie = cursor.getInt(MoviesConstants.INDEX_MOVIE_ID);
				Toast.makeText(this, "" + isFavorite, Toast.LENGTH_SHORT).show();
			} finally {
				if (cursor!=null) {
					cursor.close();
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				Intent intentSettings = new Intent(this, SettingsActivity.class);
				startActivity(intentSettings);
				break;
			case R.id.action_add_favorites:
				ContentValues contentValues = new ContentValues();
				contentValues.put(MoviesContract.MovieEntity.COLUMN_IS_FAVORITE, MoviesConstants.FAVORITE_YES);
				Uri uriUpdate = MoviesContract.MovieEntity.CONTENT_URI.buildUpon().appendPath(String.valueOf(mIdMovie)).build();
				getContentResolver().update(uriUpdate, contentValues, null, null);
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
