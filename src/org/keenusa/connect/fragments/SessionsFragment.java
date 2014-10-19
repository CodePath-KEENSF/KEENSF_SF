package org.keenusa.connect.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.keenusa.connect.R;
import org.keenusa.connect.activities.SessionDetailsActivity;
import org.keenusa.connect.adapters.StickySessionListItemAdapter;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.networking.KeenCivicoreClient.CivicoreDataResultListener;
import org.keenusa.connect.utilities.GetNextDay;
import org.keenusa.connect.utilities.KeenSessionComparator;
import org.keenusa.connect.utilities.StringConstants;

import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
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
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;

public class SessionsFragment extends Fragment {
	
	private ArrayList<KeenSession>sessionList;
	private LinearLayout llProgressBar;
	private boolean bDataLoaded = false;
	
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
		
		sessionList = new ArrayList<KeenSession>();
		setAdapter();
		
		fetchSessionList();
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

	private void fetchSessionList() {
		bDataLoaded = false;
		KeenCivicoreClient client = new KeenCivicoreClient(getActivity());
		client.fetchSessionListData(new CivicoreDataResultListener<KeenSession>() {

			@Override
			public void onListResult(List<KeenSession> list) {
				Collections.sort(list, Collections.reverseOrder(new KeenSessionComparator()));
				sessionList.addAll(list);
				expandableStickySessionListAdapter = new StickySessionListItemAdapter(getActivity(), sessionList);
				
				setSessionListToCurrentDate();
				bDataLoaded = true;
				if(llProgressBar != null){
					llProgressBar.setVisibility(View.GONE);
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
//		sessionList = (ArrayList<KeenSession>)TestDataFactory.getInstance().getSessionList();
		expandableStickySessionListAdapter = new StickySessionListItemAdapter(getActivity(), sessionList);
	}

	private void setViews(View v) {
		llProgressBar = (LinearLayout) v.findViewById(R.id.llProgressBarSessions);
		if(!bDataLoaded){
			llProgressBar.setVisibility(View.VISIBLE);
		}
		
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
    		String date = new SimpleDateFormat(StringConstants.DATE_FORMAT_LONG, Locale.ENGLISH).format(dt.toDate());
    		expandableStickySessionList.expand(Long.parseLong(date));
    	}
	}

	public void collapseAllListItems(){
		for(int i=0;i<sessionList.size();i++){
    		KeenSession session = sessionList.get(i);
    		DateTime dt = session.getDate();
    		String date = new SimpleDateFormat(StringConstants.DATE_FORMAT_LONG, Locale.ENGLISH).format(dt.toDate());
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
	
	private void setSessionListToCurrentDate() {
		int currentDateIndex = 0;
		String nextSundayDateStr = new SimpleDateFormat(StringConstants.DATE_FORMAT_LONG, Locale.ENGLISH).format(GetNextDay.getNextSunday());
		
		for (KeenSession session : sessionList) {
			String sessionDateStr = new SimpleDateFormat(StringConstants.DATE_FORMAT_LONG, Locale.ENGLISH).format(session.getDate().toDate());
			if(nextSundayDateStr.equalsIgnoreCase(sessionDateStr)){
				
				expandableStickySessionList.setSelection(currentDateIndex);
				break;
			}
			currentDateIndex = currentDateIndex + 1;
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
