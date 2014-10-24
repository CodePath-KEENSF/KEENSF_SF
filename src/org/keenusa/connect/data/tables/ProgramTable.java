package org.keenusa.connect.data.tables;

public class ProgramTable {

	public static final String TABLE_NAME = "programs";
	public static final String ID_COL_NAME = "_id";
	public static final String REMOTE_ID_COL_NAME = "remote_program_id";
	public static final String REMOTE_CREATED_COL_NAME = "remote_program_created";
	public static final String REMOTE_UPDATED_COL_NAME = "remote_program_updated";
	public static final String START_DATE_COL_NAME = "program_start";
	public static final String END_DATE_COL_NAME = "program_end";
	public static final String NAME_COL_NAME = "program_name";
	public static final String TYPE_COL_NAME = "program_type";
	public static final String TIMES_COL_NAME = "program_times";
	public static final String REGISTRATION_EMAL_TEXT_COL_NAME = "registration_email_text";
	public static final String ADDRESS_ONE_COL_NAME = "address1";
	public static final String ADDRESS_TWO_COL_NAME = "address2";
	public static final String CITY_COL_NAME = "city";
	public static final String STATE_COL_NAME = "state";
	public static final String ZIP_CODE_COL_NAME = "zipCode";

	public static String getCreateSQL() {
		return "CREATE TABLE " + TABLE_NAME + " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + REMOTE_ID_COL_NAME + " NUMBER, "
				+ REMOTE_CREATED_COL_NAME + " NUMBER, " + REMOTE_UPDATED_COL_NAME + " NUMBER, " + START_DATE_COL_NAME + " NUMBER, "
				+ END_DATE_COL_NAME + " NUMBER, " + NAME_COL_NAME + " TEXT, " + TYPE_COL_NAME + " TEXT, " + TIMES_COL_NAME + " TEXT, "
				+ REGISTRATION_EMAL_TEXT_COL_NAME + " TEXT, " + ADDRESS_ONE_COL_NAME + " TEXT, " + ADDRESS_TWO_COL_NAME + " TEXT, " + CITY_COL_NAME
				+ " TEXT, " + STATE_COL_NAME + " TEXT, " + ZIP_CODE_COL_NAME + " TEXT);";
	}

	public static String getDropTableSQL() {
		return "DROP TABLE IF EXIST " + TABLE_NAME + ";";
	}

}
