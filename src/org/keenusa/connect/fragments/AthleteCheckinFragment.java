package org.keenusa.connect.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.activities.AthleteProfileActivity;
import org.keenusa.connect.adapters.AthleteCheckinAdapter;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.AthleteAttendance;
import org.keenusa.connect.models.AthleteAttendance;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.networking.KeenCivicoreClient.CivicoreDataResultListener;
import org.keenusa.connect.networking.KeenCivicoreClient.CivicoreUpdateDataResultListener;
import org.keenusa.connect.utilities.StringConstants;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class AthleteCheckinFragment extends Fragment {

	public static final String ATHLETE_EXTRA_TAG = "ATHLETE";
	
	public String dummySearchString;
	private SearchView searchView;

	private LinearLayout llProgressBarAthleteCheckin;
	private ListView lvAthleteCheckin;
	private AthleteCheckinAdapter athleteCheckInAdapter;

	private ArrayList<KeenSession> sessionList;
	private List<AthleteAttendance> athleteAttendanceList;
	private List<AthleteAttendance> athleteAttendanceListOriginal;
	private HashMap<String, Athlete> AthleteAttendanceMap = new HashMap<String, Athlete>();

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
		
		athleteAttendanceListOriginal = new ArrayList<AthleteAttendance>();

		for (int i = 0; i < athleteAttendanceList.size(); i++) {

			athleteAttendanceListOriginal.add(new AthleteAttendance());

			athleteAttendanceListOriginal.get(i).setAttendanceValue(
					athleteAttendanceList.get(i).getAttendanceValue());

		}

		// Add existing athletes in hash map
		for(AthleteAttendance athleteAttendance: athleteAttendanceList){
			AthleteAttendanceMap.put(athleteAttendance.getAthlete().getFirstLastName(), athleteAttendance.getAthlete());
		}
			
		for(Athlete athlete: session.getProgram().getEnrolledAthletes()){
			if(AthleteAttendanceMap.get(athlete.getFirstLastName()) == null){
				athleteAttendanceList.add(new AthleteAttendance());
				AthleteAttendance athleteAttendance = athleteAttendanceList.get(athleteAttendanceList.size() - 1);
				athleteAttendance.setAthlete(athlete);
				athleteAttendance.setRemoteSessionId(session.getRemoteId());
			}
		}
	}

	private void setAdapter() {
		athleteCheckInAdapter = new AthleteCheckinAdapter(getActivity(),
				athleteAttendanceList);

	}

	private void setOnClickListeners() {
		lvAthleteCheckin.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(getActivity(), AthleteProfileActivity.class);
				i.putExtra(ATHLETE_EXTRA_TAG, athleteCheckInAdapter.getItem(position).getAthlete());
				startActivity(i);

			}
		});
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

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.miSendMessageAthletes) {
			showMassMessageDialog();
		}

		return super.onOptionsItemSelected(item);
	}

	private void showMassMessageDialog() {
		DialogFragment newFragment = new MassMessageFragment(
				session.getProgram().getEnrolledAthletes(), null);
		newFragment.show(getActivity().getSupportFragmentManager(), "Mass Message Dialog");
	}

	public void postAttendance() {
		for (int i = 0; i < athleteAttendanceList.size(); i++) {
			if (i < athleteAttendanceListOriginal.size()) {
				if (athleteAttendanceListOriginal.get(i) != null) {
					if (athleteAttendanceListOriginal.get(i).getAttendanceValue() != athleteAttendanceList
							.get(i).getAttendanceValue()) {

						updateRecord(athleteAttendanceList.get(i));
						athleteAttendanceListOriginal.get(i).setAttendanceValue(athleteAttendanceList.get(i).getAttendanceValue());
					}
				}
			} else { // new attendance records
				if(athleteAttendanceList.get(i).getAttendanceValue() != null){
					athleteAttendanceList.get(i).setRemoteSessionId(
							athleteAttendanceList.get(0).getRemoteSessionId());
					addRecord(athleteAttendanceList.get(i));
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
						Log.d("temp", "attendance post error");

					}
				});
	}

	public void addRecord(AthleteAttendance athlete) {
		client.insertNewAthleteAttendanceRecord(athlete,
				new CivicoreUpdateDataResultListener<AthleteAttendance>() {

					@Override
					public void onRecordUpdateResult(AthleteAttendance object) {
						Log.d("temp", "attendance posted");
					}

					@Override
					public void onRecordUpdateError() {
						Log.d("temp", "attendance post error");

					}
				});
	}

}
