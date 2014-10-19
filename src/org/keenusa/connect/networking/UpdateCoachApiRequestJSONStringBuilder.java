package org.keenusa.connect.networking;

import org.json.JSONException;
import org.json.JSONObject;
import org.keenusa.connect.KeenConnectApp;
import org.keenusa.connect.models.Coach;
import org.keenusa.connect.models.SharedLoggedUserDetails;

import android.content.Context;
import android.util.Log;

public class UpdateCoachApiRequestJSONStringBuilder {

	public static final String LOG_TAG_CLASS = UpdateCoachApiRequestJSONStringBuilder.class.getSimpleName();
	public static final String API_KEY_PARAMETER_KEY = "key";
	public static final String FUNCTION_PARAMETER_KEY = "function";
	public static final String TABLE_NAME_PARAMETER_KEY = "tableName";
	public static final String FIELD_LIST_PARAMETER_KEY = "fieldList";

	public static final String SELECT_FUNCTION_VALUE = "updateRecord";

	public static final String COACH_TABLE_NAME_VALUE = "contacts";
	public static final String RECORD_ID_PARAMETER_KEY = "record_id";

	public static String buildRequestJSONString(Context context, Coach coach) {
		if (coach != null) {
			String apiKey = getApiKey(context);

			JSONObject jsonParams = new JSONObject();
			try {
				jsonParams.put(FIELD_LIST_PARAMETER_KEY, getRequestFieldListValue(coach));
				jsonParams.put(API_KEY_PARAMETER_KEY, apiKey);
				jsonParams.put(FUNCTION_PARAMETER_KEY, "updateRecord");
				jsonParams.put(TABLE_NAME_PARAMETER_KEY, COACH_TABLE_NAME_VALUE);
				jsonParams.put(RECORD_ID_PARAMETER_KEY, String.valueOf(coach.getRemoteId()));

			} catch (JSONException e) {
				Log.e(LOG_TAG_CLASS, e.toString());
			}

			return jsonParams.toString();
		}
		return null;
	}

	private static String getApiKey(Context context) {
		SharedLoggedUserDetails sharedLoggedUserDetails = ((KeenConnectApp) context.getApplicationContext()).getSharedLoggedUserDetails();
		String apiKey = sharedLoggedUserDetails.getApiKey();
		return apiKey;
	}

	private static JSONObject getRequestFieldListValue(Coach coach) {
		JSONObject coachJSON = new JSONObject();
		try {
			if (coach.getPhone() != null) {
				coachJSON.put("homePhone", coach.getPhone());
			}
			if (coach.getEmail() != null) {
				coachJSON.put("emailAddress", coach.getEmail());
			}
			if (coach.getCellPhone() != null) {
				coachJSON.put("cellPhone", coach.getCellPhone());
			}

		} catch (JSONException e) {
			Log.e(LOG_TAG_CLASS, e.toString());
			return null;
		} catch (NullPointerException npe) {
			Log.e(LOG_TAG_CLASS, npe.toString());
		}
		return coachJSON;
	}
}
