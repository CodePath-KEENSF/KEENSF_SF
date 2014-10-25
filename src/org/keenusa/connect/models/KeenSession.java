package org.keenusa.connect.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.keenusa.connect.helpers.CivicoreDateStringParser;
import org.keenusa.connect.helpers.CivicoreSessionOpenToRegStringParser;
import org.keenusa.connect.helpers.CivicoreTimestampStringParser;
import org.keenusa.connect.models.remote.RemoteSession;

public class KeenSession implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5601978164288806597L;
	private long id;
	private long remoteId;
	private DateTime date;
	private KeenProgram program;
	private long remoteCreateTimestamp;
	private long remoteUpdatedTimestamp;

	private boolean openToPublicRegistration;
	private int numberOfNewCoachesNeeded;
	private int numberOfReturningCoachesNeeded;

	private int numberOfAthletesRegistered;
	private int numberOfCoachesRegistered;
	private int numberOfAthletesCheckedIn;
	private int numberOfCoachesCheckedIn;

	private int numberOfCoachesAttended;

	private List<AthleteAttendance> athleteAttendance;
	private List<CoachAttendance> coachAttendance;

	public KeenSession() {
		super();
	}

	public KeenSession(long remoteId, DateTime date, KeenProgram program, boolean openToPublicRegistration, int numberOfNewCoachesNeeded,
			int numberOfReturningCoachesNeeded, List<AthleteAttendance> athleteAttendance, List<CoachAttendance> coachAttendance) {
		super();
		this.remoteId = remoteId;
		this.date = date;
		this.program = program;
		this.openToPublicRegistration = openToPublicRegistration;
		this.numberOfNewCoachesNeeded = numberOfNewCoachesNeeded;
		this.numberOfReturningCoachesNeeded = numberOfReturningCoachesNeeded;
		this.athleteAttendance = athleteAttendance;
		this.coachAttendance = coachAttendance;
	}

	public static KeenSession fromRemoteSession(RemoteSession remoteSession) {
		KeenSession keenSession = null;
		if (remoteSession != null) {
			keenSession = new KeenSession();
			keenSession.setRemoteId(Long.valueOf(remoteSession.getRemoteId()));
			keenSession.setDate(CivicoreDateStringParser.parseDate(remoteSession.getAttendanceDate()));
			KeenProgram program = new KeenProgram();
			program.setRemoteId(Long.valueOf(remoteSession.getClassesId()));
			program.setName(remoteSession.getClassesName());
			keenSession.setProgram(program);
			if (remoteSession.getAthletes() != null) {
				keenSession.setNumberOfRegisteredAthletes(Integer.valueOf(remoteSession.getAthletes()));
			}
			if (remoteSession.getCoachesAttended() != null) {
				keenSession.setNumberOfCoachesAttended(Integer.valueOf(remoteSession.getCoachesAttended()));
			}
			if (remoteSession.getCoachesRegistered() != null) {
				keenSession.setNumberOfCoachesRegistered(Integer.valueOf(remoteSession.getCoachesRegistered()));
			}
			if (remoteSession.getNewCoachesNeeded() != null) {
				keenSession.setNumberOfNewCoachesNeeded(Integer.valueOf(remoteSession.getNewCoachesNeeded()));
			}
			if (remoteSession.getReturningCoachesNeeded() != null) {
				keenSession.setNumberOfReturningCoachesNeeded(Integer.valueOf(remoteSession.getReturningCoachesNeeded()));
			}
			keenSession.setRemoteCreateTimestamp(CivicoreTimestampStringParser.parseTimestamp(remoteSession.getCreated()).getMillis());
			keenSession.setRemoteUpdatedTimestamp(CivicoreTimestampStringParser.parseTimestamp(remoteSession.getUpdated()).getMillis());
			keenSession.setOpenToPublicRegistration(CivicoreSessionOpenToRegStringParser.parseSessionOpenToRegString(remoteSession
					.getOpenToPublicRegistration()));
		}
		return keenSession;
	}

	public static List<KeenSession> fromRemoteSessionList(List<RemoteSession> remoteSessionList) {
		List<KeenSession> sessions = null;
		if (remoteSessionList != null) {
			sessions = new ArrayList<KeenSession>(remoteSessionList.size());
			for (RemoteSession remoteSession : remoteSessionList) {
				KeenSession keenSession = fromRemoteSession(remoteSession);
				sessions.add(keenSession);
			}

		} else {
			sessions = new ArrayList<KeenSession>();
		}
		return sessions;
	}

	public int getRegisteredAthleteCount() {
		int registeredAthleteCount = 0;
		if (program != null && program.getEnrolledAthletes() != null) {
			registeredAthleteCount = program.getEnrolledAthletes().size();
		} else {
			registeredAthleteCount = getNumberOfRegisteredAthletes();
		}
		return registeredAthleteCount;
	}

	public int getRegisteredCoachCount() {
		int registeredCoachCount = 0;
		if (coachAttendance != null) {
			registeredCoachCount = coachAttendance.size();
		} else {
			registeredCoachCount = getNumberOfCoachesRegistered();
		}

		return registeredCoachCount;
	}

	public int getAttendedCoachCount() {
		int attendedCoachCount = 0;
		if (coachAttendance != null) {
			for (CoachAttendance attendance : coachAttendance) {
				if (attendance.getAttendanceValue() == CoachAttendance.AttendanceValue.ATTENDED) {
					attendedCoachCount++;
				}
			}
		} else {
			attendedCoachCount = getNumberOfCoachesAttended();
		}

		return attendedCoachCount;
	}

	public int getNumberOfRegisteredAthletes() {
		return numberOfAthletesRegistered;
	}

	private int getNumberOfCoachesAttended() {
		return numberOfCoachesAttended;
	}

	private int getNumberOfCoachesRegistered() {
		return numberOfCoachesRegistered;
	}

	private void setNumberOfRegisteredAthletes(int numberOfRegisteredAthletes) {
		this.numberOfAthletesRegistered = numberOfRegisteredAthletes;
	}

	private void setNumberOfCoachesAttended(int numberOfCoachesAttended) {
		this.numberOfCoachesAttended = numberOfCoachesAttended;
	}

	public void setNumberOfCoachesRegistered(int numberOfCoachesRegistered) {
		this.numberOfCoachesRegistered = numberOfCoachesRegistered;
	}

	public long getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(long remoteId) {
		this.remoteId = remoteId;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public boolean isOpenToPublicRegistration() {
		return openToPublicRegistration;
	}

	public void setOpenToPublicRegistration(boolean openToPublicRegistration) {
		this.openToPublicRegistration = openToPublicRegistration;
	}

	public KeenProgram getProgram() {
		return program;
	}

	public void setProgram(KeenProgram program) {
		this.program = program;
	}

	public List<AthleteAttendance> getAthleteAttendance() {
		return athleteAttendance;
	}

	public void setAthleteAttendance(List<AthleteAttendance> athleteAttendance) {
		this.athleteAttendance = athleteAttendance;
	}

	public void addAthleteAttendance(AthleteAttendance athleteAttendance) {
		if (this.athleteAttendance == null) {
			this.athleteAttendance = new ArrayList<AthleteAttendance>();
		}
		this.athleteAttendance.add(athleteAttendance);
	}

	public List<CoachAttendance> getCoachAttendance() {
		return coachAttendance;
	}

	public void addCoachAttendance(CoachAttendance coachAttendance) {
		if (this.coachAttendance == null) {
			this.coachAttendance = new ArrayList<CoachAttendance>();
		}
		this.coachAttendance.add(coachAttendance);
	}

	public void setCoachAttendance(List<CoachAttendance> coachAttendance) {
		this.coachAttendance = coachAttendance;
	}

	public int getNumberOfReturningCoachesNeeded() {
		return numberOfReturningCoachesNeeded;
	}

	public void setNumberOfReturningCoachesNeeded(int numberOfReturningCoachesNeeded) {
		this.numberOfReturningCoachesNeeded = numberOfReturningCoachesNeeded;
	}

	public int getNumberOfNewCoachesNeeded() {
		return numberOfNewCoachesNeeded;
	}

	public void setNumberOfNewCoachesNeeded(int numberOfNewCoachesNeeded) {
		this.numberOfNewCoachesNeeded = numberOfNewCoachesNeeded;
	}

	public long getRemoteCreateTimestamp() {
		return remoteCreateTimestamp;
	}

	public void setRemoteCreateTimestamp(long remoteCreateTimestamp) {
		this.remoteCreateTimestamp = remoteCreateTimestamp;
	}

	public long getRemoteUpdatedTimestamp() {
		return remoteUpdatedTimestamp;
	}

	public void setRemoteUpdatedTimestamp(long remoteUpdatedTimestamp) {
		this.remoteUpdatedTimestamp = remoteUpdatedTimestamp;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getNumberOfAthletesCheckedIn() {
		return numberOfAthletesCheckedIn;
	}

	public void setNumberOfAthletesCheckedIn(int numberOfAthletesCheckedIn) {
		this.numberOfAthletesCheckedIn = numberOfAthletesCheckedIn;
	}

	public int getNumberOfCoachesCheckedIn() {
		return numberOfCoachesCheckedIn;
	}

	public void setNumberOfCoachesCheckedIn(int numberOfCoachesCheckedIn) {
		this.numberOfCoachesCheckedIn = numberOfCoachesCheckedIn;
	}

	public int getNumberOfAthletesRegistered() {
		return numberOfAthletesRegistered;
	}

	public void setNumberOfAthletesRegistered(int numberOfAthletesRegistered) {
		this.numberOfAthletesRegistered = numberOfAthletesRegistered;
	}

}
