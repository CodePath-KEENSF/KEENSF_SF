package org.keenusa.connect.fragments;

import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.adapters.CoachListItemAdapter;
import org.keenusa.connect.models.Coach;
import org.keenusa.connect.models.TestDataFactory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CoachesFragment extends Fragment {
	CoachListItemAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// placeholder. in reality parent activity should tell what coach list it is expecting e.g. full list or coaches for the session
		adapter = new CoachListItemAdapter(getActivity(), TestDataFactory.getInstance().getCoachList());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_coaches, container, false);
		ListView lvCoaches = (ListView) v.findViewById(R.id.lvCoaches);
		lvCoaches.setAdapter(adapter);
		return v;
	}

	public CoachListItemAdapter getAdapter() {
		return adapter;
	}

	public void addAPIData(List<Coach> coaches) {
		adapter.clear();
		adapter.addAll(coaches);
	}
}
