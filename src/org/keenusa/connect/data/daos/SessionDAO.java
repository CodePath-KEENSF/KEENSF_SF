package org.keenusa.connect.data.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.keenusa.connect.data.KeenConnectDB;
import org.keenusa.connect.data.tables.ProgramTable;
import org.keenusa.connect.data.tables.SessionTable;
import org.keenusa.connect.models.KeenProgram;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.models.Location;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

public class SessionDAO {

	private KeenConnectDB localDB;
	private ProgramDAO programDAO;
	private HashMap<Long, Long> programMap = new HashMap<Long, Long>();

	private static String JOINED_TABLES_STRING = SessionTable.TABLE_NAME + " INNER JOIN " + ProgramTable.TABLE_NAME + " ON "
			+ SessionTable.TABLE_NAME + "." + SessionTable.PROGRAM_ID_COL_NAME + "=" + ProgramTable.TABLE_NAME + "." + ProgramTable.ID_COL_NAME;

	String[] simpleSessionColumnNames = { SessionTable.ID_COL_NAME, SessionTable.REMOTE_ID_COL_NAME, SessionTable.REMOTE_CREATED_COL_NAME,
			SessionTable.REMOTE_UPDATED_COL_NAME };

	String[] columnNames = { SessionTable.TABLE_NAME + "." + SessionTable.ID_COL_NAME, SessionTable.REMOTE_ID_COL_NAME,
			SessionTable.REMOTE_CREATED_COL_NAME, SessionTable.REMOTE_UPDATED_COL_NAME, SessionTable.SESSION_DATE_COL_NAME,
			SessionTable.PROGRAM_ID_COL_NAME, SessionTable.NUMBER_OF_NEW_COACHES_NEEDED_COL_NAME,
			SessionTable.NUMBER_OF_RETURNING_COACHES_NEEDED_COL_NAME, SessionTable.OPEN_FOR_REGISTRATION_FLAG_COL_NAME,
			SessionTable.NUMBER_OF_ATHLETES_CHECKED_IN_COL_NAME, SessionTable.NUMBER_OF_COACHES_CHECKED_IN_COL_NAME,
			SessionTable.NUMBER_OF_ATHLETES_REGISTERED_COL_NAME, SessionTable.NUMBER_OF_COACHES_REGISTERED_COL_NAME, ProgramTable.NAME_COL_NAME,
			ProgramTable.TIMES_COL_NAME, ProgramTable.TYPE_COL_NAME, ProgramTable.REMOTE_ID_COL_NAME, ProgramTable.ADDRESS_ONE_COL_NAME,
			ProgramTable.ADDRESS_TWO_COL_NAME, ProgramTable.CITY_COL_NAME, ProgramTable.STATE_COL_NAME, ProgramTable.ZIP_CODE_COL_NAME };

	public SessionDAO(Context context) {
		localDB = KeenConnectDB.getKeenConnectDB(context);
		programDAO = new ProgramDAO(context);
	}

	public KeenSession getSimpleSessionByRemoteId(long id) {
		KeenSession session = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor sessionCursor = db.query(SessionTable.TABLE_NAME, simpleSessionColumnNames, SessionTable.REMOTE_ID_COL_NAME + "=" + id, null, null,
				null, null);
		if (sessionCursor.getCount() > 0) {
			sessionCursor.moveToFirst();
			session = createSimpleSessionFromCursor(sessionCursor);
		}
		sessionCursor.close();
		return session;
	}

	public List<KeenSession> getKeenSessionList() {
		List<KeenSession> sessions = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		sqlBuilder.setTables(JOINED_TABLES_STRING);
		Cursor sessionsCursor = sqlBuilder.query(db, columnNames, null, null, null, null, SessionTable.TABLE_NAME + "."
				+ SessionTable.SESSION_DATE_COL_NAME + " DESC", null);
		if (sessionsCursor.getCount() > 0) {
			sessionsCursor.moveToFirst();
			sessions = createSessionListFromCursor(sessionsCursor);
		} else {
			sessions = new ArrayList<KeenSession>();
		}
		sessionsCursor.close();
		return sessions;
	}

