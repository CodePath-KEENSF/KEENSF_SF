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
					if (coachAtt.getRemoteSessionId() == session.getRemoteId()) {
						coachList.add(coachAtt);
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
			}
		});

		//		search = (Button) findViewById(R.id.btnAdd);
		//		etSearch = (EditText) findViewById(R.id.etSearch);
	}

	//	private OnChildClickListener coachItemClicked = new OnChildClickListener() {
	//		
	//		@Override
	//		public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
	//			Headers header = coachesSubListArray.get(groupPosition);
	//			Coach coach = header.getCoachNameList().get(childPosition);
	//			Toast.makeText(getBaseContext(), "Clicked on " + header.getName() + "/" + coach.getFirstName(), Toast.LENGTH_SHORT).show();
	//			return false;
	//		}
	//	};
	//	
	//	private OnGroupClickListener coachGroupClicked = new OnGroupClickListener() {
	//		
	//		@Override
	//		public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
	//			Headers header = coachesSubListArray.get(groupPosition);
	//			Toast.makeText(getBaseContext(), "Child on header " + header.getName(), Toast.LENGTH_SHORT).show();
	//			return false;
	//		}
	//	};
	//
	//	private void expandAll() {
	//		int count = coachesSubListAdapter.getGroupCount();
	//		for (int i = 0; i < count; i++) {
	//			elvCoaches.expandGroup(i);
	//		}
	//	}
	//
	////	private void loadCoachesData() {
	////		addCoachNames("Coaches", coach.getFirstName());
	////	}
	//
	//	private int addCoachNames(String header, String list) {
	//		int groupPosition = 0;
	//		
	//		Headers headers = keen.get(header);
	//		if (headers == null) {
	//			headers = new Headers();
	//			headers.setName(header);
	//			keen.put(header, headers);
	//			coachesSubListArray.add(headers);
	//		}
	//		
	//		// get coaches names  for coaches group
	//		ArrayList<Coach> coachesSubListArray = headers.getCoachNameList();
	//		int coachSize = coachesSubListArray.size();
	//		coachSize++;
	//		
	//		// create coaches name and add to header (Coaches group)
	//		coach.setSequence(String.valueOf(coachSize));
	//		coach.setName(list);
	//		coachesSubListArray.add(coach);
	//		headers.setCoachesNameList(coachesSubListArray);
	//		
	//		// find group position inside the list
	//		groupPosition = coachesSubListArray.indexOf(headers);
	//		return groupPosition;
	//	}
	//
	//	@Override
	//	public void onClick(View v) {
	//		switch (v.getId()) {
	//		case R.id.btnAdd:
	//			EditText etSearch = (EditText) findViewById(R.id.etSearch);
	//			String search = etSearch.getText().toString();
	//			etSearch.setText("");
	//			
	//			int groupPosition = addCoachNames("Coaches", search);
	//			coachesSubListAdapter.notifyDataSetChanged();
	//			collapseAll();
	////			elvCoaches.expandGroup(groupPosition);
	////			elvCoaches.setSelectedGroup(groupPosition);
	//		}
	//	}
	//
	//	private void collapseAll() {
	//		int count = coachesSubListAdapter.getGroupCount();
	//		for (int i = 0; i < count; i++) {
	////			elvCoaches.collapseGroup(i);
	//		}
	//	}
}
