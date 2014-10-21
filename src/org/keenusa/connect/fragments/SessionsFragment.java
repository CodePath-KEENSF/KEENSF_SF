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
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.AthleteAttendance;
import org.keenusa.connect.models.Coach;
import org.keenusa.connect.models.CoachAttendance;
import org.keenusa.connect.models.KeenProgram;
import org.keenusa.connect.models.KeenProgramEnrolment;
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

public class SessionsFragment extends Fragment {
	public static final int NUM_SESSION_FETCHES = 7;
	public int currentFetch = 0;
	public String dummySearchString;
	private SearchView searchView;

	private ArrayList<KeenSession> sessionList;
	private ArrayList<KeenProgram> programList;
	private ArrayList<KeenProgramEnrolment> programEnrolmentList;
	private List<AthleteAttendance> athleteAttendanceList;
	private List<CoachAttendance> coachAttendanceList;
	private List<Coach> coachList;
	private List<Athlete> athleteList;

	private HashMap<Long, KeenProgram> sessionProgramMap = new HashMap<Long, KeenProgram>();
	private HashMap<Long, KeenSession> sessionMap = new HashMap<Long, KeenSession>();
	private HashMap<String, Athlete> sessionAthleteMap = new HashMap<String, Athlete>();
	private HashMap<String, Coach> sessionCoachMap = new HashMap<String, Coach>();

	private LinearLayout llProgressBar;
	private boolean bDataLoaded = false;

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
		programEnrolmentList = new ArrayList<KeenProgramEnrolment>();
		coachAttendanceList = new ArrayList<CoachAttendance>();
		athleteAttendanceList = new ArrayList<AthleteAttendance>();
		coachList = new ArrayList<Coach>();
		athleteList = new ArrayList<Athlete>();

		setAdapter();

		fetchLists();
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

	private void fetchLists() {
		bDataLoaded = false;
		fetchProgramList();
		fetchProgramEnrolmentList();
		fetchAthleteAttendanceList();
		fetchCoachAttendanceList();
		fetchAthleteList();
		fetchCoachList();
		fetchSessionList();
	}

	private void fetchProgramList() {
		KeenCivicoreClient client = new KeenCivicoreClient(getActivity());
		client.fetchProgramListData(new CivicoreDataResultListener<KeenProgram>() {

			@Override
			public void onListResult(List<KeenProgram> list) {
				programList.clear();
				programList.addAll(list);
				for (KeenProgram program : programList) {
					sessionProgramMap.put(program.getRemoteId(), program);
				}
				monitorFetches();
			}

			@Override
			public void onListResultError() {
				monitorFetches();
			}

		});
	}

	private void fetchProgramEnrolmentList() {
		KeenCivicoreClient client = new KeenCivicoreClient(getActivity());
		client.fetchProgramEnrolmentListData(new CivicoreDataResultListener<KeenProgramEnrolment>() {

			@Override
			public void onListResult(List<KeenProgramEnrolment> list) {
				programEnrolmentList.clear();
				programEnrolmentList.addAll(list);
				//				for (KeenProgram program : programList) {
				//					sessionProgramMap.put(program.getRemoteId(), program);
				//				}
				monitorFetches();
			}

			@Override
			public void onListResultError() {
				monitorFetches();
			}

		});
	}

	private void fetchAthleteAttendanceList() {
		KeenCivicoreClient client = new KeenCivicoreClient(getActivity());
		client.fetchAthleteAttendanceListData(new CivicoreDataResultListener<AthleteAttendance>() {

			@Override
			public void onListResult(List<AthleteAttendance> list) {
				athleteAttendanceList.clear();
				athleteAttendanceList.addAll(list);
				monitorFetches();
			}

			@Override
			public void onListResultError() {
				monitorFetches();
			}

		});
	}

	private void fetchCoachAttendanceList() {
		KeenCivicoreClient client = new KeenCivicoreClient(getActivity());
		client.fetchCoachAttendanceListData(new CivicoreDataResultListener<CoachAttendance>() {

			@Override
			public void onListResult(List<CoachAttendance> list) {
				coachAttendanceList.clear();
				coachAttendanceList.addAll(list);
				monitorFetches();
			}

			@Override
			public void onListResultError() {
				monitorFetches();
			}

		});
	}

