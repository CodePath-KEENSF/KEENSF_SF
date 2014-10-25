package org.keenusa.connect.fragments;

import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.activities.AthleteProfileActivity;
import org.keenusa.connect.adapters.AtheletListItemAdapter;
import org.keenusa.connect.models.Athlete;
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
import android.widget.Toast;

public class AtheletsFragment extends Fragment implements CivicoreDataResultListener<Athlete> {

	public static final String ATHLETE_EXTRA_TAG = "ATHLETE";

	AtheletListItemAdapter adapter;
	List<Athlete> athleteList;
	ListView lvAthletes;

	private LinearLayout llProgressBar;
	private boolean bDataLoaded = false;
	public String dummySearchString;

	private SearchView searchView;

	// Creates a new fragment with given arguments
	public static AtheletsFragment newInstance() {
		AtheletsFragment atheletsFragment = new AtheletsFragment();
		return atheletsFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
		bDataLoaded = false;
		athleteList = new ArrayList<Athlete>();
		adapter = new AtheletListItemAdapter(getActivity(), athleteList);
		KeenCivicoreClient client = new KeenCivicoreClient(getActivity());
		client.fetchAthleteListData(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_athletes, container, false);
		llProgressBar = (LinearLayout) v.findViewById(R.id.llProgressBarAthletes);
		if (!bDataLoaded) {
			llProgressBar.setVisibility(View.VISIBLE);
		}
		lvAthletes = (ListView) v.findViewById(R.id.lvAthletes);
		lvAthletes.setAdapter(adapter);
		lvAthletes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(getActivity(), AthleteProfileActivity.class);
				i.putExtra(ATHLETE_EXTRA_TAG, adapter.getItem(position));
				startActivity(i);
				getActivity().overridePendingTransition(R.anim.bottom_out, R.anim.top_in);
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
		athleteList = list;
		bDataLoaded = true;
		if (llProgressBar != null) {
			llProgressBar.setVisibility(View.GONE);
		}
	}

	@Override
	public void onListResultError() {
		Toast.makeText(getActivity(), "Error in fetching data from CiviCore", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.athletes, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search_athletes);
		dummySearchString = StringConstants.DUMMY_SEARCH_STRING;
		searchView = (SearchView) searchItem.getActionView();
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				return true;
			}

			@Override
			public boolean onQueryTextChange(String searchText) {

				// Added as a work-around. the searchText was retaining values across fragment swiping
				// with this condition, the value of searchText from another fragment is discarded
				if (dummySearchString == StringConstants.DUMMY_SEARCH_STRING) {
					if (!searchText.isEmpty()) {
						dummySearchString = "";
						return true;
					}
				}

				dummySearchString = searchText;
				ArrayList<Athlete> tempAthleteList = new ArrayList<Athlete>();
				int searchTextlength = searchText.length();

				// Create the new arraylist for each search character
				for (Athlete athlete : athleteList) {

					String fullName = athlete.getFirstName() + " " + athlete.getLastName();

					if (searchTextlength <= athlete.getFirstName().length() || searchTextlength <= athlete.getLastName().length()) {

						if (fullName.toLowerCase().contains(searchText.toLowerCase())) {
							tempAthleteList.add(athlete);
						}
					}
				}

				adapter = new AtheletListItemAdapter(getActivity(), tempAthleteList);
				lvAthletes.setAdapter(adapter);

				return true;
			}
		});
		super.onCreateOptionsMenu(menu, inflater);
	}

	public void onBackPressed() {
		getActivity().overridePendingTransition(R.anim.bottom_out, R.anim.top_in);
	}
}
