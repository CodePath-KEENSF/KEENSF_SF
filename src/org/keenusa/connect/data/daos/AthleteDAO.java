package org.keenusa.connect.data.daos;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.keenusa.connect.data.KeenConnectDB;
import org.keenusa.connect.data.tables.AthleteTable;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.ContactPerson;
import org.keenusa.connect.models.Location;
import org.keenusa.connect.models.Parent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AthleteDAO {

	private KeenConnectDB localDB;

	String[] simpleColumnNames = { AthleteTable.ID_COL_NAME, AthleteTable.REMOTE_ID_COL_NAME, AthleteTable.REMOTE_CREATED_COL_NAME,
			AthleteTable.REMOTE_UPDATED_COL_NAME };

	String[] columnNames = { AthleteTable.ID_COL_NAME, AthleteTable.REMOTE_ID_COL_NAME, AthleteTable.REMOTE_CREATED_COL_NAME,
			AthleteTable.REMOTE_UPDATED_COL_NAME, AthleteTable.FIRST_NAME_COL_NAME, AthleteTable.MIDDLE_NAME_COL_NAME,
			AthleteTable.LAST_NAME_COL_NAME, AthleteTable.EMAIL_COL_NAME, AthleteTable.PHONE_COL_NAME, AthleteTable.GENDER_COL_NAME,
			AthleteTable.NICKNAME_COL_NAME, AthleteTable.DOB_COL_NAME, AthleteTable.PRIMLANGUAGE_COL_NAME, AthleteTable.ACTIVE_COL_NAME,
			AthleteTable.PARENT_MOBILE_COL_NAME, AthleteTable.PARENT_EMAIL_COL_NAME, AthleteTable.PARENT_PHONE_COL_NAME,
			AthleteTable.PARENT_RELATIONSHIP_COL_NAME, AthleteTable.PARENT_FIRST_NAME_COL_NAME, AthleteTable.PARENT_LAST_NAME_COL_NAME,
			AthleteTable.CITY_COL_NAME, AthleteTable.STATE_COL_NAME, AthleteTable.ZIPCODE_COL_NAME, AthleteTable.NUM_SESSIONS_ATTENDED_COL_NAME,
			AthleteTable.LAST_ATTENDED_DATE_COL_NAME };

	public AthleteDAO(Context context) {
		localDB = KeenConnectDB.getKeenConnectDB(context);
	}

	public Athlete getSimpleAthleteByRemoteId(long id) {
		Athlete athlete = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor athleteCursor = db.query(AthleteTable.TABLE_NAME, simpleColumnNames, AthleteTable.REMOTE_ID_COL_NAME + "=" + id, null, null, null,
				null);
		if (athleteCursor.getCount() > 0) {
			athleteCursor.moveToFirst();
			athlete = createSimpleAthleteFromCursor(athleteCursor);
		}
		athleteCursor.close();
		return athlete;
	}

	public List<Athlete> getAthleteList() {
		List<Athlete> allAthletes = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor athletesCursor = db.query(AthleteTable.TABLE_NAME, columnNames, null, null, null, null, AthleteTable.FIRST_NAME_COL_NAME + " ASC",
				null);

		if (athletesCursor.getCount() > 0) {
			athletesCursor.moveToFirst();
			allAthletes = createAthleteListFromCursor(athletesCursor);
		} else {
			allAthletes = new ArrayList<Athlete>();
		}
		athletesCursor.close();
		return allAthletes;
	}

	public Athlete getAthleteById(long id) {
		Athlete athlete = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor athleteCursor = db.query(AthleteTable.TABLE_NAME, columnNames, AthleteTable.ID_COL_NAME + "=" + id, null, null, null, null);
		if (athleteCursor.getCount() > 0) {
			athleteCursor.moveToFirst();
			athlete = createAthleteFromCursor(athleteCursor);
		}
		athleteCursor.close();
		return athlete;
	}

	public long saveNewAthlete(Athlete athlete) {
		long athleteId = 0;

		Athlete dbAthlete = getSimpleAthleteByRemoteId(athlete.getRemoteId());
		if (dbAthlete == null) {
			ContentValues values = new ContentValues();
			values.put(AthleteTable.REMOTE_ID_COL_NAME, athlete.getRemoteId());
			values.put(AthleteTable.REMOTE_CREATED_COL_NAME, athlete.getRemoteCreateTimestamp());
			values.put(AthleteTable.REMOTE_UPDATED_COL_NAME, athlete.getRemoteUpdatedTimestamp());
			values.put(AthleteTable.FIRST_NAME_COL_NAME, athlete.getFirstName());
			values.put(AthleteTable.MIDDLE_NAME_COL_NAME, athlete.getMiddleName());
			values.put(AthleteTable.LAST_NAME_COL_NAME, athlete.getLastName());
			values.put(AthleteTable.EMAIL_COL_NAME, athlete.getEmail());
			values.put(AthleteTable.PHONE_COL_NAME, athlete.getPhone());
			values.put(AthleteTable.GENDER_COL_NAME, athlete.getGender().toString());
			values.put(AthleteTable.NICKNAME_COL_NAME, athlete.getNickName());
			values.put(AthleteTable.PRIMLANGUAGE_COL_NAME, athlete.getPrimaryLanguage());
			values.put(AthleteTable.ACTIVE_COL_NAME, (athlete.isActive() ? 1 : 0));
			if (athlete.getDateOfBirth() != null) {
				values.put(AthleteTable.DOB_COL_NAME, athlete.getDateOfBirth().getMillis());
			}
			if (athlete.getPrimaryParent() != null) {
				values.put(AthleteTable.PARENT_MOBILE_COL_NAME, athlete.getPrimaryParent().getCellPhone());
				values.put(AthleteTable.PARENT_PHONE_COL_NAME, athlete.getPrimaryParent().getPhone());
				values.put(AthleteTable.PARENT_EMAIL_COL_NAME, athlete.getPrimaryParent().getEmail());
				if (athlete.getPrimaryParent().getParentRelationship() != null) {
					values.put(AthleteTable.PARENT_RELATIONSHIP_COL_NAME, athlete.getPrimaryParent().getParentRelationship().toString());
				}
				values.put(AthleteTable.PARENT_FIRST_NAME_COL_NAME, athlete.getPrimaryParent().getFirstName());
				values.put(AthleteTable.PARENT_LAST_NAME_COL_NAME, athlete.getPrimaryParent().getLastName());
			}
			if (athlete.getLocation() != null) {
				values.put(AthleteTable.CITY_COL_NAME, athlete.getLocation().getCity());
				values.put(AthleteTable.STATE_COL_NAME, athlete.getLocation().getState());
				values.put(AthleteTable.STATE_COL_NAME, athlete.getLocation().getZipCode());
			}

			SQLiteDatabase db = localDB.getWritableDatabase();
			db.beginTransaction();
			athleteId = db.insert(AthleteTable.TABLE_NAME, null, values);
			db.setTransactionSuccessful();
			db.endTransaction();
		} else if (dbAthlete.getRemoteUpdatedTimestamp() < athlete.getRemoteUpdatedTimestamp()) {
			// more recent version of the athlete - to be updated
			athlete.setId(dbAthlete.getId());
			updateAthlete(athlete);
		}
		return athleteId;
	}

	public boolean saveNewAthleteList(List<Athlete> athletes) {
		boolean areSaved = false;
		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		ContentValues values = new ContentValues();
		for (Athlete athlete : athletes) {

			Athlete dbAthlete = getSimpleAthleteByRemoteId(athlete.getRemoteId());
			if (dbAthlete == null) {
				values.clear();
				values.put(AthleteTable.REMOTE_ID_COL_NAME, athlete.getRemoteId());
				values.put(AthleteTable.REMOTE_CREATED_COL_NAME, athlete.getRemoteCreateTimestamp());
				values.put(AthleteTable.REMOTE_UPDATED_COL_NAME, athlete.getRemoteUpdatedTimestamp());
				values.put(AthleteTable.FIRST_NAME_COL_NAME, athlete.getFirstName());
				values.put(AthleteTable.MIDDLE_NAME_COL_NAME, athlete.getMiddleName());
				values.put(AthleteTable.LAST_NAME_COL_NAME, athlete.getLastName());
				values.put(AthleteTable.EMAIL_COL_NAME, athlete.getEmail());
				values.put(AthleteTable.PHONE_COL_NAME, athlete.getPhone());
				values.put(AthleteTable.GENDER_COL_NAME, athlete.getGender().toString());
				values.put(AthleteTable.NICKNAME_COL_NAME, athlete.getNickName());
				values.put(AthleteTable.PRIMLANGUAGE_COL_NAME, athlete.getPrimaryLanguage());
				values.put(AthleteTable.ACTIVE_COL_NAME, (athlete.isActive() ? 1 : 0));
				if (athlete.getDateOfBirth() != null) {
					values.put(AthleteTable.DOB_COL_NAME, athlete.getDateOfBirth().getMillis());
				}
				if (athlete.getPrimaryParent() != null) {
					values.put(AthleteTable.PARENT_MOBILE_COL_NAME, athlete.getPrimaryParent().getCellPhone());
					values.put(AthleteTable.PARENT_PHONE_COL_NAME, athlete.getPrimaryParent().getPhone());
					values.put(AthleteTable.PARENT_EMAIL_COL_NAME, athlete.getPrimaryParent().getEmail());
					if (athlete.getPrimaryParent().getParentRelationship() != null) {
						values.put(AthleteTable.PARENT_RELATIONSHIP_COL_NAME, athlete.getPrimaryParent().getParentRelationship().toString());
					}
					values.put(AthleteTable.PARENT_FIRST_NAME_COL_NAME, athlete.getPrimaryParent().getFirstName());
					values.put(AthleteTable.PARENT_LAST_NAME_COL_NAME, athlete.getPrimaryParent().getLastName());
				}
				if (athlete.getLocation() != null) {
					values.put(AthleteTable.CITY_COL_NAME, athlete.getLocation().getCity());
					values.put(AthleteTable.STATE_COL_NAME, athlete.getLocation().getState());
					values.put(AthleteTable.STATE_COL_NAME, athlete.getLocation().getZipCode());
				}
				db.insert(AthleteTable.TABLE_NAME, null, values);
			}

		}
		db.setTransactionSuccessful();
		db.endTransaction();
		areSaved = true;
		return areSaved;
	}

	public boolean updateAthleteRecord(Athlete athleteDTO) {
		boolean transactionStatus = false;

		ContentValues values = new ContentValues();
		if (athleteDTO.getEmail() != null) {
			values.put(AthleteTable.EMAIL_COL_NAME, (!athleteDTO.getEmail().isEmpty() ? athleteDTO.getEmail() : null));
		}
		if (athleteDTO.getPhone() != null) {
			values.put(AthleteTable.PHONE_COL_NAME, (!athleteDTO.getPhone().isEmpty() ? athleteDTO.getPhone() : null));
		}
		if (athleteDTO.getPrimaryParent() != null) {
			if (athleteDTO.getPrimaryParent().getPhone() != null) {
				values.put(AthleteTable.PARENT_PHONE_COL_NAME, (!athleteDTO.getPrimaryParent().getPhone().isEmpty() ? athleteDTO.getPrimaryParent()
						.getPhone() : null));
			}
			if (athleteDTO.getPrimaryParent().getCellPhone() != null) {
				values.put(AthleteTable.PARENT_MOBILE_COL_NAME, (!athleteDTO.getPrimaryParent().getCellPhone().isEmpty() ? athleteDTO
						.getPrimaryParent().getCellPhone() : null));
			}
			if (athleteDTO.getPrimaryParent().getEmail() != null) {
				values.put(AthleteTable.PARENT_EMAIL_COL_NAME, (!athleteDTO.getPrimaryParent().getEmail().isEmpty() ? athleteDTO.getPrimaryParent()
						.getEmail() : null));
			}

		}
		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		db.update(AthleteTable.TABLE_NAME, values, AthleteTable.ID_COL_NAME + "=" + athleteDTO.getId(), null);
		db.setTransactionSuccessful();
		transactionStatus = true;
		db.endTransaction();
		return transactionStatus;
	}

	private boolean updateAthlete(Athlete athlete) {
		boolean transactionStatus = false;
		ContentValues values = new ContentValues();
		values.put(AthleteTable.REMOTE_CREATED_COL_NAME, athlete.getRemoteCreateTimestamp());
		values.put(AthleteTable.REMOTE_UPDATED_COL_NAME, athlete.getRemoteUpdatedTimestamp());
		values.put(AthleteTable.FIRST_NAME_COL_NAME, athlete.getFirstName());
		values.put(AthleteTable.MIDDLE_NAME_COL_NAME, athlete.getMiddleName());
		values.put(AthleteTable.LAST_NAME_COL_NAME, athlete.getLastName());
		values.put(AthleteTable.EMAIL_COL_NAME, athlete.getEmail());
		values.put(AthleteTable.PHONE_COL_NAME, athlete.getPhone());
		values.put(AthleteTable.GENDER_COL_NAME, athlete.getGender().toString());
		values.put(AthleteTable.NICKNAME_COL_NAME, athlete.getNickName());
		values.put(AthleteTable.PRIMLANGUAGE_COL_NAME, athlete.getPrimaryLanguage());
		values.put(AthleteTable.ACTIVE_COL_NAME, (athlete.isActive() ? 1 : 0));
		if (athlete.getDateOfBirth() != null) {
			values.put(AthleteTable.DOB_COL_NAME, athlete.getDateOfBirth().getMillis());
		}
		if (athlete.getPrimaryParent() != null) {
			values.put(AthleteTable.PARENT_MOBILE_COL_NAME, athlete.getPrimaryParent().getCellPhone());
			values.put(AthleteTable.PARENT_PHONE_COL_NAME, athlete.getPrimaryParent().getPhone());
			values.put(AthleteTable.PARENT_EMAIL_COL_NAME, athlete.getPrimaryParent().getEmail());
			if (athlete.getPrimaryParent().getParentRelationship() != null) {
				values.put(AthleteTable.PARENT_RELATIONSHIP_COL_NAME, athlete.getPrimaryParent().getParentRelationship().toString());
			}
			values.put(AthleteTable.PARENT_FIRST_NAME_COL_NAME, athlete.getPrimaryParent().getFirstName());
			values.put(AthleteTable.PARENT_LAST_NAME_COL_NAME, athlete.getPrimaryParent().getLastName());
		}
		if (athlete.getLocation() != null) {
			values.put(AthleteTable.CITY_COL_NAME, athlete.getLocation().getCity());
			values.put(AthleteTable.STATE_COL_NAME, athlete.getLocation().getState());
			values.put(AthleteTable.STATE_COL_NAME, athlete.getLocation().getZipCode());
		}
		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		db.update(AthleteTable.TABLE_NAME, values, AthleteTable.ID_COL_NAME + "=" + athlete.getId(), null);
		db.setTransactionSuccessful();
		transactionStatus = true;
		db.endTransaction();
		return transactionStatus;

	}

	private List<Athlete> createAthleteListFromCursor(Cursor c) {
		ArrayList<Athlete> athletes = new ArrayList<Athlete>(c.getCount());
		if (c.getCount() > 0) {
			c.moveToFirst();
			while (!c.isAfterLast()) {
				Athlete athlete = createAthleteFromCursor(c);
				if (athlete != null) {
					athletes.add(athlete);
				}
				c.moveToNext();
			}
		}
		return athletes;
	}

	private Athlete createAthleteFromCursor(Cursor c) {
		Athlete athlete = null;
		if (c.getPosition() >= 0) {
			athlete = new Athlete();
			try {
				athlete.setId(c.getLong(c.getColumnIndexOrThrow(AthleteTable.ID_COL_NAME)));
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
				long lastAttended = c.getLong(c.getColumnIndexOrThrow(AthleteTable.LAST_ATTENDED_DATE_COL_NAME));
				if (lastAttended != 0) {
					athlete.setDateLastAttended(new DateTime(lastAttended));
				}

			} catch (IllegalArgumentException iax) {
				athlete = null;
			}
		}
		return athlete;
	}

	private Athlete createSimpleAthleteFromCursor(Cursor c) {
		Athlete athlete = null;
		if (c.getPosition() >= 0) {
			athlete = new Athlete();
			try {
				athlete.setId(c.getLong(c.getColumnIndexOrThrow(AthleteTable.ID_COL_NAME)));
				athlete.setRemoteId(c.getLong(c.getColumnIndexOrThrow(AthleteTable.REMOTE_ID_COL_NAME)));
				athlete.setRemoteCreateTimestamp(c.getLong(c.getColumnIndexOrThrow(AthleteTable.REMOTE_CREATED_COL_NAME)));
				athlete.setRemoteUpdatedTimestamp(c.getLong(c.getColumnIndexOrThrow(AthleteTable.REMOTE_UPDATED_COL_NAME)));

			} catch (IllegalArgumentException iax) {
				athlete = null;
			}
		}
		return athlete;
	}
}
