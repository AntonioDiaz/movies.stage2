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

/**
 * Created by toni on 16/02/2017.
 */
public class MoviesTrailersFragment extends Fragment implements Callback<DbMoviesVideos> {


	private static final String TAG = MoviesTrailersFragment.class.getSimpleName();
	private TextView tvTitle;
	private View lloadingTrailers;
	private int mIdMovieOriginal;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView: ");
		return inflater.inflate(R.layout.fragment_trailers, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tvTitle = (TextView)getActivity().findViewById(R.id.tv_trailers_title);
		lloadingTrailers = getActivity().findViewById(R.id.ll_loading_trailers);
		String title = getArguments().getString(MoviesConstants.MOVIE_TITLE);
		mIdMovieOriginal = getArguments().getInt(MoviesConstants.MOVIE_ORIGINAL_ID);
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
		ListView listView = (ListView)getActivity().findViewById(R.id.list_trailers);
		if (response.isSuccessful() && response.body() != null) {
			listView.setVisibility(View.VISIBLE);
			if (response.body().getVideos().size()==0) {
				View tvNoTrailers = getActivity().findViewById(R.id.tv_trailers_empty);
				listView.setEmptyView(tvNoTrailers);
			} else {
				List<Video> reviews = response.body().getVideos();
				TrailersAdapter trailersAdapter = new TrailersAdapter(getActivity(), reviews);
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
