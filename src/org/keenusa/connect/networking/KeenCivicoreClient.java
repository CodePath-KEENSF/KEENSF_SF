package org.keenusa.connect.networking;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
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
import com.loopj.android.http.ResponseHandlerInterface;

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
		COACH_LIST, ATHLETE_LIST, SESSION_LIST, PROGRAM_LIST, PROGRAM_ENROLMENT_LIST, ATHLETE_ATENDANCE_LIST, COACH_ATTENDANCE_LIST, AFFILIATE_LIST, UPDATE_ATHLETE_PROFILE, UPDATE_COACH_PROFILE,
	};

	public KeenCivicoreClient(Context context) {
		this.client = new AsyncHttpClient();
		this.context = context;
	}

	public void fetchCoachListData(final CivicoreDataResultListener<Coach> listener) {

		String url = buildSelectURL(APIRequestCode.COACH_LIST, 1);
		client.get(url, new AsyncHttpResponseHandler() {

			public void onSuccess(int arg0, Header[] arg1, List<Coach> coachList) {
				if (listener != null) {
					listener.onListResult(coachList);
				}

			}

			@Override
			public void onSuccess(final int arg0, final Header[] arg1, final byte[] arg2) {
				Runnable parser = new Runnable() {

					@Override
					public void run() {
						Log.d("RESPONSE", new String(arg2));
						Serializer serializer = new Persister();

						try {
							String response = new String(arg2);
							response = response.replaceAll("&", "&amp;");
							response = response.replaceAll("'", "&apos;");
							final RemoteCoachList coaches = serializer.read(RemoteCoachList.class, response);

							postRunnable(new Runnable() {

								@Override
								public void run() {
									onSuccess(arg0, arg1, Coach.fromRemoteCoachList(coaches.getRemoteCoaches()));

								}
							});
						} catch (Exception e) {
							Log.e(LOG_TAG_CLASS, e.toString());
						}
					}
				};
				new Thread(parser).start();

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.d("RESPONSE", arg3.toString());

			}

		});

	}

	public void fetchProgramListData(final CivicoreDataResultListener<KeenProgram> listener) {

		String url = buildSelectURL(APIRequestCode.PROGRAM_LIST, 1);
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

		String url = buildSelectURL(APIRequestCode.ATHLETE_LIST, 1);
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

		String url = buildSelectURL(APIRequestCode.ATHLETE_ATENDANCE_LIST, 1);
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
				// TODO Auto-generated method stub
				super.onPostProcessResponse(instance, response);
			}

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

		String url = buildSelectURL(APIRequestCode.COACH_ATTENDANCE_LIST, 1);
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

		String url = buildSelectURL(APIRequestCode.AFFILIATE_LIST, 1);
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
							listener.onUpdateResult(athlete);
						} else {
							listener.onUpdateError();
						}
					}
				} catch (Exception e) {
					Log.e(LOG_TAG_CLASS, e.toString());
					if (listener != null) {
						listener.onUpdateError();
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

		String url = buildUpdateCoachURL(APIRequestCode.UPDATE_ATHLETE_PROFILE, coach);
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
							listener.onUpdateResult(coach);
						} else {
							listener.onUpdateError();
						}
					}
				} catch (Exception e) {
					Log.e(LOG_TAG_CLASS, e.toString());
					if (listener != null) {
						listener.onUpdateError();
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
		String apiJSONString = ApiSelectRequestJSONStringBuilder.buildRequestJSONString(context, apiRequestCode, pageNumber);
		builder.appendQueryParameter(REQUEST_STRING_PARAMETER_KEY, apiJSONString);
		String URL = builder.build().toString();
		Log.i(LOG_TAG_URL, URL);
		return URL;
	}

	private String buildUpdateAthleteURL(APIRequestCode apiRequestCode, Athlete athlete) {
		Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();
		builder.appendQueryParameter(VERSION_PARAMETER_KEY, "2.0");
		String apiJSONString = UpdateAthleteApiRequestJSONStringBuilder.buildRequestJSONString(context, athlete);
		builder.appendQueryParameter(REQUEST_STRING_PARAMETER_KEY, apiJSONString);
		String URL = builder.build().toString();
		Log.i(LOG_TAG_URL, URL);
		return URL;
	}

	private String buildUpdateCoachURL(APIRequestCode apiRequestCode, Coach coach) {
		Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();
		builder.appendQueryParameter(VERSION_PARAMETER_KEY, "2.0");
		String apiJSONString = UpdateCoachApiRequestJSONStringBuilder.buildRequestJSONString(context, coach);
		builder.appendQueryParameter(REQUEST_STRING_PARAMETER_KEY, apiJSONString);
		String URL = builder.build().toString();
		Log.i(LOG_TAG_URL, URL);
		return URL;
	}

	public interface CivicoreDataResultListener<T> {
		public void onListResult(List<T> list);
	}

	public interface CivicoreUpdateDataResultListener<T> {
		public void onUpdateResult(T object);

		public void onUpdateError();
	}
}
