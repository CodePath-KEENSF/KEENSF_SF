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
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginActivity extends Activity {

	private TextView tvProgressUpdates;
	private Button btnLogin;
	private EditText etUserName;
	private EditText etPassword;
	private TextView tvProgressCount;
	private ProgressBar pbLoadingProgress;
	private int loadingProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		etUserName = (EditText) findViewById(R.id.etUserName);
		etPassword = (EditText) findViewById(R.id.etPassword);
		tvProgressUpdates = (TextView) findViewById(R.id.tvProgressUpdates);
		tvProgressCount = (TextView) findViewById(R.id.tvProgressCount);
		btnLogin = (Button) findViewById(R.id.btnApiLogin);
		pbLoadingProgress = (ProgressBar) findViewById(R.id.pbLoadingProgress);

		etUserName.requestFocus();
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchUIToLoginActivatedState();

				new RemoteDataLoader(LoginActivity.this, new DataLoaderResultListener() {

					@Override
					public void onDataLoaderResult() {
						LoginActivity.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								tvProgressUpdates.setText("");
								pbLoadingProgress.setProgress(100);
								tvProgressCount.setText("100%");

							}

						});
						openSessionAthleteCoachList();
						finish();

					}

					@Override
					public void onDataLoaderProgress(final String progressMessage, final int progressDelta) {
						LoginActivity.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								loadingProgress = loadingProgress + progressDelta;
								tvProgressUpdates.setText(progressMessage);
								tvProgressCount.setText(loadingProgress + "%");
								pbLoadingProgress.setProgress(loadingProgress);
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

	private void switchUIToLoginActivatedState() {
		etUserName.setVisibility(View.INVISIBLE);
		etPassword.setVisibility(View.INVISIBLE);
		btnLogin.setVisibility(View.INVISIBLE);
		closeInputFromWindow();
		pbLoadingProgress.setProgress(0);
		tvProgressCount.setText("0%");
		pbLoadingProgress.setVisibility(View.VISIBLE);
		tvProgressCount.setVisibility(View.VISIBLE);
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
