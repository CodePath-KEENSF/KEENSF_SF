package org.keenusa.connect.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.WeakHashMap;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.keenusa.connect.R;
import org.keenusa.connect.activities.SessionDetailsActivity;
import org.keenusa.connect.adapters.StickySessionListItemAdapter;
import org.keenusa.connect.data.daos.SessionDAO;
import org.keenusa.connect.models.KeenProgram;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.utilities.GetNextDay;
import org.keenusa.connect.utilities.StringConstants;

import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class SessionsFragment extends Fragment {
	//	public static final int NUM_SESSION_FETCHES = 7;
	//	public int currentFetch = 0;

	public String dummySearchString;
	private SearchView searchView;
	private boolean isFirstView = true;
	private ProgressBar progressBar;

	private List<KeenSession> sessionList;
	//	private ArrayList<KeenProgram> programList;
	//	private ArrayList<KeenProgramEnrolment> programEnrolmentList;
	//	private List<AthleteAttendance> athleteAttendanceList;
	//	private List<CoachAttendance> coachAttendanceList;
	//	private List<Coach> coachList;
	//	private List<Athlete> athleteList;
	//
	//	private HashMap<Long, KeenProgram> sessionProgramMap = new HashMap<Long, KeenProgram>();
	//	private HashMap<Long, KeenSession> sessionMap = new HashMap<Long, KeenSession>();
	//	private HashMap<String, Athlete> sessionAthleteMap = new HashMap<String, Athlete>();
	//	private HashMap<Long, Athlete> sessionAthleteIdMap = new HashMap<Long, Athlete>();
	//	private HashMap<String, Coach> sessionCoachMap = new HashMap<String, Coach>();

	private LinearLayout llLoadingSessionsIndicator;
	//	private boolean bDataLoaded = false;
	//	private ProgressBar progressBar;

	// Sticky Header List View
	private ExpandableStickyListHeadersListView expandableStickySessionListView;
	private StickyListHeadersAdapter expandableStickySessionListAdapter;
	WeakHashMap<View, Integer> mOriginalViewHeightPool = new WeakHashMap<View, Integer>();

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
		//		programList = new ArrayList<KeenProgram>();
		//		programEnrolmentList = new ArrayList<KeenProgramEnrolment>();
		//		coachAttendanceList = new ArrayList<CoachAttendance>();
		//		athleteAttendanceList = new ArrayList<AthleteAttendance>();
		//		coachList = new ArrayList<Coach>();
		//		athleteList = new ArrayList<Athlete>();

		setAdapter();
		new LoadSessionListDataTask().execute();

		//		fetchLists();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_sessions, container, false);

		//		setViews(v);
		expandableStickySessionListView = (ExpandableStickyListHeadersListView) v.findViewById(R.id.lvSessionList);
		llLoadingSessionsIndicator = (LinearLayout) v.findViewById(R.id.llLoadingSessionsIndicator);
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

		expandableStickySessionListView.setAnimExecutor(new AnimationExecutor());

		//		expandableStickySessionListView.setOnItemLongClickListener(new OnItemLongClickListener() {
		//			@Override
		//			public boolean onItemLongClick(AdapterView<?> adapter, View view,
		//					int pos, long id) {
		//				LinearLayout back = (LinearLayout)view.findViewById(R.id.back);
		//				RelativeLayout front = (RelativeLayout)view.findViewById(R.id.rlSessionList);
		//				if(front.getAlpha() == 1.0f){
		//					Animator animFront = AnimatorInflater.loadAnimator(getActivity(), R.animator.card_flip_left_out);
		//					animFront.setTarget(front);
		//					Animator animBack = AnimatorInflater.loadAnimator(getActivity(), R.animator.card_flip_left_in);
		//					animBack.setTarget(back);
		//					animFront.start();
		//					animBack.start();
		//					Log.d("temp", "front");
		//				}else{
		//					Animator animFront = AnimatorInflater.loadAnimator(getActivity(), R.animator.card_flip_left_in);
		//					animFront.setTarget(front);
		//					Animator animBack = AnimatorInflater.loadAnimator(getActivity(), R.animator.card_flip_left_out);
		//					animBack.setTarget(back);
		//					animBack.start();
		//					animFront.start();
		//					Log.d("temp", "back");
		//				}
		//				return true;
		//			}
		//		});
	}

	//	private void fetchLists() {
	//		bDataLoaded = false;
	//		fetchProgramList();
	//		fetchProgramEnrolmentList();
	//		fetchAthleteAttendanceList();
	//		fetchCoachAttendanceList();
	//		fetchAthleteList();
	//		fetchCoachList();
	//		fetchSessionList();
	//	}
	//
	//	private void fetchProgramList() {
	//		KeenCivicoreClient client = new KeenCivicoreClient(getActivity());
	//		client.fetchProgramListData(new CivicoreDataResultListener<KeenProgram>() {
	//
	//			@Override
	//			public void onListResult(List<KeenProgram> list) {
	//				programList.clear();
	//				programList.addAll(list);
	//				for (KeenProgram program : programList) {
	//					sessionProgramMap.put(program.getRemoteId(), program);
	//				}
	//				monitorFetches();
	//			}
	//
	//			@Override
	//			public void onListResultError() {
	//				monitorFetches();
	//			}
	//
	//		});
	//	}
	//
	//	private void fetchProgramEnrolmentList() {
	//		KeenCivicoreClient client = new KeenCivicoreClient(getActivity());
	//		client.fetchProgramEnrolmentListData(new CivicoreDataResultListener<KeenProgramEnrolment>() {
	//
	//			@Override
	//			public void onListResult(List<KeenProgramEnrolment> list) {
	//				programEnrolmentList.clear();
	//				programEnrolmentList.addAll(list);
	//				//				for (KeenProgram program : programList) {
	//				//					sessionProgramMap.put(program.getRemoteId(), program);
	//				//				}
	//				monitorFetches();
	//			}
	//
	//			@Override
	//			public void onListResultError() {
	//				monitorFetches();
	//			}
	//
	//		});
	//	}
	//
	//	private void fetchAthleteAttendanceList() {
	//		KeenCivicoreClient client = new KeenCivicoreClient(getActivity());
	//		client.fetchAthleteAttendanceListData(new CivicoreDataResultListener<AthleteAttendance>() {
	//
	//			@Override
	//			public void onListResult(List<AthleteAttendance> list) {
	//				athleteAttendanceList.clear();
	//				athleteAttendanceList.addAll(list);
	//				formatAthleteAttendanceNames();
	//				monitorFetches();
	//			}
	//
	//			@Override
	//			public void onListResultError() {
	//				monitorFetches();
	//			}
	//
	//		});
	//	}
	//
	//	private void fetchCoachAttendanceList() {
	//		KeenCivicoreClient client = new KeenCivicoreClient(getActivity());
	//		client.fetchCoachAttendanceListData(new CivicoreDataResultListener<CoachAttendance>() {
	//
	//			@Override
	//			public void onListResult(List<CoachAttendance> list) {
	//				coachAttendanceList.clear();
	//				coachAttendanceList.addAll(list);
	//				monitorFetches();
	//			}
	//
	//			@Override
	//			public void onListResultError() {
	//				monitorFetches();
	//			}
	//
	//		});
	//	}
	//
	//	private void fetchAthleteList() {
	//		KeenCivicoreClient client = new KeenCivicoreClient(getActivity());
	//		client.fetchAthleteListData(new CivicoreDataResultListener<Athlete>() {
	//
	//			@Override
	//			public void onListResult(List<Athlete> list) {
	//				athleteList.clear();
	//				athleteList.addAll(list);
	//				for (Athlete athlete : athleteList) {
	//					sessionAthleteMap.put(athlete.getFirstLastName(), athlete);
	//					sessionAthleteIdMap.put(athlete.getRemoteId(), athlete);
	//				}
	//				monitorFetches();
	//			}
	//
	//			@Override
	//			public void onListResultError() {
	//				monitorFetches();
	//			}
	//
	//		});
	//	}
	//
	//	private void fetchCoachList() {
	//		KeenCivicoreClient client = new KeenCivicoreClient(getActivity());
	//		client.fetchCoachListData(new CivicoreDataResultListener<Coach>() {
	//
	//			@Override
	//			public void onListResult(List<Coach> list) {
	//				coachList.clear();
	//				coachList.addAll(list);
	//				for (Coach coach : coachList) {
	//					sessionCoachMap.put(coach.getFirstLastName(), coach);
	//				}
	//				monitorFetches();
	//			}
	//
	//			@Override
	//			public void onListResultError() {
	//				monitorFetches();
	//			}
	//
	//		});
	//	}
	//
	//	private void fetchSessionList() {
	//		KeenCivicoreClient client = new KeenCivicoreClient(getActivity());
	//		client.fetchSessionListData(new CivicoreDataResultListener<KeenSession>() {
	//
	//			@Override
	//			public void onListResult(List<KeenSession> list) {
	//				sessionList.clear();
	//				sessionList.addAll(list);
	//				for (KeenSession session : sessionList) {
	//					sessionMap.put(session.getRemoteId(), session);
	//				}
	//
	//				monitorFetches();
	//			}
	//
	//			@Override
	//			public void onListResultError() {
	//				monitorFetches();
	//			}
	//
	//		});
	//	}
	//
	//	private void monitorFetches() {
	//		currentFetch++;
	//		if (currentFetch == NUM_SESSION_FETCHES) {
	//			updateSessionListViews();
	//		}
	//	}

	//	private void updateSessionListViews() {
	//
	//		//		bDataLoaded = true;
	//
	//		// Received all the lists required for session list
	//		// Get the location and time information from program associated with the given session
	//		connectSessionWithProgram();
	//		connectProgramWithEnrolmentList();
	//		connectSessionWithCoachAttendance();
	//		connectSessionWithAthleteAttendance();
	//
	//		Collections.sort(sessionList, Collections.reverseOrder(new KeenSessionComparator()));
	//
	//		expandableStickySessionListAdapter = new StickySessionListItemAdapter(getActivity(), sessionList);
	//		expandableStickySessionListView.setAdapter(expandableStickySessionListAdapter);
	//
	//		setSearchListPosition(sessionList);
	//		//		setSessionListToCurrentDate();
	//
	//		if (llLoadingSessionsIndicator != null) {
	//			llLoadingSessionsIndicator.setVisibility(View.GONE);
	//		}
	//	}

	//	private void connectSessionWithProgram() {
	//
	//		for (KeenSession session : sessionList) {
	//			KeenProgram program;
	//			program = (KeenProgram) sessionProgramMap.get(session.getProgram().getRemoteId());
	//			if (program != null) {
	//				session.setProgram(program);
	//			}
	//		}
	//	}

	//	private void connectProgramWithEnrolmentList() {
	//
	//		for (KeenProgramEnrolment programEnrolment : programEnrolmentList) {
	//			KeenProgram program;
	//			program = (KeenProgram) sessionProgramMap.get(programEnrolment.getProgram().getRemoteId());
	//			if (program != null) {
	//				programEnrolment.setProgram(program);
	//				programEnrolment.setAthlete(sessionAthleteIdMap.get(programEnrolment.getAthlete().getRemoteId()));
	//				program.addEnrolledAthletes(programEnrolment.getAthlete());
	//			}
	//		}
	//	}
	//
	//	private void connectSessionWithCoachAttendance() {
	//
	//		for (CoachAttendance coachatt : coachAttendanceList) {
	//
	//			KeenSession session;
	//			session = (KeenSession) sessionMap.get(coachatt.getRemoteSessionId());
	//			if (session != null) {
	//				session.addCoachAttendance(coachatt);
	//			}
	//
	//			Coach coach;
	//			coach = (Coach) sessionCoachMap.get(coachatt.getAttendedCoachFullName());
	//			if (coach != null) {
	//				coachatt.setCoach(coach);
	//			}
	//		}
	//	}
	//
	//	private void connectSessionWithAthleteAttendance() {
	//
	//		for (AthleteAttendance athleteatt : athleteAttendanceList) {
	//			KeenSession session;
	//			session = (KeenSession) sessionMap.get(athleteatt.getRemoteSessionId());
	//			if (session != null) {
	//				session.addAthleteAttendance(athleteatt);
	//			}
	//
	//			Athlete athlete;
	//			athlete = (Athlete) sessionAthleteMap.get(athleteatt.getAttendedAthleteFullName());
	//			if (athlete != null) {
	//				athleteatt.setAthlete(athlete);
	//			}
	//		}
	//
	//	}

	//	private void formatAthleteAttendanceNames() {
	//		for (AthleteAttendance athleteatt : athleteAttendanceList) {
	//			String LastName = "";
	//			String FirstName = "";
	//			String fullName;
	//			String athleteName = athleteatt.getAttendedAthleteFullName();
	//			String[] nameWord = athleteName.split(",");
	//
	//			if (nameWord[0] != null) {
	//				LastName = nameWord[0];
	//			}
	//
	//			if (nameWord[1] != null) {
	//				FirstName = nameWord[1];
	//				FirstName = FirstName.substring(FirstName.indexOf(' ') + 1, FirstName.length());
	//				FirstName = FirstName + " ";
	//			}
	//
	//			fullName = FirstName + LastName;
	//			athleteatt.setAthleteFullName(fullName);
	//		}
	//	}

	private void openSessionDetails(int pos) {

		Intent i = new Intent(getActivity(), SessionDetailsActivity.class);
		i.putExtra("SESSION_ID", sessionList.get(pos).getId());
		startActivity(i);
		getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
	}

	private void setAdapter() {
		expandableStickySessionListAdapter = new StickySessionListItemAdapter(getActivity(), sessionList);
	}

	//	private void setViews(View v) {
	//		llLoadingSessionsIndicator = (LinearLayout) v.findViewById(R.id.llLoadingSessionsIndicator);
	//		if (!bDataLoaded) {
	//			llLoadingSessionsIndicator.setVisibility(View.VISIBLE);
	//			loadProgressBar();
	//		}
	//
	//		expandableStickySessionListView = (ExpandableStickyListHeadersListView) v.findViewById(R.id.lvSessionList);
	//		expandableStickySessionListView.setAdapter(expandableStickySessionListAdapter);
	//	}
	//
	//	private void loadProgressBar() {
	//		//		progressBar.getProgressDrawable().setColorFilter(Color.GREEN, Mode.MULTIPLY);
	//		try {
	//			for (int i = 1; i <= 10; i++) {
	//				progressBar.setProgress(i * 10);
	//				Thread.sleep(500);
	//			}
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//	}

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
//		switch (item.getItemId()) {
//		case R.id.miCollapseAll:
//			collapseAllListItems();
//			return true;
//
//		case R.id.miExpandAll:
//			expandAllListItems();
//			return true;
//
//		default:
			return super.onOptionsItemSelected(item);
