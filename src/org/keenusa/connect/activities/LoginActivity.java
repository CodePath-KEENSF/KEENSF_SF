package org.keenusa.connect.activities;

import org.keenusa.connect.R;
import org.keenusa.connect.networking.RemoteDataLoader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

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
				new RemoteDataLoader(LoginActivity.this).start();
			}
		});
	}

	private void openSessionAthleteCoachList() {
		Intent i = new Intent(this, SessionAthleteCoachListActivity.class);
		startActivity(i);
	}

}
