package org.keenusa.connect.activities;

import org.keenusa.connect.R;
import org.keenusa.connect.adapters.CoachListItemAdapter;
import org.keenusa.connect.models.TestDataFactory;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class CoachListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coach_list);

		ListView lvCoaches = (ListView) findViewById(R.id.lvCoaches);
		CoachListItemAdapter adapter = new CoachListItemAdapter(this, TestDataFactory.getInstance().getCoachList());
		lvCoaches.setAdapter(adapter);
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
}
