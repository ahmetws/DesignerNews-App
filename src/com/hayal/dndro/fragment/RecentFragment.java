package com.hayal.dndro.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hayal.dndro.R;
import com.hayal.dndro.adapter.NewsListAdapter;
import com.hayal.dndro.model.News;
import com.hayal.dndro.util.JSONParser;
import com.hayal.dndro.util.Properties;

public class RecentFragment extends Fragment {

	View rootView;
	JSONParser jParser = new JSONParser();
	private ListView lvVideos;
	private ArrayList<News> newsList;
	NewsListAdapter friendListAdapter;
	private Activity mContext;

	JSONArray stories = null;

	public RecentFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_recent, container,
				false);
		mContext = getActivity();
		newsList = new ArrayList<News>();

		friendListAdapter = new NewsListAdapter(mContext, newsList);
		lvVideos = (ListView) rootView.findViewById(R.id.lvRecent);

		// adapteri listview a baglama
		lvVideos.setAdapter(friendListAdapter);

		new RecentDataTask().execute();
		return rootView;
	}

	private class RecentDataTask extends AsyncTask<String, Integer, String> {

		protected void onPreExecute() {
			mContext.setProgressBarIndeterminateVisibility(true);
		}

		protected void onPostExecute(String result) {
			System.out.println("onpostexecute start");
			System.out.println("news list size: " + newsList.size());
			friendListAdapter.notifyDataSetChanged();

			System.out.println("onpostexecute end");
			// r.setText(k);
			mContext.setProgressBarIndeterminateVisibility(false);

		}

		@Override
		protected String doInBackground(String... arg0) {
			String topDataURL = Properties.BASE_URL + "new/?format=json";

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			JSONObject json = jParser.getJSONFromUrl(topDataURL, params);

			if (json == null)
				return "false";
			Log.d("RecentNews", "Recent News: " + json);

			// Getting JSON Array node
			try {
				stories = json.getJSONArray(Properties.TAG_STORIES);

				for (int i = 0; i < stories.length(); i++) {
					JSONObject story = stories.getJSONObject(i);

					String title = story.getString(Properties.TAG_TITLE);
					String author = story.getString(Properties.TAG_AUTHOR);
					String url = story.getString(Properties.TAG_URL);

					Integer commentCount = story
							.getInt(Properties.TAG_COMMENT_COUNT);
					Integer voteCount = story.getInt(Properties.TAG_VOTE_COUNT);
					Integer id = story.getInt(Properties.TAG_ID);

					newsList.add(new News(id, title, author, url, commentCount,
							voteCount));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

	}
}
