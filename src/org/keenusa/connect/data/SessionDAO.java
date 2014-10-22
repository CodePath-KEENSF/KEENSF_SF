package org.keenusa.connect.data;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.keenusa.connect.data.tables.SessionTable;
import org.keenusa.connect.models.KeenProgram;
import org.keenusa.connect.models.KeenSession;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SessionDAO {

	private KeenConnectDB localDB;
	ProgramDAO programDAO;
	String[] columnNames = { SessionTable.ID_COL_NAME, SessionTable.REMOTE_ID_COL_NAME, SessionTable.REMOTE_CREATED_COL_NAME,
			SessionTable.REMOTE_UPDATED_COL_NAME, SessionTable.SESSION_DATE_COL_NAME, SessionTable.PROGRAM_ID_COL_NAME,
			SessionTable.NUMBER_OF_NEW_COACHES_NEEDED_COL_NAME, SessionTable.NUMBER_OF_RETURNING_COACHES_NEEDED_COL_NAME,
			SessionTable.OPEN_FOR_REGISTRATION_FLAG_COL_NAME };

	public SessionDAO(Context context) {
		localDB = KeenConnectDB.getKeenConnectDB(context);
		programDAO = new ProgramDAO(context);
	}

	public List<KeenSession> getKeenSessionList() {
		List<KeenSession> allSessions = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor sessionsCursor = db.query(SessionTable.TABLE_NAME, columnNames, null, null, null, null, null);

		if (sessionsCursor.getCount() > 0) {
			sessionsCursor.moveToFirst();
			allSessions = createSessionListFromCursor(sessionsCursor);
		} else {
			allSessions = new ArrayList<KeenSession>();
		}
		sessionsCursor.close();
		return allSessions;
	}

	public KeenSession getSessionByRemoteId(long id) {
		KeenSession session = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor sessionCursor = db.query(SessionTable.TABLE_NAME, columnNames, SessionTable.REMOTE_ID_COL_NAME + "=" + id, null, null, null, null);
		if (sessionCursor.getCount() > 0) {
			sessionCursor.moveToFirst();
			session = createSessionFromCursor(sessionCursor);
		}
		sessionCursor.close();
		return session;
	}

	public KeenSession getSessionById(long id) {
		KeenSession session = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor sessionCursor = db.query(SessionTable.TABLE_NAME, columnNames, SessionTable.ID_COL_NAME + "=" + id, null, null, null, null);
		if (sessionCursor.getCount() > 0) {
			sessionCursor.moveToFirst();
			session = createSessionFromCursor(sessionCursor);
		}
		sessionCursor.close();
		return session;
	}

	public long saveNewSession(KeenSession session) {

		long sessionId = 0;
		SQLiteDatabase db = localDB.getWritableDatabase();
		KeenSession dbSession = getSessionByRemoteId(session.getRemoteId());
		if (dbSession == null) {
			db.beginTransaction();
			ContentValues values = new ContentValues();
			values.put(SessionTable.REMOTE_ID_COL_NAME, session.getRemoteId());
			values.put(SessionTable.REMOTE_CREATED_COL_NAME, session.getRemoteCreateTimestamp());
			values.put(SessionTable.REMOTE_UPDATED_COL_NAME, session.getRemoteUpdatedTimestamp());
			values.put(SessionTable.SESSION_DATE_COL_NAME, session.getDate().getMillis());
			KeenProgram p = programDAO.getProgramByRemoteId(session.getProgram().getRemoteId());
			long programId = 0;
			if (p != null) {
				programId = p.getId();
			} else {
				programId = programDAO.saveNewProgram(session.getProgram());
			}
			values.put(SessionTable.PROGRAM_ID_COL_NAME, programId);
			values.put(SessionTable.NUMBER_OF_NEW_COACHES_NEEDED_COL_NAME, session.getNumberOfNewCoachesNeeded());
			values.put(SessionTable.NUMBER_OF_RETURNING_COACHES_NEEDED_COL_NAME, session.getNumberOfReturningCoachesNeeded());
			values.put(SessionTable.OPEN_FOR_REGISTRATION_FLAG_COL_NAME, (session.isOpenToPublicRegistration() ? 1 : 0));
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

	//	public long saveNewEmptySession(KeenSession session) {
	//
	//		long sessionId = 0;
	//		SQLiteDatabase db = localDB.getWritableDatabase();
	//		KeenSession dbSession = getSessionByRemoteId(session.getRemoteId());
	//		if (dbSession == null) {
	//			db.beginTransaction();
	//			ContentValues values = new ContentValues();
	//			values.put(SessionTable.REMOTE_ID_COL_NAME, session.getRemoteId());
	//			values.put(SessionTable.REMOTE_CREATED_COL_NAME, session.getRemoteCreateTimestamp());
	//			values.put(SessionTable.REMOTE_UPDATED_COL_NAME, session.getRemoteUpdatedTimestamp());
	//			values.put(SessionTable.SESSION_DATE_COL_NAME, session.getDate().getMillis());
	//			KeenProgram p = programDAO.getProgramByRemoteId(session.getProgram().getRemoteId());
	//			long programId = 0;
	//			if (p != null) {
	//				programId = p.getId();
	//			} else {
	//				programId = programDAO.saveNewProgram(session.getProgram());
	//			}
	//			values.put(SessionTable.PROGRAM_ID_COL_NAME, programId);
	//			values.put(SessionTable.NUMBER_OF_NEW_COACHES_NEEDED_COL_NAME, session.getNumberOfNewCoachesNeeded());
	//			values.put(SessionTable.NUMBER_OF_RETURNING_COACHES_NEEDED_COL_NAME, session.getNumberOfReturningCoachesNeeded());
	//			values.put(SessionTable.OPEN_FOR_REGISTRATION_FLAG_COL_NAME, (session.isOpenToPublicRegistration() ? 1 : 0));
	//			sessionId = db.insert(SessionTable.TABLE_NAME, null, values);
	//			db.setTransactionSuccessful();
	//			db.endTransaction();
	//		} else if (dbSession.getRemoteUpdatedTimestamp() < session.getRemoteUpdatedTimestamp()) {
	//			// more recent version of the session
	//			session.setId(dbSession.getId());
	//			updateSession(session);
	//		}
	//		return sessionId;
	//	}

	private boolean updateSession(KeenSession session) {

		boolean transactionStatus = false;
		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		ContentValues values = new ContentValues();
		values.put(SessionTable.REMOTE_CREATED_COL_NAME, session.getRemoteCreateTimestamp());
		values.put(SessionTable.REMOTE_UPDATED_COL_NAME, session.getRemoteUpdatedTimestamp());
		values.put(SessionTable.SESSION_DATE_COL_NAME, session.getDate().getMillis());
		long programId = (programDAO.getProgramByRemoteId(session.getProgram().getRemoteId())).getId();
		if (programId == 0) {
			programId = programDAO.saveNewProgram(session.getProgram());
		}
		values.put(SessionTable.PROGRAM_ID_COL_NAME, programId);
		values.put(SessionTable.NUMBER_OF_NEW_COACHES_NEEDED_COL_NAME, session.getNumberOfNewCoachesNeeded());
		values.put(SessionTable.NUMBER_OF_RETURNING_COACHES_NEEDED_COL_NAME, session.getNumberOfReturningCoachesNeeded());
		values.put(SessionTable.OPEN_FOR_REGISTRATION_FLAG_COL_NAME, (session.isOpenToPublicRegistration() ? 1 : 0));

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
				// TODO find program by remoteId if not found create one
				//		values.put(SessionTable.SESSION_DATE_COL_NAME, session.getProgram().getId());
				session.setNumberOfNewCoachesNeeded(c.getInt(c.getColumnIndexOrThrow(SessionTable.NUMBER_OF_NEW_COACHES_NEEDED_COL_NAME)));
				session.setNumberOfReturningCoachesNeeded(c.getInt(c.getColumnIndexOrThrow(SessionTable.NUMBER_OF_RETURNING_COACHES_NEEDED_COL_NAME)));
				boolean open = ((c.getInt(c.getColumnIndexOrThrow(SessionTable.OPEN_FOR_REGISTRATION_FLAG_COL_NAME))) == 1 ? true : false);
				session.setOpenToPublicRegistration(open);

			} catch (IllegalArgumentException iax) {
				session = null;
			}
		}
		return session;
	}

}
