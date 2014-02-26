package com.hayal.dndro.util;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.PaintDrawable;

public class Properties {

	public static String BASE_URL = "https://news.layervault.com/";

	// JSON News TAGS
	public static final String TAG_STORIES = "stories";
	public static final String TAG_TITLE = "title";
	public static final String TAG_AUTHOR = "submitter_display_name";
	public static final String TAG_SEARCH_AUTHOR = "submitter";
	public static final String TAG_SEARCH_AUTHOR_DISPLAY = "display_name";
	public static final String TAG_URL = "url";
	public static final String TAG_ID = "id";
	public static final String TAG_COMMENT_COUNT = "num_comments";
	public static final String TAG_VOTE_COUNT = "vote_count";

	public static final String TAG_COMMENT_BODY = "body";
	public static final String TAG_COMMENT_VOTE = "upvotes_count";
	public static final String TAG_COMMENT_AUTHOR = "user_display_name";
	public static final String TAG_COMMENT_DEPTH = "depth";
	public static final String TAG_COMMENT_COMMENTS = "comments";

	public static Drawable getActionBarBackgroundDrawable() {

		int color1 = Color.parseColor("#2D72D9");
		int color2 = Color.parseColor("#2D72D9");
		// to change the color of the action bar at runtime
		PaintDrawable front = new PaintDrawable(color1);
		PaintDrawable bottom = new PaintDrawable(color2);
		Drawable[] d = { bottom, front };
		LayerDrawable drawable = new LayerDrawable(d);
		drawable.setLayerInset(1, 0, 0, 0, 3);

		return drawable;

	}

}
