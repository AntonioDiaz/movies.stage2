package com.adiaz.movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adiaz.movies.entities.DbMoviesQuery;
import com.adiaz.movies.utilities.NetworkUtilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
		implements MoviesCallbak.MoviesLoadListener, SharedPreferences.OnSharedPreferenceChangeListener {

	private static Integer mpage;
	private static final String API_KEY = BuildConfig.MOVIES_API_KEY;
	private TextView mTvError;
	private RecyclerView mRecyclerViewMovies;
	private MoviesRecyclerViewAdapter mAdapterMovies;
	private ProgressBar mProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTvError = (TextView)findViewById(R.id.tv_error);
		mRecyclerViewMovies = (RecyclerView)findViewById(R.id.rv_movies);
		mProgressBar= (ProgressBar)findViewById(R.id.pb_movies);
		mpage = 1;

		mAdapterMovies = new MoviesRecyclerViewAdapter();
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		mRecyclerViewMovies.setHasFixedSize(false);
		mRecyclerViewMovies.setLayoutManager(linearLayoutManager);
		mRecyclerViewMovies.setAdapter(mAdapterMovies);



		if (NetworkUtilities.isNetworkAvailable(this)) {
			refreshAdapter();
		} else {
			mTvError.setText(getString(R.string.internet_error));
			mTvError.setVisibility(View.VISIBLE);
		}
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
	}

	private void refreshAdapter() {
		startLoading();
		String sortKey = getString(R.string.pref_sort_key);
		String defaultSortCriteria = getString(R.string.pref_sort_value_defaul);
		String sortCriteria = PreferenceManager.getDefaultSharedPreferences(this).getString(sortKey, defaultSortCriteria);

		mTvError.setVisibility(View.INVISIBLE);
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(MoviesRestApi.BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		MoviesRestApi moviesRestApi = retrofit.create(MoviesRestApi.class);
		Call<DbMoviesQuery> moviesCall = null;
		if (getString(R.string.pref_sort_value_popularity).equals(sortCriteria)) {
			moviesCall = moviesRestApi.queryMoviesPopularity(API_KEY, mpage);
		} else if (getString(R.string.pref_sort_value_top_rated).equals(sortCriteria)) {
			moviesCall = moviesRestApi.queryMoviesTopRated(API_KEY, mpage);
		} else {
			throw new UnsupportedOperationException("Sort criteria unrecognized");
		}
		Callback<DbMoviesQuery> moviesCallback = new MoviesCallbak(this);
		moviesCall.enqueue(moviesCallback);
	}

	@Override
	protected void onDestroy() {
		PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_details:
				Intent intentDetails = new Intent(this, DetailsActivity.class);
				startActivity(intentDetails);
				break;
			case R.id.action_settings:
				Intent intentSettings = new Intent(this, SettingsActivity.class);
				startActivity(intentSettings);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void startLoading() {
		mProgressBar.setVisibility(View.VISIBLE);
		mRecyclerViewMovies.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onLoadFinished(DbMoviesQuery dbMoviesQuery) {
		mProgressBar.setVisibility(View.INVISIBLE);
		if (dbMoviesQuery.getResults()!=null) {
			mAdapterMovies.setmDbMoviesQuery(dbMoviesQuery);
			mRecyclerViewMovies.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onLoadFailed() {
		mProgressBar.setVisibility(View.INVISIBLE);
		mRecyclerViewMovies.setVisibility(View.INVISIBLE);
		mTvError.setText(R.string.query_error);
		mTvError.setVisibility(View.VISIBLE);

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (getString(R.string.pref_sort_key).equals(key)){
			refreshAdapter();
		}
	}
}
