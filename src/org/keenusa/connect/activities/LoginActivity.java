package org.keenusa.connect.activities;

import org.keenusa.connect.R;
import org.keenusa.connect.networking.RemoteDataLoader;
import org.keenusa.connect.networking.RemoteDataLoader.DataLoaderResultListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends Activity {

	private TextView tvProgressUpdates;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		tvProgressUpdates = (TextView) findViewById(R.id.tvProgressUpdates);

		Button btnLogin = (Button) findViewById(R.id.btnApiLogin);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openSessionAthleteCoachList();
			}
		});

		Button btnLoginWithDataLoad = (Button) findViewById(R.id.btnLoginWithDataLoad);
		btnLoginWithDataLoad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new RemoteDataLoader(LoginActivity.this, new DataLoaderResultListener() {

					@Override
					public void onDataLoaderResult() {
						LoginActivity.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								tvProgressUpdates.setVisibility(View.GONE);

							}

						});
						Intent i = new Intent(LoginActivity.this, SessionListActivity.class);
						startActivity(i);

					}

					@Override
					public void onDataLoaderProgress(final String progressMessage) {
						LoginActivity.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								tvProgressUpdates.setVisibility(View.VISIBLE);
								tvProgressUpdates.setText(progressMessage);

							}
						});

					}

					@Override
					public void onDataLoaderError() {
						// TODO Auto-generated method stub

					}
				}).start();
			}
		});
	}

	private void openSessionAthleteCoachList() {
		Intent i = new Intent(this, SessionAthleteCoachListActivity.class);
		startActivity(i);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
	}

}
