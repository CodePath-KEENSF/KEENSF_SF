package org.keenusa.connect.data;

import org.keenusa.connect.data.tables.LocationTable;
import org.keenusa.connect.models.Location;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LocationDAO {

	private KeenConnectDB localDB = null;
	String[] columnNames = { LocationTable.ID_COL_NAME, LocationTable.ADDRESS_ONE_COL_NAME, LocationTable.ADDRESS_TWO_COL_NAME,
			LocationTable.CITY_COL_NAME, LocationTable.STATE_COL_NAME, LocationTable.ZIP_CODE_COL_NAME };

	public LocationDAO(Context context) {
		localDB = KeenConnectDB.getKeenConnectDB(context);
	}

	public Location getLocationById(long id) {
		Location location = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor locationCursor = db.query(LocationTable.TABLE_NAME, columnNames, LocationTable.ID_COL_NAME + "=" + id, null, null, null, null);
		if (locationCursor.getCount() > 0) {
			locationCursor.moveToFirst();
			location = createLocationFromCursor(locationCursor);
		}
		locationCursor.close();
		return location;
	}

	public long saveNewLocation(Location location) {
		long locationId = 0;
		if (location != null) {
			if (location.getId() > 0) {
				//update location?
			}
			SQLiteDatabase db = localDB.getWritableDatabase();
			db.beginTransaction();
			ContentValues values = new ContentValues();
			if (location.getAddress1() != null) {
				values.put(LocationTable.ADDRESS_ONE_COL_NAME, location.getAddress1());
			}
			if (location.getAddress2() != null) {
				values.put(LocationTable.ADDRESS_TWO_COL_NAME, location.getAddress2());
			}

			if (location.getCity() != null) {
				values.put(LocationTable.CITY_COL_NAME, location.getCity());
			}
			if (location.getState() != null) {
				values.put(LocationTable.STATE_COL_NAME, location.getState());
			}
			if (location.getZipCode() != null) {
				values.put(LocationTable.ZIP_CODE_COL_NAME, location.getZipCode());
			}
			locationId = db.insert(LocationTable.TABLE_NAME, null, values);
			db.setTransactionSuccessful();
			db.endTransaction();
		}
		return locationId;
	}

	private Location createLocationFromCursor(Cursor c) {
		Location location = null;
		if (c.getPosition() >= 0) {
			location = new Location();
			try {
				location.setId(c.getLong(c.getColumnIndexOrThrow(LocationTable.ID_COL_NAME)));
				location.setAddress1(c.getString(c.getColumnIndexOrThrow(LocationTable.ADDRESS_ONE_COL_NAME)));
				location.setAddress2(c.getString(c.getColumnIndexOrThrow(LocationTable.ADDRESS_TWO_COL_NAME)));
				location.setCity(c.getString(c.getColumnIndexOrThrow(LocationTable.CITY_COL_NAME)));
				location.setState(c.getString(c.getColumnIndexOrThrow(LocationTable.STATE_COL_NAME)));
				location.setZipCode(c.getString(c.getColumnIndexOrThrow(LocationTable.ZIP_CODE_COL_NAME)));
			} catch (IllegalArgumentException iax) {
				location = null;
			}
		}
		return location;
	}

}
