package org.keenusa.connect.networking;

import java.util.List;

import org.keenusa.connect.data.KeenConnectDB;
import org.keenusa.connect.data.daos.AffiliateDAO;
import org.keenusa.connect.data.daos.AthleteAttendanceDAO;
import org.keenusa.connect.data.daos.AthleteDAO;
import org.keenusa.connect.data.daos.CoachAttendanceDAO;
import org.keenusa.connect.data.daos.CoachDAO;
import org.keenusa.connect.data.daos.ProgramDAO;
import org.keenusa.connect.data.daos.ProgramEnrollmentDAO;
import org.keenusa.connect.data.daos.SessionDAO;
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
		postProgress("Loading affiliate data ...", 0);
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
		postProgress("Affiliate data is loaded", 1);
	}

	private void loadCoaches() {
		postProgress("Loading coach data ...", 0);
		List<Coach> coaches = null;
		try {
			coaches = client.fetchCoachListData();
			Log.i("DATA_LOAD", "Coaches fetched " + coaches.size());
			CoachDAO coacheDAO = new CoachDAO(context);
			coacheDAO.saveNewCoachList(coaches);
		} catch (Exception e) {
			e.printStackTrace();
		}
		postProgress("Coach data is loaded", 20);
	}

	private void loadAthletes() {
		postProgress("Loading athlete data ...", 0);
		List<Athlete> athletes = null;
		try {
			athletes = client.fetchAthleteListData();
			Log.i("DATA_LOAD", "Athletes fetched " + athletes.size());
			AthleteDAO athleteDAO = new AthleteDAO(context);
			athleteDAO.saveNewAthleteList(athletes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		postProgress("Athlete data is loaded", 4);
	}

	private void loadPrograms() {
		postProgress("Loading program data ...", 0);
		List<KeenProgram> programs = null;
		try {
			programs = client.fetchProgramListData();
			Log.i("DATA_LOAD", "Programs fetched " + programs.size());
			ProgramDAO programDAO = new ProgramDAO(context);
			programDAO.saveNewProgramList(programs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		postProgress("Program data is loaded", 1);
	}

	private void loadSessions() {
		postProgress("Loading session data ...", 0);
		List<KeenSession> sessions = null;
		try {
			sessions = client.fetchSessionListData();
			Log.i("DATA_LOAD", "Sessions fetched " + sessions.size());
			SessionDAO sessionDAO = new SessionDAO(context);
			sessionDAO.saveNewSessionList(sessions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		postProgress("Session data is loaded", 8);
	}

	private void loadProgramEnrolments() {
		postProgress("Loading program enrolment data ...", 0);
		List<KeenProgramEnrolment> keenProgramEnrolments = null;
		try {
			keenProgramEnrolments = client.fetchProgramEnrolmentListData();
			Log.i("DATA_LOAD", "Program enrolments fetched " + keenProgramEnrolments.size());
			ProgramEnrollmentDAO enrollmentDAO = new ProgramEnrollmentDAO(context);
			enrollmentDAO.saveNewProgramEnrolmentList(keenProgramEnrolments);
		} catch (Exception e) {
			e.printStackTrace();
		}
		postProgress("Program enrolment data is loaded", 7);
	}

	private void loadAthleteAttendances() {
		postProgress("Loading athlete attendance data ...", 0);
		List<AthleteAttendance> athleteAttendances = null;
		try {
			athleteAttendances = client.fetchAthleteAttendanceListData();
			Log.i("DATA_LOAD", "Athlete attendances fetched " + athleteAttendances.size());
			AthleteAttendanceDAO attendanceDAO = new AthleteAttendanceDAO(context);
			attendanceDAO.saveNewAthleteAttendanceList(athleteAttendances);
		} catch (Exception e) {
			e.printStackTrace();
		}
		postProgress("Athlete attendance data is loaded", 36);
	}

	private void loadCoachAttendances() {
		postProgress("Loading coach attendance data ...", 0);
		List<CoachAttendance> coachAttendances = null;
		try {
			coachAttendances = client.fetchCoachAttendanceListData();
			Log.i("DATA_LOAD", "Coach attendances fetched " + coachAttendances.size());
			CoachAttendanceDAO attendanceDAO = new CoachAttendanceDAO(context);
			attendanceDAO.saveNewCoachAttendanceList(coachAttendances);
		} catch (Exception e) {
			e.printStackTrace();
		}
		postProgress("Coach attendance data is loaded", 23);
	}

	private void postProgress(final String progressMessage, int progress) {
		if (listener != null)
			listener.onDataLoaderProgress(progressMessage, progress);
	}

	public interface DataLoaderResultListener {
		public void onDataLoaderResult();

		public void onDataLoaderError();

		public void onDataLoaderProgress(String progressMessage, int progress);
	}

}
