package org.keenusa.connect.data.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.keenusa.connect.data.KeenConnectDB;
import org.keenusa.connect.data.tables.AthleteTable;
import org.keenusa.connect.data.tables.ProgramEnrollmentTable;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.ContactPerson;
import org.keenusa.connect.models.KeenProgram;
import org.keenusa.connect.models.KeenProgramEnrolment;
import org.keenusa.connect.models.Location;
import org.keenusa.connect.models.Parent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

public class ProgramEnrollmentDAO {

	private KeenConnectDB localDB;
	private ProgramDAO programDAO;
	private AthleteDAO athleteDAO;

	private HashMap<Long, Long> programMap = new HashMap<Long, Long>();
	private HashMap<Long, Long> athleteMap = new HashMap<Long, Long>();

	private static String JOINED_TABLES_STRING = ProgramEnrollmentTable.TABLE_NAME + " INNER JOIN " + AthleteTable.TABLE_NAME + " ON "
			+ ProgramEnrollmentTable.TABLE_NAME + "." + ProgramEnrollmentTable.ATHLETE_ID_COL_NAME + "=" + AthleteTable.TABLE_NAME + "."
			+ AthleteTable.ID_COL_NAME;

	String[] simpleColumnNames = { ProgramEnrollmentTable.ID_COL_NAME, ProgramEnrollmentTable.REMOTE_ID_COL_NAME,
			ProgramEnrollmentTable.REMOTE_CREATED_COL_NAME, ProgramEnrollmentTable.REMOTE_UPDATED_COL_NAME };

	String[] columnNames = { ProgramEnrollmentTable.TABLE_NAME + "." + ProgramEnrollmentTable.ID_COL_NAME, ProgramEnrollmentTable.REMOTE_ID_COL_NAME,
			ProgramEnrollmentTable.REMOTE_CREATED_COL_NAME, ProgramEnrollmentTable.REMOTE_UPDATED_COL_NAME,
			ProgramEnrollmentTable.PROGRAM_ID_COL_NAME, ProgramEnrollmentTable.ATHLETE_ID_COL_NAME, ProgramEnrollmentTable.WAITLIST_COL_NAME,
			AthleteTable.REMOTE_ID_COL_NAME, AthleteTable.REMOTE_CREATED_COL_NAME, AthleteTable.REMOTE_UPDATED_COL_NAME,
			AthleteTable.FIRST_NAME_COL_NAME, AthleteTable.MIDDLE_NAME_COL_NAME, AthleteTable.LAST_NAME_COL_NAME, AthleteTable.EMAIL_COL_NAME,
			AthleteTable.PHONE_COL_NAME, AthleteTable.GENDER_COL_NAME, AthleteTable.NICKNAME_COL_NAME, AthleteTable.DOB_COL_NAME,
			AthleteTable.PRIMLANGUAGE_COL_NAME, AthleteTable.ACTIVE_COL_NAME, AthleteTable.PARENT_MOBILE_COL_NAME,
			AthleteTable.PARENT_EMAIL_COL_NAME, AthleteTable.PARENT_PHONE_COL_NAME, AthleteTable.PARENT_RELATIONSHIP_COL_NAME,
			AthleteTable.PARENT_FIRST_NAME_COL_NAME, AthleteTable.PARENT_LAST_NAME_COL_NAME, AthleteTable.CITY_COL_NAME, AthleteTable.STATE_COL_NAME,
			AthleteTable.ZIPCODE_COL_NAME };

	public ProgramEnrollmentDAO(Context context) {
		localDB = KeenConnectDB.getKeenConnectDB(context);
		programDAO = new ProgramDAO(context);
		athleteDAO = new AthleteDAO(context);
	}

	public KeenProgramEnrolment getSimpleProgramEnrolmentByRemoteId(long id) {
		KeenProgramEnrolment enrolment = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor enrolmentCursor = db.query(ProgramEnrollmentTable.TABLE_NAME, simpleColumnNames, ProgramEnrollmentTable.REMOTE_ID_COL_NAME + "=" + id,
				null, null, null, null);
		if (enrolmentCursor.getCount() > 0) {
			enrolmentCursor.moveToFirst();
			enrolment = createSimpleProgramEnrolmentFromCursor(enrolmentCursor);
		}
		enrolmentCursor.close();
		return enrolment;
	}

	public List<KeenProgramEnrolment> getKeenProgramEnrollments(long programId) {
		List<KeenProgramEnrolment> enrolments = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		sqlBuilder.setTables(JOINED_TABLES_STRING);
		Cursor enrollmentsCursor = sqlBuilder.query(db, columnNames, ProgramEnrollmentTable.PROGRAM_ID_COL_NAME + "=" + programId, null, null, null,
				AthleteTable.FIRST_NAME_COL_NAME + " DESC", null);
		if (enrollmentsCursor.getCount() > 0) {
			enrollmentsCursor.moveToFirst();
			enrolments = createProgramEnrollmentListFromCursor(enrollmentsCursor);
		} else {
			enrolments = new ArrayList<KeenProgramEnrolment>();
		}
		enrollmentsCursor.close();
		return enrolments;
	}

