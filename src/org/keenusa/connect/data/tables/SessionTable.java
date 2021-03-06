package org.keenusa.connect.data.tables;

public class SessionTable {

	public static final String TABLE_NAME = "sessions";
	public static final String ID_COL_NAME = "_id";
	public static final String REMOTE_ID_COL_NAME = "remote_session_id";
	public static final String REMOTE_CREATED_COL_NAME = "remote_session_created";
	public static final String REMOTE_UPDATED_COL_NAME = "remote_session_updated";
	public static final String SESSION_DATE_COL_NAME = "session_date";
	public static final String PROGRAM_ID_COL_NAME = "program_id";
	public static final String NUMBER_OF_NEW_COACHES_NEEDED_COL_NAME = "new_coaches_needed";
	public static final String NUMBER_OF_RETURNING_COACHES_NEEDED_COL_NAME = "returning_coaches_needed";
	public static final String OPEN_FOR_REGISTRATION_FLAG_COL_NAME = "open_for_registration";

	public static final String NUMBER_OF_ATHLETES_REGISTERED_COL_NAME = "num_athletes_reg";
	public static final String NUMBER_OF_COACHES_REGISTERED_COL_NAME = "num_coaches_reg";
	public static final String NUMBER_OF_ATHLETES_CHECKED_IN_COL_NAME = "num_athletes_checked";
	public static final String NUMBER_OF_COACHES_CHECKED_IN_COL_NAME = "num_coaches_checked";

	public static String getCreateSQL() {
		return "CREATE TABLE " + TABLE_NAME + " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + REMOTE_ID_COL_NAME + " NUMBER, "
				+ REMOTE_CREATED_COL_NAME + " NUMBER, " + REMOTE_UPDATED_COL_NAME + " NUMBER, " + SESSION_DATE_COL_NAME + " NUMBER, "
				+ PROGRAM_ID_COL_NAME + " NUMBER, " + NUMBER_OF_NEW_COACHES_NEEDED_COL_NAME + " INTEGER NOT NULL DEFAULT 0, "
				+ NUMBER_OF_RETURNING_COACHES_NEEDED_COL_NAME + " INTEGER NOT NULL DEFAULT 0, " + NUMBER_OF_ATHLETES_REGISTERED_COL_NAME
				+ " INTEGER NOT NULL DEFAULT 0, " + NUMBER_OF_COACHES_REGISTERED_COL_NAME + " INTEGER NOT NULL DEFAULT 0, "
				+ NUMBER_OF_ATHLETES_CHECKED_IN_COL_NAME + " INTEGER NOT NULL DEFAULT 0, " + NUMBER_OF_COACHES_CHECKED_IN_COL_NAME
				+ " INTEGER NOT NULL DEFAULT 0, " + OPEN_FOR_REGISTRATION_FLAG_COL_NAME + " NUMBER);";
	}

	public static String getDropTableSQL() {
		return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
	}

}
