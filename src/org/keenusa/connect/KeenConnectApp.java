package org.keenusa.connect;

import org.keenusa.connect.models.SharedLoggedUserDetails;

import android.app.Application;

public class KeenConnectApp extends Application {

	private SharedLoggedUserDetails sharedLoggedUserDetails;

	public SharedLoggedUserDetails getSharedLoggedUserDetails() {
		return sharedLoggedUserDetails;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sharedLoggedUserDetails = new SharedLoggedUserDetails(this);
	}
}