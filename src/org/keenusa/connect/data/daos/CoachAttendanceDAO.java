package org.keenusa.connect.data.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.keenusa.connect.data.KeenConnectDB;
import org.keenusa.connect.data.tables.CoachAttendanceTable;
import org.keenusa.connect.data.tables.CoachTable;
import org.keenusa.connect.data.tables.ProgramTable;
import org.keenusa.connect.models.Coach;
import org.keenusa.connect.models.CoachAttendance;
import org.keenusa.connect.models.ContactPerson;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.models.Location;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

public class CoachAttendanceDAO {

	private KeenConnectDB localDB;
	private SessionDAO sessionDAO;
	private CoachDAO coachDAO;

	private HashMap<Long, Long> sessionMap = new HashMap<Long, Long>();
	private HashMap<Long, Long> coachMap = new HashMap<Long, Long>();

	private static String JOINED_TABLES_STRING = CoachAttendanceTable.TABLE_NAME + " INNER JOIN " + CoachTable.TABLE_NAME + " ON "
			+ CoachAttendanceTable.TABLE_NAME + "." + CoachAttendanceTable.COACH_ID_COL_NAME + "=" + CoachTable.TABLE_NAME + "."
			+ CoachTable.ID_COL_NAME;

	String[] simpleColumnNames = { CoachAttendanceTable.ID_COL_NAME, CoachAttendanceTable.REMOTE_ID_COL_NAME,
			CoachAttendanceTable.REMOTE_CREATED_COL_NAME, CoachAttendanceTable.REMOTE_UPDATED_COL_NAME };

	String[] columnNames = { CoachAttendanceTable.TABLE_NAME + "." + CoachAttendanceTable.ID_COL_NAME, CoachAttendanceTable.REMOTE_ID_COL_NAME,
			CoachAttendanceTable.REMOTE_CREATED_COL_NAME, CoachAttendanceTable.REMOTE_UPDATED_COL_NAME, CoachAttendanceTable.SESSION_ID_COL_NAME,
			CoachAttendanceTable.COACH_ID_COL_NAME, CoachAttendanceTable.ATTENDANCE_VALUE_COL_NAME, CoachTable.REMOTE_ID_COL_NAME,
			CoachTable.REMOTE_CREATED_COL_NAME, CoachTable.REMOTE_UPDATED_COL_NAME, CoachTable.FIRST_NAME_COL_NAME, CoachTable.MIDDLE_NAME_COL_NAME,
			CoachTable.LAST_NAME_COL_NAME, CoachTable.EMAIL_COL_NAME, CoachTable.PHONE_COL_NAME, CoachTable.MOBILE_COL_NAME,
			CoachTable.GENDER_COL_NAME, CoachTable.DOB_COL_NAME, CoachTable.LANGUAGES_COL_NAME, CoachTable.SKILLS_COL_NAME,
			CoachTable.ACTIVE_COL_NAME, CoachTable.CITY_COL_NAME, CoachTable.STATE_COL_NAME, CoachTable.ZIPCODE_COL_NAME,
			CoachTable.NUM_SESSIONS_ATTENDED_COL_NAME, CoachTable.LAST_ATTENDED_DATE_COL_NAME };

	public CoachAttendanceDAO(Context context) {
		localDB = KeenConnectDB.getKeenConnectDB(context);
		sessionDAO = new SessionDAO(context);
		coachDAO = new CoachDAO(context);
	}

	public CoachAttendance getSimpleCoachAttendanceByRemoteId(long id) {
		CoachAttendance attendance = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor attendanceCursor = db.query(CoachAttendanceTable.TABLE_NAME, simpleColumnNames, CoachAttendanceTable.REMOTE_ID_COL_NAME + "=" + id,
				null, null, null, null);
		if (attendanceCursor.getCount() > 0) {
			attendanceCursor.moveToFirst();
			attendance = createSimpleCoachAttendanceFromCursor(attendanceCursor);
		}
		attendanceCursor.close();
		return attendance;
	}

