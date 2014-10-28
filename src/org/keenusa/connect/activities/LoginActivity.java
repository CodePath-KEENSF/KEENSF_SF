package org.keenusa.connect.activities;

import org.keenusa.connect.R;
import org.keenusa.connect.networking.RemoteDataLoader;
import org.keenusa.connect.networking.RemoteDataLoader.DataLoaderResultListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

	private TextView tvProgressUpdates;
	private Button btnLogin;
	private EditText etUserName;
	private EditText etPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		etUserName = (EditText) findViewById(R.id.etUserName);
		etPassword = (EditText) findViewById(R.id.etPassword);
		tvProgressUpdates = (TextView) findViewById(R.id.tvProgressUpdates);
		btnLogin = (Button) findViewById(R.id.btnApiLogin);

		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				etUserName.setEnabled(false);
				etPassword.setEnabled(false);
				btnLogin.setEnabled(false);
				closeInputFromWindow();
				new RemoteDataLoader(LoginActivity.this, new DataLoaderResultListener() {

					@Override
					public void onDataLoaderResult() {
						LoginActivity.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								tvProgressUpdates.setText("");

							}

						});
						openSessionAthleteCoachList();

					}

					@Override
					public void onDataLoaderProgress(final String progressMessage) {
						LoginActivity.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
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

	private void closeInputFromWindow() {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}
}
