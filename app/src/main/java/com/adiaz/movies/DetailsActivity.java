package com.adiaz.movies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adiaz.movies.data.MoviesContract;
import com.adiaz.movies.utilities.MoviesConstants;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

	private static final String TAG = DetailsActivity.class.getSimpleName();
	public static final String MOVIE_TITLE = "movie_title";
	public static final String MOVIE_ORIGINAL_ID = "movie_original_id";
	// TODO: 10/02/2017 use ButterKnife for access to Views.
	private TextView mTvTitle;
	private ImageView mIvPoster;
	private ImageView mIvFavorites;
	private TextView mTvReleaseDate;
	private TextView mTvUserRating;
	private TextView mTvPlot;
	private TextView mTvPopularity;
	private int mIdMovie;
	private int mIdMovieOriginal;
	private Cursor mCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		mTvTitle = (TextView) findViewById(R.id.tv_detail_title);
		mIvPoster = (ImageView) findViewById(R.id.iv_detail_poster);
		mIvFavorites = (ImageView) findViewById(R.id.iv_detail_favorites);
		mTvReleaseDate = (TextView) findViewById(R.id.tv_detail_release_date);
		mTvUserRating = (TextView) findViewById(R.id.tv_detail_user_rating);
		mTvPlot = (TextView) findViewById(R.id.tv_detail_plot);
		mTvPopularity = (TextView) findViewById(R.id.tv_detail_popularity);
		Uri uriMovie = getIntent().getData();
		if (uriMovie != null) {
			mCursor = getContentResolver().query(uriMovie, MoviesConstants.MAIN_MOVIES_PROJECTION, null, null, null);
			mCursor.moveToFirst();
			mTvTitle.setText(mCursor.getString(MoviesConstants.INDEX_MOVIE_ORIGINAL_TITLE));
			mTvReleaseDate.setText(mCursor.getString(MoviesConstants.INDEX_MOVIE_RELEASE_DATE));
			String rating = Float.toString(mCursor.getFloat(MoviesConstants.INDEX_MOVIE_USER_RATING));
			mTvUserRating.setText(getString(R.string.details_user_rating_value, rating));
			mTvPlot.setText(mCursor.getString(MoviesConstants.INDEX_MOVIE_OVERVIEW));
			mTvPopularity.setText(Float.toString(mCursor.getFloat(MoviesConstants.INDEX_MOVIE_POPULARITY)));
			int favorite = mCursor.getInt(MoviesConstants.INDEX_MOVIE_IS_FAVORITE);
			if (MoviesConstants.FAVORITE_YES == favorite) {
				mIvFavorites.setImageResource(R.drawable.ic_hearth_selected);
			} else {
				mIvFavorites.setImageResource(R.drawable.ic_hearth_unselected);
			}
			mIdMovie = mCursor.getInt(MoviesConstants.INDEX_MOVIE_ID);
			mIdMovieOriginal = mCursor.getInt(MoviesConstants.INDEX_MOVIE_ID_ORIGINAL);
			String imageUrl = MoviesConstants.IMAGES_PATH + mCursor.getString(MoviesConstants.INDEX_MOVIE_POSTER_PATH);
			Picasso.with(this)
					.load(imageUrl)
					.resize(400, 600)
					.centerInside()
					.placeholder(R.drawable.progress_animation)
					.into(mIvPoster);
			//loadTrailers();
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
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		if (mCursor != null) {
			mCursor.close();
		}
		super.onDestroy();
	}

	public void updateFavorites(View view) {
		int actualFavorites = mCursor.getInt(MoviesConstants.INDEX_MOVIE_IS_FAVORITE);
		int newFavorites;
		if (MoviesConstants.FAVORITE_YES == actualFavorites) {
			newFavorites = MoviesConstants.FAVORITE_NOT;
			mIvFavorites.setImageResource(R.drawable.ic_hearth_unselected);
		} else {
			newFavorites = MoviesConstants.FAVORITE_YES;
			mIvFavorites.setImageResource(R.drawable.ic_hearth_selected);
		}
		ContentValues contentValues = new ContentValues();
		contentValues.put(MoviesContract.MovieEntity.COLUMN_IS_FAVORITE, newFavorites);
		Uri uriMovie = MoviesContract.MovieEntity.CONTENT_URI.buildUpon().appendPath(String.valueOf(mIdMovie)).build();
		getContentResolver().update(uriMovie, contentValues, null, null);
		mCursor = getContentResolver().query(uriMovie, MoviesConstants.MAIN_MOVIES_PROJECTION, null, null, null);
		mCursor.moveToFirst();
	}

	public void openReviewsActivity(View view) {
		Intent intent = new Intent(this, ReviewsActivity.class);
		intent.putExtra(DetailsActivity.MOVIE_TITLE, mCursor.getString(MoviesConstants.INDEX_MOVIE_ORIGINAL_TITLE));
		intent.putExtra(DetailsActivity.MOVIE_ORIGINAL_ID, mCursor.getInt(MoviesConstants.INDEX_MOVIE_ID_ORIGINAL));
		startActivity(intent);
	}

	public void openTrailersActivity(View view) {
		Intent intent = new Intent(this, TrailersActivity.class);
		intent.putExtra(DetailsActivity.MOVIE_TITLE, mCursor.getString(MoviesConstants.INDEX_MOVIE_ORIGINAL_TITLE));
		intent.putExtra(DetailsActivity.MOVIE_ORIGINAL_ID, mCursor.getInt(MoviesConstants.INDEX_MOVIE_ID_ORIGINAL));
		startActivity(intent);
	}
}
