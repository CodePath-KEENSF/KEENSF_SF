package org.keenusa.connect;

import org.keenusa.connect.data.KeenConnectDB;
import org.keenusa.connect.models.SharedLoggedUserDetails;

import android.app.Application;

public class KeenConnectApp extends Application {

	private SharedLoggedUserDetails sharedLoggedUserDetails;
	private KeenConnectDB keenConnectDB;

	@Override
	public void onCreate() {
		super.onCreate();
		sharedLoggedUserDetails = new SharedLoggedUserDetails(this);
		keenConnectDB = KeenConnectDB.getKeenConnectDB(this);
	}

	public KeenConnectDB getKeenConnectDB() {
		return keenConnectDB;
	}

	public SharedLoggedUserDetails getSharedLoggedUserDetails() {
		return sharedLoggedUserDetails;
	}

}