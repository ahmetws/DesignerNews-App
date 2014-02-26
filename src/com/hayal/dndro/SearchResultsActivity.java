package com.hayal.dndro;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.hayal.dndro.adapter.SearchListAdapter;
import com.hayal.dndro.model.News;
import com.hayal.dndro.util.JSONParser;
import com.hayal.dndro.util.Properties;

public class SearchResultsActivity extends Activity {
	private TextView tvQuery;
	JSONParser jParser = new JSONParser();
	private ListView lvVideos;
	private ArrayList<News> newsList;
	SearchListAdapter searchAdapter;
	private Activity mContext;
	String query = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

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

		setContentView(R.layout.activity_search_results);

		// enabling action bar app icon and behaving it as toggle button
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(Properties
				.getActionBarBackgroundDrawable());
		actionBar.setIcon(R.drawable.ic_menu_icon);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle("DNdro Search");

		tvQuery = (TextView) findViewById(R.id.tvQuery);
		mContext = this;
		newsList = new ArrayList<News>();

		searchAdapter = new SearchListAdapter(mContext, newsList);
		lvVideos = (ListView) findViewById(R.id.lvSearch);

		// adapteri listview a baglama
		lvVideos.setAdapter(searchAdapter);

		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	public boolean isConnected() {

		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info == null)
			return false;
		else
			return info.isConnectedOrConnecting();

	}

	/**
	 * Handling intent data
	 */
	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			query = intent.getStringExtra(SearchManager.QUERY);

			/**
			 * Use this query to display search results like 1. Getting the data
			 * from SQLite and showing in listview 2. Making webrequest and
			 * displaying the data For now we just display the query only
			 */
			tvQuery.setText("Results for: " + query);
			new SearchDataTask().execute();
		}

	}

	private class SearchDataTask extends AsyncTask<String, Integer, String> {

		protected void onPreExecute() {
			mContext.setProgressBarIndeterminateVisibility(true);

		}

		protected void onPostExecute(String result) {
			System.out.println("onpostexecute start");
			System.out.println("news list size: " + newsList.size());
			searchAdapter.notifyDataSetChanged();

			System.out.println("onpostexecute end");
			mContext.setProgressBarIndeterminateVisibility(false);
		}

		@Override
		protected String doInBackground(String... arg0) {
			String searchUrl = Properties.BASE_URL
					+ "search?utf8=true&commit=Search&format=json";

			Log.d("SearchNews", "searchUrl: " + searchUrl);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("query", query));
			JSONArray stories = jParser.searchFromUrl(searchUrl, params);

			if (stories == null)
				return "false";
			Log.d("SearchNews", "Search News: " + stories);

			try {

				for (int i = 0; i < stories.length(); i++) {
					JSONObject story = stories.getJSONObject(i);

					String title = story.getString(Properties.TAG_TITLE);
					String author = "";
					String url = story.getString(Properties.TAG_URL);
					Integer id = story.getInt(Properties.TAG_ID);
					if (url == null || "".equals(url)) {
						url = "https://news.layervault.com/click/stories/" + id;
					}
					Integer commentCount = -1;
					Integer voteCount = -1;
					newsList.add(new News(id, title, author, url, commentCount,
							voteCount));
				}
			} catch (JSONException e) {
				Log.e("SearchNews", e.getMessage());
			}

			return null;
		}
	}
}
