package com.adiaz.movies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.adiaz.movies.utilities.MoviesConstants;
import com.squareup.picasso.Picasso;


/* Created by toni on 06/02/2017. */

public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.MoviesViewHolder>{

	//private DbMoviesQuery mDbMoviesQuery;
	private Cursor mCursorMovies;
	private final ListItemClickListener mOnClickListener;
	private Context mContext;

	private static final String TAG = MoviesRecyclerViewAdapter.class.getSimpleName();

	public MoviesRecyclerViewAdapter(ListItemClickListener mOnClickListener) {
		this.mOnClickListener = mOnClickListener;
	}

	public interface ListItemClickListener {
		void onListItemClick(int movieId);
	}

	@Override
	public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		mContext = parent.getContext();
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		View view = layoutInflater.inflate(R.layout.movies_list_item, parent, false);
		return new MoviesViewHolder(view);
	}

	@Override
	public void onBindViewHolder(MoviesViewHolder holder, int position) {
		if (mCursorMovies!=null) {
			mCursorMovies.moveToPosition(position);
			String imgUrl = MoviesConstants.IMAGES_PATH + mCursorMovies.getString(MoviesConstants.INDEX_MOVIE_POSTER_PATH);
			Picasso.with(mContext)
					.load(imgUrl)
					.resize(400, 600)
					.centerInside()
					.placeholder(R.drawable.test)
					.into(holder.mIvPoster);
			int favorite = mCursorMovies.getInt(MoviesConstants.INDEX_MOVIE_IS_FAVORITE);
			if (MoviesConstants.FAVORITE_YES==favorite) {
				holder.mIvFavorites.setImageResource(R.drawable.ic_hearth_selected);
			} else {
				holder.mIvFavorites.setImageResource(R.drawable.ic_hearth_unselected);
			}
		}
	}

	@Override
	public int getItemCount() {
		int moviesCount = 0;
		if (mCursorMovies!=null) {
			moviesCount = mCursorMovies.getCount();
		}
		return moviesCount;
	}

	public void swapCursor (Cursor newCursor) {
		mCursorMovies = newCursor;
		notifyDataSetChanged();
	}

	public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private ImageView mIvPoster;
		private ImageView mIvFavorites;

		public MoviesViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			mIvPoster = (ImageView) itemView.findViewById(R.id.iv_list_item_poster);
			mIvFavorites = (ImageView) itemView.findViewById(R.id.iv_favorites);
		}

		@Override
		public void onClick(View view) {
			mCursorMovies.moveToPosition(getAdapterPosition());
			mOnClickListener.onListItemClick(mCursorMovies.getInt(MoviesConstants.INDEX_MOVIE_ID));
		}
	}
}
