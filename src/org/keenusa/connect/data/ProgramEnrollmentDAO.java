package org.keenusa.connect.data;

import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.data.tables.ProgramEnrollmentTable;
import org.keenusa.connect.models.KeenProgramEnrolment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ProgramEnrollmentDAO {

	private KeenConnectDB localDB;
	private ProgramDAO programDAO;
	private AthleteDAO athleteDAO;

	String[] columnNames = { ProgramEnrollmentTable.ID_COL_NAME, ProgramEnrollmentTable.REMOTE_ID_COL_NAME,
			ProgramEnrollmentTable.REMOTE_CREATED_COL_NAME, ProgramEnrollmentTable.REMOTE_UPDATED_COL_NAME,
			ProgramEnrollmentTable.PROGRAM_ID_COL_NAME, ProgramEnrollmentTable.ATHLETE_ID_COL_NAME, ProgramEnrollmentTable.WAITLIST_COL_NAME };

	public ProgramEnrollmentDAO(Context context) {
		localDB = KeenConnectDB.getKeenConnectDB(context);
		programDAO = new ProgramDAO(context);
		athleteDAO = new AthleteDAO(context);
	}

	public List<KeenProgramEnrolment> getKeenProgramEnrollments(long programId) {
		List<KeenProgramEnrolment> enrollments = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor enrollmentsCursor = db.query(ProgramEnrollmentTable.TABLE_NAME, columnNames, ProgramEnrollmentTable.PROGRAM_ID_COL_NAME + "="
				+ programId, null, null, null, null);

		if (enrollmentsCursor.getCount() > 0) {
			enrollmentsCursor.moveToFirst();
			enrollments = createProgramEnrollmentListFromCursor(enrollmentsCursor);
		} else {
			enrollments = new ArrayList<KeenProgramEnrolment>();
		}
		enrollmentsCursor.close();
		return enrollments;
	}

	public KeenProgramEnrolment getProgramEnrolmentByRemoteId(long id) {
		KeenProgramEnrolment enrolment = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor enrolmentCursor = db.query(ProgramEnrollmentTable.TABLE_NAME, columnNames, ProgramEnrollmentTable.REMOTE_ID_COL_NAME + "=" + id, null,
				null, null, null);
		if (enrolmentCursor.getCount() > 0) {
			enrolmentCursor.moveToFirst();
			enrolment = createProgramEnrollmentFromCursor(enrolmentCursor);
		}
		enrolmentCursor.close();
		return enrolment;
	}

	public KeenProgramEnrolment getProgramEnrolmentById(long id) {
		KeenProgramEnrolment enrolment = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor enrolmentCursor = db.query(ProgramEnrollmentTable.TABLE_NAME, columnNames, ProgramEnrollmentTable.ID_COL_NAME + "=" + id, null, null,
				null, null);
		if (enrolmentCursor.getCount() > 0) {
			enrolmentCursor.moveToFirst();
			enrolment = createProgramEnrollmentFromCursor(enrolmentCursor);
		}
		enrolmentCursor.close();
		return enrolment;
	}

	public long saveNewProgramEnrolment(KeenProgramEnrolment enrolment) {

		long enrolmentId = 0;
		SQLiteDatabase db = localDB.getWritableDatabase();
		KeenProgramEnrolment dbProgramEnrollment = getProgramEnrolmentByRemoteId(enrolment.getRemoteId());
		if (dbProgramEnrollment == null) {
			db.beginTransaction();
			ContentValues values = new ContentValues();

			values.put(ProgramEnrollmentTable.REMOTE_ID_COL_NAME, enrolment.getRemoteId());
			if (enrolment.getRemoteCreateTimestamp() != 0) {
				values.put(ProgramEnrollmentTable.REMOTE_CREATED_COL_NAME, enrolment.getRemoteCreateTimestamp());
			}
			if (enrolment.getRemoteUpdatedTimestamp() != 0) {
				values.put(ProgramEnrollmentTable.REMOTE_UPDATED_COL_NAME, enrolment.getRemoteUpdatedTimestamp());
			}
			if (enrolment.getProgram() != null) {
				long programId = programDAO.getProgramByRemoteId(enrolment.getProgram().getRemoteId()).getId();
				values.put(ProgramEnrollmentTable.PROGRAM_ID_COL_NAME, programId);
			}
			if (enrolment.getAthlete() != null) {
				//				long athleteId = athleteDAO.getProgramByRemoteId(enrolment.getProgram().getRemoteId()).getId();
				//				values.put(ProgramEnrollmentTable.START_DATE_COL_NAME, program.getActiveFromDate().getMillis());
			}

			values.put(ProgramEnrollmentTable.WAITLIST_COL_NAME, (enrolment.isInWaitlist() ? 1 : 0));

			enrolmentId = db.insert(ProgramEnrollmentTable.TABLE_NAME, null, values);
			db.setTransactionSuccessful();
			db.endTransaction();
		} else if (dbProgramEnrollment.getRemoteUpdatedTimestamp() < dbProgramEnrollment.getRemoteUpdatedTimestamp()) {
			// more recent version of the session
			enrolment.setId(dbProgramEnrollment.getId());
			//			updateProgramEnrollement(enrolment);
		}
		return enrolmentId;
	}

	//
	//	private boolean updateProgram(KeenProgram program) {
	//		boolean transactionStatus = false;
	//		SQLiteDatabase db = localDB.getWritableDatabase();
	//		db.beginTransaction();
	//		ContentValues values = new ContentValues();
	//		values.put(ProgramTable.REMOTE_CREATED_COL_NAME, program.getRemoteCreateTimestamp());
	//		values.put(ProgramTable.REMOTE_UPDATED_COL_NAME, program.getRemoteUpdatedTimestamp());
	//		values.put(ProgramTable.START_DATE_COL_NAME, program.getActiveFromDate().getMillis());
	//		values.put(ProgramTable.END_DATE_COL_NAME, program.getActiveToDate().getMillis());
	//		values.put(ProgramTable.NAME_COL_NAME, program.getName());
	//		values.put(ProgramTable.TYPE_COL_NAME, program.getGeneralProgramType().toString());
	//		values.put(ProgramTable.TIMES_COL_NAME, program.getProgramTimes());
	//		values.put(ProgramTable.REGISTRATION_EMAL_TEXT_COL_NAME, program.getCoachRegistrationConfirmationEmailText());
	//		db.update(ProgramTable.TABLE_NAME, values, ProgramTable.ID_COL_NAME + "=" + program.getId(), null);
	//		if (program.getLocation() != null) {
	//			values.put(ProgramTable.ADDRESS_ONE_COL_NAME, program.getLocation().getAddress1());
	//			values.put(ProgramTable.ADDRESS_TWO_COL_NAME, program.getLocation().getAddress2());
	//			values.put(ProgramTable.CITY_COL_NAME, program.getLocation().getCity());
	//			values.put(ProgramTable.STATE_COL_NAME, program.getLocation().getState());
	//			values.put(ProgramTable.ZIP_CODE_COL_NAME, program.getLocation().getZipCode());
	//		}
	//		db.setTransactionSuccessful();
	//		transactionStatus = true;
	//		db.endTransaction();
	//		return transactionStatus;
	//
	//	}

	private List<KeenProgramEnrolment> createProgramEnrollmentListFromCursor(Cursor c) {
		ArrayList<KeenProgramEnrolment> enrollments = new ArrayList<KeenProgramEnrolment>(c.getCount());
		if (c.getCount() > 0) {
			c.moveToFirst();
			while (!c.isAfterLast()) {
				KeenProgramEnrolment enrollment = createProgramEnrollmentFromCursor(c);
				if (enrollment != null) {
					enrollments.add(enrollment);
				}
				c.moveToNext();
			}

		}
		return enrollments;
	}

	private KeenProgramEnrolment createProgramEnrollmentFromCursor(Cursor c) {
		KeenProgramEnrolment enrollment = null;
		if (c.getPosition() >= 0) {
			enrollment = new KeenProgramEnrolment();
			try {
				enrollment.setId(c.getLong(c.getColumnIndexOrThrow(ProgramEnrollmentTable.ID_COL_NAME)));
				enrollment.setRemoteId(c.getLong(c.getColumnIndexOrThrow(ProgramEnrollmentTable.REMOTE_ID_COL_NAME)));
				enrollment.setRemoteCreateTimestamp(c.getLong(c.getColumnIndexOrThrow(ProgramEnrollmentTable.REMOTE_CREATED_COL_NAME)));
				enrollment.setRemoteUpdatedTimestamp(c.getLong(c.getColumnIndexOrThrow(ProgramEnrollmentTable.REMOTE_UPDATED_COL_NAME)));
				//				enrollment.setProgram(c.getLong(c.getColumnIndexOrThrow(ProgramEnrollmentTable.REMOTE_UPDATED_COL_NAME)));

			} catch (IllegalArgumentException iax) {
				enrollment = null;
			}
		}
		return enrollment;
	}

}
