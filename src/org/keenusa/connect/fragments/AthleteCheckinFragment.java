package org.keenusa.connect.fragments;

import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.adapters.AthleteCheckinAdapter;
import org.keenusa.connect.models.AthleteAttendance;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.networking.KeenCivicoreClient.CivicoreDataResultListener;
import org.keenusa.connect.networking.KeenCivicoreClient.CivicoreUpdateDataResultListener;
import org.keenusa.connect.utilities.StringConstants;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class AthleteCheckinFragment extends Fragment {

	public String dummySearchString;
	private SearchView searchView;

	private LinearLayout llProgressBarAthleteCheckin;
	private ListView lvAthleteCheckin;
	private AthleteCheckinAdapter athleteCheckInAdapter;

	private ArrayList<KeenSession> sessionList;
	private List<AthleteAttendance> athleteAttendanceList;
	private List<AthleteAttendance> athleteAttendanceListOriginal;

	private boolean bDataLoaded = false;

	private KeenSession session;
	KeenCivicoreClient client;

	// Creates a new fragment with given arguments
	public static AthleteCheckinFragment newInstance(KeenSession session,
			KeenCivicoreClient client) {
		AthleteCheckinFragment athleteCheckinFragment = new AthleteCheckinFragment();
		athleteCheckinFragment.session = session;
		athleteCheckinFragment.client = client;
		return athleteCheckinFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
		bDataLoaded = false;
		fetchEnrolledAthleteList();
		setAdapter();
		removeProgressBars();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_athlete_checkin, container,
				false);

		setViews(v);

		setOnClickListeners();

		return v;
	}

	private void removeProgressBars() {
		bDataLoaded = true;
		if (llProgressBarAthleteCheckin != null) {
			llProgressBarAthleteCheckin.setVisibility(View.GONE);
		}
	}

	private void fetchEnrolledAthleteList() {
		if (session.getAthleteAttendance() == null) {
			athleteAttendanceList = new ArrayList<AthleteAttendance>();
		} else {
			athleteAttendanceList = session.getAthleteAttendance();
		}
		
//		for (int i = 0; i < session.getProgram().getEnrolledAthletes().size(); i++) {
//		sessionAttendance.get(i).enrolledAthlete = session.getProgram().getEnrolledAthletes().get(i);
//	}


		athleteAttendanceListOriginal = new ArrayList<AthleteAttendance>();

		for (int i = 0; i < athleteAttendanceList.size(); i++) {

			athleteAttendanceListOriginal.add(new AthleteAttendance());

			athleteAttendanceListOriginal.get(i).setAttendanceValue(
					athleteAttendanceList.get(i).getAttendanceValue());

		}
	}

	private void setAdapter() {
		athleteCheckInAdapter = new AthleteCheckinAdapter(getActivity(),
				athleteAttendanceList);

	}

	private void setOnClickListeners() {
		// TODO Auto-generated method stub

	}

	private void setViews(View v) {
		llProgressBarAthleteCheckin = (LinearLayout) v
				.findViewById(R.id.llProgressBarAthleteCheckin);
		if (!bDataLoaded) {
			llProgressBarAthleteCheckin.setVisibility(View.VISIBLE);
		}

		lvAthleteCheckin = (ListView) v.findViewById(R.id.lvAthleteCheckin);
		lvAthleteCheckin.setAdapter(athleteCheckInAdapter);
	}

	private void fetchSessionList() {
		client.fetchSessionListData(new CivicoreDataResultListener<KeenSession>() {

			@Override
			public void onListResult(List<KeenSession> list) {
				sessionList.clear();
				sessionList.addAll(list);

				for (KeenSession fetchedSession : sessionList) {
					if (fetchedSession.getRemoteId() == session.getRemoteId()) {
						fetchedSession = session;
					}
				}
			}

			@Override
			public void onListResultError() {
			}

		});
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.athlete_checkin, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search_athletees_checkin);
		dummySearchString = StringConstants.DUMMY_SEARCH_STRING;
		searchView = (SearchView) searchItem.getActionView();
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				return true;
			}

			@Override
			public boolean onQueryTextChange(String searchText) {

				if (dummySearchString == StringConstants.DUMMY_SEARCH_STRING) {
					if (!searchText.isEmpty()) {
						dummySearchString = "";
						return true;
					}
				}

				dummySearchString = searchText;
				ArrayList<AthleteAttendance> tempAthleteAttendanceList = new ArrayList<AthleteAttendance>();
				int searchTextlength = searchText.length();

				// Create the new arraylist for each search character
				for (AthleteAttendance athleteAttendance : athleteAttendanceList) {

					String fullName = athleteAttendance.getAthlete()
							.getFirstLastName();

					if (searchTextlength <= athleteAttendance.getAthlete()
							.getFirstName().length()
							|| searchTextlength <= athleteAttendance.getAthlete()
									.getLastName().length()) {

						if (fullName.toLowerCase().contains(
								searchText.toLowerCase())) {
							tempAthleteAttendanceList.add(athleteAttendance);
						}
					}
				}

				athleteCheckInAdapter = new AthleteCheckinAdapter(getActivity(),
						tempAthleteAttendanceList);
				lvAthleteCheckin.setAdapter(athleteCheckInAdapter);

				return true;
			}
		});
		super.onCreateOptionsMenu(menu, inflater);
	}

	public void postAttendance() {
		for (int i = 0; i < athleteAttendanceList.size(); i++) {
			if (athleteAttendanceListOriginal.get(i) != null) {
				if (athleteAttendanceListOriginal.get(i).getAttendanceValue() != athleteAttendanceList
						.get(i).getAttendanceValue()) {
					
					updateRecord(athleteAttendanceList.get(i));
				}
			}
		}
	}

	public void updateRecord(AthleteAttendance athlete) {
		client.updateAthleteAttendanceRecord(athlete,
				new CivicoreUpdateDataResultListener<AthleteAttendance>() {

					@Override
					public void onRecordUpdateResult(AthleteAttendance object) {
						Log.d("temp", "attendance posted");
					}

					@Override
					public void onRecordUpdateError() {
						// TODO Auto-generated method stub

					}
				});
	}

}