	public List<CoachAttendance> getCoachAttendancesBySessionId(long sessionId) {
		List<CoachAttendance> attendances = null;
		SQLiteDatabase db = localDB.getReadableDatabase();

		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		sqlBuilder.setTables(JOINED_TABLES_STRING);
		Cursor attendancesCursor = sqlBuilder.query(db, columnNames, CoachAttendanceTable.SESSION_ID_COL_NAME + "=" + sessionId + " AND "
				+ CoachAttendanceTable.ATTENDANCE_VALUE_COL_NAME + "<>'" + CoachAttendance.AttendanceValue.CANCELLED.toString() + "'", null, null,
				null, CoachTable.TABLE_NAME + "." + CoachTable.FIRST_NAME_COL_NAME + " ASC", null);

		if (attendancesCursor.getCount() > 0) {
			attendancesCursor.moveToFirst();
			attendances = createCoachAttendanceListFromCursor(attendancesCursor);
		} else {
			attendances = new ArrayList<CoachAttendance>();
		}
		attendancesCursor.close();
		return attendances;
	}

	public CoachAttendance getCoachAttendanceById(long id) {
		CoachAttendance attendance = null;
		SQLiteDatabase db = localDB.getReadableDatabase();

		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		sqlBuilder.setTables(JOINED_TABLES_STRING);
		Cursor attendanceCursor = sqlBuilder.query(db, columnNames, CoachAttendanceTable.ID_COL_NAME + "=" + id, null, null, null, null, null);

		if (attendanceCursor.getCount() > 0) {
			attendanceCursor.moveToFirst();
			attendance = createCoachAttendanceFromCursor(attendanceCursor);
		}
		attendanceCursor.close();
		return attendance;
	}

	public long saveNewCoachAttendance(CoachAttendance attendance) {
		long attendanceId = 0;

		CoachAttendance dbCoachAttendance = getSimpleCoachAttendanceByRemoteId(attendance.getRemoteId());
		if (dbCoachAttendance == null) {
			ContentValues values = new ContentValues();
			values.put(CoachAttendanceTable.REMOTE_ID_COL_NAME, attendance.getRemoteId());
			values.put(CoachAttendanceTable.REMOTE_CREATED_COL_NAME, attendance.getRemoteCreateTimestamp());
			values.put(CoachAttendanceTable.REMOTE_UPDATED_COL_NAME, attendance.getRemoteUpdatedTimestamp());

			long sessionId = 0;
			if (sessionMap.containsKey(attendance.getRemoteSessionId())) {
				sessionId = sessionMap.get(attendance.getRemoteSessionId());
			} else {
				KeenSession session = sessionDAO.getSimpleSessionByRemoteId(attendance.getRemoteSessionId());
				if (session != null) {
					sessionId = session.getId();
					sessionMap.put(attendance.getRemoteSessionId(), sessionId);
				}
			}
			values.put(CoachAttendanceTable.SESSION_ID_COL_NAME, sessionId);

			if (attendance.getCoach() != null) {
				long coachId = 0;
				if (coachMap.containsKey(attendance.getCoach().getRemoteId())) {
					coachId = coachMap.get(attendance.getCoach().getRemoteId());
				} else {
					Coach coach = coachDAO.getSimpleCoachByRemoteId(attendance.getCoach().getRemoteId());
					if (coach != null) {
						coachId = coach.getId();
						coachMap.put(attendance.getCoach().getRemoteId(), coachId);
					}
				}
				values.put(CoachAttendanceTable.COACH_ID_COL_NAME, coachId);
			}

			values.put(CoachAttendanceTable.ATTENDANCE_VALUE_COL_NAME, attendance.getAttendanceValue().toString());

			SQLiteDatabase db = localDB.getWritableDatabase();
			db.beginTransaction();
			attendanceId = db.insert(CoachAttendanceTable.TABLE_NAME, null, values);
			db.setTransactionSuccessful();
			db.endTransaction();
		} else if (dbCoachAttendance.getRemoteUpdatedTimestamp() < attendance.getRemoteUpdatedTimestamp()) {
			// more recent version of the coach attendance
			attendance.setId(dbCoachAttendance.getId());
			updateCoachAttendance(attendance);
		}
		return attendanceId;
	}

