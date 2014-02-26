package com.hayal.dndro;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.hayal.dndro.adapter.NavDrawerListAdapter;
import com.hayal.dndro.fragment.RecentFragment;
import com.hayal.dndro.fragment.TopFragment;
import com.hayal.dndro.model.NavDrawerItem;
import com.hayal.dndro.util.Properties;

public class MainActivity extends Activity {
	MenuItem refreshMenuItem;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	private Menu optionsMenu;

	int currentPosition = 0;

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

		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1]));

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setBackgroundDrawable(
				Properties.getActionBarBackgroundDrawable());
		getActionBar().setIcon(R.drawable.ic_menu_icon);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setTitle("DNdro");

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(currentPosition);
		}

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		if (this.optionsMenu == null)
			this.optionsMenu = menu;

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		return true;
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			currentPosition = position;
			displayView(position);
		}
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		System.out
				.println("-------------  display view start -----------------");
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new TopFragment();
			break;
		case 1:
			fragment = new RecentFragment();
			break;
		default:
			Log.e("MainActivity", "Error in creating fragment");
			throw new RuntimeException("position not found exception");
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		System.out.println("-------------  display view end -----------------");

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Take appropriate action for each action item click
		switch (item.getItemId()) {

		case R.id.action_search:
			// search action
			return true;
		case R.id.action_refresh:
			// refresh
			refreshMenuItem = item;
			// load the data from server
			// new SyncData().execute();
			displayView(currentPosition);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		if (this.optionsMenu == null)
			this.optionsMenu = menu;
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void setRefreshActionButtonState(final boolean refreshing) {
		System.out.println("+++  refresh start   +++");

		if (optionsMenu != null) {
			final MenuItem refreshItem = optionsMenu
					.findItem(R.id.action_refresh);
			System.out.println("+++  optionsMenu not null   +++");

			if (refreshItem != null) {
				if (refreshing) {
					refreshItem.setActionView(R.layout.action_progress);
					refreshItem.expandActionView();
					System.out.println("here1");
				} else {
					refreshItem.collapseActionView();
					refreshItem.setActionView(null);
					System.out.println("here2");
				}
			} else {
				System.out.println("+++  refresh item null   +++");

			}
		} else {
			System.out.println("+++  optionsMenu null   +++");

		}
		System.out.println("+++  refresh end   +++");

	}

}
