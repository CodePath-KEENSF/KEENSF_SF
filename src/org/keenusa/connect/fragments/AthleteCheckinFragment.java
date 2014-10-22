package org.keenusa.connect.fragments;

import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.AthleteAttendance;
import org.keenusa.connect.models.CoachAttendance;
import org.keenusa.connect.models.KeenSession;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

public class AthleteCheckinFragment extends Fragment {

	public ArrayList<SessionAttendance> sessionAttendance;
	private List<CoachAttendance> athleteAttendanceList;
	private List<Athlete> enrolledAthletes;

	public String dummySearchString;
	private SearchView searchView;

	private LinearLayout llProgressBarAthleteCheckin;
	private ListView lvAthleteCheckin;
	// private registeredAthleteList;

	private boolean bDataLoaded = false;

	private KeenSession session;

	// Creates a new fragment with given arguments
	public static AthleteCheckinFragment newInstance(KeenSession session) {
		AthleteCheckinFragment athleteCheckinFragment = new AthleteCheckinFragment();
		athleteCheckinFragment.session = session;
		return athleteCheckinFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
		bDataLoaded = false;
		setSessionAttendance();
		fetchEnrolledAthleteList();
	}

	private void setSessionAttendance() {
		sessionAttendance = new ArrayList<SessionAttendance>();

//		for (int i = 0; i < session.getProgram().getEnrolledAthletes().size(); i++) {
//			sessionAttendance.get(i).enrolledAthlete = session.getProgram().getEnrolledAthletes().get(i);
//		}

//		if (session.getAthleteAttendance() != null) {
//			Log.d("temp", session.getAthleteAttendance().size() + "");
//			for (int i = 0; i < session.getAthleteAttendance().size(); i++) {
//				Log.d("temp", session.getAthleteAttendance().get(i)
//						.getAttendedAthleteFullName());
//			}
//		} else {
//			Log.d("temp", "size is null");
//		}

		// Log.d("temp",session.getProgram().getEnrolledAthletes().size()+"");
		// for(int i=0;i<session.getProgram().getEnrolledAthletes().size();i++){
		// Log.d("temp",session.getProgram().getEnrolledAthletes().get(i).getFullName());
		// }

	}

	private void fetchEnrolledAthleteList() {
		// TODO Auto-generated method stub

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
		// lvAthletes.setAdapter(adapter);
	}

	public class SessionAttendance {
		private AthleteAttendance athleteAttendance;
		private Athlete enrolledAthlete;
	}
}