	private void fetchAthleteList() {
		KeenCivicoreClient client = new KeenCivicoreClient(getActivity());
		client.fetchAthleteListData(new CivicoreDataResultListener<Athlete>() {

			@Override
			public void onListResult(List<Athlete> list) {
				athleteList.clear();
				athleteList.addAll(list);
				for (Athlete athlete : athleteList) {
					sessionAthleteMap.put(athlete.getFullName(), athlete);
				}
				monitorFetches();
			}

			@Override
			public void onListResultError() {
				monitorFetches();
			}

		});
	}

	private void fetchCoachList() {
		KeenCivicoreClient client = new KeenCivicoreClient(getActivity());
		client.fetchCoachListData(new CivicoreDataResultListener<Coach>() {

			@Override
			public void onListResult(List<Coach> list) {
				coachList.clear();
				coachList.addAll(list);
				for (Coach coach : coachList) {
					sessionCoachMap.put(coach.getFirstLastName(), coach);
				}
				monitorFetches();
			}

			@Override
			public void onListResultError() {
				monitorFetches();
			}

		});
	}

	private void fetchSessionList() {
		KeenCivicoreClient client = new KeenCivicoreClient(getActivity());
		client.fetchSessionListData(new CivicoreDataResultListener<KeenSession>() {

			@Override
			public void onListResult(List<KeenSession> list) {
				sessionList.clear();
				sessionList.addAll(list);
				for (KeenSession session : sessionList) {
					sessionMap.put(session.getRemoteId(), session);
				}

				monitorFetches();
			}

			@Override
			public void onListResultError() {
				monitorFetches();
			}

		});
	}

	private void monitorFetches() {
		currentFetch++;
		if (currentFetch == NUM_SESSION_FETCHES) {
			updateSessionListViews();
		}
	}

	private void updateSessionListViews() {

		bDataLoaded = true;

		// Received all the lists required for session list
		// Get the location and time information from program associated with the given session
		connectSessionWithProgram();
		connectProgramWithEnrolmentList();
		connectSessionWithCoachAttendance();
		connectSessionWithAthleteAttendance();

		Collections.sort(sessionList, Collections.reverseOrder(new KeenSessionComparator()));

		expandableStickySessionListAdapter = new StickySessionListItemAdapter(getActivity(), sessionList);

		try {
			setSessionListToCurrentDate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (llProgressBar != null) {
			llProgressBar.setVisibility(View.GONE);
		}
	}

	private void connectSessionWithProgram() {

		for (KeenSession session : sessionList) {
			KeenProgram program;
			program = (KeenProgram) sessionProgramMap.get(session.getProgram().getRemoteId());
			if (program != null) {
				session.setProgram(program);
			}
		}
	}

	private void connectProgramWithEnrolmentList() {

		for (KeenProgramEnrolment programEnrolment : programEnrolmentList) {
			KeenProgram program;
			program = (KeenProgram) sessionProgramMap.get(programEnrolment.getProgram().getRemoteId());
			if (program != null) {
				programEnrolment.setProgram(program);
				program.addEnrolledAthletes(programEnrolment.getAthlete());
			}
		}
	}

	private void connectSessionWithCoachAttendance() {

		for (CoachAttendance coachatt : coachAttendanceList) {

			KeenSession session;
			session = (KeenSession) sessionMap.get(coachatt.getRemoteSessionId());
			if (session != null) {
				session.addCoachAttendance(coachatt);
			}

			Coach coach;
			coach = (Coach) sessionCoachMap.get(coachatt.getAttendedCoachFullName());
			if (coach != null) {
				coachatt.setCoach(coach);
			}
		}
	}

	private void connectSessionWithAthleteAttendance() {

		for (AthleteAttendance athleteatt : athleteAttendanceList) {

			KeenSession session;
			session = (KeenSession) sessionMap.get(athleteatt.getRemoteSessionId());
			if (session != null) {
				session.addAthleteAttendance(athleteatt);
			}

			Athlete athlete;
			athlete = (Athlete) sessionAthleteMap.get(athleteatt.getAttendedAthleteFullName());
			if (athlete != null) {
				athleteatt.setAthlete(athlete);
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
