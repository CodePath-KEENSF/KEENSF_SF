package org.keenusa.connect.fragments;

import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.activities.SessionDetailsActivity;
import org.keenusa.connect.adapters.StickySessionListItemAdapter;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.models.TestDataFactory;

import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class SessionsFragment extends Fragment {

	private ArrayList<KeenSession>sessionList;
	
    // Sticky Header List View
    private ExpandableStickyListHeadersListView expandableStickySessionList;
    private StickyListHeadersAdapter expandableStickySessionListAdapter;


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
		expandableStickySessionList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapter, View view,
					int pos, long id) {
				openSessionDetails(pos);
			}
		});

		expandableStickySessionList.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                if(expandableStickySessionList.isHeaderCollapsed(headerId)){
                	expandableStickySessionList.expand(headerId);
                }else {
                	expandableStickySessionList.collapse(headerId);
                }
            }
        });
	}

	private void openSessionDetails(int pos) {
		Intent i = new Intent(getActivity(), SessionDetailsActivity.class);
		i.putExtra("session", sessionList.get(pos));
		i.putExtra("program", sessionList.get(pos).getProgram());
		startActivity(i);
	}

	private void setAdapter() {
		sessionList = new ArrayList<KeenSession>(TestDataFactory.getInstance().getSessionList());
		expandableStickySessionListAdapter = new StickySessionListItemAdapter(getActivity(), sessionList);
	}

	private void setViews(View v) {
		expandableStickySessionList = (ExpandableStickyListHeadersListView) v.findViewById(R.id.lvSessionList);
		expandableStickySessionList.setAdapter(expandableStickySessionListAdapter);
	}

	public StickyListHeadersAdapter getAdapter() {
		return expandableStickySessionListAdapter;
	}

//	public void addAPIData(List<KeenSession> sessions) {
//		sessionListAdapter.clear();
//		sessionListAdapter.addAll(sessions);
//		
//		expandableStickySessionListAdapter.clear();
//		expandableStickySessionListAdapter.addAll(sessions);
//	}
}
