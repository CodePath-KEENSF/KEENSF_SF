package org.keenusa.connect.models;

import java.io.Serializable;

public class AthleteAttendance implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5486907615451742803L;
	private long remoteId;
	private long remoteSessionId;
	private Athlete athlete;
	//no comments field in the remote table youth_days_attendance
	private AttendanceValue attendanceValue;

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

}
