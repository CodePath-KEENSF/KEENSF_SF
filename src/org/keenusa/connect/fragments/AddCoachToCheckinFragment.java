package org.keenusa.connect.fragments;

import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.adapters.CoachListItemAdapter;
import org.keenusa.connect.models.Coach;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.networking.KeenCivicoreClient.CivicoreDataResultListener;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

public class AddCoachToCheckinFragment extends DialogFragment implements CivicoreDataResultListener<Coach>{

	public static final String COACH_EXTRA_TAG = "COACH";
	
	private CoachListItemAdapter adapter;
	private List<Coach> coachList;
	private ListView lvCoaches;
	private EditText etAddCoach;

	private LinearLayout llProgressBar;
	private boolean bDataLoaded = false;
	private KeenCivicoreClient client;
	private ProgressBar progressBar;
	SearchView searchView;

	public AddCoachToCheckinFragment() {
		// Empty constructor required for DialogFragment
	}

	public interface AddCoachDialogListener {
		void onFinishAddDialog(Coach coach);
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_coaches_add, container, false);
		llProgressBar = (LinearLayout) v.findViewById(R.id.llProgressBarCoachesAdd);
		if (!bDataLoaded) {
			llProgressBar.setVisibility(View.VISIBLE);
			loadProgressBar();
		}

		lvCoaches = (ListView) v.findViewById(R.id.lvCoachesAdd);
		lvCoaches.setAdapter(adapter);
		lvCoaches.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AddCoachDialogListener listner = (AddCoachDialogListener) getActivity();
				listner.onFinishAddDialog(adapter.getItem(position));
				dismiss();
			}
		});

		etAddCoach = (EditText)v.findViewById(R.id.etAddCoach);
		etAddCoach.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			public void afterTextChanged(Editable arg0) {
				
				ArrayList<Coach> tempCoachList = new ArrayList<Coach>();
				int searchTextlength = etAddCoach.getText().length();

				// Create the new arraylist for each search character
				for (Coach coach : coachList) {

					String fullName = coach.getFirstName() + " " + coach.getLastName();

					if (searchTextlength <= coach.getFirstName().length() || searchTextlength <= coach.getLastName().length()) {

						if (fullName.toLowerCase().contains(etAddCoach.getText().toString().toLowerCase())) {
							tempCoachList.add(coach);
						}
					}
				}

				adapter = new CoachListItemAdapter(getActivity(), tempCoachList);
				lvCoaches.setAdapter(adapter);
			}

		});
		
		getDialog().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

		return v;
	}
	
	private void loadProgressBar() {
//		progressBar.getProgressDrawable().setColorFilter(Color.GREEN, Mode.MULTIPLY);
		try {
			for (int i = 1; i <= 10; i++) {
				progressBar.setProgress(i*10);
				Thread.sleep(500);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	public void onListResultError() {
		Toast.makeText(getActivity(), "Error in fetching data from CiviCore", Toast.LENGTH_SHORT).show();
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.coaches, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search_coaches);
		searchView = (SearchView) searchItem.getActionView();
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				return true;
			}

			@Override
			public boolean onQueryTextChange(String searchText) {
				return true;
			}
		});
		super.onCreateOptionsMenu(menu, inflater);
	}

}
