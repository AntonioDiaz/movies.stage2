package com.adiaz.movies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adiaz.movies.R;
import com.adiaz.movies.entities.videos.Video;
import com.adiaz.movies.utilities.MoviesConstants;

import java.util.List;

/**
 * Created by toni on 13/02/2017.
 */

public class TrailersAdapter extends ArrayAdapter<Video> {

	private Context mContext;

	private static class ViewHolder {
		TextView trailerTitle;
		ImageView ivPlay;
		ImageView ivShare;
	}

	public TrailersAdapter(Context context, List<Video> objects) {
		super(context, R.layout.trailer_list_item, objects);
		mContext = context;
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Video video = getItem(position);
		ViewHolder viewHolder;
		if (convertView==null) {
			viewHolder = new ViewHolder();
			LayoutInflater layoutInflater = LayoutInflater.from(getContext());
			convertView = layoutInflater.inflate(R.layout.trailer_list_item, parent, false);
			viewHolder.trailerTitle = (TextView) convertView.findViewById(R.id.tv_trailer_title);
			viewHolder.ivPlay = (ImageView) convertView.findViewById(R.id.iv_trailer_play);
			viewHolder.ivShare = (ImageView) convertView.findViewById(R.id.iv_trailer_share);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.trailerTitle.setText(video.getName());
		viewHolder.ivPlay.setTag(MoviesConstants.YOUTUBE_URL + video.getKey());
		viewHolder.ivPlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Uri uriYoutube = Uri.parse((String) view.getTag());
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, uriYoutube ));
			}
		});
		viewHolder.ivShare.setTag(MoviesConstants.YOUTUBE_URL + video.getKey());
		viewHolder.ivShare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT, (String) view.getTag());
				mContext.startActivity(Intent.createChooser(intent, "Share"));
			}
		});
		return convertView;
	}



}
