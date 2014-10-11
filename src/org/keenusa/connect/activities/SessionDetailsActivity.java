package org.keenusa.connect.activities;

import org.keenusa.connect.R;
import org.keenusa.connect.fragments.AtheletsFragment;
import org.keenusa.connect.fragments.CoachesFragment;
import org.keenusa.connect.listeners.FragmentTabListener;
import org.keenusa.connect.models.Program;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

public class SessionDetailsActivity extends FragmentActivity {
	private TextView tvProgramNameLabel, tvLocationLabel, tvDateLabel, tvSessionPeriodLabel;
	private TextView tvProgramName, tvLocation, tvDate, tvSessionPeriod;
	Program program = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_session_details);
		initialHeader();
		setupTabs();
	}

	private void initialHeader() {
		tvProgramNameLabel = (TextView) findViewById(R.id.tvProgramNameLabel);
		tvLocationLabel = (TextView) findViewById(R.id.tvLocationLabel);
		tvDateLabel = (TextView) findViewById(R.id.tvDateLabel);
		tvSessionPeriodLabel = (TextView) findViewById(R.id.tvSessionPeriodLabel);
		tvProgramName = (TextView) findViewById(R.id.tvProgramName);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvSessionPeriod = (TextView) findViewById(R.id.tvSessionPeriod);
		
//		tvProgramName.setText(program.getName());
//		tvLocation.setLocation(program.getLocation());
//		tvDate.setText(program.getProgramTimes());
//		tvSessionPeriod.setText(program.getProgramPeriod);
	}

	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab coaches = actionBar
			.newTab()
			.setText("Coaches")
//			.setIcon(R.drawable.ic_home)
			.setTag("CoachesFragment")
			.setTabListener(
				new FragmentTabListener<CoachesFragment>(R.id.flContainer, this, "Coaches", CoachesFragment.class));

		actionBar.addTab(coaches);
		actionBar.selectTab(coaches);

		Tab athletes = actionBar
			.newTab()
			.setText("Athletes")
//			.setIcon(R.drawable.ic_mentions)
			.setTag("AtheletsFragment")
			.setTabListener(
			    new FragmentTabListener<AtheletsFragment>(R.id.flContainer, this, "Athletes", AtheletsFragment.class));

		actionBar.addTab(athletes);
	}
}
