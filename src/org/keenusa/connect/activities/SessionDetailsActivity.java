package org.keenusa.connect.activities;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.joda.time.DateTime;
import org.keenusa.connect.R;
import org.keenusa.connect.fragments.MassMessageFragment;
import org.keenusa.connect.fragments.UpdateAthleteProfileFragment;
import org.keenusa.connect.models.KeenProgram;
import org.keenusa.connect.models.KeenSession;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SessionDetailsActivity extends FragmentActivity {
//	private TextView tvProgramNameLabel, tvLocationLabel, tvDateLabel, tvProgramTypeLabel, tvProgramActiveDateLabel, tvAttCoachesLabel, tvAttAthletesLabel;
	private TextView tvProgramName, tvLocation1, tvLocation2, tvDate, tvProgramType, tvProgramActiveDate, tvAttCoaches, tvAttAthletes;
	private Button btnCoachChkIn, btnAthleteChkIn;
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
			String address = "";
			session = (KeenSession) getIntent().getSerializableExtra("session");
			program = (KeenProgram) getIntent().getSerializableExtra("program");
			
			tvDate.setText("Session Date: " + formamtDate(session.getDate()));
			tvProgramName.setText(program.getName());
			if (program.getLocation() != null) {
				address = "";
				if (program.getLocation().getAddress1() != null) {
					address += program.getLocation().getAddress1() + " ";
				}
				tvLocation2.setText(address);
			} else {
				tvLocation2.setText(address);
			}
			if (program.getLocation() != null) {
				address = "";
				if(program.getLocation().getAddress2() != null){
					address += program.getLocation().getAddress2() + " ";
				}
				if(program.getLocation().getCity() != null){
					address += program.getLocation().getCity() + " ";
				}
				if(program.getLocation().getState() != null){
					address += program.getLocation().getState() + " ";
				}
				if(program.getLocation().getZipCode() != null){
					address += program.getLocation().getZipCode() + " ";
				}
				tvLocation1.setText(address);
			} else {
				tvLocation1.setText("No Address");
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
//			tvProgramNameLabel = (TextView) findViewById(R.id.tvProgramNameLabel);
//			tvLocationLabel = (TextView) findViewById(R.id.tvLocationLabel);
//			tvDateLabel = (TextView) findViewById(R.id.tvDateLabel);
//			tvProgramActiveDateLabel = (TextView) findViewById(R.id.tvProgramActiveDateLabel);
//			tvProgramTypeLabel = (TextView) findViewById(R.id.tvProgramTypeLabel);
//			tvAttCoachesLabel = (TextView) findViewById(R.id.tvAttCoachesLabel);
//			tvAttAthletesLabel = (TextView) findViewById(R.id.tvAttAthletesLabel);
			btnCoachChkIn = (Button) findViewById(R.id.btnCoachChkIn);
			btnAthleteChkIn = (Button) findViewById(R.id.btnAthleteChkIn);
			
			tvProgramName = (TextView) findViewById(R.id.tvProgramName);
			tvLocation1 = (TextView) findViewById(R.id.tvLocation1);
			tvLocation2 = (TextView) findViewById(R.id.tvLocation2);
			tvDate = (TextView) findViewById(R.id.tvDate);
			tvProgramActiveDate = (TextView) findViewById(R.id.tvProgramActiveDate);
			tvProgramType = (TextView) findViewById(R.id.tvProgramType);
			tvAttCoaches = (TextView) findViewById(R.id.tvAttCoaches);
			tvAttAthletes = (TextView) findViewById(R.id.tvAttAthletes);
		}
		
		public void athleteCheckIn(View v) {
			openAthleteCheckIn(session, program);
		}
		
		public void coachCheckIn(View v) {
			openCoachCheckIn(session, program);
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
			getMenuInflater().inflate(R.menu.session_details, menu);
			return true;
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			if (item.getItemId() == R.id.miCheckin){
				// Start the check-in activity
				Intent checkinIntent = new Intent(getBaseContext(), AthleteCoachCheckinActivity.class);
				checkinIntent.putExtra("session", session);
				startActivity(checkinIntent);

			}else if(item.getItemId() == R.id.miSendMessage){
				showMassMessageDialog();			}
			
			return super.onOptionsItemSelected(item);
		}

		private void showMassMessageDialog() {
			DialogFragment newFragment = new MassMessageFragment();
			newFragment.show(getSupportFragmentManager(), "Mass Message Dialog");
		}


		
}
