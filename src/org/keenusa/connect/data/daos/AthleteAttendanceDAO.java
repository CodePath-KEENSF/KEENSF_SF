package org.keenusa.connect.data.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.keenusa.connect.data.KeenConnectDB;
import org.keenusa.connect.data.tables.AthleteAttendanceTable;
import org.keenusa.connect.data.tables.AthleteTable;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.AthleteAttendance;
import org.keenusa.connect.models.ContactPerson;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.models.Location;
import org.keenusa.connect.models.Parent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

public class AthleteAttendanceDAO {

	private KeenConnectDB localDB;
	private SessionDAO sessionDAO;
	private AthleteDAO athleteDAO;

	private HashMap<Long, Long> sessionMap = new HashMap<Long, Long>();
	private HashMap<Long, Long> athleteMap = new HashMap<Long, Long>();

	private static String JOINED_TABLES_STRING = AthleteAttendanceTable.TABLE_NAME + " INNER JOIN " + AthleteTable.TABLE_NAME + " ON "
			+ AthleteAttendanceTable.TABLE_NAME + "." + AthleteAttendanceTable.ATHLETE_ID_COL_NAME + "=" + AthleteTable.TABLE_NAME + "."
			+ AthleteTable.ID_COL_NAME;

	String[] simpleColumnNames = { AthleteAttendanceTable.ID_COL_NAME, AthleteAttendanceTable.REMOTE_ID_COL_NAME,
			AthleteAttendanceTable.REMOTE_CREATED_COL_NAME, AthleteAttendanceTable.REMOTE_UPDATED_COL_NAME };

	String[] columnNames = { AthleteAttendanceTable.TABLE_NAME + "." + AthleteAttendanceTable.ID_COL_NAME, AthleteAttendanceTable.REMOTE_ID_COL_NAME,
			AthleteAttendanceTable.REMOTE_CREATED_COL_NAME, AthleteAttendanceTable.REMOTE_UPDATED_COL_NAME,
			AthleteAttendanceTable.SESSION_ID_COL_NAME, AthleteAttendanceTable.ATHLETE_ID_COL_NAME, AthleteAttendanceTable.ATTENDANCE_VALUE_COL_NAME,
			AthleteTable.REMOTE_ID_COL_NAME, AthleteTable.REMOTE_CREATED_COL_NAME, AthleteTable.REMOTE_UPDATED_COL_NAME,
			AthleteTable.FIRST_NAME_COL_NAME, AthleteTable.MIDDLE_NAME_COL_NAME, AthleteTable.LAST_NAME_COL_NAME, AthleteTable.EMAIL_COL_NAME,
			AthleteTable.PHONE_COL_NAME, AthleteTable.GENDER_COL_NAME, AthleteTable.NICKNAME_COL_NAME, AthleteTable.DOB_COL_NAME,
			AthleteTable.PRIMLANGUAGE_COL_NAME, AthleteTable.ACTIVE_COL_NAME, AthleteTable.PARENT_MOBILE_COL_NAME,
			AthleteTable.PARENT_EMAIL_COL_NAME, AthleteTable.PARENT_PHONE_COL_NAME, AthleteTable.PARENT_RELATIONSHIP_COL_NAME,
			AthleteTable.PARENT_FIRST_NAME_COL_NAME, AthleteTable.PARENT_LAST_NAME_COL_NAME, AthleteTable.CITY_COL_NAME, AthleteTable.STATE_COL_NAME,
			AthleteTable.ZIPCODE_COL_NAME, AthleteTable.NUM_SESSIONS_ATTENDED_COL_NAME, AthleteTable.LAST_ATTENDED_DATE_COL_NAME };

	public AthleteAttendanceDAO(Context context) {
		localDB = KeenConnectDB.getKeenConnectDB(context);
		sessionDAO = new SessionDAO(context);
		athleteDAO = new AthleteDAO(context);
	}

	public AthleteAttendance getSimpleAthleteAttendanceByRemoteId(long id) {
		AthleteAttendance attendance = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor attendanceCursor = db.query(AthleteAttendanceTable.TABLE_NAME, simpleColumnNames,
				AthleteAttendanceTable.REMOTE_ID_COL_NAME + "=" + id, null, null, null, null);
		if (attendanceCursor.getCount() > 0) {
			attendanceCursor.moveToFirst();
			attendance = createSimpleAthleteAttendanceFromCursor(attendanceCursor);
		}
		attendanceCursor.close();
		return attendance;
	}

