package org.keenusa.connect.networking;

import android.content.Context;
import android.util.Log;

public abstract class PostAndUpdateRemoteDataTask<T> extends Thread {

	public static final String LOG_TAG_CLASS = PostAndUpdateRemoteDataTask.class.getSimpleName();

	protected final Context context;
	protected final SyncKeenCivicoreClient client;
	protected final PostAndUpdateRemoteDataTaskListener<T> listener;

	public PostAndUpdateRemoteDataTask(Context context, PostAndUpdateRemoteDataTaskListener<T> listener) {
		super();
		this.context = context;
		this.client = new SyncKeenCivicoreClient(context);
		this.listener = listener;
	}

	public abstract T getDTOObject();

	public abstract boolean postData() throws Exception;

	public abstract boolean updateDataLocally();

	@Override
	public void run() {
		if (postDataToRemote() && updateDataLocally()) {
			postSuccessResult();
		} else {
			postErrorResult();
		}
	}

	private void postSuccessResult() {
		if (listener != null)
			listener.onDataPostAndUpdateTaskSuccess(getDTOObject());
	}

	private boolean postDataToRemote() {
		boolean isPosted = false;
		try {
			if (postData()) {
				isPosted = true;
			} else {
				Log.e(LOG_TAG_CLASS, getDTOObject().getClass().getSimpleName() + " data is not saved to Remote");
			}
		} catch (Exception e) {
			Log.e(LOG_TAG_CLASS, e.toString());
		}
		return isPosted;
	}

	private void postErrorResult() {
		if (listener != null)
			listener.onPostAndUpdateTaskError();
	}

	public interface PostAndUpdateRemoteDataTaskListener<T> {
		public void onDataPostAndUpdateTaskSuccess(T recordDTO);

		public void onPostAndUpdateTaskError();

		public void onPostAndUpdateTaskProgress(String progressMessage);
	}

}
