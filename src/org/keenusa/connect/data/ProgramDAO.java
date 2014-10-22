package org.keenusa.connect.data;

import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.data.tables.ProgramTable;
import org.keenusa.connect.models.KeenProgram;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ProgramDAO {

	private KeenConnectDB localDB = null;
	String[] columnNames = { ProgramTable.ID_COL_NAME, ProgramTable.REMOTE_ID_COL_NAME, ProgramTable.REMOTE_CREATED_COL_NAME,
			ProgramTable.REMOTE_UPDATED_COL_NAME, ProgramTable.LOCATION_ID_COL_NAME, ProgramTable.START_DATE_COL_NAME,
			ProgramTable.END_DATE_COL_NAME, ProgramTable.NAME_COL_NAME, ProgramTable.TYPE_COL_NAME, ProgramTable.TIMES_COL_NAME,
			ProgramTable.REGISTRATION_EMAL_TEXT_COL_NAME };

	public ProgramDAO(Context context) {
		localDB = KeenConnectDB.getKeenConnectDB(context);
	}

	public List<KeenProgram> getKeenProgramList() {
		List<KeenProgram> allPrograms = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor programsCursor = db.query(ProgramTable.TABLE_NAME, columnNames, null, null, null, null, null);

		if (programsCursor.getCount() > 0) {
			programsCursor.moveToFirst();
			allPrograms = createProgramListFromCursor(programsCursor);
		} else {
			allPrograms = new ArrayList<KeenProgram>();
		}
		programsCursor.close();
		return allPrograms;
	}

	public KeenProgram getProgramByRemoteId(long id) {
		KeenProgram program = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor programCursor = db.query(ProgramTable.TABLE_NAME, columnNames, ProgramTable.REMOTE_ID_COL_NAME + "=" + id, null, null, null, null);
		if (programCursor.getCount() > 0) {
			programCursor.moveToFirst();
			program = createProgramFromCursor(programCursor);
		}
		programCursor.close();
		return program;
	}

	public KeenProgram getProgramById(long id) {
		KeenProgram program = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor programCursor = db.query(ProgramTable.TABLE_NAME, columnNames, ProgramTable.ID_COL_NAME + "=" + id, null, null, null, null);
		if (programCursor.getCount() > 0) {
			programCursor.moveToFirst();
			program = createProgramFromCursor(programCursor);
		}
		programCursor.close();
		return program;
	}

	public long saveNewProgram(KeenProgram program) {

		long programId = 0;
		SQLiteDatabase db = localDB.getWritableDatabase();
		KeenProgram dbProgram = getProgramByRemoteId(program.getRemoteId());
		if (dbProgram == null) {
			db.beginTransaction();
			ContentValues values = new ContentValues();

			values.put(ProgramTable.REMOTE_ID_COL_NAME, program.getRemoteId());
			if (program.getRemoteCreateTimestamp() != 0) {
				values.put(ProgramTable.REMOTE_CREATED_COL_NAME, program.getRemoteCreateTimestamp());
			}
			if (program.getRemoteUpdatedTimestamp() != 0) {
				values.put(ProgramTable.REMOTE_UPDATED_COL_NAME, program.getRemoteUpdatedTimestamp());
			}
			if (program.getLocation() != null) {
				values.put(ProgramTable.LOCATION_ID_COL_NAME, program.getLocation().getId());
			}
			if (program.getActiveFromDate() != null) {
				values.put(ProgramTable.START_DATE_COL_NAME, program.getActiveFromDate().getMillis());
			}
			if (program.getActiveToDate() != null) {
				values.put(ProgramTable.END_DATE_COL_NAME, program.getActiveToDate().getMillis());
			}
			if (program.getName() != null) {
				values.put(ProgramTable.NAME_COL_NAME, program.getName());
			}
			if (program.getGeneralProgramType() != null) {
				values.put(ProgramTable.TYPE_COL_NAME, program.getGeneralProgramType().toString());
			}
			if (program.getProgramTimes() != null) {
				values.put(ProgramTable.TIMES_COL_NAME, program.getProgramTimes());
			}
			if (program.getCoachRegistrationConfirmationEmailText() != null) {
				values.put(ProgramTable.REGISTRATION_EMAL_TEXT_COL_NAME, program.getCoachRegistrationConfirmationEmailText());
			}
			programId = db.insert(ProgramTable.TABLE_NAME, null, values);
			db.setTransactionSuccessful();
			db.endTransaction();
		} else if (dbProgram.getRemoteUpdatedTimestamp() < dbProgram.getRemoteUpdatedTimestamp()) {
			// more recent version of the session
			program.setId(dbProgram.getId());
			updateProgram(program);
		}
		return programId;
	}

	private boolean updateProgram(KeenProgram program) {
		boolean transactionStatus = false;
		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		ContentValues values = new ContentValues();
		values.put(ProgramTable.REMOTE_CREATED_COL_NAME, program.getRemoteCreateTimestamp());
		values.put(ProgramTable.REMOTE_UPDATED_COL_NAME, program.getRemoteUpdatedTimestamp());
		//			values.put(ProgramTable.LOCATION_ID_COL_NAME, program.getDate().getMillis());
		values.put(ProgramTable.START_DATE_COL_NAME, program.getActiveFromDate().getMillis());
		values.put(ProgramTable.END_DATE_COL_NAME, program.getActiveToDate().getMillis());
		values.put(ProgramTable.NAME_COL_NAME, program.getName());
		values.put(ProgramTable.TYPE_COL_NAME, program.getGeneralProgramType().toString());
		values.put(ProgramTable.TIMES_COL_NAME, program.getProgramTimes());
		values.put(ProgramTable.REGISTRATION_EMAL_TEXT_COL_NAME, program.getCoachRegistrationConfirmationEmailText());
		db.update(ProgramTable.TABLE_NAME, values, ProgramTable.ID_COL_NAME + "=" + program.getId(), null);
		db.setTransactionSuccessful();
		transactionStatus = true;
		db.endTransaction();
		return transactionStatus;

	}

	private List<KeenProgram> createProgramListFromCursor(Cursor c) {
		ArrayList<KeenProgram> programs = new ArrayList<KeenProgram>(c.getCount());
		if (c.getCount() > 0) {
			c.moveToFirst();
			while (!c.isAfterLast()) {
				KeenProgram program = createProgramFromCursor(c);
				if (program != null) {
					programs.add(program);
				}
				c.moveToNext();
			}

		}
		return programs;
	}

	private KeenProgram createProgramFromCursor(Cursor c) {
		KeenProgram program = null;
		if (c.getPosition() >= 0) {
			program = new KeenProgram();
			try {
				program.setId(c.getLong(c.getColumnIndexOrThrow(ProgramTable.ID_COL_NAME)));
				program.setRemoteId(c.getLong(c.getColumnIndexOrThrow(ProgramTable.REMOTE_ID_COL_NAME)));
				//				program.setRemoteCreateTimestamp(c.getLong(c.getColumnIndexOrThrow(ProgramTable.REMOTE_CREATED_COL_NAME)));
				//				program.setRemoteUpdatedTimestamp(c.getLong(c.getColumnIndexOrThrow(ProgramTable.REMOTE_UPDATED_COL_NAME)));
				//				//				program.setLocation(location);
				//				program.setActiveFromDate(new DateTime(c.getLong(c.getColumnIndexOrThrow(ProgramTable.START_DATE_COL_NAME))));
				//				program.setActiveToDate(new DateTime(c.getLong(c.getColumnIndexOrThrow(ProgramTable.END_DATE_COL_NAME))));
				//				program.setName(c.getString(c.getColumnIndexOrThrow(ProgramTable.NAME_COL_NAME)));
				//				program.setGeneralProgramType((KeenProgram.GeneralProgramType.valueOf(c.getString(c.getColumnIndexOrThrow(ProgramTable.TYPE_COL_NAME)))));
				//				program.setProgramTimes(c.getString(c.getColumnIndexOrThrow(ProgramTable.TIMES_COL_NAME)));
				//				program.setCoachRegistrationConfirmationEmailText(c.getString(c.getColumnIndexOrThrow(ProgramTable.REGISTRATION_EMAL_TEXT_COL_NAME)));

			} catch (IllegalArgumentException iax) {
				program = null;
			}
		}
		return program;
	}

}
