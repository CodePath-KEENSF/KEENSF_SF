package org.keenusa.connect.helpers;

import org.keenusa.connect.R;
import org.keenusa.connect.activities.AthleteListActivity;
import org.keenusa.connect.activities.CheckInActivity;
import org.keenusa.connect.activities.CoachListActivity;
import org.keenusa.connect.activities.SessionListActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class KeenNavigationDrawer extends DrawerLayout {

	private ActionBarDrawerToggle drawerToggle;
	private ListView lvDrawer;
	private ArrayAdapter<String> drawerAdapter;
	private String[] drawerNavItems;

	public KeenNavigationDrawer(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public KeenNavigationDrawer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public KeenNavigationDrawer(Context context) {
		super(context);
	}

	// setupDrawerConfiguration((ListView) findViewById(R.id.lvDrawer),
	// R.layout.drawer_list_item, R.id.flContent);
	public void setupDrawerConfiguration(ListView drawerListView,
			int drawerItemRes) {
		// Setup navigation items array
		drawerNavItems = getResources().getStringArray(
				R.array.navigation_drawer_array);
		// Set the adapter for the list view
		drawerAdapter = new ArrayAdapter<String>(getActivity(), drawerItemRes,
				drawerNavItems);
		// Setup drawer list view and related adapter
		lvDrawer = drawerListView;
		lvDrawer.setAdapter(drawerAdapter);
		// Setup item listener
		lvDrawer.setOnItemClickListener(new FragmentDrawerItemListener());
		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		drawerToggle = setupDrawerToggle();
		setDrawerListener(drawerToggle);
		// set a custom shadow that overlays the main content when the drawer
		setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// Setup action buttons
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}

	/** Swaps fragments in the main content view */
	public void selectDrawerItem(int position) {

		// Highlight the selected item, update the title, and close the drawer
		lvDrawer.setItemChecked(position, true);

		// Switch to another activity
		Intent intent = null;
		switch (position) {
		case 0:
			intent = new Intent(getActivity(), SessionListActivity.class);
			break;
		case 1:
			intent = new Intent(getActivity(), CheckInActivity.class);
			break;
		case 2:
			intent = new Intent(getActivity(), AthleteListActivity.class);
			break;
		case 3:
			intent = new Intent(getActivity(), CoachListActivity.class);
			break;
		default:
			intent = new Intent(getActivity(), SessionListActivity.class);
			break;
		}
		
		getActivity().startActivity(intent);

		// setTitle(navItem.getTitle());
		closeDrawer(lvDrawer);
	}

	public ActionBarDrawerToggle getDrawerToggle() {
		return drawerToggle;
	}

	private Activity getActivity() {
		return (Activity) getContext();
	}

	private ActionBar getActionBar() {
		return getActivity().getActionBar();
	}

	private void setTitle(CharSequence title) {
		getActionBar().setTitle(title);
	}

	private class FragmentDrawerItemListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectDrawerItem(position);
		}
	}

	private ActionBarDrawerToggle setupDrawerToggle() {
		return new ActionBarDrawerToggle(getActivity(), /* host Activity */
		this, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				// setTitle(getCurrentTitle());
				getActivity().invalidateOptionsMenu(); // call
														// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				// setTitle("Navigate");
				getActivity().invalidateOptionsMenu(); // call
														// onPrepareOptionsMenu()
			}
		};
	}

	public boolean isDrawerOpen() {
		return isDrawerOpen(lvDrawer);
	}

}
