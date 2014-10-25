package org.keenusa.connect.data;

import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.data.tables.CoachAttendanceTable;
import org.keenusa.connect.data.tables.ProgramTable;
import org.keenusa.connect.models.CoachAttendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoachAttendanceDAO {

	private KeenConnectDB localDB;
	private SessionDAO sessionDAO;
	private CoachDAO coachDAO;

	String[] columnNames = { CoachAttendanceTable.ID_COL_NAME, CoachAttendanceTable.REMOTE_ID_COL_NAME, CoachAttendanceTable.REMOTE_CREATED_COL_NAME,
			CoachAttendanceTable.REMOTE_UPDATED_COL_NAME, CoachAttendanceTable.SESSION_ID_COL_NAME, CoachAttendanceTable.COACH_ID_COL_NAME,
			CoachAttendanceTable.ATTENDANCE_VALUE_COL_NAME };

	public CoachAttendanceDAO(Context context) {
		localDB = KeenConnectDB.getKeenConnectDB(context);
		sessionDAO = new SessionDAO(context);
		coachDAO = new CoachDAO(context);
	}

	public List<CoachAttendance> getCoachAttendancesBySessionId(long sessionId) {
		List<CoachAttendance> attendances = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor attendancesCursor = db.query(CoachAttendanceTable.TABLE_NAME, columnNames, CoachAttendanceTable.SESSION_ID_COL_NAME + "=" + sessionId,
				null, null, null, null);

		if (attendancesCursor.getCount() > 0) {
			attendancesCursor.moveToFirst();
			attendances = createCoachAttendanceListFromCursor(attendancesCursor);
		} else {
			attendances = new ArrayList<CoachAttendance>();
		}
		attendancesCursor.close();
		return attendances;
	}

	public CoachAttendance getCoachAttendanceByRemoteId(long id) {
		CoachAttendance attendance = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor attendanceCursor = db.query(CoachAttendanceTable.TABLE_NAME, columnNames, CoachAttendanceTable.REMOTE_ID_COL_NAME + "=" + id, null,
				null, null, null);
		if (attendanceCursor.getCount() > 0) {
			attendanceCursor.moveToFirst();
			attendance = createCoachAttendanceFromCursor(attendanceCursor);
		}
		attendanceCursor.close();
		return attendance;
	}

	public CoachAttendance getCoachAttendanceById(long id) {
		CoachAttendance attendance = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor attendanceCursor = db.query(CoachAttendanceTable.TABLE_NAME, columnNames, CoachAttendanceTable.ID_COL_NAME + "=" + id, null, null,
				null, null);
		if (attendanceCursor.getCount() > 0) {
			attendanceCursor.moveToFirst();
			attendance = createCoachAttendanceFromCursor(attendanceCursor);
		}
		attendanceCursor.close();
		return attendance;
	}

	public long saveNewCoachAttendance(CoachAttendance attendance) {

		long attendanceId = 0;
		SQLiteDatabase db = localDB.getWritableDatabase();
		CoachAttendance dbCoachAttendance = getCoachAttendanceByRemoteId(attendance.getRemoteId());
		if (dbCoachAttendance == null) {
			db.beginTransaction();
			ContentValues values = new ContentValues();

			values.put(CoachAttendanceTable.REMOTE_ID_COL_NAME, attendance.getRemoteId());
			if (attendance.getRemoteCreateTimestamp() != 0) {
				values.put(CoachAttendanceTable.REMOTE_CREATED_COL_NAME, attendance.getRemoteCreateTimestamp());
			}
			if (attendance.getRemoteUpdatedTimestamp() != 0) {
				values.put(CoachAttendanceTable.REMOTE_UPDATED_COL_NAME, attendance.getRemoteUpdatedTimestamp());
			}

			long sessionId = sessionDAO.getSessionByRemoteId(attendance.getRemoteSessionId()).getId();
			values.put(CoachAttendanceTable.SESSION_ID_COL_NAME, sessionId);

			if (attendance.getCoach() != null) {
				long coachId = coachDAO.geCoachByRemoteId(attendance.getCoach().getRemoteId()).getId();
				values.put(CoachAttendanceTable.COACH_ID_COL_NAME, coachId);
			}

			values.put(CoachAttendanceTable.ATTENDANCE_VALUE_COL_NAME, attendance.getAttendanceValue().toString());

			attendanceId = db.insert(CoachAttendanceTable.TABLE_NAME, null, values);
			db.setTransactionSuccessful();
			db.endTransaction();
		} else if (dbCoachAttendance.getRemoteUpdatedTimestamp() < dbCoachAttendance.getRemoteUpdatedTimestamp()) {
			// more recent version of the coach atendance
			attendance.setId(dbCoachAttendance.getId());
			updateCoachAttendance(attendance);
		}
		return attendanceId;
	}

	private boolean updateCoachAttendance(CoachAttendance attendance) {
		boolean transactionStatus = false;
		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		ContentValues values = new ContentValues();
		if (attendance.getRemoteCreateTimestamp() != 0) {
			values.put(CoachAttendanceTable.REMOTE_CREATED_COL_NAME, attendance.getRemoteCreateTimestamp());
		}
		if (attendance.getRemoteUpdatedTimestamp() != 0) {
			values.put(CoachAttendanceTable.REMOTE_UPDATED_COL_NAME, attendance.getRemoteUpdatedTimestamp());
		}

		long sessionId = sessionDAO.getSessionByRemoteId(attendance.getRemoteSessionId()).getId();
		values.put(CoachAttendanceTable.SESSION_ID_COL_NAME, sessionId);

		if (attendance.getCoach() != null) {
			long coachId = coachDAO.geCoachByRemoteId(attendance.getCoach().getRemoteId()).getId();
			values.put(CoachAttendanceTable.COACH_ID_COL_NAME, coachId);
		}

		values.put(CoachAttendanceTable.ATTENDANCE_VALUE_COL_NAME, attendance.getAttendanceValue().toString());
		db.update(ProgramTable.TABLE_NAME, values, ProgramTable.ID_COL_NAME + "=" + attendance.getId(), null);
		db.setTransactionSuccessful();
		transactionStatus = true;
		db.endTransaction();
		return transactionStatus;

	}

	private List<CoachAttendance> createCoachAttendanceListFromCursor(Cursor c) {
		ArrayList<CoachAttendance> attendances = new ArrayList<CoachAttendance>(c.getCount());
		if (c.getCount() > 0) {
			c.moveToFirst();
			while (!c.isAfterLast()) {
				CoachAttendance attendance = createCoachAttendanceFromCursor(c);
				if (attendance != null) {
					attendances.add(attendance);
				}
				c.moveToNext();
			}

		}
		return attendances;
	}

	private CoachAttendance createCoachAttendanceFromCursor(Cursor c) {

		CoachAttendance attendance = null;
		if (c.getPosition() >= 0) {
			attendance = new CoachAttendance();
			try {
				attendance.setId(c.getLong(c.getColumnIndexOrThrow(CoachAttendanceTable.ID_COL_NAME)));
				attendance.setRemoteId(c.getLong(c.getColumnIndexOrThrow(CoachAttendanceTable.REMOTE_ID_COL_NAME)));
				attendance.setRemoteCreateTimestamp(c.getLong(c.getColumnIndexOrThrow(CoachAttendanceTable.REMOTE_CREATED_COL_NAME)));
				attendance.setRemoteUpdatedTimestamp(c.getLong(c.getColumnIndexOrThrow(CoachAttendanceTable.REMOTE_UPDATED_COL_NAME)));
				attendance.setRemoteSessionId(sessionDAO.getSessionByRemoteId(
						c.getLong(c.getColumnIndexOrThrow(CoachAttendanceTable.SESSION_ID_COL_NAME))).getRemoteId());
				attendance.setAttendanceValue(CoachAttendance.AttendanceValue.valueOf(c.getString(c
						.getColumnIndexOrThrow(CoachAttendanceTable.ATTENDANCE_VALUE_COL_NAME))));

			} catch (IllegalArgumentException iax) {
				attendance = null;
			}
		}
		return attendance;
	}
}
