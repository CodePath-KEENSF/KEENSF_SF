package org.keenusa.connect.activities;

import org.keenusa.connect.R;
import org.keenusa.connect.fragments.AtheletsFragment;
import org.keenusa.connect.fragments.CoachesFragment;
import org.keenusa.connect.fragments.SessionsFragment;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.utilities.DebugInfo;
import org.keenusa.connect.utilities.IntentCode;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.content.Intent;
import android.os.Bundle;
import android.service.textservice.SpellCheckerService.Session;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

public class SessionAthleteCoachListActivity extends FragmentActivity implements TabListener{

	private FragmentPagerAdapter adapterViewPager;
	private ViewPager vpPager;
	
	private ActionBar actionBar;
	
	private SessionsFragment sessionsFragment;
	
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

	public class ListsPagerAdapter extends FragmentPagerAdapter {

		public ListsPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		// Returns total number of pages
		@Override
		public int getCount() {
			return 3;
		} 

		// Returns the fragment to display for that page
		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0: // Fragment # 0 - Session List
				sessionsFragment = SessionsFragment.newInstance();
				return sessionsFragment;
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == IntentCode.RESULT_OK
				&& requestCode == IntentCode.REQUEST_CODE) {
			KeenSession session = (KeenSession)data.getSerializableExtra("session");
			sessionsFragment.updateSession(session);
			
		} else if (resultCode == IntentCode.CHECK_IN_FAIL
				&& requestCode == IntentCode.REQUEST_CODE) {
			DebugInfo.showToast(this, "check-in failed");
		}

	}
	
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
	}
}