	public KeenSession getSessionById(long id) {
		KeenSession session = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		sqlBuilder.setTables(JOINED_TABLES_STRING);
		Cursor sessionCursor = sqlBuilder.query(db, columnNames, SessionTable.TABLE_NAME + "." + SessionTable.ID_COL_NAME + "=" + id, null, null,
				null, null, null);
		if (sessionCursor.getCount() > 0) {
			sessionCursor.moveToFirst();
			session = createSessionFromCursor(sessionCursor);
		}
		sessionCursor.close();
		return session;
	}

	public long saveNewSession(KeenSession session) {
		long sessionId = 0;

		KeenSession dbSession = getSimpleSessionByRemoteId(session.getRemoteId());
		if (dbSession == null) {

			ContentValues values = new ContentValues();
			values.put(SessionTable.REMOTE_ID_COL_NAME, session.getRemoteId());
			values.put(SessionTable.REMOTE_CREATED_COL_NAME, session.getRemoteCreateTimestamp());
			values.put(SessionTable.REMOTE_UPDATED_COL_NAME, session.getRemoteUpdatedTimestamp());
			if (session.getDate() != null) {
				values.put(SessionTable.SESSION_DATE_COL_NAME, session.getDate().getMillis());
			}

			if (session.getProgram() != null) {
				long programId = 0;
				if (programMap.containsKey(session.getProgram().getRemoteId())) {
					programId = programMap.get(session.getProgram().getRemoteId());
				} else {
					KeenProgram program = programDAO.getSimpleProgramByRemoteId(session.getProgram().getRemoteId());
					if (program != null) {
						programId = program.getId();
						programMap.put(session.getProgram().getRemoteId(), programId);
					}
				}
				values.put(SessionTable.PROGRAM_ID_COL_NAME, programId);
			}

			values.put(SessionTable.NUMBER_OF_NEW_COACHES_NEEDED_COL_NAME, session.getNumberOfNewCoachesNeeded());
			values.put(SessionTable.NUMBER_OF_RETURNING_COACHES_NEEDED_COL_NAME, session.getNumberOfReturningCoachesNeeded());
			values.put(SessionTable.OPEN_FOR_REGISTRATION_FLAG_COL_NAME, (session.isOpenToPublicRegistration() ? 1 : 0));

			SQLiteDatabase db = localDB.getWritableDatabase();
			db.beginTransaction();
			sessionId = db.insert(SessionTable.TABLE_NAME, null, values);
			db.setTransactionSuccessful();
			db.endTransaction();
		} else if (dbSession.getRemoteUpdatedTimestamp() < session.getRemoteUpdatedTimestamp()) {
			// more recent version of the session
			session.setId(dbSession.getId());
			updateSession(session);
		}
		return sessionId;
	}

