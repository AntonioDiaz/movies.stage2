package com.adiaz.movies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.movies.data.MoviesContract;


/* Created by toni on 06/02/2017. */

public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.MoviesViewHolder>{

	//private DbMoviesQuery mDbMoviesQuery;
	private Cursor mCursorMovies;
	private final ListItemClickListener mOnClickListener;

	private static final String TAG = MoviesRecyclerViewAdapter.class.getSimpleName();

	public MoviesRecyclerViewAdapter(ListItemClickListener mOnClickListener) {
		this.mOnClickListener = mOnClickListener;
	}

	public interface ListItemClickListener {
		void onListItemClick(int movieId);
	}

	@Override
	public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		Context context = parent.getContext();
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.movies_list_item, parent, false);
		return new MoviesViewHolder(view);
	}

	@Override
	public void onBindViewHolder(MoviesViewHolder holder, int position) {
		if (mCursorMovies!=null) {
			int titleIndex = mCursorMovies.getColumnIndex(MoviesContract.MovieEntity.COLUMN_TITLE);
			mCursorMovies.moveToPosition(position);
			String title = mCursorMovies.getString(titleIndex);
			holder.mTvTitle.setText(position + " " + title);
		}
	}

	@Override
	public int getItemCount() {
		int moviesCount = 0;
		if (mCursorMovies!=null) {
			moviesCount = mCursorMovies.getCount();
		}
		//Log.d(TAG, "getItemCount: " + moviesCount);
		return moviesCount;
	}

	public void swapCursor (Cursor newCursor) {
		mCursorMovies = newCursor;
		notifyDataSetChanged();
	}

	public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private TextView mTvTitle;

		public MoviesViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			mTvTitle = (TextView) itemView.findViewById(R.id.tv_list_item_title);
		}

		@Override
		public void onClick(View view) {
			int indexId = mCursorMovies.getColumnIndex(MoviesContract.MovieEntity._ID);
			mCursorMovies.moveToPosition(getAdapterPosition());

			mOnClickListener.onListItemClick(mCursorMovies.getInt(indexId));
		}
	}
}
