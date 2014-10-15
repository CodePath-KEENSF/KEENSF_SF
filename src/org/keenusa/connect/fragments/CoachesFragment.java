package org.keenusa.connect.fragments;

import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.activities.CoachProfileActivity;
import org.keenusa.connect.adapters.CoachListItemAdapter;
import org.keenusa.connect.models.Coach;
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

public class CoachesFragment extends Fragment implements CivicoreDataResultListener<Coach>{

	public static final String COACH_EXTRA_TAG = "COACH";
	CoachListItemAdapter adapter;

	// Creates a new fragment with given arguments
	public static CoachesFragment newInstance() {
		CoachesFragment coachesFragment = new CoachesFragment();
		return coachesFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// placeholder. in reality parent activity should tell what coach list it is expecting e.g. full list or coaches for the session
		adapter = new CoachListItemAdapter(getActivity(), TestDataFactory.getInstance().getCoachList());
		KeenCivicoreClient client = new KeenCivicoreClient(getActivity());
		client.fetchCoachListData(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_coaches, container, false);
		ListView lvCoaches = (ListView) v.findViewById(R.id.lvCoaches);
		lvCoaches.setAdapter(adapter);
		lvCoaches.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(getActivity(), CoachProfileActivity.class);
				i.putExtra(COACH_EXTRA_TAG, adapter.getItem(position));
				startActivity(i);

			}
		});
		
		return v;
	}
	
	public CoachListItemAdapter getAdapter() {
		return adapter;
	}

	public void addAPIData(List<Coach> coaches) {
		adapter.clear();
		adapter.addAll(coaches);
	}

	@Override
	public void onListResult(List<Coach> list) {
		addAPIData(list);
	}

}
