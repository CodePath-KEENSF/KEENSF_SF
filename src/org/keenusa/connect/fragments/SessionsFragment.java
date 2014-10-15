package org.keenusa.connect.fragments;

import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.activities.SessionDetailsActivity;
import org.keenusa.connect.adapters.SessionListItemAdapter;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.models.TestDataFactory;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SessionsFragment extends Fragment {

	private ListView lvSessionList;
	private ArrayList<KeenSession>sessionList;
	private SessionListItemAdapter sessionListAdapter;

	// Creates a new fragment with given arguments
	public static SessionsFragment newInstance() {
		SessionsFragment sessionsFragment = new SessionsFragment();
		return sessionsFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAdapter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_sessions, container, false);
		
		setViews(v);

		setOnClickListeners();
		
		return v;
	}

	private void setOnClickListeners() {
		lvSessionList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapter, View view,
					int pos, long id) {
				openSessionDetails(pos);
			}
		});
	}

	private void openSessionDetails(int pos) {
		Intent i = new Intent(getActivity(), SessionDetailsActivity.class);
		startActivity(i);
	}

	private void setAdapter() {
		sessionList = new ArrayList<KeenSession>(TestDataFactory.getInstance().getSessionList());
		sessionListAdapter = new SessionListItemAdapter(getActivity(), sessionList);
	}

	private void setViews(View v) {
		lvSessionList = (ListView)v.findViewById(R.id.lvSessionList);
		lvSessionList.setAdapter(sessionListAdapter);
	}

	public SessionListItemAdapter getAdapter() {
		return sessionListAdapter;
	}

	public void addAPIData(List<KeenSession> sessions) {
		sessionListAdapter.clear();
		sessionListAdapter.addAll(sessions);
	}
}
