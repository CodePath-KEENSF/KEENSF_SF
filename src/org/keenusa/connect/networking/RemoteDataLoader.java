package org.keenusa.connect.networking;

import java.util.List;

import org.keenusa.connect.data.ProgramDAO;
import org.keenusa.connect.data.SessionDAO;
import org.keenusa.connect.models.Affiliate;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.AthleteAttendance;
import org.keenusa.connect.models.Coach;
import org.keenusa.connect.models.CoachAttendance;
import org.keenusa.connect.models.KeenProgram;
import org.keenusa.connect.models.KeenProgramEnrolment;
import org.keenusa.connect.models.KeenSession;

import android.content.Context;
import android.util.Log;

public class RemoteDataLoader extends Thread {

	final Context context;
	final SyncKeenCivicoreClient client;
	final DataLoaderResultListener listener;

	public RemoteDataLoader(Context context, DataLoaderResultListener listener) {
		super();
		this.context = context;
		this.client = new SyncKeenCivicoreClient(context);
		this.listener = listener;
	}

	@Override
	public void run() {

		loadAffiliate();
		loadCoaches();
		loadAthletes();
		loadPrograms();
		loadSessions();
		loadProgramEnrolments();
		loadAthleteAttendances();
		loadCoachAttendances();

	}

	private void loadAffiliate() {
		postProgress("Loading affiliate data ...");
		List<Affiliate> affiliate = null;
		try {
			affiliate = client.fetchAffiliateListData();
			Log.i("DATA_LOAD", "Affiliate fetched " + affiliate.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		postProgress("Affiliate data is loaded");
	}

	private void loadCoaches() {
		postProgress("Loading coach data ...");
		List<Coach> coaches = null;
		try {
			coaches = client.fetchCoachListData();
			Log.i("DATA_LOAD", "Coaches fetched " + coaches.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		postProgress("Coach data is loaded");
	}

	private void loadAthletes() {
		postProgress("Loading athlete data ...");
		List<Athlete> athletes = null;
		try {
			athletes = client.fetchAthleteListData();
			Log.i("DATA_LOAD", "Athletes fetched " + athletes.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		postProgress("Athlete data is loaded");
	}

	private void loadPrograms() {
		postProgress("Loading program data ...");
		List<KeenProgram> programs = null;
		try {
			programs = client.fetchProgramListData();
			Log.i("DATA_LOAD", "Programs fetched " + programs.size());
			ProgramDAO programDAO = new ProgramDAO(context);
			for (KeenProgram program : programs) {
				programDAO.saveNewProgram(program);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		postProgress("Program data is loaded");
	}

	private void loadSessions() {
		postProgress("Loading session data ...");
		List<KeenSession> sessions = null;
		try {
			sessions = client.fetchSessionListData();
			Log.i("DATA_LOAD", "Sessions fetched " + sessions.size());
			SessionDAO sessionDAO = new SessionDAO(context);
			for (KeenSession session : sessions) {
				sessionDAO.saveNewSession(session);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		postProgress("Session data is loaded");
	}

	private void loadProgramEnrolments() {
		postProgress("Loading program enrolment data ...");
		List<KeenProgramEnrolment> keenProgramEnrolments = null;
		try {
			keenProgramEnrolments = client.fetchProgramEnrolmentListData();
			Log.i("DATA_LOAD", "Program enrolments fetched " + keenProgramEnrolments.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		postProgress("Program enrolment data is loaded");
	}

	private void loadAthleteAttendances() {
		postProgress("Loading athlete attendance data ...");
		List<AthleteAttendance> athleteAttendances = null;
		try {
			athleteAttendances = client.fetchAthleteAttendanceListData();
			Log.i("DATA_LOAD", "Athlete attendances fetched " + athleteAttendances.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		postProgress("Athlete attendance data is loaded");
	}

	private void loadCoachAttendances() {
		postProgress("Loading coach attendance data ...");
		List<CoachAttendance> coachAttendances = null;
		try {
			coachAttendances = client.fetchCoachAttendanceListData();
			Log.i("DATA_LOAD", "Coach attendances fetched " + coachAttendances.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		postProgress("Coach attendance data is loaded");
	}

	private void postProgress(final String progressMessage) {
		if (listener != null)
			listener.onDataLoaderProgress(progressMessage);
	}

	public interface DataLoaderResultListener {
		public void onDataLoaderResult();

		public void onDataLoaderError();

		public void onDataLoaderProgress(String progressMessage);
	}

}
