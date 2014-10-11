package org.keenusa.connect.fragments;

import org.keenusa.connect.R;
import org.keenusa.connect.adapters.AtheletListItemAdapter;
import org.keenusa.connect.models.TestDataFactory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class AtheletsFragment extends Fragment {
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
		return v;
	}

	public AtheletListItemAdapter getAdapter() {
		return adapter;
	}
}
