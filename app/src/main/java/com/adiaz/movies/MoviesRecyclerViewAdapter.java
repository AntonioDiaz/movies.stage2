package com.adiaz.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.movies.entities.DbMoviesQuery;

/* Created by toni on 06/02/2017. */

public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.MoviesViewHolder>{

	private DbMoviesQuery mDbMoviesQuery;

	@Override
	public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.movies_list_item, parent, false);
		return new MoviesViewHolder(view);
	}

	@Override
	public void onBindViewHolder(MoviesViewHolder holder, int position) {
		if (mDbMoviesQuery!=null && mDbMoviesQuery.getResults().size()>position) {
			String title = mDbMoviesQuery.getResults().get(position).getTitle();
			holder.mTvTitle.setText(title);
		}
	}

	@Override
	public int getItemCount() {
		int moviesCount = 0;
		if (mDbMoviesQuery!=null) {
			moviesCount = mDbMoviesQuery.getResults().size();
		}
		return moviesCount;
	}

	public void setmDbMoviesQuery(DbMoviesQuery mDbMoviesQuery) {
		this.mDbMoviesQuery = mDbMoviesQuery;
		notifyDataSetChanged();
	}

	public class MoviesViewHolder extends RecyclerView.ViewHolder {

		private TextView mTvTitle;

		public MoviesViewHolder(View itemView) {
			super(itemView);
			mTvTitle = (TextView) itemView.findViewById(R.id.tv_list_item_title);
		}
	}
}
