package org.keenusa.connect.networking;

import java.util.List;

import org.keenusa.connect.data.AffiliateDAO;
import org.keenusa.connect.data.AthleteAttendanceDAO;
import org.keenusa.connect.data.AthleteDAO;
import org.keenusa.connect.data.CoachAttendanceDAO;
import org.keenusa.connect.data.CoachDAO;
import org.keenusa.connect.data.KeenConnectDB;
import org.keenusa.connect.data.ProgramDAO;
import org.keenusa.connect.data.ProgramEnrollmentDAO;
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
		// TODO Check if Internet connection

		loadAffiliate();
		loadCoaches();
		loadAthletes();
		loadPrograms();
		loadSessions();
		loadProgramEnrolments();
		loadAthleteAttendances();
		loadCoachAttendances();
		postResult();
	}

	private void postResult() {
		if (listener != null)
			listener.onDataLoaderResult();

	}

	private void loadAffiliate() {
		postProgress("Loading affiliate data ...");
		List<Affiliate> affiliates = null;
		try {
			affiliates = client.fetchAffiliateListData();
			Log.i("DATA_LOAD", "Affiliate fetched " + affiliates.size());
			// server is responsive data is available clean local db
			KeenConnectDB.getKeenConnectDB(context).cleanDB();
			AffiliateDAO affiliateDAO = new AffiliateDAO(context);
			for (Affiliate affiliate : affiliates) {
				affiliateDAO.saveNewAffiliate(affiliate);
			}
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
			CoachDAO coacheDAO = new CoachDAO(context);
			for (Coach coach : coaches) {
				coacheDAO.saveNewCoach(coach);
			}
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
			AthleteDAO athleteDAO = new AthleteDAO(context);
			for (Athlete athlete : athletes) {
				athleteDAO.saveNewAthlete(athlete);
			}
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
			ProgramEnrollmentDAO enrollmentDAO = new ProgramEnrollmentDAO(context);
			for (KeenProgramEnrolment enrollment : keenProgramEnrolments) {
				enrollmentDAO.saveNewProgramEnrolment(enrollment);
			}
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
			AthleteAttendanceDAO attendanceDAO = new AthleteAttendanceDAO(context);
			for (AthleteAttendance attendance : athleteAttendances) {
				attendanceDAO.saveNewAthleteAttendance(attendance);
			}
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
			CoachAttendanceDAO attendanceDAO = new CoachAttendanceDAO(context);
			for (CoachAttendance attendance : coachAttendances) {
				attendanceDAO.saveNewCoachAttendance(attendance);
			}
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
