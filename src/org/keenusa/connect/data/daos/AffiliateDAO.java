package org.keenusa.connect.data.daos;

import org.keenusa.connect.data.KeenConnectDB;
import org.keenusa.connect.data.tables.AffiliateTable;
import org.keenusa.connect.models.Affiliate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AffiliateDAO {

	private KeenConnectDB localDB = null;
	String[] columnNames = { AffiliateTable.ID_COL_NAME, AffiliateTable.REMOTE_ID_COL_NAME, AffiliateTable.REMOTE_CREATED_COL_NAME,
			AffiliateTable.REMOTE_UPDATED_COL_NAME, AffiliateTable.NAME_COL_NAME, AffiliateTable.CONTACT_NAME_COL_NAME,
			AffiliateTable.EMAIL_COL_NAME, AffiliateTable.WEBSITE_COL_NAME };

	public AffiliateDAO(Context context) {
		localDB = KeenConnectDB.getKeenConnectDB(context);
	}

	public Affiliate getAffiliateById(long id) {
		Affiliate affiliate = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor affiliateCursor = db.query(AffiliateTable.TABLE_NAME, columnNames, AffiliateTable.ID_COL_NAME + "=" + id, null, null, null, null);
		if (affiliateCursor.getCount() > 0) {
			affiliateCursor.moveToFirst();
			affiliate = createAffiliateFromCursor(affiliateCursor);
		}
		affiliateCursor.close();
		return affiliate;
	}

	public Affiliate getAffiliateByRemoteId(long id) {
		Affiliate affiliate = null;
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor affiliateCursor = db.query(AffiliateTable.TABLE_NAME, columnNames, AffiliateTable.REMOTE_ID_COL_NAME + "=" + id, null, null, null,
				null);
		if (affiliateCursor.getCount() > 0) {
			affiliateCursor.moveToFirst();
			affiliate = createAffiliateFromCursor(affiliateCursor);
		}
		affiliateCursor.close();
		return affiliate;
	}

	public long saveNewAffiliate(Affiliate affiliate) {
		long affiliateId = 0;
		if (affiliate != null) {

			Affiliate dbAffiliate = getAffiliateByRemoteId(affiliate.getRemoteId());
			if (dbAffiliate == null) {

				ContentValues values = new ContentValues();
				values.put(AffiliateTable.REMOTE_ID_COL_NAME, affiliate.getRemoteId());
				if (affiliate.getRemoteCreateTimestamp() != 0) {
					values.put(AffiliateTable.REMOTE_CREATED_COL_NAME, affiliate.getRemoteCreateTimestamp());
				}
				if (affiliate.getRemoteUpdatedTimestamp() != 0) {
					values.put(AffiliateTable.REMOTE_UPDATED_COL_NAME, affiliate.getRemoteUpdatedTimestamp());
				}
				if (affiliate.getName() != null) {
					values.put(AffiliateTable.NAME_COL_NAME, affiliate.getName());
				}
				if (affiliate.getContactName() != null) {
					values.put(AffiliateTable.CONTACT_NAME_COL_NAME, affiliate.getContactName());
				}
				if (affiliate.getEmail() != null) {
					values.put(AffiliateTable.EMAIL_COL_NAME, affiliate.getEmail());
				}
				if (affiliate.getWebsite() != null) {
					values.put(AffiliateTable.WEBSITE_COL_NAME, affiliate.getWebsite());
				}

				SQLiteDatabase db = localDB.getWritableDatabase();
				db.beginTransaction();
				affiliateId = db.insert(AffiliateTable.TABLE_NAME, null, values);
				db.setTransactionSuccessful();
				db.endTransaction();
			} else if (dbAffiliate.getRemoteUpdatedTimestamp() < affiliate.getRemoteUpdatedTimestamp()) {
				// more recent version
				affiliate.setId(dbAffiliate.getId());
				updateAffiliate(affiliate);
			}
		}
		return affiliateId;
	}

	private boolean updateAffiliate(Affiliate affiliate) {
		boolean transactionStatus = false;

		ContentValues values = new ContentValues();
		values.put(AffiliateTable.REMOTE_CREATED_COL_NAME, affiliate.getRemoteCreateTimestamp());
		values.put(AffiliateTable.REMOTE_UPDATED_COL_NAME, affiliate.getRemoteUpdatedTimestamp());
		values.put(AffiliateTable.REMOTE_ID_COL_NAME, affiliate.getRemoteId());
		values.put(AffiliateTable.NAME_COL_NAME, affiliate.getName());
		values.put(AffiliateTable.CONTACT_NAME_COL_NAME, affiliate.getContactName());
		values.put(AffiliateTable.EMAIL_COL_NAME, affiliate.getEmail());
		values.put(AffiliateTable.WEBSITE_COL_NAME, affiliate.getWebsite());

		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		db.update(AffiliateTable.TABLE_NAME, values, AffiliateTable.ID_COL_NAME + "=" + affiliate.getId(), null);
		db.setTransactionSuccessful();
		transactionStatus = true;
		db.endTransaction();
		return transactionStatus;

	}

	private Affiliate createAffiliateFromCursor(Cursor c) {
		Affiliate affiliate = null;
		if (c.getPosition() >= 0) {
			affiliate = new Affiliate();
			try {
				affiliate.setId(c.getLong(c.getColumnIndexOrThrow(AffiliateTable.ID_COL_NAME)));
				affiliate.setRemoteCreateTimestamp(c.getLong(c.getColumnIndexOrThrow(AffiliateTable.REMOTE_CREATED_COL_NAME)));
				affiliate.setRemoteUpdatedTimestamp(c.getLong(c.getColumnIndexOrThrow(AffiliateTable.REMOTE_UPDATED_COL_NAME)));
				affiliate.setRemoteId(c.getLong(c.getColumnIndexOrThrow(AffiliateTable.REMOTE_ID_COL_NAME)));
				affiliate.setName(c.getString(c.getColumnIndexOrThrow(AffiliateTable.NAME_COL_NAME)));
				affiliate.setContactName(c.getString(c.getColumnIndexOrThrow(AffiliateTable.CONTACT_NAME_COL_NAME)));
				affiliate.setEmail(c.getString(c.getColumnIndexOrThrow(AffiliateTable.EMAIL_COL_NAME)));
				affiliate.setWebsite(c.getString(c.getColumnIndexOrThrow(AffiliateTable.WEBSITE_COL_NAME)));
			} catch (IllegalArgumentException iax) {
				affiliate = null;
			}
		}
		return affiliate;
	}
}