	public List<AthleteAttendance> getAthleteAttendancesBySessionId(long sessionId) {
		List<AthleteAttendance> attendances = null;
		SQLiteDatabase db = localDB.getReadableDatabase();

		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		sqlBuilder.setTables(JOINED_TABLES_STRING);
		Cursor attendancesCursor = sqlBuilder.query(db, columnNames, AthleteAttendanceTable.SESSION_ID_COL_NAME + "=" + sessionId, null, null, null,
				AthleteTable.TABLE_NAME + "." + AthleteTable.FIRST_NAME_COL_NAME + " ASC", null);

		if (attendancesCursor.getCount() > 0) {
			attendancesCursor.moveToFirst();
			attendances = createAthleteAttendanceListFromCursor(attendancesCursor);
		} else {
			attendances = new ArrayList<AthleteAttendance>();
		}
		attendancesCursor.close();
		return attendances;
	}

	public AthleteAttendance getAthleteAttendanceById(long id) {
		AthleteAttendance attendance = null;
		SQLiteDatabase db = localDB.getReadableDatabase();

		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		sqlBuilder.setTables(JOINED_TABLES_STRING);
		Cursor attendanceCursor = sqlBuilder.query(db, columnNames, AthleteAttendanceTable.ID_COL_NAME + "=" + id, null, null, null, null, null);

		if (attendanceCursor.getCount() > 0) {
			attendanceCursor.moveToFirst();
			attendance = createAthleteAttendanceFromCursor(attendanceCursor);
		}
		attendanceCursor.close();
		return attendance;
	}

	public boolean updateAthleteAttendanceStatus(AthleteAttendance attendance) {
		boolean transactionStatus = false;
		ContentValues values = new ContentValues();
		values.put(AthleteAttendanceTable.ATTENDANCE_VALUE_COL_NAME, attendance.getAttendanceValue().toString());
		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		db.update(AthleteAttendanceTable.TABLE_NAME, values, AthleteAttendanceTable.ID_COL_NAME + "=" + attendance.getId(), null);
		db.setTransactionSuccessful();
		transactionStatus = true;
		db.endTransaction();
		return transactionStatus;
	}

	// should be enforcing uniqueness of session -athlete combination - no duplicate attendances
	public long saveNewAthleteAttendance(AthleteAttendance attendance) {
		long attendanceId = 0;

		AthleteAttendance dbAthleteAttendance = getSimpleAthleteAttendanceByRemoteId(attendance.getRemoteId());
		if (dbAthleteAttendance == null) {
			ContentValues values = new ContentValues();
			values.put(AthleteAttendanceTable.REMOTE_ID_COL_NAME, attendance.getRemoteId());
			values.put(AthleteAttendanceTable.REMOTE_CREATED_COL_NAME, attendance.getRemoteCreateTimestamp());
			values.put(AthleteAttendanceTable.REMOTE_UPDATED_COL_NAME, attendance.getRemoteUpdatedTimestamp());

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
			values.put(AthleteAttendanceTable.SESSION_ID_COL_NAME, sessionId);

			if (attendance.getAthlete() != null) {

				long athleteId = 0;
				if (athleteMap.containsKey(attendance.getAthlete().getRemoteId())) {
					athleteId = athleteMap.get(attendance.getAthlete().getRemoteId());
				} else {
					Athlete athlete = athleteDAO.getSimpleAthleteByRemoteId(attendance.getAthlete().getRemoteId());
					if (athlete != null) {
						athleteId = athlete.getId();
						athleteMap.put(attendance.getAthlete().getRemoteId(), athleteId);
					}
				}
				values.put(AthleteAttendanceTable.ATHLETE_ID_COL_NAME, athleteId);
			}
			values.put(AthleteAttendanceTable.ATTENDANCE_VALUE_COL_NAME, attendance.getAttendanceValue().toString());

			SQLiteDatabase db = localDB.getWritableDatabase();
			db.beginTransaction();
			attendanceId = db.insert(AthleteAttendanceTable.TABLE_NAME, null, values);
			db.setTransactionSuccessful();
			db.endTransaction();
		} else if (dbAthleteAttendance.getRemoteUpdatedTimestamp() < attendance.getRemoteUpdatedTimestamp()) {
			// more recent version of the athlete attendance
			attendance.setId(dbAthleteAttendance.getId());
			updateAthleteAttendance(attendance);
		}
		return attendanceId;
	}