	public KeenProgramEnrolment getProgramEnrolmentById(long id) {
		KeenProgramEnrolment enrolment = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		sqlBuilder.setTables(JOINED_TABLES_STRING);
		Cursor enrolmentCursor = sqlBuilder.query(db, columnNames, ProgramEnrollmentTable.ID_COL_NAME + "=" + id, null, null, null, null, null);
		if (enrolmentCursor.getCount() > 0) {
			enrolmentCursor.moveToFirst();
			enrolment = createProgramEnrollmentFromCursor(enrolmentCursor);
		}
		enrolmentCursor.close();
		return enrolment;
	}

	public long saveNewProgramEnrolment(KeenProgramEnrolment enrolment) {
		long enrolmentId = 0;
		KeenProgramEnrolment dbProgramEnrollment = getSimpleProgramEnrolmentByRemoteId(enrolment.getRemoteId());
		if (dbProgramEnrollment == null) {
			ContentValues values = new ContentValues();
			values.put(ProgramEnrollmentTable.REMOTE_ID_COL_NAME, enrolment.getRemoteId());
			values.put(ProgramEnrollmentTable.REMOTE_CREATED_COL_NAME, enrolment.getRemoteCreateTimestamp());
			values.put(ProgramEnrollmentTable.REMOTE_UPDATED_COL_NAME, enrolment.getRemoteUpdatedTimestamp());
			if (enrolment.getProgram() != null) {
				long programId = 0;
				if (programMap.containsKey(enrolment.getProgram().getRemoteId())) {
					programId = programMap.get(enrolment.getProgram().getRemoteId());
				} else {
					KeenProgram program = programDAO.getSimpleProgramByRemoteId(enrolment.getProgram().getRemoteId());
					if (program != null) {
						programId = program.getId();
						programMap.put(enrolment.getProgram().getRemoteId(), programId);
					}
				}
				values.put(ProgramEnrollmentTable.PROGRAM_ID_COL_NAME, programId);
			}
			if (enrolment.getAthlete() != null) {
				long athleteId = 0;
				if (athleteMap.containsKey(enrolment.getAthlete().getRemoteId())) {
					athleteId = athleteMap.get(enrolment.getAthlete().getRemoteId());
				} else {
					Athlete athlete = athleteDAO.getSimpleAthleteByRemoteId(enrolment.getAthlete().getRemoteId());
					if (athlete != null) {
						athleteId = athlete.getId();
						athleteMap.put(enrolment.getAthlete().getRemoteId(), athleteId);
					}
				}
				values.put(ProgramEnrollmentTable.ATHLETE_ID_COL_NAME, athleteId);
			}
			values.put(ProgramEnrollmentTable.WAITLIST_COL_NAME, (enrolment.isInWaitlist() ? 1 : 0));

			SQLiteDatabase db = localDB.getWritableDatabase();
			db.beginTransaction();
			enrolmentId = db.insert(ProgramEnrollmentTable.TABLE_NAME, null, values);
			db.setTransactionSuccessful();
			db.endTransaction();
		} else if (dbProgramEnrollment.getRemoteUpdatedTimestamp() < enrolment.getRemoteUpdatedTimestamp()) {
			// more recent version of the enrolment
			enrolment.setId(dbProgramEnrollment.getId());
			updateProgramEnrollement(enrolment);
		}
		return enrolmentId;
	}

