package org.keenusa.connect.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.helpers.CivicoreAthleteAttendanceStringParser;
import org.keenusa.connect.models.remote.RemoteAthleteAttendance;

public class AthleteAttendance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5486907615451742803L;
	private long remoteId;
	private long remoteSessionId;
	private Athlete athlete;
	//no comments field in the remote table youth_days_attendance
	private AttendanceValue attendanceValue;
	// used locally
	private String athleteFullName;

	//attendance in remote source (lookup values) in the source id58 for youth
	public enum AttendanceValue {
		ATTENDED, CALLED_IN_ABSENCE, NO_CALL_NO_SHOW
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

	private void setAthleteFullName(String athleteFullName) {
		this.athleteFullName = athleteFullName;
	}

	public String getAttendedAthleteFullName() {
		if (getAthlete() != null && getAthlete().getFullName() != null && !getAthlete().getFullName().isEmpty()) {
			return getAthlete().getFullName();
		} else {
			return getAthleteFullName();
		}
	}

}
