package com.adiaz.movies;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.adiaz.movies.data.MoviesContract;
import com.adiaz.movies.loaders.MoviesAsyncTask;
import com.adiaz.movies.loaders.MoviesCursorLoader;
import com.adiaz.movies.utilities.NetworkUtilities;
import com.adiaz.movies.utilities.PreferencesUtilities;


// TODO: 09/02/2017 add possibility the user to choose the number of films.
public class MainActivity extends AppCompatActivity
		implements
			SharedPreferences.OnSharedPreferenceChangeListener,
			MoviesAsyncTask.FetchMoviesListener,
			MoviesCursorLoader.LoadCursorListener,
			MoviesRecyclerViewAdapter.ListItemClickListener {

	public static final int PAGES = 1;
	private static final String API_KEY = BuildConfig.MOVIES_API_KEY;
	private static final short LOADER_CURSOR_ID = 23;
	private static final String TAG = MainActivity.class.getSimpleName();
	private TextView mTvError;
	private RecyclerView mRecyclerViewMovies;
	private MoviesRecyclerViewAdapter mAdapterMovies;
	private View mLoadingView;
	private int mPosition = RecyclerView.NO_POSITION;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate: ");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTvError = (TextView) findViewById(R.id.tv_error);
		mRecyclerViewMovies = (RecyclerView) findViewById(R.id.rv_movies);
		mLoadingView = findViewById(R.id.ll_loading);
		mAdapterMovies = new MoviesRecyclerViewAdapter(this);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		mRecyclerViewMovies.setHasFixedSize(false);
		mRecyclerViewMovies.setLayoutManager(linearLayoutManager);
		mRecyclerViewMovies.setAdapter(mAdapterMovies);
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
		if (NetworkUtilities.isNetworkAvailable(this)) {
			refreshAdapter();
		} else {
			mTvError.setText(getString(R.string.internet_error));
			mTvError.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (NetworkUtilities.isNetworkAvailable(this)) {
			boolean refreshRequired = PreferencesUtilities.isNecesaryRefesh(this);
			Log.d(TAG, "onStart: refreshRequired " + refreshRequired);
			if (refreshRequired) {
				refreshAdapter();
			} else {
				this.onFetchMoviesFinished();
			}
		} else {
			mTvError.setText(getString(R.string.internet_error));
			mTvError.setVisibility(View.VISIBLE);
		}
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

	private void refreshAdapter() {
		Log.d(TAG, "refreshAdapter: ");
		MoviesAsyncTask moviesAsyncTask = new MoviesAsyncTask(this, this);
		moviesAsyncTask.execute();
	}

	private void startLoading() {
		Log.d(TAG, "startLoading: .....");
		mTvError.setVisibility(View.INVISIBLE);
		mLoadingView.setVisibility(View.VISIBLE);
		mRecyclerViewMovies.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (getString(R.string.pref_sort_key).equals(key)) {
			refreshAdapter();
		}
	}

	@Override
	public void onFetchMoviesStarted() {
		startLoading();
	}

	@Override
	public void onFetchMoviesFinished() {
		LoaderManager loaderManager = getSupportLoaderManager();
		MoviesCursorLoader moviesCursorLoader = new MoviesCursorLoader(this, this);
		if (loaderManager.getLoader(LOADER_CURSOR_ID)==null) {
			loaderManager.initLoader(LOADER_CURSOR_ID, null, moviesCursorLoader);
		} else {
			loaderManager.restartLoader(LOADER_CURSOR_ID, null, moviesCursorLoader);
		}
	}

	@Override
	public void onLoadCursorFinished(Cursor cursor) {
		mLoadingView.setVisibility(View.INVISIBLE);
		mTvError.setVisibility(View.INVISIBLE);
		if (cursor!=null && cursor.getCount()>0) {
			mAdapterMovies.swapCursor(cursor);
			if (mPosition == RecyclerView.NO_POSITION) {
				mPosition = 0;
			}
			mRecyclerViewMovies.smoothScrollToPosition(mPosition);
			mRecyclerViewMovies.setVisibility(View.VISIBLE);
		} else {
			mTvError.setText("error ...");
			mTvError.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onListItemClick(int clickedItemIndex) {
		Intent intent = new Intent(this, DetailsActivity.class);
		String idStr = String.valueOf(clickedItemIndex);
		Uri uriMovie = MoviesContract.MovieEntity.CONTENT_URI.buildUpon().appendPath(idStr).build();
		intent.setData(uriMovie);
		startActivity(intent);
	}
}
