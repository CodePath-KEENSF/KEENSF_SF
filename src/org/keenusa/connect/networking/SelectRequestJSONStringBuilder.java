package org.keenusa.connect.networking;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.keenusa.connect.KeenConnectApp;
import org.keenusa.connect.models.SharedLoggedUserDetails;

import android.content.Context;
import android.util.Log;

public class SelectRequestJSONStringBuilder {

	public static final String PAGE_SIZE = "2000";

	public static final String LOG_TAG_CLASS = SelectRequestJSONStringBuilder.class.getSimpleName();
	public static final String API_KEY_PARAMETER_KEY = "key";
	public static final String FUNCTION_PARAMETER_KEY = "function";
	public static final String TABLE_NAME_PARAMETER_KEY = "tableName";
	public static final String FIELD_LIST_PARAMETER_KEY = "fieldList";
	public static final String WHERE_FIELD_PARAMETER_KEY = "whereField";
	public static final String WHERE_FIELD_VALUE_PARAMETER_KEY = "whereFieldValue";
	public static final String PAGE_SIZE_PARAMETER_KEY = "pageSize";
	public static final String PAGE_NUMBER_PARAMETER_KEY = "pageNumber";

	public static final String SELECT_FUNCTION_VALUE = "getAll";

	public static final String COACH_TABLE_NAME_VALUE = "contacts";
	public static final String ATHLETE_TABLE_NAME_VALUE = "youth";
	public static final String SESSION_TABLE_NAME_VALUE = "classes_days";
	public static final String PROGRAM_TABLE_NAME_VALUE = "classes";
	public static final String AFFILIATE_TABLE_NAME_VALUE = "affiliates";
	public static final String PROGRAM_ENROLMENT_TABLE_NAME_VALUE = "youth_classes";
	public static final String ATHLETE_ATTENDANCE_TABLE_NAME_VALUE = "youth_days_attendance";
	public static final String COACH_ATTENDANCE_TABLE_NAME_VALUE = "contacts_days_attendance";

	private static final String[] COACH_FILEDS_LIST = { "firstName", "lastName", "middleName", "dob", "inactive", "gender", "emailAddress",
			"cellPhone", "homePhone", "homeCity", "homeState", "homeZipCode", "foreignLanguage", "skillsExperience" };
	private static final String[] ATHLETE_FILEDS_LIST = { "firstName", "lastName", "nickName", "dateOfBirth", "gender", "email", "homePhone", "city",
			"state", "zipCode", "status", "parentGuardianCellPhone", "parentGuardianEmailAddress", "parentGuardianHomePhone",
			"parentGuardianRelationship", "primaryLanguageAtHome", "primaryParentGuardianFirstName", "primaryParentGuardianLastName" };
	private static final String[] SESSION_FILEDS_LIST = { "attendanceDate", "classes_id", "athletes", "coachesAttended", "coachesRegistered",
			"newCoachesNeeded", "openToPublicRegistration", "returningCoachesNeeded" };
	private static final String[] PROGRAM_FILEDS_LIST = { "address1", "address2", "approvalEmailMessage", "city", "classEndDate", "className",
			"classStartDate", "generalProgramType", "registrationConfirmation", "state", "times", "zipCode" };
	private static final String[] AFFILIATE_FILEDS_LIST = { "affiliateName", "contactName", "email", "website" };
	private static final String[] PROGRAM_ENROLMENT_FILEDS_LIST = { "classes_id", "waitlist", "youth_id" };
	private static final String[] ATHLETE_ATTENDANCE_FILEDS_LIST = { "attendance", "classes_days_id", "youth_id" };
	private static final String[] COACH_ATTENDANCE_FILEDS_LIST = { "attendance", "classes_days_id", "comments", "contacts_id" };

	public static String buildRequestJSONString(Context context, KeenCivicoreClient.APIRequestCode apiRequestCode, int page) {
		SharedLoggedUserDetails sharedLoggedUserDetails = ((KeenConnectApp) context.getApplicationContext()).getSharedLoggedUserDetails();
		String apiKey = sharedLoggedUserDetails.getApiKey();

		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put(PAGE_NUMBER_PARAMETER_KEY, String.valueOf(page));
			jsonParams.put(PAGE_SIZE_PARAMETER_KEY, PAGE_SIZE);
			jsonParams.put(FIELD_LIST_PARAMETER_KEY, getRequestFieldListValue(apiRequestCode));
			jsonParams.put(TABLE_NAME_PARAMETER_KEY, getRequestTableNameFieldValue(apiRequestCode));
			jsonParams.put(FUNCTION_PARAMETER_KEY, SELECT_FUNCTION_VALUE);
			jsonParams.put(API_KEY_PARAMETER_KEY, apiKey);

		} catch (JSONException e) {
			Log.e(LOG_TAG_CLASS, e.toString());
		}

		return jsonParams.toString();
	}

	private static String getRequestTableNameFieldValue(KeenCivicoreClient.APIRequestCode apiRequestCode) {
		switch (apiRequestCode) {
		case COACH_LIST:
			return COACH_TABLE_NAME_VALUE;
		case ATHLETE_LIST:
			return ATHLETE_TABLE_NAME_VALUE;
		case SESSION_LIST:
			return SESSION_TABLE_NAME_VALUE;
		case PROGRAM_LIST:
			return PROGRAM_TABLE_NAME_VALUE;
		case AFFILIATE_LIST:
			return AFFILIATE_TABLE_NAME_VALUE;
		case PROGRAM_ENROLMENT_LIST:
			return PROGRAM_ENROLMENT_TABLE_NAME_VALUE;
		case ATHLETE_ATENDANCE_LIST:
			return ATHLETE_ATTENDANCE_TABLE_NAME_VALUE;
		case COACH_ATTENDANCE_LIST:
			return COACH_ATTENDANCE_TABLE_NAME_VALUE;
		}
		return null;
	}

	private static JSONArray getRequestFieldListValue(KeenCivicoreClient.APIRequestCode apiRequestCode) {
		String[] fieldList = null;
		switch (apiRequestCode) {
		case COACH_LIST:
			fieldList = COACH_FILEDS_LIST;
			break;
		case ATHLETE_LIST:
			fieldList = ATHLETE_FILEDS_LIST;
			break;
		case SESSION_LIST:
			fieldList = SESSION_FILEDS_LIST;
			break;
		case PROGRAM_LIST:
			fieldList = PROGRAM_FILEDS_LIST;
			break;
		case AFFILIATE_LIST:
			fieldList = AFFILIATE_FILEDS_LIST;
			break;
		case PROGRAM_ENROLMENT_LIST:
			fieldList = PROGRAM_ENROLMENT_FILEDS_LIST;
			break;
		case ATHLETE_ATENDANCE_LIST:
			fieldList = ATHLETE_ATTENDANCE_FILEDS_LIST;
			break;
		case COACH_ATTENDANCE_LIST:
			fieldList = COACH_ATTENDANCE_FILEDS_LIST;
			break;
		}
		if (fieldList != null) {
			LinkedList<String> list = new LinkedList<String>();
			for (String s : fieldList) {
				list.add(s);
			}
			return new JSONArray(list);
		}
		return null;
	}
}
