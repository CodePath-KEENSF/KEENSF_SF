package org.keenusa.connect.data;

import org.keenusa.connect.data.tables.AffiliateTable;
import org.keenusa.connect.data.tables.AthleteTable;
import org.keenusa.connect.data.tables.CoachTable;
import org.keenusa.connect.data.tables.LocationTable;
import org.keenusa.connect.data.tables.ProgramEnrollmentTable;
import org.keenusa.connect.data.tables.ProgramTable;
import org.keenusa.connect.data.tables.SessionTable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class KeenConnectDB extends SQLiteOpenHelper {

	private static final String DB_NAME = "keenConnectDB";
	private static final int DB_VERSION = 1;
	private static KeenConnectDB instance;

	public static KeenConnectDB getKeenConnectDB(Context context) {
		if (instance == null) {
			// Create the SQLiteOpenHelper if does not exist
			CursorFactory factory = null;
			instance = new KeenConnectDB(context, DB_NAME, factory);
		}
		return instance;
	}

	private KeenConnectDB(Context context, String name, SQLiteDatabase.CursorFactory factory) {
		super(context, name, factory, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTables(db);
		createTables(db);
	}

	private void createTables(SQLiteDatabase db) {
		db.execSQL(AffiliateTable.getCreateSQL());
		db.execSQL(AthleteTable.getCreateSQL());
		db.execSQL(CoachTable.getCreateSQL());
		db.execSQL(ProgramTable.getCreateSQL());
		db.execSQL(SessionTable.getCreateSQL());
		db.execSQL(ProgramEnrollmentTable.getCreateSQL());
		// TODO delete
		db.execSQL(LocationTable.getCreateSQL());
	}

	private void dropTables(SQLiteDatabase db) {
		db.execSQL(AffiliateTable.getDropTableSQL());
		db.execSQL(AthleteTable.getDropTableSQL());
		db.execSQL(CoachTable.getDropTableSQL());
		db.execSQL(ProgramTable.getDropTableSQL());
		db.execSQL(SessionTable.getDropTableSQL());
		db.execSQL(ProgramEnrollmentTable.getDropTableSQL());

		// TODO delete
		db.execSQL(LocationTable.getDropTableSQL());

	}

}
