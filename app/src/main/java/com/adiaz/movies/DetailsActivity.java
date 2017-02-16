package com.adiaz.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.adiaz.movies.utilities.MoviesConstants;

public class DetailsActivity extends AppCompatActivity implements MoviesDetailsFragment.DetailsListener {

	private static final String TAG = DetailsActivity.class.getSimpleName();


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d(TAG, "onCreate: 00 " + (savedInstanceState == null));
		/*
		if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
			finish();
			return;
		}
		*/
		if (savedInstanceState == null) {
			MoviesDetailsFragment detailsFragment = new MoviesDetailsFragment();
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			Bundle bundle = new Bundle();
			bundle.putString(MoviesConstants.URL_ARGUMENT, getIntent().getData().toString());
			detailsFragment.setArguments(bundle);
			fragmentTransaction.add(R.id.framelayout_list, detailsFragment);
			fragmentTransaction.commit();
		}
		ActionBar actionBar = this.getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "onOptionsItemSelected: item " + item.getItemId());
		switch (item.getItemId()) {
			case R.id.action_settings:
				Intent intentSettings = new Intent(this, SettingsActivity.class);
				startActivity(intentSettings);
				break;
			case android.R.id.home:
				Log.d(TAG, "onOptionsItemSelected: back");
				onBackPressed();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void openReviews(String title, int originalId) {
		MoviesReviewsFragment moviesTrailersFragment = new MoviesReviewsFragment();
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putString(MoviesConstants.MOVIE_TITLE, title);
		bundle.putInt(MoviesConstants.MOVIE_ORIGINAL_ID, originalId);
		moviesTrailersFragment.setArguments(bundle);
		fragmentTransaction.replace(R.id.framelayout_list, moviesTrailersFragment);
		fragmentTransaction.addToBackStack("tag");
		fragmentTransaction.commit();
	}

	@Override
	public void openTrailers(String title, int originalId) {
		MoviesTrailersFragment moviesTrailersFragment = new MoviesTrailersFragment();
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putString(MoviesConstants.MOVIE_TITLE, title);
		bundle.putInt(MoviesConstants.MOVIE_ORIGINAL_ID, originalId);
		moviesTrailersFragment.setArguments(bundle);
		fragmentTransaction.replace(R.id.framelayout_list, moviesTrailersFragment);
		fragmentTransaction.addToBackStack("tag");
		fragmentTransaction.commit();
	}
}
