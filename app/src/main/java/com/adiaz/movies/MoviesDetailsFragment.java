package com.adiaz.movies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adiaz.movies.data.MoviesContract;
import com.adiaz.movies.utilities.MoviesConstants;
import com.squareup.picasso.Picasso;

/**
 * Created by toni on 15/02/2017.
 */

public class MoviesDetailsFragment extends Fragment {

	private static final String TAG = MoviesDetailsFragment.class.getSimpleName();
	public static final String MOVIE_TITLE = "movie_title";
	public static final String MOVIE_ORIGINAL_ID = "movie_original_id";
	// TODO: 10/02/2017 use ButterKnife for access to Views.
	private TextView mTvTitle;
	private ImageView mIvPoster;
	private ImageView mIvFavorites;
	private TextView mTvReleaseDate;
	private TextView mTvUserRating;
	private TextView mTvPlot;
	private TextView mTvPopularity;
	private int mIdMovie;
	private int mIdMovieOriginal;
	private Cursor mCursor;
	private boolean isDetailsVisible;
	private DetailsListener mDetailsActivityListener;


	public interface DetailsListener {
		public void openReviews(String title, int originalId);
		public void openTrailers(String title, int originalId);
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			mDetailsActivityListener = (DetailsListener)context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString() + "must implement DetailsListener");
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView: ");
		return inflater.inflate(R.layout.fragment_details, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onActivityCreated: 00");
		mTvTitle = (TextView) getActivity().findViewById(R.id.tv_detail_title);
		mIvPoster = (ImageView) getActivity().findViewById(R.id.iv_detail_poster);
		mIvFavorites = (ImageView) getActivity().findViewById(R.id.iv_detail_favorites);
		mTvReleaseDate = (TextView) getActivity().findViewById(R.id.tv_detail_release_date);
		mTvUserRating = (TextView) getActivity().findViewById(R.id.tv_detail_user_rating);
		mTvPlot = (TextView) getActivity().findViewById(R.id.tv_detail_plot);
		mTvPopularity = (TextView) getActivity().findViewById(R.id.tv_detail_popularity);
		String url = getArguments().getString(MoviesConstants.URL_ARGUMENT);
		Uri uriMovie = Uri.parse(url).buildUpon().build();
		if (mTvTitle!=null && uriMovie != null) {
			mCursor = getActivity().getContentResolver().query(uriMovie, MoviesConstants.MAIN_MOVIES_PROJECTION, null, null, null);
			mCursor.moveToFirst();
			mTvTitle.setText(mCursor.getString(MoviesConstants.INDEX_MOVIE_ORIGINAL_TITLE));
			mTvReleaseDate.setText(mCursor.getString(MoviesConstants.INDEX_MOVIE_RELEASE_DATE));
			String rating = Float.toString(mCursor.getFloat(MoviesConstants.INDEX_MOVIE_USER_RATING));
			mTvUserRating.setText(getString(R.string.details_user_rating_value, rating));
			mTvPlot.setText(mCursor.getString(MoviesConstants.INDEX_MOVIE_OVERVIEW));
			mTvPopularity.setText(Float.toString(mCursor.getFloat(MoviesConstants.INDEX_MOVIE_POPULARITY)));
			int favorite = mCursor.getInt(MoviesConstants.INDEX_MOVIE_IS_FAVORITE);
			if (MoviesConstants.FAVORITE_YES == favorite) {
				mIvFavorites.setImageResource(R.drawable.ic_hearth_selected);
			} else {
				mIvFavorites.setImageResource(R.drawable.ic_hearth_unselected);
			}
			mIdMovie = mCursor.getInt(MoviesConstants.INDEX_MOVIE_ID);
			mIdMovieOriginal = mCursor.getInt(MoviesConstants.INDEX_MOVIE_ID_ORIGINAL);
			String imageUrl = MoviesConstants.IMAGES_PATH + mCursor.getString(MoviesConstants.INDEX_MOVIE_POSTER_PATH);
			Picasso.with(getActivity())
					.load(imageUrl)
					.resize(400, 600)
					.centerInside()
					.placeholder(R.drawable.progress_animation)
					.into(mIvPoster);
			//loadTrailers();
			ImageView imageViewFav = (ImageView)getActivity().findViewById(R.id.iv_detail_favorites);
			imageViewFav.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					updateFavorites();
				}
			});
			View buttonTrailers = getActivity().findViewById(R.id.b_details_trailers);
			buttonTrailers.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					openTrailersActivity();
				}
			});
			View buttonReviews = getActivity().findViewById(R.id.b_details_reviews);
			buttonReviews.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					openReviewsActivity();
				}
			});
		}
	}

	public void updateFavorites() {
		int actualFavorites = mCursor.getInt(MoviesConstants.INDEX_MOVIE_IS_FAVORITE);
		int newFavorites;
		if (MoviesConstants.FAVORITE_YES == actualFavorites) {
			newFavorites = MoviesConstants.FAVORITE_NOT;
			mIvFavorites.setImageResource(R.drawable.ic_hearth_unselected);
		} else {
			newFavorites = MoviesConstants.FAVORITE_YES;
			mIvFavorites.setImageResource(R.drawable.ic_hearth_selected);
		}
		ContentValues contentValues = new ContentValues();
		contentValues.put(MoviesContract.MovieEntity.COLUMN_IS_FAVORITE, newFavorites);
		Uri uriMovie = MoviesContract.MovieEntity.CONTENT_URI.buildUpon().appendPath(String.valueOf(mIdMovie)).build();
		getActivity().getContentResolver().update(uriMovie, contentValues, null, null);
		mCursor = getActivity().getContentResolver().query(uriMovie, MoviesConstants.MAIN_MOVIES_PROJECTION, null, null, null);
		mCursor.moveToFirst();
	}

	public void openReviewsActivity() {
		String title = mCursor.getString(MoviesConstants.INDEX_MOVIE_ORIGINAL_TITLE);
		int idOriginal = mCursor.getInt(MoviesConstants.INDEX_MOVIE_ID_ORIGINAL);
		mDetailsActivityListener.openReviews(title, idOriginal);
	}

	public void openTrailersActivity() {
		String title = mCursor.getString(MoviesConstants.INDEX_MOVIE_ORIGINAL_TITLE);
		int idOriginal = mCursor.getInt(MoviesConstants.INDEX_MOVIE_ID_ORIGINAL);
		mDetailsActivityListener.openTrailers(title, idOriginal);
	}

}
