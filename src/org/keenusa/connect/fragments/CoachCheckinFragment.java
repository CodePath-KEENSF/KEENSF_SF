package org.keenusa.connect.fragments;

import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.adapters.CoachCheckInAdapter;
import org.keenusa.connect.models.CoachAttendance;
import org.keenusa.connect.models.CoachAttendance.AttendanceValue;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.networking.KeenCivicoreClient.CivicoreDataResultListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

public class CoachCheckinFragment extends Fragment {
	
	public String dummySearchString;
	private SearchView searchView;

	private LinearLayout llProgressBarCoachCheckin;
	private ListView lvCoachCheckin;
	private CoachCheckInAdapter coachCheckInAdapter;
	
	private ArrayList<KeenSession> sessionList;
	private List<CoachAttendance> coachAttendanceList;
	
	private boolean bDataLoaded = false;
	
	private KeenSession session;
	
	// Creates a new fragment with given arguments
	public static CoachCheckinFragment newInstance(KeenSession session) {
		CoachCheckinFragment coachCheckinFragment = new CoachCheckinFragment();
		coachCheckinFragment.session = session;
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

	private void removeProgressBars() {
		bDataLoaded = true;
		if (llProgressBarCoachCheckin != null) {
			llProgressBarCoachCheckin.setVisibility(View.GONE);
		}
	}

	private void fetchEnrolledCoachList() {
		// TODO Auto-generated method stub
		
	}

	private void setAdapter(){
		if(session.getCoachAttendance() == null){
			coachAttendanceList = new ArrayList<CoachAttendance>();
		}else{
			coachAttendanceList = session.getCoachAttendance();
		}
		coachCheckInAdapter = new CoachCheckInAdapter(getActivity(), coachAttendanceList);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_coach_checkin, container, false);

		setViews(v);

		setOnClickListeners();

		return v;
	}

	private void setOnClickListeners() {
		// TODO Auto-generated method stub
		
	}

	private void setViews(View v) {
		llProgressBarCoachCheckin = (LinearLayout)v.findViewById(R.id.llProgressBarCoachCheckin);
		if (!bDataLoaded) {
			llProgressBarCoachCheckin.setVisibility(View.VISIBLE);
		}

		lvCoachCheckin = (ListView) v.findViewById(R.id.lvCoachCheckin);
		lvCoachCheckin.setAdapter(coachCheckInAdapter);
	}

	private void fetchSessionList() {
		KeenCivicoreClient client = new KeenCivicoreClient(getActivity());
		client.fetchSessionListData(new CivicoreDataResultListener<KeenSession>() {

			@Override
			public void onListResult(List<KeenSession> list) {
				sessionList.clear();
				sessionList.addAll(list);
				
				for (KeenSession fetchedSession : sessionList) {
					if(fetchedSession.getRemoteId() == session.getRemoteId()){
						fetchedSession = session;
					}
				}
			}

			@Override
			public void onListResultError() {
			}

		});
	}
}
