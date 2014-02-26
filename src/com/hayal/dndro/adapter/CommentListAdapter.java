package com.hayal.dndro.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hayal.dndro.R;
import com.hayal.dndro.model.Comment;
import com.hayal.dndro.model.News;

public class CommentListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Comment> navDrawerItems;

	public CommentListAdapter() {
	};

	public CommentListAdapter(Context context, ArrayList<Comment> navDrawerItems) {
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
			convertView = mInflater.inflate(R.layout.list_comment, null);
		}

		LinearLayout li = (LinearLayout) convertView;
		Comment comment = navDrawerItems.get(position);
		TextView tvTitle = (TextView) convertView.findViewById(R.id.tvComment);
		tvTitle.setText(comment.getComment());

		TextView tvAuthor = (TextView) convertView
				.findViewById(R.id.tvCommentAuthor);
		tvAuthor.setText(comment.getAuthor());

		TextView tvVote = (TextView) convertView
				.findViewById(R.id.tvCommentVote);
		tvVote.setText("+" + comment.getVote());

		if (comment.getCommentList() != null) {
			for (Comment cmt : comment.getCommentList())
				createComments(cmt,li);
		}
		return convertView;
	}

	public void createComments(Comment comment,LinearLayout li) {
		System.out.println("here");
		System.out.println(comment.getAuthor());
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View vi = mInflater.inflate(R.layout.list_comment, null);
		LinearLayout llVi = (LinearLayout) vi;

		TextView tvTitle = (TextView) vi.findViewById(R.id.tvComment);
		tvTitle.setText(comment.getComment());

		TextView tvAuthor = (TextView) vi
				.findViewById(R.id.tvCommentAuthor);
		tvAuthor.setText(comment.getAuthor());

		TextView tvVote = (TextView) vi
				.findViewById(R.id.tvCommentVote);
		tvVote.setText("" + comment.getVote());
		
		li.addView(vi);
		if (comment.getCommentList() != null) {
			for (Comment cmt : comment.getCommentList())
				createComments(cmt,llVi);
		}
	}
}
