package org.keenusa.connect.networking;

import java.io.IOException;
import java.util.List;

import org.keenusa.connect.models.Affiliate;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.AthleteAttendance;
import org.keenusa.connect.models.Coach;
import org.keenusa.connect.models.CoachAttendance;
import org.keenusa.connect.models.KeenProgram;
import org.keenusa.connect.models.KeenProgramEnrolment;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.models.remote.RemoteAffiliateList;
import org.keenusa.connect.models.remote.RemoteAthleteAttendanceList;
import org.keenusa.connect.models.remote.RemoteAthleteList;
import org.keenusa.connect.models.remote.RemoteCoachAttendanceList;
import org.keenusa.connect.models.remote.RemoteCoachList;
import org.keenusa.connect.models.remote.RemoteProgramEnrolmentList;
import org.keenusa.connect.models.remote.RemoteProgramList;
import org.keenusa.connect.models.remote.RemoteSessionList;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class SyncKeenCivicoreClient {

	private final OkHttpClient client;
	private Context context;

	public static final String LOG_TAG_CLASS = SyncKeenCivicoreClient.class.getSimpleName();
	public static final String LOG_TAG_URL = "URL";
	public static final String BASE_URL = "http://fwdev.civicore.com/keen/__revision=apiTest/";
	//	public static final String URL_STRING = "http://fwdev.civicore.com/keen/__revision=apiTest/?version=2.0&api={\"key\":\"bebcc6f2fc64175348e460321de42b53.3.7e9c26ee361ea9052a5033dee59e0673.1499008737\",\"function\":\"getAll\",\"tableName\":\"contacts\",\"fieldList\":[\"firstName\",\"lastName\"],\"pageSize\":\"50\",\"pageNumber\":\"1\"}";

	public static final String VERSION_PARAMETER_KEY = "version";
	public static final String REQUEST_STRING_PARAMETER_KEY = "api";

	public enum APIRequestCode {
		COACH_LIST, ATHLETE_LIST, SESSION_LIST, PROGRAM_LIST, PROGRAM_ENROLMENT_LIST, ATHLETE_ATENDANCE_LIST, COACH_ATTENDANCE_LIST, AFFILIATE_LIST, UPDATE_ATHLETE_PROFILE, UPDATE_COACH_PROFILE, UPDATE_ATHLETE_ATTENDANCE, UPDATE_COACH_ATTENDANCE, INSERT_ATHLETE_ATTENDANCE, INSERT_COACH_ATTENDANCE
	};

	public SyncKeenCivicoreClient(Context context) {
		this.client = new OkHttpClient();
		this.context = context;
	}

	public List<Coach> fetchCoachListData() throws Exception {

		String url = buildSelectURL(APIRequestCode.COACH_LIST, 1);

		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();

		if (!response.isSuccessful()) {
			throw new IOException("Unexpected response " + response);
		}
		String responseString = response.body().string();
		RemoteCoachList remoteCoaches = (new SyncXMLHttpResponseHandler<RemoteCoachList>(RemoteCoachList.class)).parseXMLResponse(responseString);
		return Coach.fromRemoteCoachList(remoteCoaches.getRemoteCoaches());

	}

	public List<KeenProgram> fetchProgramListData() throws Exception {

		String url = buildSelectURL(APIRequestCode.PROGRAM_LIST, 1);

		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();

		if (!response.isSuccessful()) {
			throw new IOException("Unexpected response " + response);
		}
		String responseString = response.body().string();
		RemoteProgramList remotePrograms = (new SyncXMLHttpResponseHandler<RemoteProgramList>(RemoteProgramList.class))
				.parseXMLResponse(responseString);
		return KeenProgram.fromRemoteProgramList(remotePrograms.getRemotePrograms());

	}

	public List<Athlete> fetchAthleteListData() throws Exception {

		String url = buildSelectURL(APIRequestCode.ATHLETE_LIST, 1);

		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();

		if (!response.isSuccessful()) {
			throw new IOException("Unexpected response " + response);
		}
		String responseString = response.body().string();
		RemoteAthleteList remoteAthletes = (new SyncXMLHttpResponseHandler<RemoteAthleteList>(RemoteAthleteList.class))
				.parseXMLResponse(responseString);
		return Athlete.fromRemoteAthleteList(remoteAthletes.getRemoteAthletes());

	}

	public List<KeenSession> fetchSessionListData() throws Exception {

		String url = buildSelectURL(APIRequestCode.SESSION_LIST, 1);

		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();

		if (!response.isSuccessful()) {
			throw new IOException("Unexpected response " + response);
		}
		String responseString = response.body().string();
		RemoteSessionList remoteSessions = (new SyncXMLHttpResponseHandler<RemoteSessionList>(RemoteSessionList.class))
				.parseXMLResponse(responseString);
		return KeenSession.fromRemoteSessionList(remoteSessions.getRemoteSessions());

	}

	public List<KeenProgramEnrolment> fetchProgramEnrolmentListData() throws Exception {

		String url = buildSelectURL(APIRequestCode.PROGRAM_ENROLMENT_LIST, 1);

		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();

		if (!response.isSuccessful()) {
			throw new IOException("Unexpected response " + response);
		}
		String responseString = response.body().string();
		RemoteProgramEnrolmentList remoteProgramEnrolment = (new SyncXMLHttpResponseHandler<RemoteProgramEnrolmentList>(
				RemoteProgramEnrolmentList.class)).parseXMLResponse(responseString);
		return KeenProgramEnrolment.fromRemoteProgramEnrolmentList(remoteProgramEnrolment.getRemoteProgramEnrolments());

	}

	public List<AthleteAttendance> fetchAthleteAttendanceListData() throws Exception {

		String url = buildSelectURL(APIRequestCode.ATHLETE_ATENDANCE_LIST, 1);

		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();

		if (!response.isSuccessful()) {
			throw new IOException("Unexpected response " + response);
		}
		String responseString = response.body().string();
		RemoteAthleteAttendanceList remoteAthleteAttendances = (new SyncXMLHttpResponseHandler<RemoteAthleteAttendanceList>(
				RemoteAthleteAttendanceList.class)).parseXMLResponse(responseString);
		return AthleteAttendance.fromRemoteAthleteAttendanceList(remoteAthleteAttendances.getRemoteAthleteAttendances());

	}

	public List<CoachAttendance> fetchCoachAttendanceListData() throws Exception {

		String url = buildSelectURL(APIRequestCode.COACH_ATTENDANCE_LIST, 1);

		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();

		if (!response.isSuccessful()) {
			throw new IOException("Unexpected response " + response);
		}
		String responseString = response.body().string();
		RemoteCoachAttendanceList remoteCoachAttendances = (new SyncXMLHttpResponseHandler<RemoteCoachAttendanceList>(RemoteCoachAttendanceList.class))
				.parseXMLResponse(responseString);
		return CoachAttendance.fromRemoteCoachAttendanceList(remoteCoachAttendances.getRemoteCoachAttendances());

	}

	public List<Affiliate> fetchAffiliateListData() throws Exception {

		String url = buildSelectURL(APIRequestCode.AFFILIATE_LIST, 1);

		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();

		if (!response.isSuccessful()) {
			throw new IOException("Unexpected response " + response);
		}
		String responseString = response.body().string();
		RemoteAffiliateList remoteAffiliates = (new SyncXMLHttpResponseHandler<RemoteAffiliateList>(RemoteAffiliateList.class))
				.parseXMLResponse(responseString);
		return Affiliate.fromRemoteAffiliateList(remoteAffiliates.getRemoteAffiliates());

	}

	//
	//	public void updateAfthetProfileRecord(final Athlete athlete, final CivicoreUpdateDataResultListener<Athlete> listener) {
	//
	//		String url = buildUpdateAthleteURL(APIRequestCode.UPDATE_ATHLETE_PROFILE, athlete);
	//		client.get(url, new AsyncHttpResponseHandler() {
	//
	//			@Override
	//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
	//				Log.d("RESPONSE", new String(arg2));
	//				Serializer serializer = new Persister();
	//
	//				try {
	//					String response = new String(arg2);
	//					response = response.replaceAll("&", "&amp;");
	//					response = response.replaceAll("'", "&apos;");
	//					RemoteUpdateSuccessResult remoteUpdateSuccessResult = serializer.read(RemoteUpdateSuccessResult.class, response);
	//					if (listener != null) {
	//						if (Long.valueOf(remoteUpdateSuccessResult.getRemoteId()) == athlete.getRemoteId()) {
	//							listener.onRecordUpdateResult(athlete);
	//						} else {
	//							listener.onRecordUpdateError();
	//						}
	//					}
	//				} catch (Exception e) {
	//					Log.e(LOG_TAG_CLASS, e.toString());
	//					if (listener != null) {
	//						listener.onRecordUpdateError();
	//					}
	//
	//				}
	//			}
	//
	//			@Override
	//			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
	//				Log.e("RESPONSE", arg3.toString());
	//
	//			}
	//
	//		});
	//
	//	}
	//
	//	public void updateCoachProfileRecord(final Coach coach, final CivicoreUpdateDataResultListener<Coach> listener) {
	//
	//		String url = buildUpdateCoachURL(APIRequestCode.UPDATE_COACH_PROFILE, coach);
	//		client.get(url, new AsyncHttpResponseHandler() {
	//
	//			@Override
	//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
	//				Log.d("RESPONSE", new String(arg2));
	//				Serializer serializer = new Persister();
	//
	//				try {
	//					String response = new String(arg2);
	//					response = response.replaceAll("&", "&amp;");
	//					response = response.replaceAll("'", "&apos;");
	//					RemoteUpdateSuccessResult remoteUpdateSuccessResult = serializer.read(RemoteUpdateSuccessResult.class, response);
	//					if (listener != null) {
	//						if (Long.valueOf(remoteUpdateSuccessResult.getRemoteId()) == coach.getRemoteId()) {
	//							listener.onRecordUpdateResult(coach);
	//						} else {
	//							listener.onRecordUpdateError();
	//						}
	//					}
	//				} catch (Exception e) {
	//					Log.e(LOG_TAG_CLASS, e.toString());
	//					if (listener != null) {
	//						listener.onRecordUpdateError();
	//					}
	//
	//				}
	//			}
	//
	//			@Override
	//			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
	//				Log.e("RESPONSE", arg3.toString());
	//
	//			}
	//
	//		});
	//
	//	}
	//
	//	public void updateAthleteAttendanceRecord(final AthleteAttendance athleteAttendance,
	//			final CivicoreUpdateDataResultListener<AthleteAttendance> listener) {
	//
	//		String url = buildUpdateAthleteAttendanceURL(APIRequestCode.UPDATE_ATHLETE_ATTENDANCE, athleteAttendance);
	//		client.get(url, new AsyncHttpResponseHandler() {
	//
	//			@Override
	//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
	//				Log.d("RESPONSE", new String(arg2));
	//				Serializer serializer = new Persister();
	//
	//				try {
	//					String response = new String(arg2);
	//					response = response.replaceAll("&", "&amp;");
	//					response = response.replaceAll("'", "&apos;");
	//					RemoteUpdateSuccessResult remoteUpdateSuccessResult = serializer.read(RemoteUpdateSuccessResult.class, response);
	//					if (listener != null) {
	//						if (Long.valueOf(remoteUpdateSuccessResult.getRemoteId()) == athleteAttendance.getRemoteId()) {
	//							listener.onRecordUpdateResult(athleteAttendance);
	//						} else {
	//							listener.onRecordUpdateError();
	//						}
	//					}
	//				} catch (Exception e) {
	//					Log.e(LOG_TAG_CLASS, e.toString());
	//					if (listener != null) {
	//						listener.onRecordUpdateError();
	//					}
	//
	//				}
	//			}
	//
	//			@Override
	//			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
	//				Log.e("RESPONSE", arg3.toString());
	//
	//			}
	//
	//		});
	//
	//	}
	//
	//	public void updateCoachAttendanceRecord(final CoachAttendance coachAttendance, final CivicoreUpdateDataResultListener<CoachAttendance> listener) {
	//
	//		String url = buildUpdateCoachAttendanceURL(APIRequestCode.UPDATE_COACH_ATTENDANCE, coachAttendance);
	//		client.get(url, new AsyncHttpResponseHandler() {
	//
	//			@Override
	//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
	//				Log.d("RESPONSE", new String(arg2));
	//				Serializer serializer = new Persister();
	//
	//				try {
	//					String response = new String(arg2);
	//					response = response.replaceAll("&", "&amp;");
	//					response = response.replaceAll("'", "&apos;");
	//					RemoteUpdateSuccessResult remoteUpdateSuccessResult = serializer.read(RemoteUpdateSuccessResult.class, response);
	//					if (listener != null) {
	//						if (Long.valueOf(remoteUpdateSuccessResult.getRemoteId()) == coachAttendance.getRemoteId()) {
	//							listener.onRecordUpdateResult(coachAttendance);
	//						} else {
	//							listener.onRecordUpdateError();
	//						}
	//					}
	//				} catch (Exception e) {
	//					Log.e(LOG_TAG_CLASS, e.toString());
	//					if (listener != null) {
	//						listener.onRecordUpdateError();
	//					}
	//
	//				}
	//			}
	//
	//			@Override
	//			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
	//				Log.e("RESPONSE", arg3.toString());
	//
	//			}
	//
	//		});
	//
	//	}
	//
	//	public void insertNewAthleteAttendanceRecord(final AthleteAttendance athleteAttendance,
	//			final CivicoreUpdateDataResultListener<AthleteAttendance> listener) {
	//
	//		String url = buildInserAthleteAttendanceURL(APIRequestCode.INSERT_ATHLETE_ATTENDANCE, athleteAttendance);
	//		client.get(url, new AsyncHttpResponseHandler() {
	//
	//			@Override
	//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
	//				Log.d("RESPONSE", new String(arg2));
	//				Serializer serializer = new Persister();
	//
	//				try {
	//					String response = new String(arg2);
	//					response = response.replaceAll("&", "&amp;");
	//					response = response.replaceAll("'", "&apos;");
	//					RemoteUpdateSuccessResult remoteInsertSuccessResult = serializer.read(RemoteUpdateSuccessResult.class, response);
	//					if (listener != null) {
	//						if (Long.valueOf(remoteInsertSuccessResult.getRemoteId()) > 0) {
	//							athleteAttendance.setRemoteId(Long.valueOf(remoteInsertSuccessResult.getRemoteId()));
	//							listener.onRecordUpdateResult(athleteAttendance);
	//						} else {
	//							listener.onRecordUpdateError();
	//						}
	//					}
	//				} catch (Exception e) {
	//					Log.e(LOG_TAG_CLASS, e.toString());
	//					if (listener != null) {
	//						listener.onRecordUpdateError();
	//					}
	//
	//				}
	//			}
	//
	//			@Override
	//			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
	//				Log.e("RESPONSE", arg3.toString());
	//
	//			}
	//
	//		});
	//
	//	}
	//
	//	// looks like api allows duplicated attendances to be inserted - should be very careful about using the method: check that not a duplicate before posting
	//	public void insertNewCoachAttendanceRecord(final CoachAttendance coachAttendance, final CivicoreUpdateDataResultListener<CoachAttendance> listener) {
	//
	//		String url = buildInserCoachAttendanceURL(APIRequestCode.INSERT_COACH_ATTENDANCE, coachAttendance);
	//		client.get(url, new AsyncHttpResponseHandler() {
	//
	//			@Override
	//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
	//				Log.d("RESPONSE", new String(arg2));
	//				Serializer serializer = new Persister();
	//
	//				try {
	//					String response = new String(arg2);
	//					response = response.replaceAll("&", "&amp;");
	//					response = response.replaceAll("'", "&apos;");
	//					RemoteUpdateSuccessResult remoteInsertSuccessResult = serializer.read(RemoteUpdateSuccessResult.class, response);
	//					if (listener != null) {
	//						if (Long.valueOf(remoteInsertSuccessResult.getRemoteId()) > 0) {
	//							coachAttendance.setRemoteId(Long.valueOf(remoteInsertSuccessResult.getRemoteId()));
	//							listener.onRecordUpdateResult(coachAttendance);
	//						} else {
	//							listener.onRecordUpdateError();
	//						}
	//					}
	//				} catch (Exception e) {
	//					Log.e(LOG_TAG_CLASS, e.toString());
	//					if (listener != null) {
	//						listener.onRecordUpdateError();
	//					}
	//
	//				}
	//			}
	//
	//			@Override
	//			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
	//				Log.e("RESPONSE", arg3.toString());
	//
	//			}
	//
	//		});
	//
	//	}

	private String buildSelectURL(APIRequestCode apiRequestCode, int pageNumber) {
		Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();
		builder.appendQueryParameter(VERSION_PARAMETER_KEY, "2.0");
		String apiJSONString = SyncSelectRequestJSONStringBuilder.buildRequestJSONString(context, apiRequestCode, pageNumber);
		builder.appendQueryParameter(REQUEST_STRING_PARAMETER_KEY, apiJSONString);
		String URL = builder.build().toString();
		Log.i(LOG_TAG_URL, URL);
		return URL;
	}

	private String buildUpdateAthleteURL(APIRequestCode apiRequestCode, Athlete athlete) {
		Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();
		builder.appendQueryParameter(VERSION_PARAMETER_KEY, "2.0");
		String apiJSONString = UpdateAthleteRequestJSONStringBuilder.buildRequestJSONString(context, athlete);
		builder.appendQueryParameter(REQUEST_STRING_PARAMETER_KEY, apiJSONString);
		String URL = builder.build().toString();
		Log.i(LOG_TAG_URL, URL);
		return URL;
	}

	private String buildUpdateCoachURL(APIRequestCode apiRequestCode, Coach coach) {
		Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();
		builder.appendQueryParameter(VERSION_PARAMETER_KEY, "2.0");
		String apiJSONString = UpdateCoachRequestJSONStringBuilder.buildRequestJSONString(context, coach);
		builder.appendQueryParameter(REQUEST_STRING_PARAMETER_KEY, apiJSONString);
		String URL = builder.build().toString();
		Log.i(LOG_TAG_URL, URL);
		return URL;
	}

	private String buildUpdateAthleteAttendanceURL(APIRequestCode apiRequestCode, AthleteAttendance athleteAttendance) {
		Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();
		builder.appendQueryParameter(VERSION_PARAMETER_KEY, "2.0");
		String apiJSONString = UpdateAthleteAttendanceRequestJSONStringBuilder.buildRequestJSONString(context, athleteAttendance);
		builder.appendQueryParameter(REQUEST_STRING_PARAMETER_KEY, apiJSONString);
		String URL = builder.build().toString();
		Log.i(LOG_TAG_URL, URL);
		return URL;
	}

	private String buildInserAthleteAttendanceURL(APIRequestCode apiRequestCode, AthleteAttendance athleteAttendance) {
		Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();
		builder.appendQueryParameter(VERSION_PARAMETER_KEY, "2.0");
		String apiJSONString = InsertAthleteAttendanceRequestJSONStringBuilder.buildRequestJSONString(context, athleteAttendance);
		builder.appendQueryParameter(REQUEST_STRING_PARAMETER_KEY, apiJSONString);
		String URL = builder.build().toString();
		Log.i(LOG_TAG_URL, URL);
		return URL;
	}

	private String buildInserCoachAttendanceURL(APIRequestCode apiRequestCode, CoachAttendance coachAttendance) {
		Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();
		builder.appendQueryParameter(VERSION_PARAMETER_KEY, "2.0");
		String apiJSONString = InsertCoachAttendanceRequestJSONStringBuilder.buildRequestJSONString(context, coachAttendance);
		builder.appendQueryParameter(REQUEST_STRING_PARAMETER_KEY, apiJSONString);
		String URL = builder.build().toString();
		Log.i(LOG_TAG_URL, URL);
		return URL;
	}

	private String buildUpdateCoachAttendanceURL(APIRequestCode apiRequestCode, CoachAttendance coachAttendance) {
		Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();
		builder.appendQueryParameter(VERSION_PARAMETER_KEY, "2.0");
		String apiJSONString = UpdateCoachAttendanceRequestJSONStringBuilder.buildRequestJSONString(context, coachAttendance);
		builder.appendQueryParameter(REQUEST_STRING_PARAMETER_KEY, apiJSONString);
		String URL = builder.build().toString();
		Log.i(LOG_TAG_URL, URL);
		return URL;
	}

	public interface CivicoreDataResultListener<T> {
		public void onListResult(List<T> list);

		public void onListResultError();
	}

	public interface CivicoreUpdateDataResultListener<T> {
		public void onRecordUpdateResult(T object);

		public void onRecordUpdateError();
	}
}
