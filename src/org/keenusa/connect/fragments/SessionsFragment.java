package org.keenusa.connect.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.joda.time.DateTime;
import org.keenusa.connect.R;
import org.keenusa.connect.activities.SessionDetailsActivity;
import org.keenusa.connect.adapters.StickySessionListItemAdapter;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.models.TestDataFactory;

import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView.IAnimationExecutor;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class SessionsFragment extends Fragment {

	public static final String DATE_FORMAT_LONG = "MMddyyyy";
	
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
		setHasOptionsMenu(true);
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
	
	public void expandAllListItems(){
    	for(int i=0;i<sessionList.size();i++){
    		KeenSession session = sessionList.get(i);
    		DateTime dt = session.getDate();
    		String date = new SimpleDateFormat(DATE_FORMAT_LONG, Locale.ENGLISH).format(dt.toDate());
    		expandableStickySessionList.expand(Long.parseLong(date));
    	}
	}

	public void collapseAllListItems(){
		for(int i=0;i<sessionList.size();i++){
    		KeenSession session = sessionList.get(i);
    		DateTime dt = session.getDate();
    		String date = new SimpleDateFormat(DATE_FORMAT_LONG, Locale.ENGLISH).format(dt.toDate());
    		expandableStickySessionList.collapse(Long.parseLong(date));
    	}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.sessions, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.miCollapseAll:
			collapseAllListItems();
			return true;

		case R.id.miExpandAll:
			expandAllListItems();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	


	//	public void addAPIData(List<KeenSession> sessions) {
//		sessionListAdapter.clear();
//		sessionListAdapter.addAll(sessions);
//		
//		expandableStickySessionListAdapter.clear();
//		expandableStickySessionListAdapter.addAll(sessions);
	
//	}
}
