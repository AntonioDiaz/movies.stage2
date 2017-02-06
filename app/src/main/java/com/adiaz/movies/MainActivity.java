package com.adiaz.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity implements MoviesCallbak.MoviesLoadListener {

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

		startLoading();

		if (NetworkUtilities.isNetworkAvailable(this)) {
			mTvError.setVisibility(View.INVISIBLE);
			Retrofit retrofit = new Retrofit.Builder()
					.baseUrl(MoviesRestApi.BASE_URL)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			MoviesRestApi moviesRestApi = retrofit.create(MoviesRestApi.class);
			Call<DbMoviesQuery> moviesCall = moviesRestApi.queryMoviesPopularity(API_KEY, mpage);
			Callback<DbMoviesQuery> moviesCallback = new MoviesCallbak(this);
			moviesCall.enqueue(moviesCallback);
		} else {
			mTvError.setVisibility(View.VISIBLE);
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
}
