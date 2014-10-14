package org.keenusa.connect.networking;

import java.util.List;

import org.apache.http.Header;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.Coach;
import org.keenusa.connect.models.remote.RemoteAthleteList;
import org.keenusa.connect.models.remote.RemoteCoachList;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class KeenCivicoreClient2 {

	private final AsyncHttpClient client;
	private Context context;

	public static final String LOG_TAG_CLASS = KeenCivicoreClient2.class.getSimpleName();
	public static final String LOG_TAG_URL = "URL";
	public static final String BASE_URL = "http://fwdev.civicore.com/keen/__revision=apiTest/";
	//	public static final String URL_STRING = "http://fwdev.civicore.com/keen/__revision=apiTest/?version=2.0&api={\"key\":\"bebcc6f2fc64175348e460321de42b53.3.7e9c26ee361ea9052a5033dee59e0673.1499008737\",\"function\":\"getAll\",\"tableName\":\"contacts\",\"fieldList\":[\"firstName\",\"lastName\"],\"pageSize\":\"50\",\"pageNumber\":\"1\"}";

	public static final String VERSION_PARAMETER_KEY = "version";
	public static final String REQUEST_STRING_PARAMETER_KEY = "api";

	public enum APIRequestCode {
		COACH_LIST, ATHLETE_LIST
	};

	public KeenCivicoreClient2(Context context) {
		this.client = new AsyncHttpClient();
		this.context = context;
	}

	public void fetchCoachListData(final CivicoreDataResultListener<Coach> listener) {

		String url = buildURL(KeenCivicoreClient.APIRequestCode.COACH_LIST);
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

	public void fetchAthleteListData(final CivicoreDataResultListener<Athlete> listener) {

		String url = buildURL(KeenCivicoreClient.APIRequestCode.ATHLETE_LIST);
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

	private String buildURL(KeenCivicoreClient.APIRequestCode apiRequestCode) {

		Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();
		builder.appendQueryParameter(VERSION_PARAMETER_KEY, "2.0");

		String apiJSONString = ApiRequestJSONStringBuilder.buildRequestJSONString(context, apiRequestCode, 1);

		builder.appendQueryParameter(REQUEST_STRING_PARAMETER_KEY, apiJSONString);
		String URL = builder.build().toString();

		Log.i(LOG_TAG_URL, URL);
		return URL;
	}

	public interface CivicoreDataResultListener<T> {
		public void onListResult(List<T> list);
	}
}
