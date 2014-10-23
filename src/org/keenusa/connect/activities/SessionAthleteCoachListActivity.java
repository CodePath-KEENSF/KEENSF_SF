package org.keenusa.connect.activities;

import org.keenusa.connect.R;
import org.keenusa.connect.fragments.AtheletsFragment;
import org.keenusa.connect.fragments.CoachesFragment;
import org.keenusa.connect.fragments.SessionsFragment;

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

public class SessionAthleteCoachListActivity extends FragmentActivity implements TabListener{

	private FragmentPagerAdapter adapterViewPager;
	private ViewPager vpPager;
	
	private ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_session_athlete_coach_list);
		
		setupFragmentPager();
		setupTabs();
	}

//	close the app on back button press?	
//	@Override
//	public void onBackPressed() {
//		finish();
//	}

	private void setupTabs() {
		
		Tab tabFirst;
		Tab tabSecond;
		Tab tabThird;

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);

		tabFirst = actionBar.newTab();
		tabFirst.setText(getResources().getString(R.string.title_activity_session_list));
		tabFirst.setTabListener(this);
		actionBar.addTab(tabFirst);
		actionBar.selectTab(tabFirst);
		
		tabSecond = actionBar.newTab();
		tabSecond.setText(getResources().getString(R.string.title_activity_athlete_list));
		tabSecond.setTabListener(this);
		actionBar.addTab(tabSecond);
		
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
		vpPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	private void setupFragmentPager() {
		vpPager = (ViewPager) findViewById(R.id.vpPager);
		adapterViewPager = new ListsPagerAdapter(getSupportFragmentManager());
		vpPager.setAdapter(adapterViewPager);
		
		// Attach the page change listener inside the activity
		vpPager.setOnPageChangeListener(new OnPageChangeListener() {
			
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

	public static class ListsPagerAdapter extends FragmentPagerAdapter {
		private static int NUM_ITEMS = 3;

		public ListsPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		// Returns total number of pages
		@Override
		public int getCount() {
			return NUM_ITEMS;
		} 

		// Returns the fragment to display for that page
		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0: // Fragment # 0 - Session List
				return SessionsFragment.newInstance();
			case 1: // Fragment # 1 - Athlete List
				return AtheletsFragment.newInstance();
			case 2: // Fragment # 2 - Coach List
				return CoachesFragment.newInstance();
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
}
