package org.keenusa.connect.data.daos;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.keenusa.connect.data.KeenConnectDB;
import org.keenusa.connect.data.tables.CoachTable;
import org.keenusa.connect.models.Coach;
import org.keenusa.connect.models.ContactPerson;
import org.keenusa.connect.models.Location;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoachDAO {

	private KeenConnectDB localDB;

	String[] simpleColumnNames = { CoachTable.ID_COL_NAME, CoachTable.REMOTE_ID_COL_NAME, CoachTable.REMOTE_CREATED_COL_NAME,
			CoachTable.REMOTE_UPDATED_COL_NAME };

	String[] columnNames = { CoachTable.ID_COL_NAME, CoachTable.REMOTE_ID_COL_NAME, CoachTable.REMOTE_CREATED_COL_NAME,
			CoachTable.REMOTE_UPDATED_COL_NAME, CoachTable.FIRST_NAME_COL_NAME, CoachTable.MIDDLE_NAME_COL_NAME, CoachTable.LAST_NAME_COL_NAME,
			CoachTable.EMAIL_COL_NAME, CoachTable.PHONE_COL_NAME, CoachTable.MOBILE_COL_NAME, CoachTable.GENDER_COL_NAME, CoachTable.DOB_COL_NAME,
			CoachTable.LANGUAGES_COL_NAME, CoachTable.SKILLS_COL_NAME, CoachTable.ACTIVE_COL_NAME, CoachTable.CITY_COL_NAME,
			CoachTable.STATE_COL_NAME, CoachTable.ZIPCODE_COL_NAME, CoachTable.NUM_SESSIONS_ATTENDED_COL_NAME, CoachTable.LAST_ATTENDED_DATE_COL_NAME };

	public CoachDAO(Context context) {
		localDB = KeenConnectDB.getKeenConnectDB(context);
	}

	public Coach getSimpleCoachByRemoteId(long id) {
		Coach coach = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor coachCursor = db.query(CoachTable.TABLE_NAME, simpleColumnNames, CoachTable.REMOTE_ID_COL_NAME + "=" + id, null, null, null, null);
		if (coachCursor.getCount() > 0) {
			coachCursor.moveToFirst();
			coach = createSimpleCoachFromCursor(coachCursor);
		}
		coachCursor.close();
		return coach;
	}

	public List<Coach> getCoachList() {
		List<Coach> allCoaches = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor coachsCursor = db.query(CoachTable.TABLE_NAME, columnNames, null, null, null, null, CoachTable.FIRST_NAME_COL_NAME + " ASC", null);
		if (coachsCursor.getCount() > 0) {
			coachsCursor.moveToFirst();
			allCoaches = createCoachListFromCursor(coachsCursor);
		} else {
			allCoaches = new ArrayList<Coach>();
		}
		coachsCursor.close();
		return allCoaches;
	}

	public Coach getCoachById(long id) {
		Coach coach = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor coachCursor = db.query(CoachTable.TABLE_NAME, columnNames, CoachTable.ID_COL_NAME + "=" + id, null, null, null, null);
		if (coachCursor.getCount() > 0) {
			coachCursor.moveToFirst();
			coach = createCoachFromCursor(coachCursor);
		}
		coachCursor.close();
		return coach;
	}

	public long saveNewCoach(Coach coach) {
		long coachId = 0;
		Coach dbCoach = getSimpleCoachByRemoteId(coach.getRemoteId());
		if (dbCoach == null) {
			ContentValues values = new ContentValues();
			values.put(CoachTable.REMOTE_ID_COL_NAME, coach.getRemoteId());
			values.put(CoachTable.REMOTE_CREATED_COL_NAME, coach.getRemoteCreateTimestamp());
			values.put(CoachTable.REMOTE_UPDATED_COL_NAME, coach.getRemoteUpdatedTimestamp());
			values.put(CoachTable.FIRST_NAME_COL_NAME, coach.getFirstName());
			values.put(CoachTable.MIDDLE_NAME_COL_NAME, coach.getMiddleName());
			values.put(CoachTable.LAST_NAME_COL_NAME, coach.getLastName());
			values.put(CoachTable.EMAIL_COL_NAME, coach.getEmail());
			values.put(CoachTable.PHONE_COL_NAME, coach.getPhone());
			values.put(CoachTable.MOBILE_COL_NAME, coach.getCellPhone());
			values.put(CoachTable.GENDER_COL_NAME, coach.getGender().toString());
			values.put(CoachTable.LANGUAGES_COL_NAME, coach.getForeignLanguages());
			values.put(CoachTable.SKILLS_COL_NAME, coach.getSkillsExperience());
			values.put(CoachTable.ACTIVE_COL_NAME, (coach.isActive() ? 1 : 0));
			if (coach.getDateOfBirth() != null) {
				values.put(CoachTable.DOB_COL_NAME, coach.getDateOfBirth().getMillis());
			}
			if (coach.getLocation() != null) {
				values.put(CoachTable.CITY_COL_NAME, coach.getLocation().getCity());
				values.put(CoachTable.STATE_COL_NAME, coach.getLocation().getState());
				values.put(CoachTable.STATE_COL_NAME, coach.getLocation().getZipCode());
			}
			SQLiteDatabase db = localDB.getWritableDatabase();
			db.beginTransaction();
			coachId = db.insert(CoachTable.TABLE_NAME, null, values);
			db.setTransactionSuccessful();
			db.endTransaction();
		} else if (dbCoach.getRemoteUpdatedTimestamp() < coach.getRemoteUpdatedTimestamp()) {
			// more recent version of the coach - to be updated
			coach.setId(dbCoach.getId());
			updateCoach(coach);
		}
		return coachId;
	}

	public boolean updateCoachRecord(Coach coach) {
		boolean transactionStatus = false;

		ContentValues values = new ContentValues();
		if (coach.getEmail() != null) {
			values.put(CoachTable.EMAIL_COL_NAME, coach.getEmail());
		}
		if (coach.getPhone() != null) {
			values.put(CoachTable.PHONE_COL_NAME, coach.getPhone());
		}
		if (coach.getCellPhone() != null) {
			values.put(CoachTable.MOBILE_COL_NAME, coach.getCellPhone());
		}
		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		db.update(CoachTable.TABLE_NAME, values, CoachTable.ID_COL_NAME + "=" + coach.getId(), null);
		db.setTransactionSuccessful();
		transactionStatus = true;
		db.endTransaction();
		return transactionStatus;
	}

	private boolean updateCoach(Coach coach) {
		boolean transactionStatus = false;

		ContentValues values = new ContentValues();
		values.put(CoachTable.REMOTE_CREATED_COL_NAME, coach.getRemoteCreateTimestamp());
		values.put(CoachTable.REMOTE_UPDATED_COL_NAME, coach.getRemoteUpdatedTimestamp());
		values.put(CoachTable.FIRST_NAME_COL_NAME, coach.getFirstName());
		values.put(CoachTable.MIDDLE_NAME_COL_NAME, coach.getMiddleName());
		values.put(CoachTable.LAST_NAME_COL_NAME, coach.getLastName());
		values.put(CoachTable.EMAIL_COL_NAME, coach.getEmail());
		values.put(CoachTable.PHONE_COL_NAME, coach.getPhone());
		values.put(CoachTable.MOBILE_COL_NAME, coach.getCellPhone());
		values.put(CoachTable.GENDER_COL_NAME, coach.getGender().toString());
		values.put(CoachTable.LANGUAGES_COL_NAME, coach.getForeignLanguages());
		values.put(CoachTable.SKILLS_COL_NAME, coach.getSkillsExperience());
		values.put(CoachTable.ACTIVE_COL_NAME, (coach.isActive() ? 1 : 0));
		if (coach.getDateOfBirth() != null) {
			values.put(CoachTable.DOB_COL_NAME, coach.getDateOfBirth().getMillis());
		}
		if (coach.getLocation() != null) {
			values.put(CoachTable.CITY_COL_NAME, coach.getLocation().getCity());
			values.put(CoachTable.STATE_COL_NAME, coach.getLocation().getState());
			values.put(CoachTable.STATE_COL_NAME, coach.getLocation().getZipCode());
		}

		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		db.update(CoachTable.TABLE_NAME, values, CoachTable.ID_COL_NAME + "=" + coach.getId(), null);
		db.setTransactionSuccessful();
		transactionStatus = true;
		db.endTransaction();
		return transactionStatus;
	}

	private List<Coach> createCoachListFromCursor(Cursor c) {
		ArrayList<Coach> coaches = new ArrayList<Coach>(c.getCount());
		if (c.getCount() > 0) {
			c.moveToFirst();
			while (!c.isAfterLast()) {
				Coach coach = createCoachFromCursor(c);
				if (coach != null) {
					coaches.add(coach);
				}
				c.moveToNext();
			}
		}
		return coaches;
	}

	private Coach createCoachFromCursor(Cursor c) {
		Coach coach = null;
		if (c.getPosition() >= 0) {
			coach = new Coach();
			try {
				coach.setId(c.getLong(c.getColumnIndexOrThrow(CoachTable.ID_COL_NAME)));
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
				long lastAttended = c.getLong(c.getColumnIndexOrThrow(CoachTable.LAST_ATTENDED_DATE_COL_NAME));
				if (lastAttended != 0) {
					coach.setDateLastAttended(new DateTime(lastAttended));
				}

			} catch (IllegalArgumentException iax) {
				coach = null;
			}
		}
		return coach;
	}

	private Coach createSimpleCoachFromCursor(Cursor c) {
		Coach coach = null;
		if (c.getPosition() >= 0) {
			coach = new Coach();
			try {
				coach.setId(c.getLong(c.getColumnIndexOrThrow(CoachTable.ID_COL_NAME)));
				coach.setRemoteId(c.getLong(c.getColumnIndexOrThrow(CoachTable.REMOTE_ID_COL_NAME)));
				coach.setRemoteCreateTimestamp(c.getLong(c.getColumnIndexOrThrow(CoachTable.REMOTE_CREATED_COL_NAME)));
				coach.setRemoteUpdatedTimestamp(c.getLong(c.getColumnIndexOrThrow(CoachTable.REMOTE_UPDATED_COL_NAME)));

			} catch (IllegalArgumentException iax) {
				coach = null;
			}
		}
		return coach;
	}
}
