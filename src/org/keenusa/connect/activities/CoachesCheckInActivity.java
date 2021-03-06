package org.keenusa.connect.activities;

import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.adapters.CoachesCheckInAdapter;
import org.keenusa.connect.models.CoachAttendance;
import org.keenusa.connect.models.KeenProgram;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.networking.KeenCivicoreClient.CivicoreDataResultListener;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class CoachesCheckInActivity extends Activity {

	CoachAttendance coachAtt;
	KeenSession session;
	KeenProgram program;

	private ListView elvRegisteredPeople;
	private LinearLayout llProgressBar;
	private boolean bDataLoaded = false;
	private ArrayList<CoachAttendance> coachList;
	private CoachesCheckInAdapter coachCheckInAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coach_check_in);
		setView();
		getData();
	}

	private void getData() {
		session = (KeenSession) getIntent().getSerializableExtra("session");
		program = (KeenProgram) getIntent().getSerializableExtra("program");

		ActionBar actionBar = getActionBar();
		actionBar.setTitle(program.getName());
		bDataLoaded = false;
		KeenCivicoreClient client = new KeenCivicoreClient(getBaseContext());
		coachCheckInAdapter.clear();
		client.fetchCoachAttendanceListData(new CivicoreDataResultListener<CoachAttendance>() {

			@Override
			public void onListResult(List<CoachAttendance> coachAttlist) {
				for (CoachAttendance coachAtt : coachAttlist) {
					if (coachAtt.getRemoteSessionId() == session.getRemoteId()) {//&& coachAtt.getAttendanceValue() == AttendanceValue.REGISTERED) {
						coachList.add(coachAtt);
						Log.d("COACHLIST", coachAtt.getRemoteSessionId() + " " + coachAtt.getRemoteId());
					}
				}
				bDataLoaded = true;
				if (llProgressBar != null) {
					llProgressBar.setVisibility(View.GONE);
				}
				coachCheckInAdapter.notifyDataSetChanged();
			}

			@Override
			public void onListResultError() {
				Toast.makeText(CoachesCheckInActivity.this, "Error in fetching data from CiviCore", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void setView() {
		elvRegisteredPeople = (ListView) findViewById(R.id.elvRegisteredPeople);
		coachList = new ArrayList<CoachAttendance>();
		coachCheckInAdapter = new CoachesCheckInAdapter(this, coachList);
		llProgressBar = (LinearLayout) findViewById(R.id.llProgressBarSessions);
		if (!bDataLoaded) {
			llProgressBar.setVisibility(View.VISIBLE);
		}
		elvRegisteredPeople.setAdapter(coachCheckInAdapter);

		elvRegisteredPeople.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(CoachesCheckInActivity.this, CoachProfileActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
	}
}
