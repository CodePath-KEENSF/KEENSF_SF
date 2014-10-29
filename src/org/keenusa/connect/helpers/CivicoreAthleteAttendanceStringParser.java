package org.keenusa.connect.helpers;

import org.keenusa.connect.models.AthleteAttendance;

public class CivicoreAthleteAttendanceStringParser {

	public static AthleteAttendance.AttendanceValue parseAthleteAttendanceString(String athleteAttendance) {
		AthleteAttendance.AttendanceValue aa = AthleteAttendance.AttendanceValue.REGISTERED;
		if (athleteAttendance != null) {
			if (athleteAttendance.equalsIgnoreCase("attended")) {
				aa = AthleteAttendance.AttendanceValue.ATTENDED;
			}
			if (athleteAttendance.equalsIgnoreCase("called in absence")) {
				aa = AthleteAttendance.AttendanceValue.CALLED_IN_ABSENCE;
			}
			if (athleteAttendance.equalsIgnoreCase("no call - no show")) {
				aa = AthleteAttendance.AttendanceValue.NO_CALL_NO_SHOW;
			}
		}
		return aa;
	}

}
