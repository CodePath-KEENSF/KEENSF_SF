package org.keenusa.connect.networking;

import org.apache.http.Header;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.util.Log;

import com.loopj.android.http.TextHttpResponseHandler;

public abstract class CustomXMLHttpResponseHandler<T> extends TextHttpResponseHandler {

	public static final String LOG_TAG_CLASS = CustomXMLHttpResponseHandler.class.getSimpleName();

	private final Class<T> target;

	public CustomXMLHttpResponseHandler(Class<T> target) {
		this.target = target;
	}

	@Override
	public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
	}

	public void onXMLProcessingError(Throwable throwable) {
	}

	@Override
	public void onSuccess(int arg0, Header[] arg1, final String response) {
		Runnable parser = new Runnable() {
			@Override
			public void run() {
				Log.d("RESPONSE", response);
				Serializer serializer = new Persister();
				String cleanResponse = cleanResponse(response);
				try {
					final T remoteData = serializer.read(target, cleanResponse);
					postRunnable(new Runnable() {
						@Override
						public void run() {
							onDataReceived(remoteData);
						}
					});
				} catch (final Exception e) {
					Log.e(LOG_TAG_CLASS, e.toString());
					postRunnable(new Runnable() {
						@Override
						public void run() {
							onXMLProcessingError(e);
						}
					});
				}
			}
		};
		new Thread(parser).start();
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

	public abstract void onDataReceived(T result);
}
