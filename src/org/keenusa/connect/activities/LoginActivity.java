package org.keenusa.connect.activities;

import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.networking.KeenCivicoreClient.CivicoreDataResultListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

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

		Button btnCheckIn = (Button) findViewById(R.id.btnCheckIn);
		btnCheckIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openCheckIn();
			}
		});

		Button btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openSessionAthleteCoachList();
			}
		});

		Button btnSessionList = (Button) findViewById(R.id.btnSessionList);
		btnSessionList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				KeenCivicoreClient client = new KeenCivicoreClient(LoginActivity.this);
				client.fetchSessionListData(new CivicoreDataResultListener<KeenSession>() {

					@Override
					public void onListResult(List<KeenSession> list) {
						Toast.makeText(LoginActivity.this, "sessions data fetched " + list.size(), Toast.LENGTH_LONG).show();
						openSessionList();
					}
				});
			}
		});

		return true;
	}

	private void openSessionAthleteCoachList() {
		Intent i = new Intent(this, SessionAthleteCoachListActivity.class);
		startActivity(i);
	}

	private void openSessionList() {
		Intent i = new Intent(this, SessionListActivity.class);
		startActivity(i);
	}

	protected void openCheckIn() {
		Intent i = new Intent(this, CoachesCheckInActivity.class);
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
}
