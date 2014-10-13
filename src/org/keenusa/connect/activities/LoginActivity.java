package org.keenusa.connect.activities;

import org.keenusa.connect.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);

		Button btnGoToCoachList = (Button) findViewById(R.id.btnGoToCoachList);
		Button btnGoToAthleteList = (Button) findViewById(R.id.btnGoToAthleteList);
		Button btnSessionDetails = (Button) findViewById(R.id.btnSessionDetails);
		Button btnSessionList = (Button) findViewById(R.id.btnSessionList);
		Button btnCheckIn = (Button) findViewById(R.id.btnCheckIn);
		btnGoToCoachList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openCoachList();

			}

		});

		btnGoToAthleteList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openAthleteList();

			}

		});

		btnSessionDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openSessionDetails();
			}
		});
		
		btnCheckIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openCheckIn();
			}
		});
		
		btnSessionList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openSessionList();
			}
		});
		return true;
	}

	private void openSessionList() {
		Intent i = new Intent(this, SessionListActivity.class);
		startActivity(i);
	}

	protected void openCheckIn() {
		Intent i = new Intent(this, CheckInActivity.class);
		startActivity(i);
	}

	protected void openSessionDetails() {
		Intent i = new Intent(this, SessionDetailsActivity.class);
		startActivity(i);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void openCoachList() {
		Intent i = new Intent(this, CoachListActivity.class);
		startActivity(i);

	}

	private void openAthleteList() {
		Intent i = new Intent(this, AthleteListActivity.class);
		startActivity(i);

	}
}
