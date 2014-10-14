package org.keenusa.connect.activities;

import java.util.ArrayList;

import org.keenusa.connect.R;
import org.keenusa.connect.adapters.SessionListItemAdapter;
import org.keenusa.connect.helpers.KeenNavigationDrawer;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.models.TestDataFactory;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SessionListActivity extends Activity {

	// Main Session List View
	private ListView lvSessionList;
	private ArrayList<KeenSession>sessionList;
	private SessionListItemAdapter sessionListAdapter;
	
	// Navigation Drawer
	private KeenNavigationDrawer dlDrawer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_session_list);
		getViews();
		setAdapter();
		setNavigationDrawer();
		setOnClickListeners();
	}

	private void setOnClickListeners() {
		lvSessionList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapter, View view,
					int pos, long id) {
				openSessionDetails(pos);
			}
		});
	}

	private void setNavigationDrawer() {
		// Setup drawer view
		dlDrawer.setupDrawerConfiguration((ListView) findViewById(R.id.lvDrawer), 
                     R.layout.drawer_nav_item);
	}

	private void openSessionDetails(int pos) {
		Intent i = new Intent(this, SessionDetailsActivity.class);
		startActivity(i);
	}

	private void setAdapter() {
		sessionList = new ArrayList<KeenSession>(TestDataFactory.getInstance().getSessionList());
		sessionListAdapter = new SessionListItemAdapter(this, sessionList);
		lvSessionList.setAdapter(sessionListAdapter);
	}

	private void getViews() {
		lvSessionList = (ListView)findViewById(R.id.lvSessionList);
		dlDrawer = (KeenNavigationDrawer) findViewById(R.id.drawer_layout);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		if (dlDrawer.isDrawerOpen()) {
			// Uncomment to hide menu items                        
			// menu.findItem(R.id.mi_test).setVisible(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		// Uncomment to inflate menu items to Action Bar      
		// inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (dlDrawer.getDrawerToggle().onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		dlDrawer.getDrawerToggle().syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		dlDrawer.getDrawerToggle().onConfigurationChanged(newConfig);
	}
}
