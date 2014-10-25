package org.keenusa.connect.data;

import org.keenusa.connect.data.tables.AffiliateTable;
import org.keenusa.connect.data.tables.AthleteAttendanceTable;
import org.keenusa.connect.data.tables.AthleteTable;
import org.keenusa.connect.data.tables.CoachAttendanceTable;
import org.keenusa.connect.data.tables.CoachTable;
import org.keenusa.connect.data.tables.ProgramEnrollmentTable;
import org.keenusa.connect.data.tables.ProgramTable;
import org.keenusa.connect.data.tables.SessionTable;
import org.keenusa.connect.models.AthleteAttendance;
import org.keenusa.connect.models.CoachAttendance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
		createTriggers(db);
	}

	private void createTriggers(SQLiteDatabase db) {
		// increment number of athletes enrolled when new program enrollment record is inserted
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TRIGGER update_reg_ath_count AFTER INSERT ON ").append(ProgramEnrollmentTable.TABLE_NAME);
		sb.append(" BEGIN ");
		sb.append("UPDATE ").append(SessionTable.TABLE_NAME).append(" SET ").append(SessionTable.NUMBER_OF_ATHLETES_REGISTERED_COL_NAME).append("=")
				.append(SessionTable.NUMBER_OF_ATHLETES_REGISTERED_COL_NAME).append(" + 1");
		sb.append(" WHERE ").append(SessionTable.PROGRAM_ID_COL_NAME).append("=").append("new.").append(ProgramEnrollmentTable.PROGRAM_ID_COL_NAME);
		sb.append("; END;");
		Log.e("TRIGGER", sb.toString());
		db.execSQL(sb.toString());

		// increment number of athletes checked in when new not registered athlete attendance record is inserted
		sb = new StringBuilder();
		sb.append("CREATE TRIGGER update_checked_ath_count AFTER INSERT ON ").append(AthleteAttendanceTable.TABLE_NAME);
		sb.append(" WHEN ").append("new.").append(AthleteAttendanceTable.ATTENDANCE_VALUE_COL_NAME).append("<>'")
				.append(AthleteAttendance.AttendanceValue.REGISTERED.toString()).append("'");
		sb.append(" BEGIN ");
		sb.append("UPDATE ").append(SessionTable.TABLE_NAME).append(" SET ").append(SessionTable.NUMBER_OF_ATHLETES_CHECKED_IN_COL_NAME).append("=")
				.append(SessionTable.NUMBER_OF_ATHLETES_CHECKED_IN_COL_NAME).append(" + 1");
		sb.append(" WHERE ").append(SessionTable.ID_COL_NAME).append("=").append("new.").append(AthleteAttendanceTable.SESSION_ID_COL_NAME);
		sb.append("; END;");
		Log.e("TRIGGER", sb.toString());
		db.execSQL(sb.toString());

		// increment number of coaches registered when new coach attendance record is inserted
		sb = new StringBuilder();
		sb.append("CREATE TRIGGER update_registered_coach_count AFTER INSERT ON ").append(CoachAttendanceTable.TABLE_NAME);
		sb.append(" BEGIN ");
		sb.append("UPDATE ").append(SessionTable.TABLE_NAME).append(" SET ").append(SessionTable.NUMBER_OF_COACHES_REGISTERED_COL_NAME).append("=")
				.append(SessionTable.NUMBER_OF_COACHES_REGISTERED_COL_NAME).append(" + 1");
		sb.append(" WHERE ").append(SessionTable.ID_COL_NAME).append("=").append("new.").append(CoachAttendanceTable.SESSION_ID_COL_NAME);
		sb.append("; END;");
		Log.e("TRIGGER", sb.toString());
		db.execSQL(sb.toString());

		// increment number of coaches checked in when new not registered coach attendance record is inserted
		sb = new StringBuilder();
		sb.append("CREATE TRIGGER update_checked_coach_count AFTER INSERT ON ").append(CoachAttendanceTable.TABLE_NAME);
		sb.append(" WHEN ").append("new.").append(CoachAttendanceTable.ATTENDANCE_VALUE_COL_NAME).append("<>'")
				.append(CoachAttendance.AttendanceValue.REGISTERED.toString()).append("'");
		sb.append(" BEGIN ");
		sb.append("UPDATE ").append(SessionTable.TABLE_NAME).append(" SET ").append(SessionTable.NUMBER_OF_COACHES_CHECKED_IN_COL_NAME).append("=")
				.append(SessionTable.NUMBER_OF_COACHES_CHECKED_IN_COL_NAME).append(" + 1");
		sb.append(" WHERE ").append(SessionTable.ID_COL_NAME).append("=").append("new.").append(CoachAttendanceTable.SESSION_ID_COL_NAME);
		sb.append("; END;");
		Log.e("TRIGGER", sb.toString());
		db.execSQL(sb.toString());

		// TODO decrement number of athletes checked in when attendance is updated to registered on athlete attendance record update
		//		sb = new StringBuilder();
		//		sb.append("CREATE TRIGGER update_checked_coach_count AFTER UPDATE ON ").append(CoachAttendanceTable.TABLE_NAME);
		//		sb.append(" WHEN ").append("new.").append(CoachAttendanceTable.ATTENDANCE_VALUE_COL_NAME).append("<>'")
		//				.append(CoachAttendance.AttendanceValue.REGISTERED.toString()).append("'");
		//		sb.append(" AND ").append("new.").append(CoachAttendanceTable.ATTENDANCE_VALUE_COL_NAME).append("<>'")
		//		.append("old.").append(CoachAttendanceTable.ATTENDANCE_VALUE_COL_NAME);
		//		sb.append(" BEGIN ");
		//		sb.append("UPDATE ").append(SessionTable.TABLE_NAME).append(" SET ").append(SessionTable.NUMBER_OF_COACHES_CHECKED_IN_COL_NAME).append("=")
		//				.append(SessionTable.NUMBER_OF_COACHES_CHECKED_IN_COL_NAME).append(" + 1");
		//		sb.append(" WHERE ").append(SessionTable.ID_COL_NAME).append("=").append("new.").append(CoachAttendanceTable.SESSION_ID_COL_NAME);
		//		sb.append("; END;");
		//		Log.e("TRIGGER", sb.toString());
		//		db.execSQL(sb.toString());

		// TODO increment number of athletes checked in when attendance is updated to other than registered on athlete attendance record update

		// TODO decrement number of coaches checked in when attendance is updated to registered on coach attendance record update

		// TODO increment number of coaches checked in when attendance is updated to other than registered on coach attendance record update

	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTables(db);
		createTables(db);
		createTriggers(db);
	}

	private void createTables(SQLiteDatabase db) {
		db.execSQL(AffiliateTable.getCreateSQL());
		db.execSQL(AthleteTable.getCreateSQL());
		db.execSQL(CoachTable.getCreateSQL());
		db.execSQL(ProgramTable.getCreateSQL());
		db.execSQL(SessionTable.getCreateSQL());
		db.execSQL(ProgramEnrollmentTable.getCreateSQL());
		db.execSQL(AthleteAttendanceTable.getCreateSQL());
		db.execSQL(CoachAttendanceTable.getCreateSQL());
	}

	private void dropTables(SQLiteDatabase db) {
		db.execSQL(AffiliateTable.getDropTableSQL());
		db.execSQL(AthleteTable.getDropTableSQL());
		db.execSQL(CoachTable.getDropTableSQL());
		db.execSQL(ProgramTable.getDropTableSQL());
		db.execSQL(SessionTable.getDropTableSQL());
		db.execSQL(ProgramEnrollmentTable.getDropTableSQL());
		db.execSQL(AthleteAttendanceTable.getDropTableSQL());
		db.execSQL(CoachAttendanceTable.getDropTableSQL());

	}

	// TODO proper sync should be implemented
	public void cleanDB() {
		SQLiteDatabase db = getWritableDatabase();
		dropTables(db);
		createTables(db);
		createTriggers(db);
	}

}
