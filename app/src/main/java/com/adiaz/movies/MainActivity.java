package com.adiaz.movies;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.adiaz.movies.utilities.MoviesConstants;

public class MainActivity extends AppCompatActivity implements
		MoviesListFragment.OnMovieSelectedListener, MoviesDetailsFragment.DetailsListener {

	private static final String TAG = MainActivity.class.getSimpleName();

	boolean mIsDualPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		View detailsView = findViewById(R.id.framelayout_detail);
		mIsDualPane = detailsView!=null && detailsView.getVisibility()==View.VISIBLE;
		if (savedInstanceState == null) {
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.add(R.id.framelayout_list, new MoviesListFragment());
			fragmentTransaction.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
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
	public void onMovieSelected(Uri uriMovie) {
		if (mIsDualPane) {
			Bundle bundle = new Bundle();
			bundle.putString(MoviesConstants.URL_ARGUMENT, uriMovie.toString());
			MoviesDetailsFragment detailsFragment = new MoviesDetailsFragment();
			detailsFragment.setArguments(bundle);
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.framelayout_detail, detailsFragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		} else {
			Intent intent = new Intent(this, DetailsActivity.class);
			intent.setData(uriMovie);
			startActivity(intent);
		}
	}

	@Override
	public void openReviews(String title, int originalId) {
		MoviesReviewsFragment moviesTrailersFragment = new MoviesReviewsFragment();
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putString(MoviesConstants.MOVIE_TITLE, title);
		bundle.putInt(MoviesConstants.MOVIE_ORIGINAL_ID, originalId);
		moviesTrailersFragment.setArguments(bundle);
		fragmentTransaction.replace(R.id.framelayout_detail, moviesTrailersFragment);
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
		fragmentTransaction.replace(R.id.framelayout_detail, moviesTrailersFragment);
		fragmentTransaction.addToBackStack("tag");
		fragmentTransaction.commit();
	}
}
