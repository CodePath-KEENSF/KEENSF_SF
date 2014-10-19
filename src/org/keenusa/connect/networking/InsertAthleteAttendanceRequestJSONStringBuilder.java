package org.keenusa.connect.networking;

import org.json.JSONException;
import org.json.JSONObject;
import org.keenusa.connect.KeenConnectApp;
import org.keenusa.connect.models.AthleteAttendance;
import org.keenusa.connect.models.SharedLoggedUserDetails;

import android.content.Context;
import android.util.Log;

public class InsertAthleteAttendanceRequestJSONStringBuilder {

	public static final String LOG_TAG_CLASS = InsertAthleteAttendanceRequestJSONStringBuilder.class.getSimpleName();

	public static final String API_KEY_PARAMETER_KEY = "key";
	public static final String FUNCTION_PARAMETER_KEY = "function";
	public static final String TABLE_NAME_PARAMETER_KEY = "tableName";
	public static final String FIELD_LIST_PARAMETER_KEY = "fieldList";
	public static final String SELECT_FUNCTION_VALUE = "insertRecord";
	public static final String ATHLETE_ATTENDANCE_TABLE_NAME_VALUE = "youth_days_attendance";

	public static String buildRequestJSONString(Context context, AthleteAttendance athleteAttendance) {
		// check all required data is present
		if (athleteAttendance != null && athleteAttendance.getRemoteSessionId() != 0 && athleteAttendance.getAthlete() != null
				&& athleteAttendance.getAthlete().getRemoteId() != 0) {
			String apiKey = getApiKey(context);

			JSONObject jsonParams = new JSONObject();
			try {
				jsonParams.put(FIELD_LIST_PARAMETER_KEY, getRequestFieldListValue(athleteAttendance));
				jsonParams.put(API_KEY_PARAMETER_KEY, apiKey);
				jsonParams.put(FUNCTION_PARAMETER_KEY, SELECT_FUNCTION_VALUE);
				jsonParams.put(TABLE_NAME_PARAMETER_KEY, ATHLETE_ATTENDANCE_TABLE_NAME_VALUE);

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

	private static JSONObject getRequestFieldListValue(AthleteAttendance athleteAttendance) {
		JSONObject athleteAttendanceJSON = new JSONObject();
		try {
			athleteAttendanceJSON.put("classes_days_id", String.valueOf(athleteAttendance.getRemoteSessionId()));
			athleteAttendanceJSON.put("attendance", athleteAttendance.getAttendanceValue().getRemoteKeyString());
			athleteAttendanceJSON.put("youth_id", String.valueOf(athleteAttendance.getAthlete().getRemoteId()));
		} catch (JSONException e) {
			Log.e(LOG_TAG_CLASS, e.toString());
			return null;
		}
		return athleteAttendanceJSON;
	}
}
