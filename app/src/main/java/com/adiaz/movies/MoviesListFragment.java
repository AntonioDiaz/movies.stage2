package com.adiaz.movies;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.movies.adapters.MoviesRecyclerViewAdapter;
import com.adiaz.movies.data.MoviesContract;
import com.adiaz.movies.loaders.MoviesAsyncTask;
import com.adiaz.movies.loaders.MoviesCursorLoader;
import com.adiaz.movies.utilities.NetworkUtilities;
import com.adiaz.movies.utilities.PreferencesUtilities;

/**
 * Created by toni on 15/02/2017.
 */

public class MoviesListFragment extends Fragment implements
		MoviesRecyclerViewAdapter.ListItemClickListener,
		MoviesAsyncTask.FetchMoviesListener,
		MoviesCursorLoader.LoadCursorListener {

	private OnMovieSelectedListener mListener;

	private static final short LOADER_CURSOR_ID = 23;

	private static final String TAG = MainActivity.class.getSimpleName();
	private TextView mTvError;
	private RecyclerView mRecyclerViewMovies;
	private MoviesRecyclerViewAdapter mAdapterMovies;
	private View mLoadingView;
	private int mPosition = RecyclerView.NO_POSITION;


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			mListener = (OnMovieSelectedListener) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString() + "must implement OnMovieSelectedListener");
		}
	}

	public interface OnMovieSelectedListener {
		public void onMovieSelected(Uri uriMovie);
	}


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_list, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mTvError = (TextView) getActivity().findViewById(R.id.tv_error);
		mRecyclerViewMovies = (RecyclerView) getActivity().findViewById(R.id.rv_movies);
		mLoadingView = getActivity().findViewById(R.id.ll_loading);
		mAdapterMovies = new MoviesRecyclerViewAdapter(this);
		int columns = getResources().getInteger(R.integer.columns);
//		columns = 1;
		Log.d(TAG, "onActivityCreated: columns -->" + columns);
		GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), columns);
		mRecyclerViewMovies.setHasFixedSize(false);
		mRecyclerViewMovies.setLayoutManager(gridLayoutManager);
		mRecyclerViewMovies.setAdapter(mAdapterMovies);
	}

	@Override
	public void onStart() {
		super.onStart();
		if (NetworkUtilities.isNetworkAvailable(getActivity())) {
			boolean refreshRequired = PreferencesUtilities.isNecesaryRefesh(getActivity());
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

	private void refreshAdapter() {
		MoviesAsyncTask moviesAsyncTask = new MoviesAsyncTask(getActivity(), this);
		moviesAsyncTask.execute();
	}

	@Override
	public void onListItemClick(int movieId) {
		mPosition = ((LinearLayoutManager) mRecyclerViewMovies.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
		String idStr = String.valueOf(movieId);
		Uri uriMovie = MoviesContract.MovieEntity.CONTENT_URI.buildUpon().appendPath(idStr).build();
		mListener.onMovieSelected(uriMovie);
	}

	private void startLoading() {
		mTvError.setVisibility(View.INVISIBLE);
		mLoadingView.setVisibility(View.VISIBLE);
		mRecyclerViewMovies.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onFetchMoviesStarted() {
		startLoading();
	}

	@Override
	public void onFetchMoviesFinished() {

		LoaderManager loaderManager = getActivity().getSupportLoaderManager();
		MoviesCursorLoader moviesCursorLoader = new MoviesCursorLoader(getActivity(), this);
		if (loaderManager.getLoader(LOADER_CURSOR_ID) == null) {
			loaderManager.initLoader(LOADER_CURSOR_ID, null, moviesCursorLoader);
		} else {
			loaderManager.restartLoader(LOADER_CURSOR_ID, null, moviesCursorLoader);
		}
	}

	@Override
	public void onLoadCursorFinished(Cursor cursor) {
		mLoadingView.setVisibility(View.INVISIBLE);
		mTvError.setVisibility(View.INVISIBLE);
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				mAdapterMovies.swapCursor(cursor);
				if (mPosition == RecyclerView.NO_POSITION) {
					mPosition = 0;
				}
				mRecyclerViewMovies.smoothScrollToPosition(mPosition);
				mRecyclerViewMovies.setVisibility(View.VISIBLE);
			} else {
				mRecyclerViewMovies.setVisibility(View.INVISIBLE);
				mTvError.setText(getString(R.string.no_movies));
				mTvError.setVisibility(View.VISIBLE);
			}
		}
	}
}


