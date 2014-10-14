package org.keenusa.connect.fragments;

import java.util.List;

import org.keenusa.connect.AthleteProfileActivity;
import org.keenusa.connect.R;
import org.keenusa.connect.adapters.AtheletListItemAdapter;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.TestDataFactory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AtheletsFragment extends Fragment {

	public static final String ATHLETE_EXTRA_TAG = "ATHLETE";
	AtheletListItemAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// placeholder. in reality parent activity should tell what athlete list it is expecting e.g. full list or athletes for the session
		adapter = new AtheletListItemAdapter(getActivity(), TestDataFactory.getInstance().getAthleteList());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_athletes, container, false);
		ListView lvAthletes = (ListView) v.findViewById(R.id.lvAthletes);
		lvAthletes.setAdapter(adapter);
		lvAthletes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(getActivity(), AthleteProfileActivity.class);
				i.putExtra(ATHLETE_EXTRA_TAG, adapter.getItem(position));
				startActivity(i);

			}
		});
		return v;
	}

	public AtheletListItemAdapter getAdapter() {
		return adapter;
	}

	public void addAPIData(List<Athlete> athletes) {
		adapter.clear();
		adapter.addAll(athletes);
	}
}
