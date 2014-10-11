//package org.keenusa.connect.networking;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.List;
//
//import org.keenusa.connect.models.Coach;
//import org.xmlpull.v1.XmlPullParserException;
//
//import android.os.AsyncTask;
//import android.util.Log;
//
//public class KeenCivicoreClient {
//
//	//  http://fwdev.civicore.com/keen/__revision=apiTest/
//	// API key bebcc6f2fc64175348e460321de42b53.3.7e9c26ee361ea9052a5033dee59e0673.1499008737
//
//	private KeenCivicoreClient() {
//	}
//
//	public static void fetchCoachList(final FetchCoachListResultListener listener) {
//
//		AsyncTask<String, Void, String> fetchCoachDataTask = new AsyncTask<String, Void, String>() {
//			@Override
//			protected String doInBackground(String... urls) {
//				try {
//
//					return loadXMLDataFromNetwork(urls[0]);
//
//					//					
//				} catch (IOException ioe) {
//					ioe.printStackTrace();
//					return null;
//				} catch (XmlPullParserException xppe) {
//					xppe.printStackTrace();
//					return null;
//				}
//			}
//
//			@Override
//			protected void onPostExecute(String result) {
//				Log.i("XML_RESULT", result);
//			}
//		};
//
//		fetchCoachDataTask.execute();
//	}
//
//	private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
//		InputStream stream = null;
//		// Instantiate the parser
//		CoachListCivicoreXmlParser coachListCivicoreXmlParser = new CoachListCivicoreXmlParser();
//		List<Coach> entries = null;
//
//		String urlString = "http://fwdev.civicore.com/keen/__revision=apiTest/?version=2.0&api={\"key\":\"bebcc6f2fc64175348e460321de42b53.3.7e9c26ee361ea9052a5033dee59e0673.1499008737\",\"function\":\"getAll\",\"tableName\":\"contacts\",\"fieldList\":[\"firstName\", \"lastName\"]}";
//
//		try {
//			stream = downloadUrl(urlString);
//			entries = stackOverflowXmlParser.parse(stream);
//			// Makes sure that the InputStream is closed after the app is
//			// finished using it.
//		} finally {
//			if (stream != null) {
//				stream.close();
//			}
//		}
//
//		// StackOverflowXmlParser returns a List (called "entries") of Entry objects.
//		// Each Entry object represents a single post in the XML feed.
//		// This section processes the entries list to combine each entry with HTML markup.
//		// Each entry is displayed in the UI as a link that optionally includes
//		// a text summary.
//		for (Entry entry : entries) {
//			htmlString.append("<p><a href='");
//			htmlString.append(entry.link);
//			htmlString.append("'>" + entry.title + "</a></p>");
//			// If the user set the preference to include summary text,
//			// adds it to the display.
//			if (pref) {
//				htmlString.append(entry.summary);
//			}
//		}
//		return htmlString.toString();
//	}
//
//	// Given a string representation of a URL, sets up a connection and gets
//	// an input stream.
//	private InputStream downloadUrl(String urlString) throws IOException {
//		URL url = new URL(urlString);
//		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		conn.setReadTimeout(10000 /* milliseconds */);
//		conn.setConnectTimeout(15000 /* milliseconds */);
//		conn.setRequestMethod("GET");
//		conn.setDoInput(true);
//		// Starts the query
//		conn.connect();
//		return conn.getInputStream();
//	}
//
//	public interface FetchCoachListResultListener {
//		public void onFetchCoachListResult(List<Coach> coaches);
//	}
//
//}
