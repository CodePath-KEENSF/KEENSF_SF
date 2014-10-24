package org.keenusa.connect.fragments;

import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.activities.CoachProfileActivity;
import org.keenusa.connect.adapters.CoachCheckInAdapter;
import org.keenusa.connect.models.Coach;
import org.keenusa.connect.models.CoachAttendance;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.networking.KeenCivicoreClient.CivicoreDataResultListener;
import org.keenusa.connect.networking.KeenCivicoreClient.CivicoreUpdateDataResultListener;
import org.keenusa.connect.utilities.CheckinEditMode;
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

public class CoachCheckinFragment extends Fragment {

	public static final String COACH_EXTRA_TAG = "COACH";

	public String dummySearchString;
	private SearchView searchView;

	private LinearLayout llProgressBarCoachCheckin;
	private ListView lvCoachCheckin;
	private CoachCheckInAdapter coachCheckInAdapter;

	private ArrayList<KeenSession> sessionList;
	private List<CoachAttendance> coachAttendanceList;
	private List<CoachAttendance> coachAttendanceListOriginal;

	private boolean bDataLoaded = false;

	private KeenSession session;
	KeenCivicoreClient client;

	// Creates a new fragment with given arguments
	public static CoachCheckinFragment newInstance(KeenSession session, KeenCivicoreClient client) {
		CoachCheckinFragment coachCheckinFragment = new CoachCheckinFragment();
		coachCheckinFragment.session = session;
		coachCheckinFragment.client = client;
		return coachCheckinFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
		bDataLoaded = false;
		fetchEnrolledCoachList();
		setAdapter();
		removeProgressBars();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_coach_checkin, container, false);

		setViews(v);

		setOnClickListeners();

		return v;
	}

	private void removeProgressBars() {
		bDataLoaded = true;
		if (llProgressBarCoachCheckin != null) {
			llProgressBarCoachCheckin.setVisibility(View.GONE);
		}
	}

	private void fetchEnrolledCoachList() {
		if (session.getCoachAttendance() == null) {
			coachAttendanceList = new ArrayList<CoachAttendance>();
		} else {
			coachAttendanceList = session.getCoachAttendance();
		}

		coachAttendanceListOriginal = new ArrayList<CoachAttendance>();

		for (int i = 0; i < coachAttendanceList.size(); i++) {

			coachAttendanceListOriginal.add(new CoachAttendance());

			coachAttendanceListOriginal.get(i).setAttendanceValue(coachAttendanceList.get(i).getAttendanceValue());

		}
	}

	private void setAdapter() {
		coachCheckInAdapter = new CoachCheckInAdapter(getActivity(), coachAttendanceList);
	}

	private void setOnClickListeners() {
		lvCoachCheckin.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(getActivity(), CoachProfileActivity.class);
				i.putExtra(COACH_EXTRA_TAG,
						coachCheckInAdapter.getItem(position).getCoach());
				startActivity(i);

			}
		});
	}

	private void setViews(View v) {
		llProgressBarCoachCheckin = (LinearLayout) v.findViewById(R.id.llProgressBarCoachCheckin);
		if (!bDataLoaded) {
			llProgressBarCoachCheckin.setVisibility(View.VISIBLE);
		}

		lvCoachCheckin = (ListView) v.findViewById(R.id.lvCoachCheckin);
		lvCoachCheckin.setAdapter(coachCheckInAdapter);
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

	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		
		MenuItem miAddCoaches = menu.findItem(R.id.miAddCoaches);
		if(CheckinEditMode.editMode == true){
	    	miAddCoaches.setVisible(true);
	    }else{
	    	miAddCoaches.setVisible(false);
	    }
		super.onPrepareOptionsMenu(menu);
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.d("temp", "onCfreate");
		inflater.inflate(R.menu.coach_checkin, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search_coaches_checkin);
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
				ArrayList<CoachAttendance> tempCoachAttendanceList = new ArrayList<CoachAttendance>();
				int searchTextlength = searchText.length();

				// Create the new arraylist for each search character
				for (CoachAttendance coachAttendance : coachAttendanceList) {

					String fullName = coachAttendance.getCoach().getFirstLastName();

					if (searchTextlength <= coachAttendance.getCoach().getFirstName().length()
							|| searchTextlength <= coachAttendance.getCoach().getLastName().length()) {

						if (fullName.toLowerCase().contains(searchText.toLowerCase())) {
							tempCoachAttendanceList.add(coachAttendance);
						}
					}
				}

				coachCheckInAdapter = new CoachCheckInAdapter(getActivity(), tempCoachAttendanceList);
				lvCoachCheckin.setAdapter(coachCheckInAdapter);

				return true;
			}
		});
		super.onCreateOptionsMenu(menu, inflater);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("temp", "OnSelect");
		if (item.getItemId() == R.id.miSendMessageCoaches) {
			showMassMessageDialog();
		} else if (item.getItemId() == R.id.miAddCoaches) {
			DialogFragment newFragment = new AddCoachToCheckinFragment();
			newFragment.show(getActivity().getSupportFragmentManager(),
					"Add Coach");
		}

		return super.onOptionsItemSelected(item);
	}

	private void showMassMessageDialog() {
		DialogFragment newFragment = new MassMessageFragment(null, coachAttendanceList);
		newFragment.show(getActivity().getSupportFragmentManager(),
				"Mass Message Dialog");
	}

	public void postAttendance() {
		for (int i = 0; i < coachAttendanceList.size(); i++) {
			if (i < coachAttendanceListOriginal.size()) {
				if (coachAttendanceListOriginal.get(i) != null) {
				if (coachAttendanceListOriginal.get(i).getAttendanceValue() != coachAttendanceList.get(i).getAttendanceValue()) {

						updateRecord(coachAttendanceList.get(i));
					}
				}
			} else { // new attendance recors
				coachAttendanceList.get(i).setRemoteSessionId(
						coachAttendanceList.get(0).getRemoteSessionId());
				addRecord(coachAttendanceList.get(i));
			}

		}
	}

	public void updateRecord(CoachAttendance coach) {
		client.updateCoachAttendanceRecord(coach, new CivicoreUpdateDataResultListener<CoachAttendance>() {

			@Override
			public void onRecordUpdateResult(CoachAttendance object) {
				Log.d("temp", "attendance posted");
			}

			@Override
			public void onRecordUpdateError() {
				Log.d("temp", "attendance post error");

			}
		});
	}

	public void addRecord(CoachAttendance coach) {
		client.insertNewCoachAttendanceRecord(coach,
				new CivicoreUpdateDataResultListener<CoachAttendance>() {

					@Override
					public void onRecordUpdateResult(CoachAttendance object) {
						Log.d("temp", "attendance posted");
					}

					@Override
					public void onRecordUpdateError() {
						Log.d("temp", "attendance post error");

					}
				});
	}

	public void addCoach(Coach coach) {
		for(CoachAttendance coachAtt: coachAttendanceList){
			if(coachAtt.getCoach().getFirstLastName().equals(coach.getFirstLastName())){
				return;
			}
		}
		coachAttendanceList.add(new CoachAttendance());
		coachAttendanceList.get(coachAttendanceList.size() - 1).setCoach(coach);
		coachCheckInAdapter.notifyDataSetChanged();
	}

}
