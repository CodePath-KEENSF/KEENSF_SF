package org.keenusa.connect.models;

import java.io.Serializable;

public class ParticipantAttendance  implements Serializable {

	private static final long serialVersionUID = 6935509003996703494L;
	
	private AttendanceValue attendanceValue;
	private Participant participant;

	// used locally
	private String participantFullName;

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

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	public AttendanceValue getAttendanceValue() {
		return attendanceValue;
	}

	public void setAttendanceValue(AttendanceValue attendanceValue) {
		this.attendanceValue = attendanceValue;
	}


}
