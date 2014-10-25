package org.keenusa.connect.data;

import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.data.tables.AthleteAttendanceTable;
import org.keenusa.connect.data.tables.ProgramTable;
import org.keenusa.connect.models.AthleteAttendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AthleteAttendanceDAO {

	private KeenConnectDB localDB;
	private SessionDAO sessionDAO;
	private AthleteDAO athleteDAO;

	String[] columnNames = { AthleteAttendanceTable.ID_COL_NAME, AthleteAttendanceTable.REMOTE_ID_COL_NAME,
			AthleteAttendanceTable.REMOTE_CREATED_COL_NAME, AthleteAttendanceTable.REMOTE_UPDATED_COL_NAME,
			AthleteAttendanceTable.SESSION_ID_COL_NAME, AthleteAttendanceTable.ATHLETE_ID_COL_NAME, AthleteAttendanceTable.ATTENDANCE_VALUE_COL_NAME };

	public AthleteAttendanceDAO(Context context) {
		localDB = KeenConnectDB.getKeenConnectDB(context);
		sessionDAO = new SessionDAO(context);
		athleteDAO = new AthleteDAO(context);
	}

	public List<AthleteAttendance> getAthleteAttendancesBySessionId(long sessionId) {
		List<AthleteAttendance> attendances = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor attendancesCursor = db.query(AthleteAttendanceTable.TABLE_NAME, columnNames, AthleteAttendanceTable.SESSION_ID_COL_NAME + "="
				+ sessionId, null, null, null, null);

		if (attendancesCursor.getCount() > 0) {
			attendancesCursor.moveToFirst();
			attendances = createAthleteAttendanceListFromCursor(attendancesCursor);
		} else {
			attendances = new ArrayList<AthleteAttendance>();
		}
		attendancesCursor.close();
		return attendances;
	}

	public AthleteAttendance getAthleteAttendanceByRemoteId(long id) {
		AthleteAttendance attendance = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor attendanceCursor = db.query(AthleteAttendanceTable.TABLE_NAME, columnNames, AthleteAttendanceTable.REMOTE_ID_COL_NAME + "=" + id,
				null, null, null, null);
		if (attendanceCursor.getCount() > 0) {
			attendanceCursor.moveToFirst();
			attendance = createAthleteAttendanceFromCursor(attendanceCursor);
		}
		attendanceCursor.close();
		return attendance;
	}

	public AthleteAttendance getAthleteAttendanceById(long id) {
		AthleteAttendance attendance = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor attendanceCursor = db.query(AthleteAttendanceTable.TABLE_NAME, columnNames, AthleteAttendanceTable.ID_COL_NAME + "=" + id, null, null,
				null, null);
		if (attendanceCursor.getCount() > 0) {
			attendanceCursor.moveToFirst();
			attendance = createAthleteAttendanceFromCursor(attendanceCursor);
		}
		attendanceCursor.close();
		return attendance;
	}

	public long saveNewAthleteAttendance(AthleteAttendance attendance) {

		long attendanceId = 0;
		SQLiteDatabase db = localDB.getWritableDatabase();
		AthleteAttendance dbAthleteAttendance = getAthleteAttendanceByRemoteId(attendance.getRemoteId());
		if (dbAthleteAttendance == null) {
			db.beginTransaction();
			ContentValues values = new ContentValues();

			values.put(AthleteAttendanceTable.REMOTE_ID_COL_NAME, attendance.getRemoteId());
			if (attendance.getRemoteCreateTimestamp() != 0) {
				values.put(AthleteAttendanceTable.REMOTE_CREATED_COL_NAME, attendance.getRemoteCreateTimestamp());
			}
			if (attendance.getRemoteUpdatedTimestamp() != 0) {
				values.put(AthleteAttendanceTable.REMOTE_UPDATED_COL_NAME, attendance.getRemoteUpdatedTimestamp());
			}

			long sessionId = sessionDAO.getSessionByRemoteId(attendance.getRemoteSessionId()).getId();
			values.put(AthleteAttendanceTable.SESSION_ID_COL_NAME, sessionId);

			if (attendance.getAthlete() != null) {
				long athleteId = athleteDAO.geAthleteByRemoteId(attendance.getAthlete().getRemoteId()).getId();
				values.put(AthleteAttendanceTable.ATHLETE_ID_COL_NAME, athleteId);
			}

			values.put(AthleteAttendanceTable.ATTENDANCE_VALUE_COL_NAME, attendance.getAttendanceValue().toString());

			attendanceId = db.insert(AthleteAttendanceTable.TABLE_NAME, null, values);
			db.setTransactionSuccessful();
			db.endTransaction();
		} else if (dbAthleteAttendance.getRemoteUpdatedTimestamp() < dbAthleteAttendance.getRemoteUpdatedTimestamp()) {
			// more recent version of the athlete atendance
			attendance.setId(dbAthleteAttendance.getId());
			updateAthleteAttendance(attendance);
		}
		return attendanceId;
	}

	private boolean updateAthleteAttendance(AthleteAttendance attendance) {
		boolean transactionStatus = false;
		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		ContentValues values = new ContentValues();
		if (attendance.getRemoteCreateTimestamp() != 0) {
			values.put(AthleteAttendanceTable.REMOTE_CREATED_COL_NAME, attendance.getRemoteCreateTimestamp());
		}
		if (attendance.getRemoteUpdatedTimestamp() != 0) {
			values.put(AthleteAttendanceTable.REMOTE_UPDATED_COL_NAME, attendance.getRemoteUpdatedTimestamp());
		}

		long sessionId = sessionDAO.getSessionByRemoteId(attendance.getRemoteSessionId()).getId();
		values.put(AthleteAttendanceTable.SESSION_ID_COL_NAME, sessionId);

		if (attendance.getAthlete() != null) {
			long athleteId = athleteDAO.geAthleteByRemoteId(attendance.getAthlete().getRemoteId()).getId();
			values.put(AthleteAttendanceTable.ATHLETE_ID_COL_NAME, athleteId);
		}

		values.put(AthleteAttendanceTable.ATTENDANCE_VALUE_COL_NAME, attendance.getAttendanceValue().toString());
		db.update(ProgramTable.TABLE_NAME, values, ProgramTable.ID_COL_NAME + "=" + attendance.getId(), null);
		db.setTransactionSuccessful();
		transactionStatus = true;
		db.endTransaction();
		return transactionStatus;

	}

	private List<AthleteAttendance> createAthleteAttendanceListFromCursor(Cursor c) {
		ArrayList<AthleteAttendance> attendances = new ArrayList<AthleteAttendance>(c.getCount());
		if (c.getCount() > 0) {
			c.moveToFirst();
			while (!c.isAfterLast()) {
				AthleteAttendance attendance = createAthleteAttendanceFromCursor(c);
				if (attendance != null) {
					attendances.add(attendance);
				}
				c.moveToNext();
			}

		}
		return attendances;
	}

	private AthleteAttendance createAthleteAttendanceFromCursor(Cursor c) {

		AthleteAttendance attendance = null;
		if (c.getPosition() >= 0) {
			attendance = new AthleteAttendance();
			try {
				attendance.setId(c.getLong(c.getColumnIndexOrThrow(AthleteAttendanceTable.ID_COL_NAME)));
				attendance.setRemoteId(c.getLong(c.getColumnIndexOrThrow(AthleteAttendanceTable.REMOTE_ID_COL_NAME)));
				attendance.setRemoteCreateTimestamp(c.getLong(c.getColumnIndexOrThrow(AthleteAttendanceTable.REMOTE_CREATED_COL_NAME)));
				attendance.setRemoteUpdatedTimestamp(c.getLong(c.getColumnIndexOrThrow(AthleteAttendanceTable.REMOTE_UPDATED_COL_NAME)));
				attendance.setRemoteSessionId(sessionDAO.getSessionByRemoteId(
						c.getLong(c.getColumnIndexOrThrow(AthleteAttendanceTable.SESSION_ID_COL_NAME))).getRemoteId());
				attendance.setAttendanceValue(AthleteAttendance.AttendanceValue.valueOf(c.getString(c
						.getColumnIndexOrThrow(AthleteAttendanceTable.ATTENDANCE_VALUE_COL_NAME))));

			} catch (IllegalArgumentException iax) {
				attendance = null;
			}
		}
		return attendance;
	}
}
