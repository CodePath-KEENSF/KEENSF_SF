package org.keenusa.connect.activities;

import java.util.ArrayList;

import org.keenusa.connect.R;
import org.keenusa.connect.adapters.SessionListItemAdapter;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.models.TestDataFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SessionListActivity extends Activity {

	private ListView lvSessionList;
	ArrayList<KeenSession>sessionList;
	SessionListItemAdapter sessionListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_session_list);
		getViews();
		setAdapter();
		setOnClickListeners();
	}

	private void setOnClickListeners() {
		lvSessionList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapter, View item,
					int pos, long id) {
				openSessionDetails(pos);
			}
		});
	}

	private void openSessionDetails(int pos) {
		Intent i = new Intent(this, SessionDetailsActivity.class);
		startActivity(i);
	}

	private void setAdapter() {
		sessionList = new ArrayList<KeenSession>(TestDataFactory.getInstance().getSessionList());
		sessionListAdapter = new SessionListItemAdapter(this, sessionList);
		lvSessionList.setAdapter(sessionListAdapter);
	}

	private void getViews() {
		lvSessionList = (ListView)findViewById(R.id.lvSessionList);
	}
}
