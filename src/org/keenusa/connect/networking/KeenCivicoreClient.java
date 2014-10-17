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
	public static final String RECORD_ID_PARAMETER_KEY = "record_id";

	public enum APIRequestCode {
		COACH_LIST, ATHLETE_LIST, SESSION_LIST, PROGRAM_LIST, PROGRAM_ENROLMENT_LIST, ATHLETE_ATENDANCE_LIST, COACH_ATTENDANCE_LIST, AFFILIATE_LIST
	};

	public KeenCivicoreClient(Context context) {
		this.client = new AsyncHttpClient();
		this.context = context;
	}

	public void fetchCoachListData(final CivicoreDataResultListener<Coach> listener) {

		String url = buildURL(APIRequestCode.COACH_LIST);
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("RESPONSE", new String(arg2));
				Serializer serializer = new Persister();

				try {
					String response = new String(arg2);
					response = response.replaceAll("&", "&amp;");
					response = response.replaceAll("'", "&apos;");
					RemoteCoachList coaches = serializer.read(RemoteCoachList.class, response);
					if (listener != null) {
						listener.onListResult(Coach.fromRemoteCoachList(coaches.getRemoteCoaches()));
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

	public void fetchProgramListData(final CivicoreDataResultListener<KeenProgram> listener) {

		String url = buildURL(APIRequestCode.PROGRAM_LIST);
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("RESPONSE", new String(arg2));
				Serializer serializer = new Persister();

				try {
					String response = new String(arg2);
					response = response.replaceAll("&", "&amp;");
					response = response.replaceAll("'", "&apos;");
					response = response.replaceAll("<registrationConfirmation>", "<registrationConfirmation><![CDATA[");
					response = response.replaceAll("</registrationConfirmation>", "]]></registrationConfirmation>");
					response = response.replaceAll("<approvalEmailMessage>", "<approvalEmailMessage><![CDATA[");
					response = response.replaceAll("</approvalEmailMessage>", "]]></approvalEmailMessage>");
					RemoteProgramList remoteProgramList = serializer.read(RemoteProgramList.class, response);
					if (listener != null) {
						listener.onListResult(KeenProgram.fromRemoteProgramList(remoteProgramList.getRemotePrograms()));
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

	public void fetchAthleteListData(final CivicoreDataResultListener<Athlete> listener) {

		String url = buildURL(APIRequestCode.ATHLETE_LIST);
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("RESPONSE", new String(arg2));
				Serializer serializer = new Persister();

				try {
					String response = new String(arg2);
					response = response.replaceAll("&", "&amp;");
					response = response.replaceAll("'", "&apos;");

					RemoteAthleteList athletes = serializer.read(RemoteAthleteList.class, response);
					if (listener != null) {
						listener.onListResult(Athlete.fromRemoteAthleteList(athletes.getRemoteAthletes()));
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

	public void fetchSessionListData(final CivicoreDataResultListener<KeenSession> listener) {

		String url = buildURL(APIRequestCode.SESSION_LIST);
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

		String url = buildURL(APIRequestCode.PROGRAM_ENROLMENT_LIST);
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("RESPONSE", new String(arg2));
				Serializer serializer = new Persister();

				try {
					String response = new String(arg2);
					response = response.replaceAll("&", "&amp;");
					response = response.replaceAll("'", "&apos;");
					RemoteProgramEnrolmentList remoteProgramEnrolmentList = serializer.read(RemoteProgramEnrolmentList.class, response);
					if (listener != null) {
						listener.onListResult(KeenProgramEnrolment.fromRemoteProgramEnrolmentList(remoteProgramEnrolmentList
								.getRemoteProgramEnrolments()));
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

	public void fetchAthleteAttendanceListData(final CivicoreDataResultListener<AthleteAttendance> listener) {

		String url = buildURL(APIRequestCode.ATHLETE_ATENDANCE_LIST);
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("RESPONSE", new String(arg2));
				Serializer serializer = new Persister();

				try {
					String response = new String(arg2);
					response = response.replaceAll("&", "&amp;");
					response = response.replaceAll("'", "&apos;");
					RemoteAthleteAttendanceList remoteAthleteAttendanceList = serializer.read(RemoteAthleteAttendanceList.class, response);
					if (listener != null) {
						listener.onListResult(AthleteAttendance.fromRemoteAthleteAttendanceList(remoteAthleteAttendanceList
								.getRemoteAthleteAttendances()));
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

	public void fetchCoachAttendanceListData(final CivicoreDataResultListener<CoachAttendance> listener) {

		String url = buildURL(APIRequestCode.COACH_ATTENDANCE_LIST);
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("RESPONSE", new String(arg2));
				Serializer serializer = new Persister();

				try {
					String response = new String(arg2);
					response = response.replaceAll("&", "&amp;");
					response = response.replaceAll("'", "&apos;");
					RemoteCoachAttendanceList remoteCoachAttendanceList = serializer.read(RemoteCoachAttendanceList.class, response);
					if (listener != null) {
						listener.onListResult(CoachAttendance.fromRemoteCoachAttendanceList(remoteCoachAttendanceList.getRemoteCoachAttendances()));
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

	public void fetchAffiliateListData(final CivicoreDataResultListener<Affiliate> listener) {

		String url = buildURL(APIRequestCode.AFFILIATE_LIST);
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.d("RESPONSE", new String(arg2));
				Serializer serializer = new Persister();

				try {
					String response = new String(arg2);
					response = response.replaceAll("&", "&amp;");
					response = response.replaceAll("'", "&apos;");
					RemoteAffiliateList remoteAffiliateList = serializer.read(RemoteAffiliateList.class, response);
					if (listener != null) {
						listener.onListResult(Affiliate.fromRemoteAffiliateList(remoteAffiliateList.getRemoteAffiliates()));
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

	private String buildURL(APIRequestCode apiRequestCode) {

		Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();
		builder.appendQueryParameter(VERSION_PARAMETER_KEY, "2.0");

		String apiJSONString = ApiRequestJSONStringBuilder.buildRequestJSONString(context, apiRequestCode, 1);

		builder.appendQueryParameter(REQUEST_STRING_PARAMETER_KEY, apiJSONString);

		//		if (remoteRecordId > 0) {
		//			builder.appendQueryParameter(RECORD_ID_PARAMETER_KEY, String.valueOf(remoteRecordId));
		//		}

		String URL = builder.build().toString();

		Log.i(LOG_TAG_URL, URL);
		return URL;
	}

	public interface CivicoreDataResultListener<T> {
		public void onListResult(List<T> list);
	}
}
