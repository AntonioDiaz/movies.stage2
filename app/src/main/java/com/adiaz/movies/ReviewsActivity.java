package com.adiaz.movies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.adiaz.movies.adapters.ReviewsAdapter;
import com.adiaz.movies.entities.reviews.DbMoviesReviews;
import com.adiaz.movies.entities.reviews.Review;
import com.adiaz.movies.loaders.MoviesRestApi;
import com.adiaz.movies.utilities.MoviesConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReviewsActivity extends AppCompatActivity implements Callback<DbMoviesReviews> {

	private static final String TAG = ReviewsActivity.class.getSimpleName();
	private TextView tvTitle;
	private View lloadingReviews;
	private int mIdMovieOriginal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reviews);
		tvTitle = (TextView) findViewById(R.id.tv_reviews_title);
		lloadingReviews = findViewById(R.id.ll_loading_reviews);
		String title = getIntent().getStringExtra(DetailsActivity.MOVIE_TITLE);
		mIdMovieOriginal = getIntent().getIntExtra(DetailsActivity.MOVIE_ORIGINAL_ID, 0);
		tvTitle.setText(title);
		lloadingReviews.setVisibility(View.VISIBLE);
		loadReviews();
	}

	private void loadReviews() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(MoviesConstants.BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		MoviesRestApi moviesRestApi = retrofit.create(MoviesRestApi.class);
		Call<DbMoviesReviews> call = moviesRestApi.queryMoviesReviews(mIdMovieOriginal, BuildConfig.MOVIES_API_KEY);
		call.enqueue(this);
	}


	@Override
	public void onResponse(Call<DbMoviesReviews> call, Response<DbMoviesReviews> response) {
		lloadingReviews.setVisibility(View.INVISIBLE);
		if (response.isSuccessful() && response.body() != null) {
			ListView listView = (ListView) findViewById(R.id.list_reviews);
			if (response.body().getReviews().size()==0) {
				View viewNoReviews = findViewById(R.id.tv_reviews_empty);
				listView.setEmptyView(viewNoReviews);
			} else {
				List<Review> reviews = response.body().getReviews();
				ReviewsAdapter reviewsAdapter = new ReviewsAdapter(this, reviews);
				listView.setAdapter(reviewsAdapter);
				reviewsAdapter.notifyDataSetChanged();
				listView.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onFailure(Call<DbMoviesReviews> call, Throwable t) {
		Log.d(TAG, "onFailure: ");
	}

}
