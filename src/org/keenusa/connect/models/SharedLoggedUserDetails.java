package org.keenusa.connect.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedLoggedUserDetails {

	public static final String API_KEY = "key";

	private String apiKey;

	private final SharedPreferences pref;

	public SharedLoggedUserDetails(Context context) {
		pref = PreferenceManager.getDefaultSharedPreferences(context);
		apiKey = pref.getString(API_KEY, "bebcc6f2fc64175348e460321de42b53.3.7e9c26ee361ea9052a5033dee59e0673.1499008737");
	}

	public boolean savePreferences(String apiKey) {
		this.apiKey = apiKey;

		Editor prefEditor = pref.edit();
		prefEditor.putString(API_KEY, apiKey);
		prefEditor.commit();
		return true;
	}

	public String getApiKey() {
		return apiKey;
	}

}
