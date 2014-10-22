package org.keenusa.connect.networking;

import java.io.IOException;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class RemoteDataLoader extends Thread {

	final Context context;

	public RemoteDataLoader(Context context) {
		super();
		this.context = context;
	}

	@Override
	public void run() {
		// fetch programs
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder().url("http://www.google.com").build();

		try {
			Response response = client.newCall(request).execute();
			String responseStr = response.body().string();
			System.err.println(responseStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//		client.fetchProgramListData(new CivicoreDataResultListener<KeenProgram>() {
		//
		//			@Override
		//			public void onListResultError() {
		//				Toast.makeText(context, "Error in fetching Program data from CiviCore", Toast.LENGTH_SHORT).show();
		//
		//			}
		//
		//			@Override
		//			public void onListResult(List<KeenProgram> list) {
		//				ProgramDAO programDAO = new ProgramDAO(context);
		//				for (KeenProgram program : list) {
		//					programDAO.saveNewProgram(program);
		//				}
		//
		//			}
		//		});
		//		client.fetchSessionListData(new CivicoreDataResultListener<KeenSession>() {
		//
		//			@Override
		//			public void onListResultError() {
		//				Toast.makeText(context, "Error in fetching Program data from CiviCore", Toast.LENGTH_SHORT).show();
		//
		//			}
		//
		//			@Override
		//			public void onListResult(List<KeenSession> list) {
		//				SessionDAO sessionDAO = new SessionDAO(context);
		//				for (KeenSession session : list) {
		//					sessionDAO.saveNewSession(session);
		//				}
		//
		//			}
		//		});

	}
}
