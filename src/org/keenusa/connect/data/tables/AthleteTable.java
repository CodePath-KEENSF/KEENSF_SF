package org.keenusa.connect.data.tables;

public class AthleteTable {

	public static final String TABLE_NAME = "athletes";
	public static final String ID_COL_NAME = "_id";
	public static final String REMOTE_ID_COL_NAME = "remote_athlete_id";
	public static final String REMOTE_CREATED_COL_NAME = "remote_athlete_created";
	public static final String REMOTE_UPDATED_COL_NAME = "remote_athlete_updated";
	public static final String FIRST_NAME_COL_NAME = "athlete_first_name";
	public static final String MIDDLE_NAME_COL_NAME = "athlete_middle_name";
	public static final String LAST_NAME_COL_NAME = "athlete_last_name";
	public static final String EMAIL_COL_NAME = "athlete_email";
	public static final String PHONE_COL_NAME = "athlete_phone";
	public static final String GENDER_COL_NAME = "athlete_gender";
	public static final String NICKNAME_COL_NAME = "athlete_nickname";
	public static final String DOB_COL_NAME = "athlete_dob";
	public static final String PRIMLANGUAGE_COL_NAME = "athlete_language";
	public static final String STATUS_COL_NAME = "athlete_status";
	public static final String PARENT_MOBILE_COL_NAME = "parent_mobile";
	public static final String PARENT_EMAIL_COL_NAME = "parent_email";
	public static final String PARENT_PHONE_COL_NAME = "parent_phone";
	public static final String PARENT_RELATIONSHIP_COL_NAME = "parent_relationship";
	public static final String PARENT_FIRST_NAME_COL_NAME = "parent_first_name";
	public static final String PARENT_LAST_NAME_COL_NAME = "parent_last_name";
	public static final String CITY_COL_NAME = "athlete_city";
	public static final String STATE_COL_NAME = "athlete_state";
	public static final String ZIPCODE_COL_NAME = "athlete_zipcode";

	public static String getCreateSQL() {
		return "CREATE TABLE " + TABLE_NAME + " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + REMOTE_ID_COL_NAME + " NUMBER, "
				+ REMOTE_CREATED_COL_NAME + " NUMBER, " + REMOTE_UPDATED_COL_NAME + " NUMBER, " + FIRST_NAME_COL_NAME + " TEXT, "
				+ MIDDLE_NAME_COL_NAME + " TEXT, " + LAST_NAME_COL_NAME + " TEXT, " + EMAIL_COL_NAME + " TEXT, " + PHONE_COL_NAME + " TEXT, "
				+ GENDER_COL_NAME + " TEXT, " + NICKNAME_COL_NAME + " TEXT, " + DOB_COL_NAME + " NUMBER, " + PRIMLANGUAGE_COL_NAME + " TEXT, "
				+ STATUS_COL_NAME + " NUMBER, " + CITY_COL_NAME + " TEXT, " + STATE_COL_NAME + " TEXT, " + ZIPCODE_COL_NAME + " TEXT, "
				+ PARENT_MOBILE_COL_NAME + " TEXT, " + PARENT_EMAIL_COL_NAME + " TEXT, " + PARENT_PHONE_COL_NAME + " TEXT, "
				+ PARENT_RELATIONSHIP_COL_NAME + " TEXT, " + PARENT_FIRST_NAME_COL_NAME + " TEXT, " + PARENT_LAST_NAME_COL_NAME + " TEXT);";
	}

	public static String getDropTableSQL() {
		return "DROP TABLE IF EXIST " + TABLE_NAME + ";";
	}

}
