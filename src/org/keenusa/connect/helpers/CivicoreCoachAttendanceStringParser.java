package org.keenusa.connect.helpers;

import org.keenusa.connect.models.CoachAttendance;

public class CivicoreCoachAttendanceStringParser {

	public static CoachAttendance.AttendanceValue parseCoachAttendanceString(String coachAttendance) {
		CoachAttendance.AttendanceValue ca = null;
		if (coachAttendance != null) {
			if (coachAttendance.equalsIgnoreCase("attended")) {
				ca = CoachAttendance.AttendanceValue.ATTENDED;
			}
			if (coachAttendance.equalsIgnoreCase("called in absence")) {
				ca = CoachAttendance.AttendanceValue.CALLED_IN_ABSENCE;
			}
			if (coachAttendance.equalsIgnoreCase("no call - no show")) {
				ca = CoachAttendance.AttendanceValue.NO_CALL_NO_SHOW;
			}
			if (coachAttendance.equalsIgnoreCase("registered")) {
				ca = CoachAttendance.AttendanceValue.REGISTERED;
			}
			if (coachAttendance.equalsIgnoreCase("cancelled")) {
				ca = CoachAttendance.AttendanceValue.CANCELLED;
			}
		}
		return ca;
	}

}
