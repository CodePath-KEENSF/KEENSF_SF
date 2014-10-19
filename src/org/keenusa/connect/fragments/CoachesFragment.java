package org.keenusa.connect.fragments;

import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.activities.CoachProfileActivity;
import org.keenusa.connect.adapters.CoachListItemAdapter;
import org.keenusa.connect.models.Coach;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.networking.KeenCivicoreClient.CivicoreDataResultListener;
import org.keenusa.connect.utilities.StringConstants;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class CoachesFragment extends Fragment implements CivicoreDataResultListener<Coach> {

	public static final String COACH_EXTRA_TAG = "COACH";
	CoachListItemAdapter adapter;
	List<Coach> coachList;
	ListView lvCoaches;

	private LinearLayout llProgressBar;
	private boolean bDataLoaded = false;
	public String dummySearchString;

	SearchView searchView;

	// Creates a new fragment with given arguments
	public static CoachesFragment newInstance() {
		CoachesFragment coachesFragment = new CoachesFragment();
		return coachesFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		bDataLoaded = false;
		coachList = new ArrayList<Coach>();
		adapter = new CoachListItemAdapter(getActivity(), coachList);
		KeenCivicoreClient client = new KeenCivicoreClient(getActivity());
		client.fetchCoachListData(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_coaches, container, false);
		llProgressBar = (LinearLayout) v.findViewById(R.id.llProgressBarCoaches);
		if (!bDataLoaded) {
			llProgressBar.setVisibility(View.VISIBLE);
		}

		lvCoaches = (ListView) v.findViewById(R.id.lvCoaches);
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
		coachList = list;
		bDataLoaded = true;
		if (llProgressBar != null) {
			llProgressBar.setVisibility(View.GONE);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.coaches, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search_coaches);
		dummySearchString = StringConstants.DUMMY_SEARCH_STRING;
		searchView = (SearchView) searchItem.getActionView();
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				return true;
			}

			@Override
			public boolean onQueryTextChange(String searchText) {

				if (dummySearchString == StringConstants.DUMMY_SEARCH_STRING) {
					if (!searchText.isEmpty()) {
						dummySearchString = "";
						return true;
					}
				}

				dummySearchString = searchText;
				ArrayList<Coach> tempCoachList = new ArrayList<Coach>();
				int searchTextlength = searchText.length();

				// Create the new arraylist for each search character
				for (Coach coach : coachList) {

					String fullName = coach.getFirstName() + " " + coach.getLastName();

					if (searchTextlength <= coach.getFirstName().length() || searchTextlength <= coach.getLastName().length()) {

						if (fullName.toLowerCase().contains(searchText.toLowerCase())) {
							tempCoachList.add(coach);
						}
					}
				}

				adapter = new CoachListItemAdapter(getActivity(), tempCoachList);
				lvCoaches.setAdapter(adapter);

				return true;
			}
		});
		super.onCreateOptionsMenu(menu, inflater);
	}
}
