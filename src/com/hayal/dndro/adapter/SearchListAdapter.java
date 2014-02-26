package com.hayal.dndro.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hayal.dndro.OpenSiteActivity;
import com.hayal.dndro.R;
import com.hayal.dndro.model.News;

public class SearchListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<News> navDrawerItems;

	public SearchListAdapter() {
	};

	public SearchListAdapter(Context context, ArrayList<News> navDrawerItems) {
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.list, null);
		}

		TextView tvTitle = (TextView) convertView
				.findViewById(R.id.tvNewsTitleTop);
		tvTitle.setText(navDrawerItems.get(position).getTitle());

		TextView tvAuthor = (TextView) convertView
				.findViewById(R.id.tvNewsAuthorTop);

		TextView tvCommentCount = (TextView) convertView
				.findViewById(R.id.tvNewsCountTop);

		TextView tvVote = (TextView) convertView.findViewById(R.id.tvNewsVote);

		tvCommentCount.setVisibility(View.GONE);
		tvVote.setVisibility(View.GONE);
		tvAuthor.setVisibility(View.GONE);

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context, OpenSiteActivity.class);
				intent.putExtra("news", navDrawerItems.get(position));
				context.startActivity(intent);

			}
		});

		return convertView;
	}
}
