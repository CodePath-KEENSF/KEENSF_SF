package org.keenusa.connect.data.tables;

public class LocationTable {

	public static final String TABLE_NAME = "locations";
	public static final String ID_COL_NAME = "_id";
	public static final String ADDRESS_ONE_COL_NAME = "address1";
	public static final String ADDRESS_TWO_COL_NAME = "address2";
	public static final String CITY_COL_NAME = "city";
	public static final String STATE_COL_NAME = "state";
	public static final String ZIP_CODE_COL_NAME = "zipCode";

	public static String getCreateSQL() {
		return "CREATE TABLE " + TABLE_NAME + " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + ADDRESS_ONE_COL_NAME + " TEXT, "
				+ ADDRESS_TWO_COL_NAME + " TEXT, " + CITY_COL_NAME + " TEXT, " + STATE_COL_NAME + " TEXT, " + ZIP_CODE_COL_NAME + " TEXT);";
	}

	public static String getDropTableSQL() {
		return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
	}

}
