package org.keenusa.connect.data.tables;

public class CoachTable {

	public static final String TABLE_NAME = "coaches";
	public static final String ID_COL_NAME = "_id";
	public static final String REMOTE_ID_COL_NAME = "remote_coach_id";
	public static final String REMOTE_CREATED_COL_NAME = "remote_coach_created";
	public static final String REMOTE_UPDATED_COL_NAME = "remote_coach_updated";
	public static final String FIRST_NAME_COL_NAME = "coach_first_name";
	public static final String MIDDLE_NAME_COL_NAME = "coach_middle_name";
	public static final String LAST_NAME_COL_NAME = "coach_last_name";
	public static final String EMAIL_COL_NAME = "coach_email";
	public static final String PHONE_COL_NAME = "coach_phone";
	public static final String MOBILE_COL_NAME = "coach_mobile";
	public static final String GENDER_COL_NAME = "coach_gender";
	public static final String DOB_COL_NAME = "coach_dob";
	public static final String LANGUAGES_COL_NAME = "coach_languages";
	public static final String SKILLS_COL_NAME = "coach_skills";
	public static final String ACTIVE_COL_NAME = "coach_active";
	public static final String CITY_COL_NAME = "coach_city";
	public static final String STATE_COL_NAME = "coach_state";
	public static final String ZIPCODE_COL_NAME = "coach_zipcode";

	public static String getCreateSQL() {
		return "CREATE TABLE " + TABLE_NAME + " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + REMOTE_ID_COL_NAME + " NUMBER, "
				+ REMOTE_CREATED_COL_NAME + " NUMBER, " + REMOTE_UPDATED_COL_NAME + " NUMBER, " + FIRST_NAME_COL_NAME + " TEXT, "
				+ MIDDLE_NAME_COL_NAME + " TEXT, " + LAST_NAME_COL_NAME + " TEXT, " + EMAIL_COL_NAME + " TEXT, " + PHONE_COL_NAME + " TEXT, "
				+ MOBILE_COL_NAME + " TEXT, " + GENDER_COL_NAME + " TEXT, " + DOB_COL_NAME + " NUMBER, " + LANGUAGES_COL_NAME + " TEXT, "
				+ ACTIVE_COL_NAME + " NUMBER, " + CITY_COL_NAME + " TEXT, " + STATE_COL_NAME + " TEXT, " + ZIPCODE_COL_NAME + " TEXT, "
				+ SKILLS_COL_NAME + " TEXT);";
	}

	public static String getDropTableSQL() {
		return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
	}

}
