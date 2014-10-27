package org.keenusa.connect.fragments;

import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.activities.AthleteProfileActivity;
import org.keenusa.connect.adapters.AtheletListItemAdapter;
import org.keenusa.connect.data.daos.AthleteDAO;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.utilities.StringConstants;

import android.content.Intent;
import android.os.AsyncTask;
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

public class AtheletsFragment extends Fragment {

	public static final String ATHLETE_EXTRA_TAG = "ATHLETE_ID";

	AtheletListItemAdapter adapter;
	List<Athlete> athleteList;
	ListView lvAthletes;

	private LinearLayout llLoadingAthletesIndicator;
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
		athleteList = new ArrayList<Athlete>();
		adapter = new AtheletListItemAdapter(getActivity(), athleteList);
		new LoadAthleteListDataTask().execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_athletes, container, false);
		llLoadingAthletesIndicator = (LinearLayout) v.findViewById(R.id.llLoadingAthletesIndicator);
		lvAthletes = (ListView) v.findViewById(R.id.lvAthletes);
		lvAthletes.setAdapter(adapter);
		lvAthletes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(getActivity(), AthleteProfileActivity.class);
				i.putExtra(ATHLETE_EXTRA_TAG, adapter.getItem(position).getId());
				startActivity(i);
				getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
			}
		});
		return v;
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

	private class LoadAthleteListDataTask extends AsyncTask<String, Void, List<Athlete>> {

		@Override
		protected void onPreExecute() {
			if (llLoadingAthletesIndicator != null) {
				llLoadingAthletesIndicator.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected List<Athlete> doInBackground(String... params) {
			AthleteDAO athleteDAO = new AthleteDAO(getActivity());
			List<Athlete> athletes = athleteDAO.getAthleteList();
			return athletes;
		}

		@Override
		protected void onPostExecute(List<Athlete> athletes) {
			if (llLoadingAthletesIndicator != null) {
				llLoadingAthletesIndicator.setVisibility(View.GONE);
			}
			adapter.clear();
			adapter.addAll(athletes);
		}

	}

	@Override
	public void onResume() {
		new LoadAthleteListDataTask().execute();
		super.onResume();
	}
}