	public boolean saveNewAthleteAttendanceList(List<AthleteAttendance> attendances) {
		boolean areSaved = false;
		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		ContentValues values = new ContentValues();
		for (AthleteAttendance attendance : attendances) {
			AthleteAttendance dbAthleteAttendance = getSimpleAthleteAttendanceByRemoteId(attendance.getRemoteId());
			if (dbAthleteAttendance == null) {
				values.clear();
				values.put(AthleteAttendanceTable.REMOTE_ID_COL_NAME, attendance.getRemoteId());
				values.put(AthleteAttendanceTable.REMOTE_CREATED_COL_NAME, attendance.getRemoteCreateTimestamp());
				values.put(AthleteAttendanceTable.REMOTE_UPDATED_COL_NAME, attendance.getRemoteUpdatedTimestamp());

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
				values.put(AthleteAttendanceTable.SESSION_ID_COL_NAME, sessionId);

				if (attendance.getAthlete() != null) {

					long athleteId = 0;
					if (athleteMap.containsKey(attendance.getAthlete().getRemoteId())) {
						athleteId = athleteMap.get(attendance.getAthlete().getRemoteId());
					} else {
						Athlete athlete = athleteDAO.getSimpleAthleteByRemoteId(attendance.getAthlete().getRemoteId());
						if (athlete != null) {
							athleteId = athlete.getId();
							athleteMap.put(attendance.getAthlete().getRemoteId(), athleteId);
						}
					}
					values.put(AthleteAttendanceTable.ATHLETE_ID_COL_NAME, athleteId);
				}
				values.put(AthleteAttendanceTable.ATTENDANCE_VALUE_COL_NAME, attendance.getAttendanceValue().toString());
				db.insert(AthleteAttendanceTable.TABLE_NAME, null, values);
			}
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		areSaved = true;
		return areSaved;
	}

	private boolean updateAthleteAttendance(AthleteAttendance attendance) {
		boolean transactionStatus = false;

		ContentValues values = new ContentValues();
		values.put(AthleteAttendanceTable.REMOTE_CREATED_COL_NAME, attendance.getRemoteCreateTimestamp());
		values.put(AthleteAttendanceTable.REMOTE_UPDATED_COL_NAME, attendance.getRemoteUpdatedTimestamp());

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
		values.put(AthleteAttendanceTable.SESSION_ID_COL_NAME, sessionId);

		if (attendance.getAthlete() != null) {

			long athleteId = 0;
			if (athleteMap.containsKey(attendance.getAthlete().getRemoteId())) {
				athleteId = athleteMap.get(attendance.getAthlete().getRemoteId());
			} else {
				Athlete athlete = athleteDAO.getSimpleAthleteByRemoteId(attendance.getAthlete().getRemoteId());
				if (athlete != null) {
					athleteId = athlete.getId();
					athleteMap.put(attendance.getAthlete().getRemoteId(), athleteId);
				}
			}
			values.put(AthleteAttendanceTable.ATHLETE_ID_COL_NAME, athleteId);
		}
		values.put(AthleteAttendanceTable.ATTENDANCE_VALUE_COL_NAME, attendance.getAttendanceValue().toString());

		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		db.update(AthleteAttendanceTable.TABLE_NAME, values, AthleteAttendanceTable.ID_COL_NAME + "=" + attendance.getId(), null);
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
				attendance.setSessionId(c.getLong(c.getColumnIndexOrThrow(AthleteAttendanceTable.SESSION_ID_COL_NAME)));
				attendance.setAttendanceValue(AthleteAttendance.AttendanceValue.valueOf(c.getString(c
						.getColumnIndexOrThrow(AthleteAttendanceTable.ATTENDANCE_VALUE_COL_NAME))));

				Athlete athlete = new Athlete();
				athlete.setId(c.getLong(c.getColumnIndexOrThrow(AthleteAttendanceTable.ATHLETE_ID_COL_NAME)));
				athlete.setRemoteId(c.getLong(c.getColumnIndexOrThrow(AthleteTable.REMOTE_ID_COL_NAME)));
				athlete.setRemoteCreateTimestamp(c.getLong(c.getColumnIndexOrThrow(AthleteTable.REMOTE_CREATED_COL_NAME)));
				athlete.setRemoteUpdatedTimestamp(c.getLong(c.getColumnIndexOrThrow(AthleteTable.REMOTE_UPDATED_COL_NAME)));
				athlete.setFirstName(c.getString(c.getColumnIndexOrThrow(AthleteTable.FIRST_NAME_COL_NAME)));
				athlete.setMiddleName(c.getString(c.getColumnIndexOrThrow(AthleteTable.MIDDLE_NAME_COL_NAME)));
				athlete.setLastName(c.getString(c.getColumnIndexOrThrow(AthleteTable.LAST_NAME_COL_NAME)));
				athlete.setEmail(c.getString(c.getColumnIndexOrThrow(AthleteTable.EMAIL_COL_NAME)));
				athlete.setPhone(c.getString(c.getColumnIndexOrThrow(AthleteTable.PHONE_COL_NAME)));
				athlete.setGender(ContactPerson.Gender.valueOf(c.getString(c.getColumnIndexOrThrow(AthleteTable.GENDER_COL_NAME))));
				athlete.setNickName(c.getString(c.getColumnIndexOrThrow(AthleteTable.NICKNAME_COL_NAME)));
				long dob = c.getLong(c.getColumnIndexOrThrow(AthleteTable.DOB_COL_NAME));
				if (dob != 0) {
					athlete.setDateOfBirth(new DateTime(dob));
				}
				athlete.setPrimaryLanguage(c.getString(c.getColumnIndexOrThrow(AthleteTable.PRIMLANGUAGE_COL_NAME)));
				boolean active = ((c.getInt(c.getColumnIndexOrThrow(AthleteTable.ACTIVE_COL_NAME))) == 1 ? true : false);
				athlete.setActive(active);

				Parent parent = new Parent();
				parent.setFirstName(c.getString(c.getColumnIndexOrThrow(AthleteTable.PARENT_FIRST_NAME_COL_NAME)));
				parent.setLastName(c.getString(c.getColumnIndexOrThrow(AthleteTable.PARENT_LAST_NAME_COL_NAME)));
				parent.setCellPhone(c.getString(c.getColumnIndexOrThrow(AthleteTable.PARENT_MOBILE_COL_NAME)));
				parent.setPhone(c.getString(c.getColumnIndexOrThrow(AthleteTable.PARENT_PHONE_COL_NAME)));
				parent.setEmail(c.getString(c.getColumnIndexOrThrow(AthleteTable.PARENT_EMAIL_COL_NAME)));
				String relationship = c.getString(c.getColumnIndexOrThrow(AthleteTable.PARENT_RELATIONSHIP_COL_NAME));
				if (relationship != null) {
					parent.setParentRelationship(Parent.ParentRelationship.valueOf(relationship));
				}
				athlete.setPrimaryParent(parent);
				Location location = new Location();
				location.setCity(c.getString(c.getColumnIndexOrThrow(AthleteTable.CITY_COL_NAME)));
				location.setState(c.getString(c.getColumnIndexOrThrow(AthleteTable.STATE_COL_NAME)));
				location.setZipCode(c.getString(c.getColumnIndexOrThrow(AthleteTable.ZIPCODE_COL_NAME)));
				athlete.setLocation(location);
				athlete.setNumberOfSessionsAttended(c.getInt(c.getColumnIndexOrThrow(AthleteTable.NUM_SESSIONS_ATTENDED_COL_NAME)));
				athlete.setDateLastAttended(new DateTime(c.getLong(c.getColumnIndexOrThrow(AthleteTable.LAST_ATTENDED_DATE_COL_NAME))));
				attendance.setAthlete(athlete);

			} catch (IllegalArgumentException iax) {
				attendance = null;
			}
		}
		return attendance;
	}

	private AthleteAttendance createSimpleAthleteAttendanceFromCursor(Cursor c) {
		AthleteAttendance attendance = null;
		if (c.getPosition() >= 0) {
			attendance = new AthleteAttendance();
			try {
				attendance.setId(c.getLong(c.getColumnIndexOrThrow(AthleteAttendanceTable.ID_COL_NAME)));
				attendance.setRemoteId(c.getLong(c.getColumnIndexOrThrow(AthleteAttendanceTable.REMOTE_ID_COL_NAME)));
				attendance.setRemoteCreateTimestamp(c.getLong(c.getColumnIndexOrThrow(AthleteAttendanceTable.REMOTE_CREATED_COL_NAME)));
				attendance.setRemoteUpdatedTimestamp(c.getLong(c.getColumnIndexOrThrow(AthleteAttendanceTable.REMOTE_UPDATED_COL_NAME)));

			} catch (IllegalArgumentException iax) {
				attendance = null;
			}
		}
		return attendance;
	}
}
