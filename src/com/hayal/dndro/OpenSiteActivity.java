package com.hayal.dndro;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hayal.dndro.model.News;
import com.hayal.dndro.util.Properties;

public class OpenSiteActivity extends Activity {
	public News news;

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
		Bundle bundle = getIntent().getExtras();
		news = bundle.getParcelable("news");

		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(Properties
				.getActionBarBackgroundDrawable());
		actionBar.setIcon(R.drawable.ic_menu_icon);
		if (news.getTitle().length() > 30) {
			actionBar.setTitle(news.getTitle().substring(0, 30) + "...");
		} else {
			actionBar.setTitle(news.getTitle());
		}
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		setContentView(R.layout.activity_open_site);

		WebView webView = (WebView) findViewById(R.id.wvSite);

		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		webView.loadUrl(news.getUrl());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.open_site, menu);
		return true;
	}

	public boolean isConnected() {

		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info == null)
			return false;
		else
			return info.isConnectedOrConnecting();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {

		case R.id.action_share:
			share();
			return true;
		case R.id.action_comment:
			showComments();
			return true;
		}
		finish();
		return true;

	}

	public void showComments() {

		Intent intent = new Intent(getApplicationContext(),
				CommentActivity.class);

		intent.putExtra("storyNo", news.getId());

		startActivity(intent);

	}

	public void share() {

		// create the send intent
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

		// set the type
		shareIntent.setType("text/plain");

		// add a subject
		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "DNdro");

		// build the body of the message to be shared
		String shareMessage = news.getTitle() + " " + news.getUrl();

		// add the message
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);

		// start the chooser for sharing
		startActivity(Intent.createChooser(shareIntent, "Share News"));

	}
}
