package com.adiaz.movies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adiaz.movies.R;
import com.adiaz.movies.entities.reviews.Review;

import java.util.List;

/** Created by toni on 11/02/2017. */

public class ReviewsAdapter extends ArrayAdapter <Review> {

	private static final String TAG = ReviewsAdapter.class.getSimpleName();

	private static class ViewHolder {
		TextView autor;
		TextView content;
	}

	public ReviewsAdapter(Context context, List<Review> reviewsList) {
		super(context, R.layout.review_list_item, reviewsList);
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Review review = getItem(position);
		ViewHolder viewHolder;
		if (convertView==null) {
			viewHolder = new ViewHolder();
			LayoutInflater layoutInflater = LayoutInflater.from(getContext());
			convertView = layoutInflater.inflate(R.layout.review_list_item, parent, false);
			viewHolder.autor = (TextView) convertView.findViewById(R.id.tv_review_author);
			viewHolder.content = (TextView) convertView.findViewById(R.id.tv_review);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.autor.setText(review.getAuthor());
		viewHolder.content.setText(review.getContent());
		return convertView;
	}
}
