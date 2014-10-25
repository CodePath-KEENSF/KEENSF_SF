package org.keenusa.connect.data;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.keenusa.connect.data.tables.AthleteTable;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.ContactPerson;
import org.keenusa.connect.models.Location;
import org.keenusa.connect.models.Parent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoachDAO {

	private KeenConnectDB localDB;

	String[] columnNames = { AthleteTable.ID_COL_NAME, AthleteTable.REMOTE_ID_COL_NAME, AthleteTable.REMOTE_CREATED_COL_NAME,
			AthleteTable.REMOTE_UPDATED_COL_NAME, AthleteTable.FIRST_NAME_COL_NAME, AthleteTable.MIDDLE_NAME_COL_NAME,
			AthleteTable.LAST_NAME_COL_NAME, AthleteTable.EMAIL_COL_NAME, AthleteTable.PHONE_COL_NAME, AthleteTable.GENDER_COL_NAME,
			AthleteTable.NICKNAME_COL_NAME, AthleteTable.DOB_COL_NAME, AthleteTable.PRIMLANGUAGE_COL_NAME, AthleteTable.ACTIVE_COL_NAME,
			AthleteTable.PARENT_MOBILE_COL_NAME, AthleteTable.PARENT_EMAIL_COL_NAME, AthleteTable.PARENT_PHONE_COL_NAME,
			AthleteTable.PARENT_RELATIONSHIP_COL_NAME, AthleteTable.PARENT_FIRST_NAME_COL_NAME, AthleteTable.PARENT_LAST_NAME_COL_NAME,
			AthleteTable.CITY_COL_NAME, AthleteTable.STATE_COL_NAME, AthleteTable.ZIPCODE_COL_NAME };

	public CoachDAO(Context context) {
		localDB = KeenConnectDB.getKeenConnectDB(context);
	}

	public List<Athlete> getAthleteList() {
		List<Athlete> allAthletes = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor athletesCursor = db.query(AthleteTable.TABLE_NAME, columnNames, null, null, null, null,
				AthleteTable.REMOTE_CREATED_COL_NAME + " DESC", null);

		if (athletesCursor.getCount() > 0) {
			athletesCursor.moveToFirst();
			allAthletes = createAthleteListFromCursor(athletesCursor);
		} else {
			allAthletes = new ArrayList<Athlete>();
		}
		athletesCursor.close();
		return allAthletes;
	}

	public Athlete geAthleteByRemoteId(long id) {
		Athlete athlete = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor athleteCursor = db.query(AthleteTable.TABLE_NAME, columnNames, AthleteTable.REMOTE_ID_COL_NAME + "=" + id, null, null, null, null);
		if (athleteCursor.getCount() > 0) {
			athleteCursor.moveToFirst();
			athlete = createAthleteFromCursor(athleteCursor);
		}
		athleteCursor.close();
		return athlete;
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
		SQLiteDatabase db = localDB.getWritableDatabase();
		Athlete dbAthlete = geAthleteByRemoteId(athlete.getRemoteId());
		if (dbAthlete == null) {
			db.beginTransaction();

			ContentValues values = new ContentValues();
			values.put(AthleteTable.REMOTE_ID_COL_NAME, athlete.getRemoteId());
			values.put(AthleteTable.REMOTE_CREATED_COL_NAME, athlete.getRemoteCreateTimestamp());
			values.put(AthleteTable.REMOTE_UPDATED_COL_NAME, athlete.getRemoteUpdatedTimestamp());
			if (athlete.getFirstName() != null) {
				values.put(AthleteTable.FIRST_NAME_COL_NAME, athlete.getFirstName());
			}
			if (athlete.getMiddleName() != null) {
				values.put(AthleteTable.MIDDLE_NAME_COL_NAME, athlete.getMiddleName());
			}
			if (athlete.getLastName() != null) {
				values.put(AthleteTable.LAST_NAME_COL_NAME, athlete.getLastName());
			}
			if (athlete.getEmail() != null) {
				values.put(AthleteTable.EMAIL_COL_NAME, athlete.getEmail());
			}
			if (athlete.getPhone() != null) {
				values.put(AthleteTable.PHONE_COL_NAME, athlete.getPhone());
			}

			values.put(AthleteTable.GENDER_COL_NAME, athlete.getGender().toString());
			if (athlete.getNickName() != null) {
				values.put(AthleteTable.NICKNAME_COL_NAME, athlete.getNickName());
			}
			if (athlete.getDateOfBirth() != null) {
				values.put(AthleteTable.DOB_COL_NAME, athlete.getDateOfBirth().getMillis());
			}

			if (athlete.getPrimaryLanguage() != null) {
				values.put(AthleteTable.PRIMLANGUAGE_COL_NAME, athlete.getPrimaryLanguage());
			}
			values.put(AthleteTable.ACTIVE_COL_NAME, (athlete.isActive() ? 1 : 0));
			if (athlete.getPrimaryParent() != null) {
				if (athlete.getPrimaryParent().getCellPhone() != null) {
					values.put(AthleteTable.PARENT_MOBILE_COL_NAME, athlete.getPrimaryParent().getCellPhone());
				}
				if (athlete.getPrimaryParent().getPhone() != null) {
					values.put(AthleteTable.PARENT_PHONE_COL_NAME, athlete.getPrimaryParent().getPhone());
				}
				if (athlete.getPrimaryParent().getEmail() != null) {
					values.put(AthleteTable.PARENT_EMAIL_COL_NAME, athlete.getPrimaryParent().getEmail());
				}
				if (athlete.getPrimaryParent().getParentRelationship() != null) {
					values.put(AthleteTable.PARENT_RELATIONSHIP_COL_NAME, athlete.getPrimaryParent().getParentRelationship().toString());
				}
				if (athlete.getPrimaryParent().getFirstName() != null) {
					values.put(AthleteTable.PARENT_FIRST_NAME_COL_NAME, athlete.getPrimaryParent().getFirstName());
				}
				if (athlete.getPrimaryParent().getLastName() != null) {
					values.put(AthleteTable.PARENT_LAST_NAME_COL_NAME, athlete.getPrimaryParent().getLastName());
				}
			}
			if (athlete.getLocation() != null) {
				if (athlete.getLocation().getCity() != null) {
					values.put(AthleteTable.CITY_COL_NAME, athlete.getLocation().getCity());
				}
				if (athlete.getLocation().getState() != null) {
					values.put(AthleteTable.STATE_COL_NAME, athlete.getLocation().getState());
				}
				if (athlete.getLocation().getZipCode() != null) {
					values.put(AthleteTable.STATE_COL_NAME, athlete.getLocation().getZipCode());
				}
			}

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

	private boolean updateAthlete(Athlete athlete) {

		boolean transactionStatus = false;
		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		ContentValues values = new ContentValues();

		values.put(AthleteTable.REMOTE_CREATED_COL_NAME, athlete.getRemoteCreateTimestamp());
		values.put(AthleteTable.REMOTE_UPDATED_COL_NAME, athlete.getRemoteUpdatedTimestamp());
		if (athlete.getFirstName() != null) {
			values.put(AthleteTable.FIRST_NAME_COL_NAME, athlete.getFirstName());
		}
		if (athlete.getMiddleName() != null) {
			values.put(AthleteTable.MIDDLE_NAME_COL_NAME, athlete.getMiddleName());
		}
		if (athlete.getLastName() != null) {
			values.put(AthleteTable.LAST_NAME_COL_NAME, athlete.getLastName());
		}
		if (athlete.getEmail() != null) {
			values.put(AthleteTable.EMAIL_COL_NAME, athlete.getEmail());
		}
		if (athlete.getPhone() != null) {
			values.put(AthleteTable.PHONE_COL_NAME, athlete.getPhone());
		}

		values.put(AthleteTable.GENDER_COL_NAME, athlete.getGender().toString());
		if (athlete.getNickName() != null) {
			values.put(AthleteTable.NICKNAME_COL_NAME, athlete.getNickName());
		}
		if (athlete.getDateOfBirth() != null) {
			values.put(AthleteTable.DOB_COL_NAME, athlete.getDateOfBirth().getMillis());
		}

		if (athlete.getPrimaryLanguage() != null) {
			values.put(AthleteTable.PRIMLANGUAGE_COL_NAME, athlete.getPrimaryLanguage());
		}
		values.put(AthleteTable.ACTIVE_COL_NAME, (athlete.isActive() ? 1 : 0));
		if (athlete.getPrimaryParent() != null) {
			if (athlete.getPrimaryParent().getCellPhone() != null) {
				values.put(AthleteTable.PARENT_MOBILE_COL_NAME, athlete.getPrimaryParent().getCellPhone());
			}
			if (athlete.getPrimaryParent().getPhone() != null) {
				values.put(AthleteTable.PARENT_PHONE_COL_NAME, athlete.getPrimaryParent().getPhone());
			}
			if (athlete.getPrimaryParent().getEmail() != null) {
				values.put(AthleteTable.PARENT_EMAIL_COL_NAME, athlete.getPrimaryParent().getEmail());
			}
			if (athlete.getPrimaryParent().getParentRelationship() != null) {
				values.put(AthleteTable.PARENT_RELATIONSHIP_COL_NAME, athlete.getPrimaryParent().getParentRelationship().toString());
			}
			if (athlete.getPrimaryParent().getFirstName() != null) {
				values.put(AthleteTable.PARENT_FIRST_NAME_COL_NAME, athlete.getPrimaryParent().getFirstName());
			}
			if (athlete.getPrimaryParent().getLastName() != null) {
				values.put(AthleteTable.PARENT_LAST_NAME_COL_NAME, athlete.getPrimaryParent().getLastName());
			}
		}
		if (athlete.getLocation() != null) {
			if (athlete.getLocation().getCity() != null) {
				values.put(AthleteTable.CITY_COL_NAME, athlete.getLocation().getCity());
			}
			if (athlete.getLocation().getState() != null) {
				values.put(AthleteTable.STATE_COL_NAME, athlete.getLocation().getState());
			}
			if (athlete.getLocation().getZipCode() != null) {
				values.put(AthleteTable.STATE_COL_NAME, athlete.getLocation().getZipCode());
			}
		}

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

			} catch (IllegalArgumentException iax) {
				athlete = null;
			}
		}
		return athlete;
	}
}
