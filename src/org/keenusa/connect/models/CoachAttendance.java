package org.keenusa.connect.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.helpers.CivicoreCoachAttendanceStringParser;
import org.keenusa.connect.models.remote.RemoteCoachAttendance;

public class CoachAttendance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9026136333954262937L;
	private long id;
	private long remoteId;
	private long remoteSessionId;
	private Coach coach;
	// mostly empty
	private String comments;
	private AttendanceValue attendanceValue;

	// used locally
	private String coachFullName;

	// attendance in remote source (lookup values) in the source 106 for contacts
	public enum AttendanceValue {
		REGISTERED("Registered", 637), ATTENDED("Attended", 638), CALLED_IN_ABSENCE("Called in absence", 639), CANCELLED("Cancelled", 647), NO_CALL_NO_SHOW(
				"No Call - No Show", 640);

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

	public static CoachAttendance fromRemoteCoachAttendance(RemoteCoachAttendance remoteCoachAttendance) {
		CoachAttendance coachAttendance = null;
		if (remoteCoachAttendance != null) {
			coachAttendance = new CoachAttendance();
			coachAttendance.setRemoteId(Long.valueOf(remoteCoachAttendance.getRemoteId()));
			coachAttendance.setCoachFullName(remoteCoachAttendance.getContactName());

			Coach coach = new Coach();
			coach.setRemoteId(Long.valueOf(remoteCoachAttendance.getContactId()));
			coachAttendance.setCoach(coach);

			coachAttendance.setRemoteSessionId(Long.valueOf(remoteCoachAttendance.getClassesDaysId()));
			coachAttendance.setAttendanceValue(CivicoreCoachAttendanceStringParser.parseCoachAttendanceString(remoteCoachAttendance.getAttendance()));
			coachAttendance.setComments(remoteCoachAttendance.getComments());
		}
		return coachAttendance;
	}

	public static List<CoachAttendance> fromRemoteCoachAttendanceList(List<RemoteCoachAttendance> remoteCoachAttendanceList) {
		List<CoachAttendance> coachAttendances = null;
		if (remoteCoachAttendanceList != null) {
			coachAttendances = new ArrayList<CoachAttendance>(remoteCoachAttendanceList.size());
			for (RemoteCoachAttendance remoteCoachAttendance : remoteCoachAttendanceList) {
				CoachAttendance coachAttendance = fromRemoteCoachAttendance(remoteCoachAttendance);
				coachAttendances.add(coachAttendance);
			}

		} else {
			coachAttendances = new ArrayList<CoachAttendance>();
		}
		return coachAttendances;
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

	public Coach getCoach() {
		return coach;
	}

	public void setCoach(Coach coach) {
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

	private String getCoachFullName() {
		return coachFullName;
	}

	private void setCoachFullName(String coachFullName) {
		this.coachFullName = coachFullName;
	}

	public String getAttendedCoachFullName() {
		if (getCoach() != null && getCoach().getFullName() != null && !getCoach().getFullName().isEmpty()) {
			return getCoach().getFullName();
		} else {
			return getCoachFullName();
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