	public boolean saveNewProgramEnrolmentList(List<KeenProgramEnrolment> enrolments) {
		boolean areSaved = false;
		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		ContentValues values = new ContentValues();
		for (KeenProgramEnrolment enrolment : enrolments) {
			KeenProgramEnrolment dbProgramEnrollment = getSimpleProgramEnrolmentByRemoteId(enrolment.getRemoteId());
			if (dbProgramEnrollment == null) {
				values.clear();
				values.put(ProgramEnrollmentTable.REMOTE_ID_COL_NAME, enrolment.getRemoteId());
				values.put(ProgramEnrollmentTable.REMOTE_CREATED_COL_NAME, enrolment.getRemoteCreateTimestamp());
				values.put(ProgramEnrollmentTable.REMOTE_UPDATED_COL_NAME, enrolment.getRemoteUpdatedTimestamp());
				if (enrolment.getProgram() != null) {
					long programId = 0;
					if (programMap.containsKey(enrolment.getProgram().getRemoteId())) {
						programId = programMap.get(enrolment.getProgram().getRemoteId());
					} else {
						KeenProgram program = programDAO.getSimpleProgramByRemoteId(enrolment.getProgram().getRemoteId());
						if (program != null) {
							programId = program.getId();
							programMap.put(enrolment.getProgram().getRemoteId(), programId);
						}
					}
					values.put(ProgramEnrollmentTable.PROGRAM_ID_COL_NAME, programId);
				}
				if (enrolment.getAthlete() != null) {
					long athleteId = 0;
					if (athleteMap.containsKey(enrolment.getAthlete().getRemoteId())) {
						athleteId = athleteMap.get(enrolment.getAthlete().getRemoteId());
					} else {
						Athlete athlete = athleteDAO.getSimpleAthleteByRemoteId(enrolment.getAthlete().getRemoteId());
						if (athlete != null) {
							athleteId = athlete.getId();
							athleteMap.put(enrolment.getAthlete().getRemoteId(), athleteId);
						}
					}
					values.put(ProgramEnrollmentTable.ATHLETE_ID_COL_NAME, athleteId);
				}
				values.put(ProgramEnrollmentTable.WAITLIST_COL_NAME, (enrolment.isInWaitlist() ? 1 : 0));
				db.insert(ProgramEnrollmentTable.TABLE_NAME, null, values);

			}
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		areSaved = true;
		return areSaved;
	}

	private boolean updateProgramEnrollement(KeenProgramEnrolment enrolment) {
		boolean transactionStatus = false;

		ContentValues values = new ContentValues();
		values.put(ProgramEnrollmentTable.REMOTE_CREATED_COL_NAME, enrolment.getRemoteCreateTimestamp());
		values.put(ProgramEnrollmentTable.REMOTE_UPDATED_COL_NAME, enrolment.getRemoteUpdatedTimestamp());
		if (enrolment.getProgram() != null) {
			long programId = 0;
			if (programMap.containsKey(enrolment.getProgram().getRemoteId())) {
				programId = programMap.get(enrolment.getProgram().getRemoteId());
			} else {
				KeenProgram program = programDAO.getSimpleProgramByRemoteId(enrolment.getProgram().getRemoteId());
				if (program != null) {
					programId = program.getId();
					programMap.put(enrolment.getProgram().getRemoteId(), programId);
				}
			}
			values.put(ProgramEnrollmentTable.PROGRAM_ID_COL_NAME, programId);
		}
		if (enrolment.getAthlete() != null) {
			long athleteId = 0;
			if (athleteMap.containsKey(enrolment.getAthlete().getRemoteId())) {
				athleteId = athleteMap.get(enrolment.getAthlete().getRemoteId());
			} else {
				Athlete athlete = athleteDAO.getSimpleAthleteByRemoteId(enrolment.getAthlete().getRemoteId());
				if (athlete != null) {
					athleteId = athlete.getId();
					athleteMap.put(enrolment.getAthlete().getRemoteId(), athleteId);
				}
			}
			values.put(ProgramEnrollmentTable.ATHLETE_ID_COL_NAME, athleteId);
		}
		values.put(ProgramEnrollmentTable.WAITLIST_COL_NAME, (enrolment.isInWaitlist() ? 1 : 0));

		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		db.update(ProgramEnrollmentTable.TABLE_NAME, values, ProgramEnrollmentTable.ID_COL_NAME + "=" + enrolment.getId(), null);
		db.setTransactionSuccessful();
		transactionStatus = true;
		db.endTransaction();
		return transactionStatus;

	}

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
				KeenProgram program = new KeenProgram();
				program.setId(c.getLong(c.getColumnIndexOrThrow(ProgramEnrollmentTable.PROGRAM_ID_COL_NAME)));
				enrollment.setProgram(program);
				Athlete athlete = new Athlete();
				athlete.setId(c.getLong(c.getColumnIndexOrThrow(ProgramEnrollmentTable.ATHLETE_ID_COL_NAME)));
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
				enrollment.setAthlete(athlete);
			} catch (IllegalArgumentException iax) {
				enrollment = null;
			}
		}
		return enrollment;
	}

	private KeenProgramEnrolment createSimpleProgramEnrolmentFromCursor(Cursor c) {
		KeenProgramEnrolment enrolment = null;
		if (c.getPosition() >= 0) {
			enrolment = new KeenProgramEnrolment();
			try {
				enrolment.setId(c.getLong(c.getColumnIndexOrThrow(ProgramEnrollmentTable.ID_COL_NAME)));
				enrolment.setRemoteId(c.getLong(c.getColumnIndexOrThrow(ProgramEnrollmentTable.REMOTE_ID_COL_NAME)));
				enrolment.setRemoteCreateTimestamp(c.getLong(c.getColumnIndexOrThrow(ProgramEnrollmentTable.REMOTE_CREATED_COL_NAME)));
				enrolment.setRemoteUpdatedTimestamp(c.getLong(c.getColumnIndexOrThrow(ProgramEnrollmentTable.REMOTE_UPDATED_COL_NAME)));

			} catch (IllegalArgumentException iax) {
				enrolment = null;
			}
		}
		return enrolment;
	}

}
