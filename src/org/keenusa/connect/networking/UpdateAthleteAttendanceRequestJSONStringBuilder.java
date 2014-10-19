package org.keenusa.connect.networking;

import org.json.JSONException;
import org.json.JSONObject;
import org.keenusa.connect.KeenConnectApp;
import org.keenusa.connect.models.AthleteAttendance;
import org.keenusa.connect.models.SharedLoggedUserDetails;

import android.content.Context;
import android.util.Log;

public class UpdateAthleteAttendanceRequestJSONStringBuilder {

	public static final String LOG_TAG_CLASS = UpdateAthleteAttendanceRequestJSONStringBuilder.class.getSimpleName();

	public static final String API_KEY_PARAMETER_KEY = "key";
	public static final String FUNCTION_PARAMETER_KEY = "function";
	public static final String TABLE_NAME_PARAMETER_KEY = "tableName";
	public static final String FIELD_LIST_PARAMETER_KEY = "fieldList";
	public static final String UPDATE_FUNCTION_VALUE = "updateRecord";
	public static final String ATHLETE_ATTENDANCE_TABLE_NAME_VALUE = "youth_days_attendance";
	public static final String RECORD_ID_PARAMETER_KEY = "record_id";

	public static String buildRequestJSONString(Context context, AthleteAttendance athleteAttendance) {
		if (athleteAttendance != null) {
			String apiKey = getApiKey(context);

			JSONObject jsonParams = new JSONObject();
			try {
				jsonParams.put(FIELD_LIST_PARAMETER_KEY, getRequestFieldListValue(athleteAttendance));
				jsonParams.put(API_KEY_PARAMETER_KEY, apiKey);
				jsonParams.put(FUNCTION_PARAMETER_KEY, UPDATE_FUNCTION_VALUE);
				jsonParams.put(TABLE_NAME_PARAMETER_KEY, ATHLETE_ATTENDANCE_TABLE_NAME_VALUE);
				jsonParams.put(RECORD_ID_PARAMETER_KEY, String.valueOf(athleteAttendance.getRemoteId()));

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
			if (athleteAttendance.getAttendanceValue() != null) {
				athleteAttendanceJSON.put("attendance", athleteAttendance.getAttendanceValue().getRemoteKeyString());
			}

		} catch (JSONException e) {
			Log.e(LOG_TAG_CLASS, e.toString());
			return null;
		}
		return athleteAttendanceJSON;
	}
}
