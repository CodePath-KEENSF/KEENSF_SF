package org.keenusa.connect.activities;

import org.keenusa.connect.R;
import org.keenusa.connect.fragments.AthleteCheckinFragment;
import org.keenusa.connect.fragments.CoachCheckinFragment;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.utilities.CheckinEditMode;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AthleteCoachCheckinActivity extends FragmentActivity implements TabListener{
	
	private FragmentPagerAdapter adapterViewPager;
	private ViewPager vpPagerCheckin;
	
	private ActionBar actionBar;
	
	private KeenSession session;
	KeenCivicoreClient client;
	
	AthleteCheckinFragment athleteCheckinFragment;
	CoachCheckinFragment coachCheckinFragment;
	boolean editMode = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_athlete_coach_checkin);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		client = new KeenCivicoreClient(this);
		getData();
		setupFragmentPager();
		setupTabs();
	}
	
	private void getData() {
		session = (KeenSession) getIntent().getSerializableExtra("session");
	}

	private void setupTabs() {
		
		Tab tabFirst;
		Tab tabSecond;

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);

		tabFirst = actionBar.newTab();
		tabFirst.setText(getResources().getString(R.string.title_activity_athlete_list));
		tabFirst.setTabListener(this);
		actionBar.addTab(tabFirst);
		
		tabSecond = actionBar.newTab();
		tabSecond.setText(getResources().getString(R.string.title_activity_coach_list));
		tabSecond.setTabListener(this);
		actionBar.addTab(tabSecond);
	}

	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		vpPagerCheckin.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	private void setupFragmentPager() {
		vpPagerCheckin = (ViewPager) findViewById(R.id.vpPagerCheckin);
		adapterViewPager = new ListsPagerAdapter(getSupportFragmentManager());
		vpPagerCheckin.setAdapter(adapterViewPager);
		
		// Attach the page change listener inside the activity
		vpPagerCheckin.setOnPageChangeListener(new OnPageChangeListener() {
			
			// This method will be invoked when a new page becomes selected.
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
			
			// This method will be invoked when the current page is scrolled
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// Code goes here
			}
			
			// Called when the scroll state changes: 
			// SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
			@Override
			public void onPageScrollStateChanged(int state) {
				// Code goes here
			}
		});
	}

	public class ListsPagerAdapter extends FragmentPagerAdapter {

		public ListsPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		// Returns total number of pages
		@Override
		public int getCount() {
			return 2;
		} 

		// Returns the fragment to display for that page
		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0: // Fragment # 0 - Athlete List
				athleteCheckinFragment = AthleteCheckinFragment.newInstance(session, client);
				return athleteCheckinFragment;
			case 1: // Fragment # 1 - Coach List
				coachCheckinFragment = CoachCheckinFragment.newInstance(session, client);
				return coachCheckinFragment;
			default:
				return null;
			}
		}

		// Returns the page title for the top indicator
		@Override
		public CharSequence getPageTitle(int position) {
			return "Page " + position;
		}
	}
	
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		if(editMode == true){
		    MenuItem miSaveCheckin = menu.findItem(R.id.miSaveCheckin);
		    miSaveCheckin.setVisible(true);

			MenuItem miEditCheckin = menu.findItem(R.id.miEditCheckin);
			miEditCheckin.setVisible(false);
		    return true;

		}else{
			MenuItem miEditCheckin = menu.findItem(R.id.miEditCheckin);
			miEditCheckin.setVisible(true);

		    MenuItem miSaveCheckin = menu.findItem(R.id.miSaveCheckin);
		    miSaveCheckin.setVisible(false);

		    CheckinEditMode.editMode = false;
		    
		    return true;
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.check_in, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
	if (item.getItemId() == R.id.miEditCheckin){
		editMode = true;
		CheckinEditMode.editMode = true;
		invalidateOptionsMenu();
		return true;
	}else if(item.getItemId() == R.id.miSaveCheckin){
		editMode = false;
		CheckinEditMode.editMode = false;
		invalidateOptionsMenu();
		postAttendance();
		return true;		
	}else if (item.getItemId() == android.R.id.home) {
    	Log.d("temp", "finishing activity");
        finish();
        return true;
    } else {
        return super.onOptionsItemSelected(item);
    }
}

	private void postAttendance() {
		coachCheckinFragment.postAttendance();
	}

}
