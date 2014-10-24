package org.keenusa.connect.data.tables;

public class ProgramEnrollmentTable {

	public static final String TABLE_NAME = "program_enrolments";
	public static final String ID_COL_NAME = "_id";
	public static final String REMOTE_ID_COL_NAME = "remote_enrolment_id";
	public static final String REMOTE_CREATED_COL_NAME = "remote_enrolment_created";
	public static final String REMOTE_UPDATED_COL_NAME = "remote_enrolment_updated";
	public static final String PROGRAM_ID_COL_NAME = "program_id";
	public static final String ATHLETE_ID_COL_NAME = "athlete_id";
	public static final String WAITLIST_COL_NAME = "waitlisted";

	public static String getCreateSQL() {
		return "CREATE TABLE " + TABLE_NAME + " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + REMOTE_ID_COL_NAME + " NUMBER, "
				+ REMOTE_CREATED_COL_NAME + " NUMBER, " + REMOTE_UPDATED_COL_NAME + " NUMBER, " + PROGRAM_ID_COL_NAME + " NUMBER, "
				+ ATHLETE_ID_COL_NAME + " NUMBER, " + WAITLIST_COL_NAME + " NUMBER);";
	}

	public static String getDropTableSQL() {
		return "DROP TABLE IF EXIST " + TABLE_NAME + ";";
	}

}
