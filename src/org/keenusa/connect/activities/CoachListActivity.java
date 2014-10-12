package org.keenusa.connect.activities;

import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.fragments.CoachesFragment;
import org.keenusa.connect.models.Coach;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.networking.KeenCivicoreClient.CivicoreDataResultListener;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

public class CoachListActivity extends FragmentActivity implements CivicoreDataResultListener<Coach> {

	CoachesFragment coachesFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coach_list);

		FragmentTransaction sft = getSupportFragmentManager().beginTransaction();
		coachesFragment = new CoachesFragment();
		sft.add(R.id.flContainer, coachesFragment);
		sft.commit();

		KeenCivicoreClient client = new KeenCivicoreClient(this);
		client.fetchCoachListData(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.coach_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListResult(List<Coach> list) {
		coachesFragment.addAPIData(list);
	}

}
