package com.hayal.dndro;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;

import com.hayal.dndro.adapter.CommentListAdapter;
import com.hayal.dndro.model.Comment;
import com.hayal.dndro.util.JSONParser;
import com.hayal.dndro.util.Properties;

public class CommentActivity extends Activity {
	JSONParser jParser = new JSONParser();
	private ListView lvVideos;
	private ArrayList<Comment> commentList;
	CommentListAdapter commentAdapter;
	private Activity mContext;
	String query = "";

	int storyNo = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!isConnected()) {
			new AlertDialog.Builder(this)
					.setTitle("no internet access")
					.setMessage("please connect internet first")
					.setPositiveButton(
							"OK",
							new android.content.DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							}).show();
		}
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_comment);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setBackgroundDrawable(Properties.getActionBarBackgroundDrawable());
		getActionBar().setIcon(R.drawable.ic_menu_icon);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setTitle("DNdro Comment");

		mContext = this;
		commentList = new ArrayList<Comment>();

		commentAdapter = new CommentListAdapter(mContext, commentList);
		lvVideos = (ListView) findViewById(R.id.lvComment);

		storyNo = getIntent().getExtras().getInt("storyNo");

		lvVideos.setAdapter(commentAdapter);

		new CommentDataTask().execute();

	}

	public boolean isConnected() {

		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info == null)
			return false;
		else
			return info.isConnectedOrConnecting();

	}

	private class CommentDataTask extends AsyncTask<String, Integer, String> {

		protected void onPreExecute() {
			mContext.setProgressBarIndeterminateVisibility(true);

		}

		protected void onPostExecute(String result) {
			System.out.println("onpostexecute start");
			System.out.println("news list size: " + commentList.size());
			commentAdapter.notifyDataSetChanged();

			System.out.println("onpostexecute end");
			mContext.setProgressBarIndeterminateVisibility(false);

		}

		@Override
		protected String doInBackground(String... arg0) {
			String commentUrl = Properties.BASE_URL + "stories/" + storyNo
					+ "/?format=json";

			Log.d("Comment", "Comment url: " + commentUrl);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = jParser.getComments(commentUrl, params);

			if (json == null)
				return "false";
			Log.d("Comment", "Comment News: " + json);
			Comment cmt = null;
			try {
				JSONObject story = json.getJSONObject("story");

				JSONArray comments = story.getJSONArray("comments");
				for (int i = 0; i < comments.length(); i++) {
					JSONObject comment = comments.getJSONObject(i);

					String title = comment
							.getString(Properties.TAG_COMMENT_BODY);
					String author = comment
							.getString(Properties.TAG_COMMENT_AUTHOR);
					Integer vote = comment.getInt(Properties.TAG_COMMENT_VOTE);
					Integer depth = comment
							.getInt(Properties.TAG_COMMENT_DEPTH);

					JSONArray commentsArr = comment
							.getJSONArray(Properties.TAG_COMMENT_COMMENTS);

					cmt = new Comment(title, author, vote, depth);
					if (commentsArr != null) {
						cmt.setCommentList(getCommentsFromJsonArray(commentsArr));
					}

					commentList.add(cmt);
				}
			} catch (JSONException e) {
				Log.e("SearchNews", e.getMessage());
			}

			return null;
		}

		public List<Comment> getCommentsFromJsonArray(JSONArray json)
				throws JSONException {
			Comment cmt = null;
			List<Comment> list = new ArrayList<Comment>();
			for (int i = 0; i < json.length(); i++) {
				JSONObject comment = json.getJSONObject(i);

				String title = comment.getString(Properties.TAG_COMMENT_BODY);
				String author = comment
						.getString(Properties.TAG_COMMENT_AUTHOR);
				Integer vote = comment.getInt(Properties.TAG_COMMENT_VOTE);
				Integer depth = comment.getInt(Properties.TAG_COMMENT_DEPTH);

				JSONArray commentsArr = comment
						.getJSONArray(Properties.TAG_COMMENT_COMMENTS);

				cmt = new Comment(title, author, vote, depth);
				if (commentsArr != null) {
					cmt.setCommentList(getCommentsFromJsonArray(commentsArr));
				}

				list.add(cmt);
			}

			return list;
		}
	}
}