	public boolean saveNewSessionList(List<KeenSession> sessions) {
		boolean areSaved = false;
		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		ContentValues values = new ContentValues();
		for (KeenSession session : sessions) {
			KeenSession dbSession = getSimpleSessionByRemoteId(session.getRemoteId());
			if (dbSession == null) {
				values.clear();
				values.put(SessionTable.REMOTE_ID_COL_NAME, session.getRemoteId());
				values.put(SessionTable.REMOTE_CREATED_COL_NAME, session.getRemoteCreateTimestamp());
				values.put(SessionTable.REMOTE_UPDATED_COL_NAME, session.getRemoteUpdatedTimestamp());
				if (session.getDate() != null) {
					values.put(SessionTable.SESSION_DATE_COL_NAME, session.getDate().getMillis());
				}

				if (session.getProgram() != null) {
					long programId = 0;
					if (programMap.containsKey(session.getProgram().getRemoteId())) {
						programId = programMap.get(session.getProgram().getRemoteId());
					} else {
						KeenProgram program = programDAO.getSimpleProgramByRemoteId(session.getProgram().getRemoteId());
						if (program != null) {
							programId = program.getId();
							programMap.put(session.getProgram().getRemoteId(), programId);
						}
					}
					values.put(SessionTable.PROGRAM_ID_COL_NAME, programId);
				}

				values.put(SessionTable.NUMBER_OF_NEW_COACHES_NEEDED_COL_NAME, session.getNumberOfNewCoachesNeeded());
				values.put(SessionTable.NUMBER_OF_RETURNING_COACHES_NEEDED_COL_NAME, session.getNumberOfReturningCoachesNeeded());
				values.put(SessionTable.OPEN_FOR_REGISTRATION_FLAG_COL_NAME, (session.isOpenToPublicRegistration() ? 1 : 0));
				db.insert(SessionTable.TABLE_NAME, null, values);

			}
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		areSaved = true;
		return areSaved;
	}

	private boolean updateSession(KeenSession session) {

		boolean transactionStatus = false;

		ContentValues values = new ContentValues();
		values.put(SessionTable.REMOTE_CREATED_COL_NAME, session.getRemoteCreateTimestamp());
		values.put(SessionTable.REMOTE_UPDATED_COL_NAME, session.getRemoteUpdatedTimestamp());
		if (session.getDate() != null) {
			values.put(SessionTable.SESSION_DATE_COL_NAME, session.getDate().getMillis());
		}

		if (session.getProgram() != null) {
			long programId = 0;
			if (programMap.containsKey(session.getProgram().getRemoteId())) {
				programId = programMap.get(session.getProgram().getRemoteId());
			} else {
				KeenProgram program = programDAO.getSimpleProgramByRemoteId(session.getProgram().getRemoteId());
				if (program != null) {
					programId = program.getId();
					programMap.put(session.getProgram().getRemoteId(), programId);
				}
			}
			values.put(SessionTable.PROGRAM_ID_COL_NAME, programId);
		}

		values.put(SessionTable.NUMBER_OF_NEW_COACHES_NEEDED_COL_NAME, session.getNumberOfNewCoachesNeeded());
		values.put(SessionTable.NUMBER_OF_RETURNING_COACHES_NEEDED_COL_NAME, session.getNumberOfReturningCoachesNeeded());
		values.put(SessionTable.OPEN_FOR_REGISTRATION_FLAG_COL_NAME, (session.isOpenToPublicRegistration() ? 1 : 0));

		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		db.update(SessionTable.TABLE_NAME, values, SessionTable.ID_COL_NAME + "=" + session.getId(), null);
		db.setTransactionSuccessful();
		transactionStatus = true;
		db.endTransaction();
		return transactionStatus;

	}

	private List<KeenSession> createSessionListFromCursor(Cursor c) {
		ArrayList<KeenSession> sessions = new ArrayList<KeenSession>(c.getCount());
		if (c.getCount() > 0) {
			c.moveToFirst();
			while (!c.isAfterLast()) {
				KeenSession session = createSessionFromCursor(c);
				if (session != null) {
					sessions.add(session);
				}
				c.moveToNext();
			}
		}
		return sessions;
	}

	private KeenSession createSimpleSessionFromCursor(Cursor c) {
		KeenSession session = null;
		if (c.getPosition() >= 0) {
			session = new KeenSession();
			try {
				session.setId(c.getLong(c.getColumnIndexOrThrow(SessionTable.ID_COL_NAME)));
				session.setRemoteId(c.getLong(c.getColumnIndexOrThrow(SessionTable.REMOTE_ID_COL_NAME)));
				session.setRemoteCreateTimestamp(c.getLong(c.getColumnIndexOrThrow(SessionTable.REMOTE_CREATED_COL_NAME)));
				session.setRemoteUpdatedTimestamp(c.getLong(c.getColumnIndexOrThrow(SessionTable.REMOTE_UPDATED_COL_NAME)));
			} catch (IllegalArgumentException iax) {
				Log.e(SessionDAO.class.getSimpleName(), iax.toString());
				session = null;
			}
		}
		return session;
	}

	private KeenSession createSessionFromCursor(Cursor c) {
		KeenSession session = null;
		if (c.getPosition() >= 0) {
			session = new KeenSession();
			try {
				session.setId(c.getLong(c.getColumnIndexOrThrow(SessionTable.ID_COL_NAME)));
				session.setRemoteId(c.getLong(c.getColumnIndexOrThrow(SessionTable.REMOTE_ID_COL_NAME)));
				session.setRemoteCreateTimestamp(c.getLong(c.getColumnIndexOrThrow(SessionTable.REMOTE_CREATED_COL_NAME)));
				session.setRemoteUpdatedTimestamp(c.getLong(c.getColumnIndexOrThrow(SessionTable.REMOTE_UPDATED_COL_NAME)));
				session.setDate(new DateTime(c.getLong(c.getColumnIndexOrThrow(SessionTable.SESSION_DATE_COL_NAME))));

				KeenProgram program = new KeenProgram();
				program.setId(c.getLong(c.getColumnIndexOrThrow(SessionTable.PROGRAM_ID_COL_NAME)));
				program.setRemoteId(c.getLong(c.getColumnIndexOrThrow(ProgramTable.REMOTE_ID_COL_NAME)));
				program.setName(c.getString(c.getColumnIndexOrThrow(ProgramTable.NAME_COL_NAME)));
				program.setProgramTimes(c.getString(c.getColumnIndexOrThrow(ProgramTable.TIMES_COL_NAME)));
				String gpt = c.getString(c.getColumnIndexOrThrow(ProgramTable.TYPE_COL_NAME));
				if (gpt != null) {
					program.setGeneralProgramType((KeenProgram.GeneralProgramType.valueOf(c.getString(c
							.getColumnIndexOrThrow(ProgramTable.TYPE_COL_NAME)))));
				}
				Location location = new Location();
				location.setAddress1(c.getString(c.getColumnIndexOrThrow(ProgramTable.ADDRESS_ONE_COL_NAME)));
				location.setAddress2(c.getString(c.getColumnIndexOrThrow(ProgramTable.ADDRESS_TWO_COL_NAME)));
				location.setCity(c.getString(c.getColumnIndexOrThrow(ProgramTable.CITY_COL_NAME)));
				location.setState(c.getString(c.getColumnIndexOrThrow(ProgramTable.STATE_COL_NAME)));
				location.setZipCode(c.getString(c.getColumnIndexOrThrow(ProgramTable.ZIP_CODE_COL_NAME)));
				program.setLocation(location);
				session.setProgram(program);

				session.setNumberOfNewCoachesNeeded(c.getInt(c.getColumnIndexOrThrow(SessionTable.NUMBER_OF_NEW_COACHES_NEEDED_COL_NAME)));
				session.setNumberOfReturningCoachesNeeded(c.getInt(c.getColumnIndexOrThrow(SessionTable.NUMBER_OF_RETURNING_COACHES_NEEDED_COL_NAME)));
				boolean open = ((c.getInt(c.getColumnIndexOrThrow(SessionTable.OPEN_FOR_REGISTRATION_FLAG_COL_NAME))) == 1 ? true : false);
				session.setOpenToPublicRegistration(open);
				session.setNumberOfAthletesRegistered(c.getInt(c.getColumnIndexOrThrow(SessionTable.NUMBER_OF_ATHLETES_REGISTERED_COL_NAME)));
				session.setNumberOfAthletesCheckedIn(c.getInt(c.getColumnIndexOrThrow(SessionTable.NUMBER_OF_ATHLETES_CHECKED_IN_COL_NAME)));
				session.setNumberOfCoachesRegistered(c.getInt(c.getColumnIndexOrThrow(SessionTable.NUMBER_OF_COACHES_REGISTERED_COL_NAME)));
				session.setNumberOfCoachesCheckedIn(c.getInt(c.getColumnIndexOrThrow(SessionTable.NUMBER_OF_COACHES_CHECKED_IN_COL_NAME)));
			} catch (IllegalArgumentException iax) {
				Log.e(SessionDAO.class.getSimpleName(), iax.toString());
				session = null;
			}
		}
		return session;
	}

}
