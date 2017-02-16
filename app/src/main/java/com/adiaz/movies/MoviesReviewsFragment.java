package com.adiaz.movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by toni on 16/02/2017.
 */
public class MoviesReviewsFragment extends Fragment implements Callback<DbMoviesReviews> {


	private static final String TAG = MoviesReviewsFragment.class.getSimpleName();
	private TextView tvTitle;
	private View lloadingReviews;
	private int mIdMovieOriginal;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView: ");
		return inflater.inflate(R.layout.fragment_reviews, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tvTitle = (TextView) getActivity().findViewById(R.id.tv_reviews_title);
		lloadingReviews = getActivity().findViewById(R.id.ll_loading_reviews);
		String title = getArguments().getString(MoviesConstants.MOVIE_TITLE);
		mIdMovieOriginal = getArguments().getInt(MoviesConstants.MOVIE_ORIGINAL_ID, 0);
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
			ListView listView = (ListView) getActivity().findViewById(R.id.list_reviews);
			if (response.body().getReviews().size()==0) {
				View viewNoReviews = getActivity().findViewById(R.id.tv_reviews_empty);
				listView.setEmptyView(viewNoReviews);
			} else {
				List<Review> reviews = response.body().getReviews();
				ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getActivity(), reviews);
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
