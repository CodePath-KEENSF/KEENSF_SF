package org.keenusa.connect.data.tables;

public class CoachAttendanceTable {

	public static final String TABLE_NAME = "coach_attendances";
	public static final String ID_COL_NAME = "_id";
	public static final String REMOTE_ID_COL_NAME = "remote_coach_attend_id";
	public static final String REMOTE_CREATED_COL_NAME = "remote_coach_attend_created";
	public static final String REMOTE_UPDATED_COL_NAME = "remote_coach_attend_updated";
	public static final String SESSION_ID_COL_NAME = "session_id";
	public static final String COACH_ID_COL_NAME = "coach_id";
	public static final String ATTENDANCE_VALUE_COL_NAME = "attendance";

	public static String getCreateSQL() {
		return "CREATE TABLE " + TABLE_NAME + " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + REMOTE_ID_COL_NAME + " NUMBER, "
				+ REMOTE_CREATED_COL_NAME + " NUMBER, " + REMOTE_UPDATED_COL_NAME + " NUMBER, " + SESSION_ID_COL_NAME + " NUMBER, "
				+ COACH_ID_COL_NAME + " NUMBER, " + ATTENDANCE_VALUE_COL_NAME + " TEXT);";
	}

	public static String getDropTableSQL() {
		return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
	}

}