	public boolean saveNewCoachAttendanceList(List<CoachAttendance> attendances) {
		boolean areSaved = false;
		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		ContentValues values = new ContentValues();
		for (CoachAttendance attendance : attendances) {

			CoachAttendance dbCoachAttendance = getSimpleCoachAttendanceByRemoteId(attendance.getRemoteId());
			if (dbCoachAttendance == null) {
				values.clear();
				values.put(CoachAttendanceTable.REMOTE_ID_COL_NAME, attendance.getRemoteId());
				values.put(CoachAttendanceTable.REMOTE_CREATED_COL_NAME, attendance.getRemoteCreateTimestamp());
				values.put(CoachAttendanceTable.REMOTE_UPDATED_COL_NAME, attendance.getRemoteUpdatedTimestamp());

				long sessionId = 0;
				if (sessionMap.containsKey(attendance.getRemoteSessionId())) {
					sessionId = sessionMap.get(attendance.getRemoteSessionId());
				} else {
					KeenSession session = sessionDAO.getSimpleSessionByRemoteId(attendance.getRemoteSessionId());
					if (session != null) {
						sessionId = session.getId();
						sessionMap.put(attendance.getRemoteSessionId(), sessionId);
					}
				}
				values.put(CoachAttendanceTable.SESSION_ID_COL_NAME, sessionId);

				if (attendance.getCoach() != null) {
					long coachId = 0;
					if (coachMap.containsKey(attendance.getCoach().getRemoteId())) {
						coachId = coachMap.get(attendance.getCoach().getRemoteId());
					} else {
						Coach coach = coachDAO.getSimpleCoachByRemoteId(attendance.getCoach().getRemoteId());
						if (coach != null) {
							coachId = coach.getId();
							coachMap.put(attendance.getCoach().getRemoteId(), coachId);
						}
					}
					values.put(CoachAttendanceTable.COACH_ID_COL_NAME, coachId);
				}

				values.put(CoachAttendanceTable.ATTENDANCE_VALUE_COL_NAME, attendance.getAttendanceValue().toString());
				db.insert(CoachAttendanceTable.TABLE_NAME, null, values);
			}

		}
		db.setTransactionSuccessful();
		db.endTransaction();
		areSaved = true;
		return areSaved;
	}

