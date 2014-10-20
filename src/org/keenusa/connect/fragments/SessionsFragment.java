package org.keenusa.connect.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.keenusa.connect.R;
import org.keenusa.connect.activities.SessionDetailsActivity;
import org.keenusa.connect.adapters.StickySessionListItemAdapter;
import org.keenusa.connect.models.KeenProgram;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

public class SessionsFragment extends Fragment {
	public String dummySearchString;
	private SearchView searchView;

	private ArrayList<KeenSession> sessionList;
	private ArrayList<KeenProgram> programList;
	private LinearLayout llProgressBar;
	private boolean bDataLoaded = false;
	private HashMap<Long, KeenProgram> sessionProgramMap = new HashMap<Long, KeenProgram>();

	// Sticky Header List View
	private ExpandableStickyListHeadersListView expandableStickySessionListView;
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
		programList = new ArrayList<KeenProgram>();
		setAdapter();

		fetchProgramSessionList();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_sessions, container, false);

		setViews(v);

		setOnClickListeners();

		return v;
	}

	private void setOnClickListeners() {
		expandableStickySessionListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
				openSessionDetails(pos);
			}
		});

		expandableStickySessionListView.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
			@Override
			public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
				if (expandableStickySessionListView.isHeaderCollapsed(headerId)) {
					expandableStickySessionListView.expand(headerId);
				} else {
					expandableStickySessionListView.collapse(headerId);
				}
			}
		});
	}

	private void fetchProgramSessionList() {
		bDataLoaded = false;

		KeenCivicoreClient clientProgram = new KeenCivicoreClient(getActivity());
		clientProgram.fetchProgramListData(new CivicoreDataResultListener<KeenProgram>() {

			@Override
			public void onListResult(List<KeenProgram> list) {
				KeenCivicoreClient clientSession = new KeenCivicoreClient(getActivity());
				clientSession.fetchSessionListData(new CivicoreDataResultListener<KeenSession>() {

					@Override
					public void onListResult(List<KeenSession> list) {
						sessionList.addAll(list);
						// Received both session list and program list
						// Get the location and time information from program associated with the given session
						getSessionInfoFromProgramList();

						Collections.sort(sessionList, Collections.reverseOrder(new KeenSessionComparator()));

						expandableStickySessionListAdapter = new StickySessionListItemAdapter(getActivity(), sessionList);

						setSessionListToCurrentDate();
						bDataLoaded = true;
						if (llProgressBar != null) {
							llProgressBar.setVisibility(View.GONE);
						}
					}

					@Override
					public void onListResultError() {
						Toast.makeText(SessionsFragment.this.getActivity(), "Error in fetching data from CiviCore", Toast.LENGTH_SHORT).show();
					}
				});

				programList.addAll(list);
				for (KeenProgram program : programList) {
					sessionProgramMap.put(program.getRemoteId(), program);
				}

			}

			@Override
			public void onListResultError() {
				Toast.makeText(SessionsFragment.this.getActivity(), "Error in fetching data from CiviCore", Toast.LENGTH_SHORT).show();
			}
		});

	}

	private void getSessionInfoFromProgramList() {

		for (KeenSession session : sessionList) {
			KeenProgram program;
			program = (KeenProgram) sessionProgramMap.get(session.getProgram().getRemoteId());
			if (program != null) {
				session.setProgram(program);
			}
		}
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
		if (!bDataLoaded) {
			llProgressBar.setVisibility(View.VISIBLE);
		}

		expandableStickySessionListView = (ExpandableStickyListHeadersListView) v.findViewById(R.id.lvSessionList);
		expandableStickySessionListView.setAdapter(expandableStickySessionListAdapter);
	}

	public StickyListHeadersAdapter getAdapter() {
		return expandableStickySessionListAdapter;
	}

	public void expandAllListItems() {
		for (int i = 0; i < sessionList.size(); i++) {
			KeenSession session = sessionList.get(i);
			DateTime dt = session.getDate();
			String date = new SimpleDateFormat(StringConstants.DATE_FORMAT_LONG, Locale.ENGLISH).format(dt.toDate());
			expandableStickySessionListView.expand(Long.parseLong(date));
		}
	}

	public void collapseAllListItems() {
		for (int i = 0; i < sessionList.size(); i++) {
			KeenSession session = sessionList.get(i);
			DateTime dt = session.getDate();
			String date = new SimpleDateFormat(StringConstants.DATE_FORMAT_LONG, Locale.ENGLISH).format(dt.toDate());
			expandableStickySessionListView.collapse(Long.parseLong(date));
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.sessions, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search_sessions);
		dummySearchString = StringConstants.DUMMY_SEARCH_STRING;
		searchView = (SearchView) searchItem.getActionView();
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				return true;
			}

			@Override
			public boolean onQueryTextChange(String searchText) {

				// Added as a work-around. the searchText was retaining values across fragment swiping
				// with this condition, the value of searchText from another fragment is discarded
				if (dummySearchString == StringConstants.DUMMY_SEARCH_STRING) {
					if (!searchText.isEmpty()) {
						dummySearchString = "";
						return true;
					}
				}

				dummySearchString = searchText;
				ArrayList<KeenSession> tempSessionList = new ArrayList<KeenSession>();
				int searchTextlength = searchText.length();

				// separate words in search text
				String[] searchWords = searchText.split(" ");

				// Create the new arraylist for each search character
				for (KeenSession session : sessionList) {

					boolean searchMatched = true;

					String fullName = getFullSessionName(session);

					for (String searchWord : searchWords) {
						if (!(fullName.toLowerCase().contains(searchWord.toLowerCase()))) {
							searchMatched = false;
							break;
						}
					}
					if (searchMatched) {
						tempSessionList.add(session);
					}
				}

				expandableStickySessionListAdapter = new StickySessionListItemAdapter(getActivity(), tempSessionList);
				expandableStickySessionListView.setAdapter(expandableStickySessionListAdapter);

				setSearchListPosition(tempSessionList);

				return true;
			}
		});
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

	private String getFullSessionName(KeenSession session) {
		KeenProgram program = session.getProgram();

		String name;
		String date;
		String location;
		String time;

		if (program != null && program.getName() != null) {
			name = session.getProgram().getName();
		} else {
			name = "Dummy Program";
		}

		if (program != null && program.getLocation() != null && program.getLocation().getCity() != null && program.getLocation().getState() != null) {
			location = session.getProgram().getLocation().getCity() + ", " + session.getProgram().getLocation().getState();
		} else {
			location = "San Francisco";
		}

		if (program != null && program.getProgramTimes() != null) {
			time = session.getProgram().getProgramTimes();
		} else {
			time = "12pm - 1pm";
		}

		if (session.getDate() != null) {
			DateTime dt = session.getDate();
			date = new SimpleDateFormat(StringConstants.DATE_FORMAT, Locale.ENGLISH).format(dt.toDate());
		} else {
			date = "01/01/2001";
		}

		String fullName = name + " " + time + " " + location + " " + date;

		return fullName;
	}

	private void setSessionListToCurrentDate() {
		int currentDateIndex = 0;
		String nextSundayDateStr = new SimpleDateFormat(StringConstants.DATE_FORMAT_LONG, Locale.ENGLISH).format(GetNextDay.getNextSunday());

		for (KeenSession session : sessionList) {
			String sessionDateStr = new SimpleDateFormat(StringConstants.DATE_FORMAT_LONG, Locale.ENGLISH).format(session.getDate().toDate());
			if (nextSundayDateStr.equalsIgnoreCase(sessionDateStr)) {

				expandableStickySessionListView.setSelection(currentDateIndex);
				break;
			}
			currentDateIndex = currentDateIndex + 1;
		}
	}

	private void setSearchListPosition(ArrayList<KeenSession> tempSessionList) {
		int currentDateIndex = 0;
		LocalDate today = new LocalDate();
		String currentDateStr = new SimpleDateFormat(StringConstants.DATE_FORMAT_YEAR, Locale.ENGLISH).format(today.toDate());
		long currentDate = Long.parseLong(currentDateStr);
		long currentMax = 20000000;

		for (KeenSession session : tempSessionList) {
			String sessionDateStr = new SimpleDateFormat(StringConstants.DATE_FORMAT_YEAR, Locale.ENGLISH).format(session.getDate().toDate());
			long sessionDate = Long.parseLong(sessionDateStr);

			long dateDiff = Math.abs(sessionDate - currentDate);
			if (dateDiff <= currentMax) {
				currentMax = dateDiff;
			} else {
				expandableStickySessionListView.setSelection(currentDateIndex - 1);
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
