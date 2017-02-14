package com.adiaz.movies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.adiaz.movies.adapters.TrailersAdapter;
import com.adiaz.movies.entities.videos.DbMoviesVideos;
import com.adiaz.movies.entities.videos.Video;
import com.adiaz.movies.loaders.MoviesRestApi;
import com.adiaz.movies.utilities.MoviesConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/* Created by toni on 13/02/2017. */

public class TrailersActivity extends AppCompatActivity implements Callback<DbMoviesVideos> {

	private static final String TAG = TrailersActivity.class.getSimpleName();
	private TextView tvTitle;
	private View lloadingTrailers;
	private int mIdMovieOriginal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trailers);
		tvTitle = (TextView)findViewById(R.id.tv_trailers_title);
		lloadingTrailers = findViewById(R.id.ll_loading_trailers);
		String title = getIntent().getStringExtra(DetailsActivity.MOVIE_TITLE);
		mIdMovieOriginal = getIntent().getIntExtra(DetailsActivity.MOVIE_ORIGINAL_ID, 0);
		tvTitle.setText(title);
		lloadingTrailers.setVisibility(View.VISIBLE);
		loadReviews();
	}

	private void loadReviews() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(MoviesConstants.BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		MoviesRestApi moviesRestApi = retrofit.create(MoviesRestApi.class);
		Call<DbMoviesVideos> call = moviesRestApi.queryMoviesVideos(mIdMovieOriginal, BuildConfig.MOVIES_API_KEY);
		call.enqueue(this);
	}

	@Override
	public void onResponse(Call<DbMoviesVideos> call, Response<DbMoviesVideos> response) {
		lloadingTrailers.setVisibility(View.INVISIBLE);
		ListView listView = (ListView) findViewById(R.id.list_trailers);
		if (response.isSuccessful() && response.body() != null) {
			listView.setVisibility(View.VISIBLE);
			if (response.body().getVideos().size()==0) {
				View tvNoTrailers = findViewById(R.id.tv_trailers_empty);
				listView.setEmptyView(tvNoTrailers);
			} else {
				List<Video> reviews = response.body().getVideos();
				TrailersAdapter trailersAdapter = new TrailersAdapter(this, reviews);
				listView.setAdapter(trailersAdapter);
				trailersAdapter.notifyDataSetChanged();
				listView.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onFailure(Call<DbMoviesVideos> call, Throwable t) {
		Log.d(TAG, "onFailure: ");
	}

}
