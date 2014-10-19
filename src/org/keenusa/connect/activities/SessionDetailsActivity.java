package org.keenusa.connect.activities;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.joda.time.DateTime;
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
	private TextView tvProgramNameLabel, tvLocationLabel, tvDateLabel, tvProgramTypeLabel, tvProgramActiveDateLabel, tvAttCoachesLabel, tvAttAthletesLabel;
	private TextView tvProgramName, tvLocation, tvDate, tvProgramType, tvProgramActiveDate, tvAttCoaches, tvAttAthletes;
	public static final String DATE_FORMAT = "MM/dd/yyyy";

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
			
			tvDate.setText(formamtDate(session.getDate()));
			tvProgramName.setText(program.getName());
			if (program.getLocation() != null) {
				String address = program.getLocation().getAddress2() + " " +
						 program.getLocation().getCity() + " " + program.getLocation().getState() + " " + program.getLocation().getZipCode();
				tvLocation.setText(address);
			} else {
				tvLocation.setText("No Address");
			}
			if (program.getGeneralProgramType() != null) {
				tvProgramType.setText(program.getGeneralProgramType().toString());
			} else {
				tvProgramType.setText("No Program Type");
			}
			if (session.getProgram().getProgramTimes() != null) {
				tvProgramActiveDate.setText(session.getProgram().getProgramTimes());
			} else {
				tvProgramActiveDate.setText("12pm - 1pm");
			}
			tvAttCoaches.setText(session.getRegisteredCoachCount()+"");
			tvAttAthletes.setText(session.getRegisteredAthleteCount()+"");
		
		}

        private CharSequence formamtDate(DateTime date) {
			DateTime dateTime = session.getDate();
			String result = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(dateTime.toDate());
			return result;
		}

		private void setView() {
			tvProgramNameLabel = (TextView) findViewById(R.id.tvProgramNameLabel);
			tvLocationLabel = (TextView) findViewById(R.id.tvLocationLabel);
			tvDateLabel = (TextView) findViewById(R.id.tvDateLabel);
			tvProgramActiveDateLabel = (TextView) findViewById(R.id.tvProgramActiveDateLabel);
			tvProgramTypeLabel = (TextView) findViewById(R.id.tvProgramTypeLabel);
			tvAttCoachesLabel = (TextView) findViewById(R.id.tvAttCoachesLabel);
			tvAttAthletesLabel = (TextView) findViewById(R.id.tvAttAthletesLabel);
			
			tvProgramName = (TextView) findViewById(R.id.tvProgramName);
			tvLocation = (TextView) findViewById(R.id.tvLocation);
			tvDate = (TextView) findViewById(R.id.tvDate);
			tvProgramActiveDate = (TextView) findViewById(R.id.tvProgramActiveDate);
			tvProgramType = (TextView) findViewById(R.id.tvProgramType);
			tvAttCoaches = (TextView) findViewById(R.id.tvAttCoaches);
			tvAttAthletes = (TextView) findViewById(R.id.tvAttAthletes);
		}

		private void openCoachCheckIn(KeenSession session2, KeenProgram program2) {
			Intent i = new Intent(this, CoachesCheckInActivity.class);
			i.putExtra("session", session2);
			i.putExtra("program", program2);
			startActivity(i);
		}
		
		private void openAthleteCheckIn(KeenSession session2, KeenProgram program2) {
			Intent i = new Intent(this, AthleteCheckInActivity.class);
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
			int id = item.getItemId();
			if (id == R.id.miCoachesAttendance) {
				openCoachCheckIn(session, program);
				
			} else if (id == R.id.miAthletesAttendance) {
				openAthleteCheckIn(session, program);
				
			}
			return super.onOptionsItemSelected(item);
		}

		

		
}
