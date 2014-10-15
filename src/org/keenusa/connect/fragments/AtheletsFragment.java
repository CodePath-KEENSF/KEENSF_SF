package org.keenusa.connect.fragments;

import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.activities.AthleteProfileActivity;
import org.keenusa.connect.adapters.AtheletListItemAdapter;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.TestDataFactory;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.networking.KeenCivicoreClient.CivicoreDataResultListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AtheletsFragment extends Fragment implements CivicoreDataResultListener<Athlete>{

	public static final String ATHLETE_EXTRA_TAG = "ATHLETE";
	AtheletListItemAdapter adapter;

	// Creates a new fragment with given arguments
	public static AtheletsFragment newInstance() {
		AtheletsFragment atheletsFragment = new AtheletsFragment();
		return atheletsFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// placeholder. in reality parent activity should tell what athlete list it is expecting e.g. full list or athletes for the session
		adapter = new AtheletListItemAdapter(getActivity(), TestDataFactory.getInstance().getAthleteList());
		KeenCivicoreClient client = new KeenCivicoreClient(getActivity());
		client.fetchAthleteListData(this);
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

	@Override
	public void onListResult(List<Athlete> list) {
		addAPIData(list);
	}
}
