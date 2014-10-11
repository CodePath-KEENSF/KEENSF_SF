package org.keenusa.connect.models;

public class CoachAttendance {

	private long remoteId;
	private long remoteSessionId;
	private Coach coach;
	// mostly empty
	private String comments;
	private AttendanceValue attendanceValue;

	// attendance in remote source (lookup values) in the source 106 for contacts
	public enum AttendanceValue {
		REGISTERED, ATTENDED, CALLED_IN_ABSENCE, CANCELLED, NO_CALL_NO_SHOW
	}

	public CoachAttendance() {
	}

	public CoachAttendance(long remoteId, long remoteSessionId, Coach coach, String comments, AttendanceValue attendanceValue) {
		super();
		this.remoteId = remoteId;
		this.remoteSessionId = remoteSessionId;
		this.coach = coach;
		this.comments = comments;
		this.attendanceValue = attendanceValue;
	}

	public AttendanceValue getAttendanceValue() {
		return attendanceValue;
	}

	public void setAttendanceValue(AttendanceValue attendanceValue) {
		this.attendanceValue = attendanceValue;
	}

	public long getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(long remoteId) {
		this.remoteId = remoteId;
	}

	public Coach getCoachId() {
		return coach;
	}

	public void setCoachId(Coach coach) {
		this.coach = coach;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public long getRemoteSessionId() {
		return remoteSessionId;
	}

	public void setRemoteSessionId(long remoteSessionId) {
		this.remoteSessionId = remoteSessionId;
	}

}