//		}
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

	private void setSearchListPosition(List<KeenSession> tempSessionList) {
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

	//	public void updateSession(KeenSession newSession) {
	//		KeenSession oldSession = (KeenSession) sessionMap.get(newSession.getRemoteId());
	//		oldSession.setAthleteAttendance(newSession.getAthleteAttendance());
	//		oldSession.setCoachAttendance(newSession.getCoachAttendance());
	//		expandableStickySessionListAdapter = new StickySessionListItemAdapter(getActivity(), sessionList);
	//		expandableStickySessionListView.setAdapter(expandableStickySessionListAdapter);
	//
	//		for (int i = 0; i < sessionList.size(); i++) {
	//			if (sessionList.get(i).getRemoteId() == oldSession.getRemoteId()) {
	//				expandableStickySessionListView.setSelection(i);
	//				break;
	//			}
	//		}
	//	}

	//	public void addAPIData(List<KeenSession> sessions) {
	//		sessionListAdapter.clear();
	//		sessionListAdapter.addAll(sessions);
	//		
	//		expandableStickySessionListAdapter.clear();
	//		expandableStickySessionListAdapter.addAll(sessions);

	//	}

	//animation executor
	class AnimationExecutor implements ExpandableStickyListHeadersListView.IAnimationExecutor {

		@Override
		public void executeAnim(final View target, final int animType) {
			if (ExpandableStickyListHeadersListView.ANIMATION_EXPAND == animType && target.getVisibility() == View.VISIBLE) {
				return;
			}
			if (ExpandableStickyListHeadersListView.ANIMATION_COLLAPSE == animType && target.getVisibility() != View.VISIBLE) {
				return;
			}
			if (mOriginalViewHeightPool.get(target) == null) {
				mOriginalViewHeightPool.put(target, target.getHeight());
			}
			final int viewHeight = mOriginalViewHeightPool.get(target);
			float animStartY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? 0f : viewHeight;
			float animEndY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? viewHeight : 0f;
			final ViewGroup.LayoutParams lp = target.getLayoutParams();

			ValueAnimator animator = ValueAnimator.ofFloat(animStartY, animEndY);
			animator.setDuration(300);
			target.setVisibility(View.VISIBLE);
			animator.addListener(new AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animator) {
				}

				@Override
				public void onAnimationEnd(Animator animator) {
					if (animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND) {
						target.setVisibility(View.VISIBLE);
					} else {
						target.setVisibility(View.GONE);
					}
					target.getLayoutParams().height = viewHeight;
				}

				@Override
				public void onAnimationCancel(Animator animator) {

				}

				@Override
				public void onAnimationRepeat(Animator animator) {

				}
			});

			animator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator valueAnimator) {
					lp.height = ((Float) valueAnimator.getAnimatedValue()).intValue();
					target.setLayoutParams(lp);
					target.requestLayout();
				}
			});
			animator.start();
		}
	}

	private class LoadSessionListDataTask extends AsyncTask<String, Void, List<KeenSession>> {

		@Override
		protected void onPreExecute() {
			if (llLoadingSessionsIndicator != null) {
				llLoadingSessionsIndicator.setVisibility(View.VISIBLE);
				loadProgressBar();
			}
		}

		@Override
		protected List<KeenSession> doInBackground(String... params) {
			SessionDAO sessionDAO = new SessionDAO(getActivity());
			List<KeenSession> sessions = sessionDAO.getKeenSessionList();
			return sessions;
		}

		@Override
		protected void onPostExecute(List<KeenSession> sessions) {
			if (llLoadingSessionsIndicator != null) {
				llLoadingSessionsIndicator.setVisibility(View.GONE);
			}
			sessionList = sessions;
			expandableStickySessionListAdapter = new StickySessionListItemAdapter(getActivity(), sessions);
			expandableStickySessionListView.setAdapter(expandableStickySessionListAdapter);
			setSearchListPosition(sessionList);
			if (isFirstView) {
				isFirstView = false;
				setSessionListToCurrentDate();
			}
		}

	}

	private void loadProgressBar() {
		try {
			for (int i = 1; i <= 10; i++) {
				progressBar.setProgress(i*10);
				Thread.sleep(500);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onResume() {
		new LoadSessionListDataTask().execute();
		super.onResume();
	}
}
