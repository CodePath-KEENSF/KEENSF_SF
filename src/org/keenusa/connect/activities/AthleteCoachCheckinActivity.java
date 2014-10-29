package org.keenusa.connect.activities;

import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.data.daos.AthleteAttendanceDAO;
import org.keenusa.connect.data.daos.CoachAttendanceDAO;
import org.keenusa.connect.data.daos.ProgramEnrollmentDAO;
import org.keenusa.connect.data.daos.SessionDAO;
import org.keenusa.connect.fragments.AddCoachToCheckinFragment.AddCoachDialogListener;
import org.keenusa.connect.fragments.AthleteCheckinFragment;
import org.keenusa.connect.fragments.CoachCheckinFragment;
import org.keenusa.connect.models.AthleteAttendance;
import org.keenusa.connect.models.Coach;
import org.keenusa.connect.models.CoachAttendance;
import org.keenusa.connect.models.KeenProgramEnrolment;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.utilities.CheckinMenuActions;
import org.keenusa.connect.utilities.IntentCode;
import org.keenusa.connect.utilities.PostCheckinUpdate;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AthleteCoachCheckinActivity extends FragmentActivity implements TabListener, AddCoachDialogListener {

	private FragmentPagerAdapter adapterViewPager;
	private ViewPager vpPagerCheckin;

	private ActionBar actionBar;

	private KeenSession session;
	KeenCivicoreClient client;

	AthleteCheckinFragment athleteCheckinFragment;
	CoachCheckinFragment coachCheckinFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_athlete_coach_checkin);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		client = new KeenCivicoreClient(this);
		getData();

	}

	private void getData() {
		long sessionId = getIntent().getLongExtra("SESSION_ID", 0);
		new LoadSessionDataTask().execute(sessionId);
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

	public boolean onPrepareOptionsMenu(Menu menu) {
//		if (CheckinMenuActions.editMode == true) {
//			MenuItem miSaveCheckin = menu.findItem(R.id.miSaveCheckin);
//			miSaveCheckin.setVisible(true);
//
//			//			MenuItem miEditCheckin = menu.findItem(R.id.miEditCheckin);
//			//			miEditCheckin.setVisible(false);
//		} else {
//			//			MenuItem miEditCheckin = menu.findItem(R.id.miEditCheckin);
//			//			miEditCheckin.setVisible(true);
//
//			MenuItem miSaveCheckin = menu.findItem(R.id.miSaveCheckin);
//			miSaveCheckin.setVisible(false);
//		}

		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.check_in, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		/*	if (item.getItemId() == R.id.miEditCheckin) {
			//		CheckinEditMode.editMode = true;
			//		invalidateOptionsMenu();
			return true;
		} else if (item.getItemId() == R.id.miSaveCheckin) {
			//		CheckinEditMode.editMode = false;
			//		invalidateOptionsMenu();
			postAttendance();
			return true; 
		} else */ if (item.getItemId() == android.R.id.home) {
			UpdateSession();
			overridePendingTransition(R.anim.left_in, R.anim.right_out);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	public void onBackPressed() {
		UpdateSession();
		finish();
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
	}

	private void UpdateSession() {
		Intent finishCheckinIntent = new Intent();
		finishCheckinIntent.putExtra("session", session);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
		setResult(IntentCode.RESULT_OK, finishCheckinIntent);
		finish();
	}

	private void postAttendance() {
		PostCheckinUpdate.done = 0;
		coachCheckinFragment.postAttendance();
		athleteCheckinFragment.postAttendance();
	}

	@Override
	public void onFinishAddDialog(Coach coach) {
		coachCheckinFragment.addCoach(coach);
	}

	public void refreshCoachAttendance() {
		coachCheckinFragment.refreshAttendance();
	}

	public void refreshAthleteAttendance() {
		athleteCheckinFragment.refreshAttendance();
	}

	private class LoadSessionDataTask extends AsyncTask<Long, Void, KeenSession> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected KeenSession doInBackground(Long... params) {
			SessionDAO sessionDAO = new SessionDAO(AthleteCoachCheckinActivity.this);
			KeenSession session = sessionDAO.getSessionById(params[0]);

			ProgramEnrollmentDAO peDAO = new ProgramEnrollmentDAO(AthleteCoachCheckinActivity.this);
			List<KeenProgramEnrolment> enrolments = peDAO.getKeenProgramEnrollments(session.getProgram().getId());
			session.getProgram().setProgramEnrolments(enrolments);

			AthleteAttendanceDAO aaDAO = new AthleteAttendanceDAO(AthleteCoachCheckinActivity.this);
			List<AthleteAttendance> aAttehndances = aaDAO.getAthleteAttendancesBySessionId(session.getId());
			session.setAthleteAttendance(aAttehndances);

			CoachAttendanceDAO caDAO = new CoachAttendanceDAO(AthleteCoachCheckinActivity.this);
			List<CoachAttendance> cAttehndances = caDAO.getCoachAttendancesBySessionId(session.getId());
			session.setCoachAttendance(cAttehndances);

			return session;
		}

		@Override
		protected void onPostExecute(KeenSession dbSession) {
			session = dbSession;
			setupFragmentPager();
			setupTabs();
		}

	}
}
