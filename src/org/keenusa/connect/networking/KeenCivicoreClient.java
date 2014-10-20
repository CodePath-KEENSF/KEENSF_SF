package org.keenusa.connect.networking;

import java.util.List;

import org.apache.http.Header;
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
import org.keenusa.connect.models.remote.RemoteUpdateSuccessResult;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class KeenCivicoreClient {

	private final AsyncHttpClient client;
	private Context context;

	public static final String LOG_TAG_CLASS = KeenCivicoreClient.class.getSimpleName();
	public static final String LOG_TAG_URL = "URL";
	public static final String BASE_URL = "http://fwdev.civicore.com/keen/__revision=apiTest/";
	//	public static final String URL_STRING = "http://fwdev.civicore.com/keen/__revision=apiTest/?version=2.0&api={\"key\":\"bebcc6f2fc64175348e460321de42b53.3.7e9c26ee361ea9052a5033dee59e0673.1499008737\",\"function\":\"getAll\",\"tableName\":\"contacts\",\"fieldList\":[\"firstName\",\"lastName\"],\"pageSize\":\"50\",\"pageNumber\":\"1\"}";

	public static final String VERSION_PARAMETER_KEY = "version";
	public static final String REQUEST_STRING_PARAMETER_KEY = "api";

	public enum APIRequestCode {
		COACH_LIST, ATHLETE_LIST, SESSION_LIST, PROGRAM_LIST, PROGRAM_ENROLMENT_LIST, ATHLETE_ATENDANCE_LIST, COACH_ATTENDANCE_LIST, AFFILIATE_LIST, UPDATE_ATHLETE_PROFILE, UPDATE_COACH_PROFILE, UPDATE_ATHLETE_ATTENDANCE, UPDATE_COACH_ATTENDANCE, INSERT_ATHLETE_ATTENDANCE
	};

	public KeenCivicoreClient(Context context) {
		this.client = new AsyncHttpClient();
		this.context = context;
	}

	public void fetchCoachListData(final CivicoreDataResultListener<Coach> listener) {
		String url = buildSelectURL(APIRequestCode.COACH_LIST, 1);
		client.get(url, new CustomXMLHttpResponseHandler<RemoteCoachList>(RemoteCoachList.class) {
			@Override
			public void onDataReceived(RemoteCoachList result) {
				if (listener != null) {
					listener.onListResult(Coach.fromRemoteCoachList(result.getRemoteCoaches()));
				}
			}

			@Override
			public void onXMLProcessingError(Throwable throwable) {
				if (listener != null) {
					listener.onListResultError();
				}
				Log.e(LOG_TAG_CLASS, throwable.toString());
				super.onXMLProcessingError(throwable);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable throwable) {
				if (listener != null) {
					listener.onListResultError();
				}
				Log.e(LOG_TAG_CLASS, throwable.toString());
				super.onXMLProcessingError(throwable);

			}
		});
	}

	public void fetchProgramListData(final CivicoreDataResultListener<KeenProgram> listener) {
		String url = buildSelectURL(APIRequestCode.PROGRAM_LIST, 1);
		client.get(url, new CustomXMLHttpResponseHandler<RemoteProgramList>(RemoteProgramList.class) {
			@Override
			public void onDataReceived(RemoteProgramList result) {
				if (listener != null) {
					listener.onListResult(KeenProgram.fromRemoteProgramList(result.getRemotePrograms()));
				}
			}

			@Override
			public void onXMLProcessingError(Throwable throwable) {
				if (listener != null) {
					listener.onListResultError();
				}
				Log.e(LOG_TAG_CLASS, throwable.toString());
				super.onXMLProcessingError(throwable);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable throwable) {
				if (listener != null) {
					listener.onListResultError();
				}
				Log.e(LOG_TAG_CLASS, throwable.toString());
				super.onXMLProcessingError(throwable);

			}
		});
	}

	public void fetchAthleteListData(final CivicoreDataResultListener<Athlete> listener) {
		String url = buildSelectURL(APIRequestCode.ATHLETE_LIST, 1);
		client.get(url, new CustomXMLHttpResponseHandler<RemoteAthleteList>(RemoteAthleteList.class) {
			@Override
			public void onDataReceived(RemoteAthleteList result) {
				if (listener != null) {
					listener.onListResult(Athlete.fromRemoteAthleteList(result.getRemoteAthletes()));
				}
			}

			@Override
			public void onXMLProcessingError(Throwable throwable) {
				if (listener != null) {
					listener.onListResultError();
				}
				Log.e(LOG_TAG_CLASS, throwable.toString());
				super.onXMLProcessingError(throwable);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable throwable) {
				if (listener != null) {
					listener.onListResultError();
				}
				Log.e(LOG_TAG_CLASS, throwable.toString());
				super.onXMLProcessingError(throwable);

			}
		});
	}

	// TODO running session list fetch asynchronously crashes Sessions fragment because it expects populated session list in order to be created
	//	public void fetchSessionListData(final CivicoreDataResultListener<KeenSession> listener) {
	//		String url = buildSelectURL(APIRequestCode.SESSION_LIST, 1);
	//		client.get(url, new CiviCoreXMLResponseHandler<RemoteSessionList>(RemoteSessionList.class) {
	//			@Override
	//			public void onDataReceived(RemoteSessionList result) {
	//				if (listener != null) {
	//					listener.onListResult(KeenSession.fromRemoteSessionList(result.getRemoteSessions()));
	//				}
	//			}
	//		});
	//	}

	public void fetchSessionListData(final CivicoreDataResultListener<KeenSession> listener) {

		String url = buildSelectURL(APIRequestCode.SESSION_LIST, 1);
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("RESPONSE", new String(arg2));
				Serializer serializer = new Persister();

				try {
					String response = new String(arg2);
					response = response.replaceAll("&", "&amp;");
					response = response.replaceAll("'", "&apos;");
					RemoteSessionList remoteSessionList = serializer.read(RemoteSessionList.class, response);
					if (listener != null) {
						listener.onListResult(KeenSession.fromRemoteSessionList(remoteSessionList.getRemoteSessions()));
					}
				} catch (Exception e) {
					Log.e(LOG_TAG_CLASS, e.toString());
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.d("RESPONSE", arg3.toString());

			}

		});

	}

	public void fetchProgramEnrolmentListData(final CivicoreDataResultListener<KeenProgramEnrolment> listener) {
		String url = buildSelectURL(APIRequestCode.PROGRAM_ENROLMENT_LIST, 1);
		client.get(url, new CustomXMLHttpResponseHandler<RemoteProgramEnrolmentList>(RemoteProgramEnrolmentList.class) {
			@Override
			public void onDataReceived(RemoteProgramEnrolmentList result) {
				if (listener != null) {
					listener.onListResult(KeenProgramEnrolment.fromRemoteProgramEnrolmentList(result.getRemoteProgramEnrolments()));
				}
			}

			@Override
			public void onXMLProcessingError(Throwable throwable) {
				if (listener != null) {
					listener.onListResultError();
				}
				Log.e(LOG_TAG_CLASS, throwable.toString());
				super.onXMLProcessingError(throwable);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable throwable) {
				if (listener != null) {
					listener.onListResultError();
				}
				Log.e(LOG_TAG_CLASS, throwable.toString());
				super.onXMLProcessingError(throwable);

			}
		});
	}

	public void fetchAthleteAttendanceListData(final CivicoreDataResultListener<AthleteAttendance> listener) {
		String url = buildSelectURL(APIRequestCode.ATHLETE_ATENDANCE_LIST, 1);
		client.get(url, new CustomXMLHttpResponseHandler<RemoteAthleteAttendanceList>(RemoteAthleteAttendanceList.class) {
			@Override
			public void onDataReceived(RemoteAthleteAttendanceList result) {
				if (listener != null) {
					listener.onListResult(AthleteAttendance.fromRemoteAthleteAttendanceList(result.getRemoteAthleteAttendances()));
				}
			}

			@Override
			public void onXMLProcessingError(Throwable throwable) {
				if (listener != null) {
					listener.onListResultError();
				}
				Log.e(LOG_TAG_CLASS, throwable.toString());
				super.onXMLProcessingError(throwable);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable throwable) {
				if (listener != null) {
					listener.onListResultError();
				}
				Log.e(LOG_TAG_CLASS, throwable.toString());
				super.onXMLProcessingError(throwable);

			}
		});
	}

	public void fetchCoachAttendanceListData(final CivicoreDataResultListener<CoachAttendance> listener) {
		String url = buildSelectURL(APIRequestCode.COACH_ATTENDANCE_LIST, 1);
		client.get(url, new CustomXMLHttpResponseHandler<RemoteCoachAttendanceList>(RemoteCoachAttendanceList.class) {
			@Override
			public void onDataReceived(RemoteCoachAttendanceList result) {
				if (listener != null) {
					listener.onListResult(CoachAttendance.fromRemoteCoachAttendanceList(result.getRemoteCoachAttendances()));
				}
			}

			@Override
			public void onXMLProcessingError(Throwable throwable) {
				if (listener != null) {
					listener.onListResultError();
				}
				Log.e(LOG_TAG_CLASS, throwable.toString());
				super.onXMLProcessingError(throwable);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable throwable) {
				if (listener != null) {
					listener.onListResultError();
				}
				Log.e(LOG_TAG_CLASS, throwable.toString());
				super.onXMLProcessingError(throwable);

			}
		});
	}

	public void fetchAffiliateListData(final CivicoreDataResultListener<Affiliate> listener) {
		String url = buildSelectURL(APIRequestCode.AFFILIATE_LIST, 1);
		client.get(url, new CustomXMLHttpResponseHandler<RemoteAffiliateList>(RemoteAffiliateList.class) {
			@Override
			public void onDataReceived(RemoteAffiliateList result) {
				if (listener != null) {
					listener.onListResult(Affiliate.fromRemoteAffiliateList(result.getRemoteAffiliates()));
				}
			}

			@Override
			public void onXMLProcessingError(Throwable throwable) {
				if (listener != null) {
					listener.onListResultError();
				}
				Log.e(LOG_TAG_CLASS, throwable.toString());
				super.onXMLProcessingError(throwable);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable throwable) {
				if (listener != null) {
					listener.onListResultError();
				}
				Log.e(LOG_TAG_CLASS, throwable.toString());
				super.onXMLProcessingError(throwable);

			}
		});
	}

	public void updateAfthetProfileRecord(final Athlete athlete, final CivicoreUpdateDataResultListener<Athlete> listener) {

		String url = buildUpdateAthleteURL(APIRequestCode.UPDATE_ATHLETE_PROFILE, athlete);
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("RESPONSE", new String(arg2));
				Serializer serializer = new Persister();

				try {
					String response = new String(arg2);
					response = response.replaceAll("&", "&amp;");
					response = response.replaceAll("'", "&apos;");
					RemoteUpdateSuccessResult remoteUpdateSuccessResult = serializer.read(RemoteUpdateSuccessResult.class, response);
					if (listener != null) {
						if (Long.valueOf(remoteUpdateSuccessResult.getRemoteId()) == athlete.getRemoteId()) {
							listener.onRecordUpdateResult(athlete);
						} else {
							listener.onRecordUpdateError();
						}
					}
				} catch (Exception e) {
					Log.e(LOG_TAG_CLASS, e.toString());
					if (listener != null) {
						listener.onRecordUpdateError();
					}

				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.e("RESPONSE", arg3.toString());

			}

		});

	}

	public void updateCoachProfileRecord(final Coach coach, final CivicoreUpdateDataResultListener<Coach> listener) {

		String url = buildUpdateCoachURL(APIRequestCode.UPDATE_COACH_PROFILE, coach);
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("RESPONSE", new String(arg2));
				Serializer serializer = new Persister();

				try {
					String response = new String(arg2);
					response = response.replaceAll("&", "&amp;");
					response = response.replaceAll("'", "&apos;");
					RemoteUpdateSuccessResult remoteUpdateSuccessResult = serializer.read(RemoteUpdateSuccessResult.class, response);
					if (listener != null) {
						if (Long.valueOf(remoteUpdateSuccessResult.getRemoteId()) == coach.getRemoteId()) {
							listener.onRecordUpdateResult(coach);
						} else {
							listener.onRecordUpdateError();
						}
					}
				} catch (Exception e) {
					Log.e(LOG_TAG_CLASS, e.toString());
					if (listener != null) {
						listener.onRecordUpdateError();
					}

				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.e("RESPONSE", arg3.toString());

			}

		});

	}

	public void updateAfthetAttendanceRecord(final AthleteAttendance athleteAttendance,
			final CivicoreUpdateDataResultListener<AthleteAttendance> listener) {

		String url = buildUpdateAthleteAttendanceURL(APIRequestCode.UPDATE_ATHLETE_ATTENDANCE, athleteAttendance);
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("RESPONSE", new String(arg2));
				Serializer serializer = new Persister();

				try {
					String response = new String(arg2);
					response = response.replaceAll("&", "&amp;");
					response = response.replaceAll("'", "&apos;");
					RemoteUpdateSuccessResult remoteUpdateSuccessResult = serializer.read(RemoteUpdateSuccessResult.class, response);
					if (listener != null) {
						if (Long.valueOf(remoteUpdateSuccessResult.getRemoteId()) == athleteAttendance.getRemoteId()) {
							listener.onRecordUpdateResult(athleteAttendance);
						} else {
							listener.onRecordUpdateError();
						}
					}
				} catch (Exception e) {
					Log.e(LOG_TAG_CLASS, e.toString());
					if (listener != null) {
						listener.onRecordUpdateError();
					}

				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.e("RESPONSE", arg3.toString());

			}

		});

	}

	public void updateCoachAttendanceRecord(final CoachAttendance coachAttendance, final CivicoreUpdateDataResultListener<CoachAttendance> listener) {

		String url = buildUpdateCoachAttendanceURL(APIRequestCode.UPDATE_COACH_ATTENDANCE, coachAttendance);
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("RESPONSE", new String(arg2));
				Serializer serializer = new Persister();

				try {
					String response = new String(arg2);
					response = response.replaceAll("&", "&amp;");
					response = response.replaceAll("'", "&apos;");
					RemoteUpdateSuccessResult remoteUpdateSuccessResult = serializer.read(RemoteUpdateSuccessResult.class, response);
					if (listener != null) {
						if (Long.valueOf(remoteUpdateSuccessResult.getRemoteId()) == coachAttendance.getRemoteId()) {
							listener.onRecordUpdateResult(coachAttendance);
						} else {
							listener.onRecordUpdateError();
						}
					}
				} catch (Exception e) {
					Log.e(LOG_TAG_CLASS, e.toString());
					if (listener != null) {
						listener.onRecordUpdateError();
					}

				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.e("RESPONSE", arg3.toString());

			}

		});

	}

	public void insertNewAfthetAttendanceRecord(final AthleteAttendance athleteAttendance,
			final CivicoreUpdateDataResultListener<AthleteAttendance> listener) {

		String url = buildInserAthleteAttendanceURL(APIRequestCode.INSERT_ATHLETE_ATTENDANCE, athleteAttendance);
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("RESPONSE", new String(arg2));
				Serializer serializer = new Persister();

				try {
					String response = new String(arg2);
					response = response.replaceAll("&", "&amp;");
					response = response.replaceAll("'", "&apos;");
					RemoteUpdateSuccessResult remoteInsertSuccessResult = serializer.read(RemoteUpdateSuccessResult.class, response);
					if (listener != null) {
						if (Long.valueOf(remoteInsertSuccessResult.getRemoteId()) > 0) {
							athleteAttendance.setRemoteId(Long.valueOf(remoteInsertSuccessResult.getRemoteId()));
							listener.onRecordUpdateResult(athleteAttendance);
						} else {
							listener.onRecordUpdateError();
						}
					}
				} catch (Exception e) {
					Log.e(LOG_TAG_CLASS, e.toString());
					if (listener != null) {
						listener.onRecordUpdateError();
					}

				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.e("RESPONSE", arg3.toString());

			}

		});

	}

	private String buildSelectURL(APIRequestCode apiRequestCode, int pageNumber) {
		Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();
		builder.appendQueryParameter(VERSION_PARAMETER_KEY, "2.0");
		String apiJSONString = SelectRequestJSONStringBuilder.buildRequestJSONString(context, apiRequestCode, pageNumber);
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
