package org.keenusa.connect.activities;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.keenusa.connect.R;
import org.keenusa.connect.adapters.CoachesSubListAdapter;
import org.keenusa.connect.adapters.Headers;
import org.keenusa.connect.models.Coach;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;

public class CheckInActivity extends Activity implements OnClickListener {
	private LinkedHashMap<String, Headers> keen = new LinkedHashMap<String, Headers>();
	private ArrayList<Headers> coachesSubListArray = new ArrayList<Headers>();
	private CoachesSubListAdapter coachesSubListAdapter;
	private ExpandableListView elvCoaches;
	Coach coach;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_in);

		// TODO - fixing loading coach data to check-in
		//		loadCoachesData();
		elvCoaches = (ExpandableListView) findViewById(R.id.elvRegisteredPeople);
		coachesSubListAdapter = new CoachesSubListAdapter(CheckInActivity.this, coachesSubListArray);
		elvCoaches.setAdapter(coachesSubListAdapter);
		expandAll();
		Button search = (Button) findViewById(R.id.btnAdd);
		search.setOnClickListener(this);
		elvCoaches.setOnChildClickListener(coachItemClicked);
		elvCoaches.setOnGroupClickListener(coachGroupClicked);
	}

	private OnChildClickListener coachItemClicked = new OnChildClickListener() {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
			Headers header = coachesSubListArray.get(groupPosition);
			Coach coach = header.getCoachNameList().get(childPosition);
			Toast.makeText(getBaseContext(), "Clicked on " + header.getName() + "/" + coach.getFirstName(), Toast.LENGTH_SHORT).show();
			return false;
		}
	};

	private OnGroupClickListener coachGroupClicked = new OnGroupClickListener() {

		@Override
		public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
			Headers header = coachesSubListArray.get(groupPosition);
			Toast.makeText(getBaseContext(), "Child on header " + header.getName(), Toast.LENGTH_SHORT).show();
			return false;
		}
	};

	private void expandAll() {
		int count = coachesSubListAdapter.getGroupCount();
		for (int i = 0; i < count; i++) {
			elvCoaches.expandGroup(i);
		}
	}

	//	private void loadCoachesData() {
	//		addCoachNames("Coaches", coach.getFirstName());
	//	}

	private int addCoachNames(String header, String list) {
		int groupPosition = 0;

		Headers headers = keen.get(header);
		if (headers == null) {
			headers = new Headers();
			headers.setName(header);
			keen.put(header, headers);
			coachesSubListArray.add(headers);
		}

		// get coaches names  for coaches group
		ArrayList<Coach> coachesSubListArray = headers.getCoachNameList();
		int coachSize = coachesSubListArray.size();
		coachSize++;

		// create coaches name and add to header (Coaches group)
		//		coach.setSequence(String.valueOf(coachSize));
		//		coach.setName(list);
		coachesSubListArray.add(coach);
		headers.setCoachesNameList(coachesSubListArray);

		// find group position inside the list
		groupPosition = coachesSubListArray.indexOf(headers);
		return groupPosition;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAdd:
			EditText etSearch = (EditText) findViewById(R.id.etSearch);
			String search = etSearch.getText().toString();
			etSearch.setText("");

			int groupPosition = addCoachNames("Coaches", search);
			coachesSubListAdapter.notifyDataSetChanged();
			collapseAll();
			elvCoaches.expandGroup(groupPosition);
			elvCoaches.setSelectedGroup(groupPosition);
		}
	}

	private void collapseAll() {
		int count = coachesSubListAdapter.getGroupCount();
		for (int i = 0; i < count; i++) {
			elvCoaches.collapseGroup(i);
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
	}
}
