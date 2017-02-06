package com.adiaz.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.adiaz.movies.entities.DbMoviesQuery;
import com.adiaz.movies.utilities.NetworkUtilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MoviesCallbak.MoviesLoadListener {

	private static Integer mpage;
	private static final String API_KEY = BuildConfig.MOVIES_API_KEY;
	private static TextView mTvHello;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTvHello = (TextView)findViewById(R.id.tv_hello);
		// TODO: 06/02/2017 check if network
		mpage = 1;
		if (NetworkUtilities.isNetworkAvailable(this)) {
			Retrofit retrofit = new Retrofit.Builder()
					.baseUrl(MoviesRestApi.BASE_URL)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			MoviesRestApi moviesRestApi = retrofit.create(MoviesRestApi.class);
			Call<DbMoviesQuery> moviesCall = moviesRestApi.queryMoviesPopularity(API_KEY, mpage);
			Callback<DbMoviesQuery> moviesCallback = new MoviesCallbak(this);
			moviesCall.enqueue(moviesCallback);
		} else {
			Toast.makeText(this, "NO NETWORK", Toast.LENGTH_SHORT).show();
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
				/*Intent intentDetails = new Intent(this, DetailsActivity.class);
				startActivity(intentDetails);*/
				Toast.makeText(this, "api " + BuildConfig.MOVIES_API_KEY, Toast.LENGTH_SHORT).show();
				break;
			case R.id.action_settings:
				Intent intentSettings = new Intent(this, SettingsActivity.class);
				startActivity(intentSettings);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onLoadFinished(DbMoviesQuery dbMoviesQuery) {
		if (dbMoviesQuery.getResults()!=null) {
			mTvHello.setText("" + dbMoviesQuery.getResults().size());
		}
	}

	@Override
	public void onLoadFailed() {
		Toast.makeText(this, "there was a problem", Toast.LENGTH_SHORT).show();
	}
}
