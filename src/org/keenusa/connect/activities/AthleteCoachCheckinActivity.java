package org.keenusa.connect.activities;

import org.keenusa.connect.R;
import org.keenusa.connect.fragments.AthleteCheckinFragment;
import org.keenusa.connect.fragments.CoachCheckinFragment;
import org.keenusa.connect.models.KeenSession;

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

public class AthleteCoachCheckinActivity extends FragmentActivity implements TabListener{
	
	private FragmentPagerAdapter adapterViewPager;
	private ViewPager vpPagerCheckin;
	
	private ActionBar actionBar;
	
	private KeenSession session;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_athlete_coach_checkin);
		
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
				return AthleteCheckinFragment.newInstance(session);
			case 1: // Fragment # 1 - Coach List
				return CoachCheckinFragment.newInstance(session);
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
