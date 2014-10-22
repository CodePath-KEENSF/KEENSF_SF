package org.keenusa.connect.networking;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.util.Log;

public class SyncXMLHttpResponseHandler<T> {

	public static final String LOG_TAG_CLASS = SyncXMLHttpResponseHandler.class.getSimpleName();

	private final Class<T> target;

	public SyncXMLHttpResponseHandler(Class<T> target) {
		this.target = target;
	}

	public void onXMLProcessingError(Throwable throwable) {
		Log.e(LOG_TAG_CLASS, throwable.toString());
	}

	public T parseXMLResponse(String response) {
		Log.d("RESPONSE", response);
		Serializer serializer = new Persister();
		String cleanResponse = cleanResponse(response);
		try {
			T remoteData = serializer.read(target, cleanResponse);
			return remoteData;
		} catch (Exception e) {
			onXMLProcessingError(e);
		}
		return null;
	}

	private String cleanResponse(String response) {
		response = response.replaceAll("&", "&amp;");
		response = response.replaceAll("'", "&apos;");
		response = response.replaceAll("<registrationConfirmation>", "<registrationConfirmation><![CDATA[");
		response = response.replaceAll("</registrationConfirmation>", "]]></registrationConfirmation>");
		response = response.replaceAll("<approvalEmailMessage>", "<approvalEmailMessage><![CDATA[");
		response = response.replaceAll("</approvalEmailMessage>", "]]></approvalEmailMessage>");
		return response;
	}

}
