package org.keenusa.connect.data.tables;

public class AffiliateTable {

	public static final String TABLE_NAME = "affiliates";
	public static final String ID_COL_NAME = "_id";
	public static final String REMOTE_ID_COL_NAME = "remote_affiliate_id";
	public static final String REMOTE_CREATED_COL_NAME = "remote_affiliate_created";
	public static final String REMOTE_UPDATED_COL_NAME = "remote_affiliate_updated";
	public static final String NAME_COL_NAME = "affiliate_name";
	public static final String CONTACT_NAME_COL_NAME = "affiliate_contact_name";
	public static final String EMAIL_COL_NAME = "email";
	public static final String WEBSITE_COL_NAME = "website";

	public static String getCreateSQL() {
		return "CREATE TABLE " + TABLE_NAME + " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + REMOTE_ID_COL_NAME + " NUMBER, "
				+ REMOTE_CREATED_COL_NAME + " NUMBER, " + REMOTE_UPDATED_COL_NAME + " NUMBER, " + NAME_COL_NAME + " TEXT, " + CONTACT_NAME_COL_NAME
				+ " TEXT, " + EMAIL_COL_NAME + " TEXT, " + WEBSITE_COL_NAME + " TEXT);";
	}

	public static String getDropTableSQL() {
		return "DROP TABLE IF EXIST " + TABLE_NAME + ";";
	}

}
