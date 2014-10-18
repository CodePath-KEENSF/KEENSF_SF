package org.keenusa.connect.activities;

import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.adapters.AthletesCheckInAdapter;
import org.keenusa.connect.models.AthleteAttendance;
import org.keenusa.connect.models.KeenProgram;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.networking.KeenCivicoreClient.CivicoreDataResultListener;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AthleteCheckInActivity extends Activity{
	AthleteAttendance athleteAtt;
	KeenSession session;
	KeenProgram program;
	
	private ListView lvRegisteredAthletes;
	private ArrayList<AthleteAttendance> athleteList;
	private AthletesCheckInAdapter athleteCheckInAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_athlete_check_in);
		setView();
		getData();
	}
	
	private void getData() {
		session = (KeenSession) getIntent().getSerializableExtra("session");
		program = (KeenProgram) getIntent().getSerializableExtra("program");
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(program.getName());
		
		KeenCivicoreClient client = new KeenCivicoreClient(getBaseContext());
		athleteCheckInAdapter.clear();
		client.fetchAthleteAttendanceListData(new CivicoreDataResultListener<AthleteAttendance>() {
			
			@Override
			public void onListResult(List<AthleteAttendance> athleteAttlist) {
				for (AthleteAttendance athleteAttendance : athleteAttlist) {
					if (athleteAttendance.getRemoteSessionId() == session.getRemoteId()) {
						athleteList.add(athleteAttendance);
					}
				}
				athleteCheckInAdapter.notifyAll();
			}
			
		});
		
	}

	private void setView() {
		lvRegisteredAthletes = (ListView) findViewById(R.id.lvRegisteredAthletes);
		athleteList = new ArrayList<AthleteAttendance>();
		athleteCheckInAdapter = new AthletesCheckInAdapter(this, athleteList);

		lvRegisteredAthletes.setAdapter(athleteCheckInAdapter);
		
		lvRegisteredAthletes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(AthleteCheckInActivity.this, AthleteProfileActivity.class);
				startActivity(i);
			}
		});
	}
}