	private boolean updateCoachAttendance(CoachAttendance attendance) {
		boolean transactionStatus = false;

		ContentValues values = new ContentValues();
		values.put(CoachAttendanceTable.REMOTE_CREATED_COL_NAME, attendance.getRemoteCreateTimestamp());
		values.put(CoachAttendanceTable.REMOTE_UPDATED_COL_NAME, attendance.getRemoteUpdatedTimestamp());

		long sessionId = 0;
		if (sessionMap.containsKey(attendance.getRemoteSessionId())) {
			sessionId = sessionMap.get(attendance.getRemoteSessionId());
		} else {
			KeenSession session = sessionDAO.getSimpleSessionByRemoteId(attendance.getRemoteSessionId());
			if (session != null) {
				sessionId = session.getId();
				sessionMap.put(attendance.getRemoteSessionId(), sessionId);
			}
		}
		values.put(CoachAttendanceTable.SESSION_ID_COL_NAME, sessionId);

		if (attendance.getCoach() != null) {
			long coachId = 0;
			if (coachMap.containsKey(attendance.getCoach().getRemoteId())) {
				coachId = coachMap.get(attendance.getCoach().getRemoteId());
			} else {
				Coach coach = coachDAO.getSimpleCoachByRemoteId(attendance.getCoach().getRemoteId());
				if (coach != null) {
					coachId = coach.getId();
					coachMap.put(attendance.getCoach().getRemoteId(), coachId);
				}
			}
			values.put(CoachAttendanceTable.COACH_ID_COL_NAME, coachId);
		}

		values.put(CoachAttendanceTable.ATTENDANCE_VALUE_COL_NAME, attendance.getAttendanceValue().toString());
		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
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
				attendance.setSessionId(c.getLong(c.getColumnIndexOrThrow(CoachAttendanceTable.SESSION_ID_COL_NAME)));
				attendance.setAttendanceValue(CoachAttendance.AttendanceValue.valueOf(c.getString(c
						.getColumnIndexOrThrow(CoachAttendanceTable.ATTENDANCE_VALUE_COL_NAME))));

				Coach coach = new Coach();
				coach.setId(c.getLong(c.getColumnIndexOrThrow(CoachAttendanceTable.ID_COL_NAME)));
				coach.setRemoteId(c.getLong(c.getColumnIndexOrThrow(CoachTable.REMOTE_ID_COL_NAME)));
				coach.setRemoteCreateTimestamp(c.getLong(c.getColumnIndexOrThrow(CoachTable.REMOTE_CREATED_COL_NAME)));
				coach.setRemoteUpdatedTimestamp(c.getLong(c.getColumnIndexOrThrow(CoachTable.REMOTE_UPDATED_COL_NAME)));
				coach.setFirstName(c.getString(c.getColumnIndexOrThrow(CoachTable.FIRST_NAME_COL_NAME)));
				coach.setMiddleName(c.getString(c.getColumnIndexOrThrow(CoachTable.MIDDLE_NAME_COL_NAME)));
				coach.setLastName(c.getString(c.getColumnIndexOrThrow(CoachTable.LAST_NAME_COL_NAME)));
				coach.setEmail(c.getString(c.getColumnIndexOrThrow(CoachTable.EMAIL_COL_NAME)));
				coach.setPhone(c.getString(c.getColumnIndexOrThrow(CoachTable.PHONE_COL_NAME)));
				coach.setGender(ContactPerson.Gender.valueOf(c.getString(c.getColumnIndexOrThrow(CoachTable.GENDER_COL_NAME))));
				coach.setCellPhone(c.getString(c.getColumnIndexOrThrow(CoachTable.MOBILE_COL_NAME)));
				long dob = c.getLong(c.getColumnIndexOrThrow(CoachTable.DOB_COL_NAME));
				if (dob != 0) {
					coach.setDateOfBirth(new DateTime(dob));
				}
				coach.setForeignLanguages(c.getString(c.getColumnIndexOrThrow(CoachTable.LANGUAGES_COL_NAME)));
				coach.setSkillsExperience(c.getString(c.getColumnIndexOrThrow(CoachTable.SKILLS_COL_NAME)));
				boolean active = ((c.getInt(c.getColumnIndexOrThrow(CoachTable.ACTIVE_COL_NAME))) == 1 ? true : false);
				coach.setActive(active);
				Location location = new Location();
				location.setCity(c.getString(c.getColumnIndexOrThrow(CoachTable.CITY_COL_NAME)));
				location.setState(c.getString(c.getColumnIndexOrThrow(CoachTable.STATE_COL_NAME)));
				location.setZipCode(c.getString(c.getColumnIndexOrThrow(CoachTable.ZIPCODE_COL_NAME)));
				coach.setLocation(location);
				coach.setNumberOfSessionsAttended(c.getInt(c.getColumnIndexOrThrow(CoachTable.NUM_SESSIONS_ATTENDED_COL_NAME)));
				coach.setDateLastAttended(new DateTime(c.getLong(c.getColumnIndexOrThrow(CoachTable.LAST_ATTENDED_DATE_COL_NAME))));
				attendance.setCoach(coach);

			} catch (IllegalArgumentException iax) {
				attendance = null;
			}
		}
		return attendance;
	}

	private CoachAttendance createSimpleCoachAttendanceFromCursor(Cursor c) {
		CoachAttendance attendance = null;
		if (c.getPosition() >= 0) {
			attendance = new CoachAttendance();
			try {
				attendance.setId(c.getLong(c.getColumnIndexOrThrow(CoachAttendanceTable.ID_COL_NAME)));
				attendance.setRemoteId(c.getLong(c.getColumnIndexOrThrow(CoachAttendanceTable.REMOTE_ID_COL_NAME)));
				attendance.setRemoteCreateTimestamp(c.getLong(c.getColumnIndexOrThrow(CoachAttendanceTable.REMOTE_CREATED_COL_NAME)));
				attendance.setRemoteUpdatedTimestamp(c.getLong(c.getColumnIndexOrThrow(CoachAttendanceTable.REMOTE_UPDATED_COL_NAME)));

			} catch (IllegalArgumentException iax) {
				attendance = null;
			}
		}
		return attendance;
	}
}
