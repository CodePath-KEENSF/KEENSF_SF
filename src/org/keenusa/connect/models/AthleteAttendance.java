package org.keenusa.connect.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.helpers.CivicoreAthleteAttendanceStringParser;
import org.keenusa.connect.helpers.CivicoreTimestampStringParser;
import org.keenusa.connect.models.remote.RemoteAthleteAttendance;

public class AthleteAttendance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5486907615451742803L;
	private long id;
	private long sessionId;

	private long remoteId;
	private long remoteSessionId;
	private long remoteCreateTimestamp;
	private long remoteUpdatedTimestamp;
	// not stored in DB - redundant
	private String athleteFullName;

	private Athlete athlete;
	private AttendanceValue attendanceValue;

	//attendance in remote source (lookup values) in the source id58 for youth
	public enum AttendanceValue {
		REGISTERED("Registered", 746), ATTENDED("Attended", 413), CALLED_IN_ABSENCE("Called in absence", 414), NO_CALL_NO_SHOW("No Call - No Show",
				422);
		private final String displayName;
		private final int remoteKey;

		private AttendanceValue(String displayName, int remoteKey) {
			this.displayName = displayName;
			this.remoteKey = remoteKey;
		}

		public String getDisplayName() {
			return displayName;
		}

		public String getRemoteKeyString() {
			return String.valueOf(remoteKey);
		}
	}

	public AthleteAttendance() {
	}

	public AthleteAttendance(long remoteId, long remoteSessionId, Athlete athlete, AttendanceValue attendanceValue) {
		super();
		this.remoteId = remoteId;
		this.athlete = athlete;
		this.remoteSessionId = remoteSessionId;
		this.attendanceValue = attendanceValue;
	}

	public static AthleteAttendance fromRemoteAthleteAttendance(RemoteAthleteAttendance remoteAthleteAttendance) {
		AthleteAttendance athleteAttendance = null;
		if (remoteAthleteAttendance != null) {
			athleteAttendance = new AthleteAttendance();
			athleteAttendance.setRemoteId(Long.valueOf(remoteAthleteAttendance.getRemoteId()));
			athleteAttendance.setAthleteFullName(remoteAthleteAttendance.getYouthName());

			Athlete athlete = new Athlete();
			athlete.setRemoteId(Long.valueOf(remoteAthleteAttendance.getYouthId()));
			athleteAttendance.setAthlete(athlete);
			athleteAttendance.setRemoteSessionId(Long.valueOf(remoteAthleteAttendance.getClassesDaysId()));
			athleteAttendance.setAttendanceValue(CivicoreAthleteAttendanceStringParser.parseAthleteAttendanceString(remoteAthleteAttendance
					.getAttendance()));
			athleteAttendance
					.setRemoteCreateTimestamp(CivicoreTimestampStringParser.parseTimestamp(remoteAthleteAttendance.getCreated()).getMillis());
			athleteAttendance.setRemoteUpdatedTimestamp(CivicoreTimestampStringParser.parseTimestamp(remoteAthleteAttendance.getUpdated())
					.getMillis());
		}
		return athleteAttendance;
	}

	public static List<AthleteAttendance> fromRemoteAthleteAttendanceList(List<RemoteAthleteAttendance> remoteAthleteAttendanceList) {
		List<AthleteAttendance> athleteAttendances = null;
		if (remoteAthleteAttendanceList != null) {
			athleteAttendances = new ArrayList<AthleteAttendance>(remoteAthleteAttendanceList.size());
			for (RemoteAthleteAttendance remoteAthleteAttendance : remoteAthleteAttendanceList) {
				AthleteAttendance athleteAttendance = fromRemoteAthleteAttendance(remoteAthleteAttendance);
				athleteAttendances.add(athleteAttendance);
			}

		} else {
			athleteAttendances = new ArrayList<AthleteAttendance>();
		}
		return athleteAttendances;
	}

	public AttendanceValue getAttendanceValue() {
		return attendanceValue;
	}

	public void setAttendanceValue(AttendanceValue attendanceValue) {
		this.attendanceValue = attendanceValue;
	}

	public long getRemoteSessionId() {
		return remoteSessionId;
	}

	public void setRemoteSessionId(long remoteSessionId) {
		this.remoteSessionId = remoteSessionId;
	}

	public long getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(long remoteId) {
		this.remoteId = remoteId;
	}

	public Athlete getAthlete() {
		return athlete;
	}

	public void setAthlete(Athlete athlete) {
		this.athlete = athlete;
	}

	private String getAthleteFullName() {
		return athleteFullName;
	}

	public void setAthleteFullName(String athleteFullName) {
		this.athleteFullName = athleteFullName;
	}

	public String getAttendedAthleteFullName() {
		if (getAthlete() != null && getAthlete().getFullName() != null && !getAthlete().getFullName().isEmpty()) {
			return getAthlete().getFullName();
		} else {
			return getAthleteFullName();
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public long getSessionId() {
		return sessionId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

}
