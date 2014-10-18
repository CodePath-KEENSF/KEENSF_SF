package org.keenusa.connect.activities;

import org.keenusa.connect.R;
import org.keenusa.connect.models.KeenProgram;
import org.keenusa.connect.models.KeenSession;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class SessionDetailsActivity extends FragmentActivity {
	private TextView tvProgramNameLabel, tvLocationLabel, tvDateLabel, tvProgramTypeLabel, tvProgramActiveDateLabel;
	private TextView tvProgramName, tvLocation, tvDate, tvProgramType, tvProgramActiveDate;
	public static final String DATE_FORMAT = "MM/dd/yyyy";

	// this is the session that is passed from session list activity or session list activity could pass session id if we use SQLite
		KeenSession session;
		KeenProgram program;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_session_details);
			setView();
			setData();
		}

		private void setData() {
			session = (KeenSession) getIntent().getSerializableExtra("session");
			program = (KeenProgram) getIntent().getSerializableExtra("program");
			
			tvDate.setText(session.getDate().toString().substring(0, 10));
			tvProgramName.setText(program.getName());
			String address = /*program.getLocation().getAddress1() + */program.getLocation().getAddress2() + " " +
					 program.getLocation().getCity() + " " + program.getLocation().getState() + " " + program.getLocation().getZipCode();
			tvLocation.setText(address);
			tvProgramType.setText(program.getGeneralProgramType().toString());
			tvProgramActiveDate.setText(session.getProgram().getProgramTimes());

		}

//		@SuppressWarnings("deprecation")
//		private CharSequence setDateTime(String dateTime) {
//			Date date = new Date(dateTime);
//			DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
//			return dateFormat.format(date);
//		}

		private void setView() {
			tvProgramNameLabel = (TextView) findViewById(R.id.tvProgramNameLabel);
			tvLocationLabel = (TextView) findViewById(R.id.tvLocationLabel);
			tvDateLabel = (TextView) findViewById(R.id.tvDateLabel);
			tvProgramActiveDateLabel = (TextView) findViewById(R.id.tvProgramActiveDateLabel);
			tvProgramTypeLabel = (TextView) findViewById(R.id.tvProgramTypeLabel);
			tvProgramName = (TextView) findViewById(R.id.tvProgramName);
			tvLocation = (TextView) findViewById(R.id.tvLocation);
			tvDate = (TextView) findViewById(R.id.tvDate);
			tvProgramActiveDate = (TextView) findViewById(R.id.tvProgramActiveDate);
			tvProgramType = (TextView) findViewById(R.id.tvProgramType);

			//		tvProgramName.setText(program.getName());
			//		tvLocation.setLocation(program.getLocation());
			//		tvDate.setText(program.getProgramTimes());
			//		tvSessionPeriod.setText(program.getProgramPeriod);
		}

//	private void setupTabs() {
//		ActionBar actionBar = getActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//		actionBar.setDisplayShowTitleEnabled(true);
//
//		Tab coaches = actionBar.newTab().setText("Coaches")
//				//			.setIcon(R.drawable.ic_home)
//				.setTag("CoachesFragment")
//				.setTabListener(new FragmentTabListener<CoachesFragment>(R.id.flContainer, this, "Coaches", CoachesFragment.class));
//
//		actionBar.addTab(coaches);
//		actionBar.selectTab(coaches);
//
//		Tab athletes = actionBar.newTab().setText("Athletes")
//				//			.setIcon(R.drawable.ic_mentions)
//				.setTag("AtheletsFragment")
//				.setTabListener(new FragmentTabListener<AtheletsFragment>(R.id.flContainer, this, "Athletes", AtheletsFragment.class));
//
//		actionBar.addTab(athletes);
//	}
		private void openCoachCheckIn(KeenSession session2, KeenProgram program2) {
			Intent i = new Intent(this, CoachesCheckInActivity.class);
			i.putExtra("session", session2);
			i.putExtra("program", program2);
			startActivity(i);
		}
		
		private void openAthleteCheckIn(KeenSession session2, KeenProgram program2) {
			Intent i = new Intent(this, SessionDetailsActivity.class);
			i.putExtra("session", session2);
			i.putExtra("program", program2);
			startActivity(i);
		}
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.attendance, menu);
			return true;
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// Handle action bar item clicks here. The action bar will
			// automatically handle clicks on the Home/Up button, so long
			// as you specify a parent activity in AndroidManifest.xml.
			int id = item.getItemId();
			if (id == R.id.miCoachesAttendance) {
				openCoachCheckIn(session, program);
				
			} else if (id == R.id.miAthletesAttendance) {
				openAthleteCheckIn(session, program);
				
			}
			return super.onOptionsItemSelected(item);
		}

		

		
}
