package org.keenusa.connect.networking;

import org.json.JSONException;
import org.json.JSONObject;
import org.keenusa.connect.KeenConnectApp;
import org.keenusa.connect.models.CoachAttendance;
import org.keenusa.connect.models.SharedLoggedUserDetails;

import android.content.Context;
import android.util.Log;

public class InsertCoachAttendanceRequestJSONStringBuilder {

	public static final String LOG_TAG_CLASS = InsertCoachAttendanceRequestJSONStringBuilder.class.getSimpleName();

	public static final String API_KEY_PARAMETER_KEY = "key";
	public static final String FUNCTION_PARAMETER_KEY = "function";
	public static final String TABLE_NAME_PARAMETER_KEY = "tableName";
	public static final String FIELD_LIST_PARAMETER_KEY = "fieldList";
	public static final String SELECT_FUNCTION_VALUE = "insertRecord";
	public static final String COACH_ATTENDANCE_TABLE_NAME_VALUE = "contacts_days_attendance";

	public static String buildRequestJSONString(Context context, CoachAttendance coachAttendance) {
		// check all required data is present
		if (coachAttendance != null && coachAttendance.getRemoteSessionId() != 0 && coachAttendance.getCoach() != null
				&& coachAttendance.getCoach().getRemoteId() != 0 && coachAttendance.getAttendanceValue() != null) {
			String apiKey = getApiKey(context);

			JSONObject jsonParams = new JSONObject();
			try {
				jsonParams.put(FIELD_LIST_PARAMETER_KEY, getRequestFieldListValue(coachAttendance));
				jsonParams.put(API_KEY_PARAMETER_KEY, apiKey);
				jsonParams.put(FUNCTION_PARAMETER_KEY, SELECT_FUNCTION_VALUE);
				jsonParams.put(TABLE_NAME_PARAMETER_KEY, COACH_ATTENDANCE_TABLE_NAME_VALUE);

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

	private static JSONObject getRequestFieldListValue(CoachAttendance coachAttendance) {
		JSONObject coachAttendanceJSON = new JSONObject();
		try {
			coachAttendanceJSON.put("classes_days_id", String.valueOf(coachAttendance.getRemoteSessionId()));
			coachAttendanceJSON.put("attendance", coachAttendance.getAttendanceValue().getRemoteKeyString());
			coachAttendanceJSON.put("contacts_id", String.valueOf(coachAttendance.getCoach().getRemoteId()));
		} catch (JSONException e) {
			Log.e(LOG_TAG_CLASS, e.toString());
			return null;
		}
		return coachAttendanceJSON;
	}
}
