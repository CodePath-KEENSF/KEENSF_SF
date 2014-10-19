package org.keenusa.connect.utilities;

import android.content.Context;
import android.util.Log;

public class DebugInfo {
	public static final int currentMethod = 3;
	public static final int depth = 1;

	public static void stackTrace() {
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		for (int i = currentMethod; i < currentMethod + depth; i++) {
			Log.d("StackTrace",
					ste[i].getClassName() + " # " + ste[i].getMethodName());
		}
		Log.d("StackTrace", "\n");
	}
	
	public static void showToast(Context context, String message){
		//Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	
	public static void logError(String message){
		Log.e("Error",message);
	}

	public static void logMessage(String message){
		Log.d("DebugMessage",message);
	}

	public static void tempMessage(String message){
		Log.d("temp",message);
	}

}
