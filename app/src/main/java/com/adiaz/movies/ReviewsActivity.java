package com.adiaz.movies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.adiaz.movies.utilities.MoviesConstants;

public class ReviewsActivity extends AppCompatActivity {

	private static final String TAG = ReviewsActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
			finish();
			return;
		}
		if (savedInstanceState==null) {
			MoviesTrailersFragment moviesTrailersFragment = new MoviesTrailersFragment();
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			Bundle bundle = new Bundle();
			bundle.putString(MoviesConstants.URL_ARGUMENT, getIntent().getData().toString());
			moviesTrailersFragment.setArguments(bundle);
			fragmentTransaction.add(R.id.framelayout_list, moviesTrailersFragment);
			fragmentTransaction.commit();
		}
		ActionBar actionBar = this.getSupportActionBar();
		if (actionBar!=null) {
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
		switch (item.getItemId()) {
			case R.id.action_settings:
				Intent intentSettings = new Intent(this, SettingsActivity.class);
				startActivity(intentSettings);
				break;
			case android.R.id.home:
				onBackPressed();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

}
