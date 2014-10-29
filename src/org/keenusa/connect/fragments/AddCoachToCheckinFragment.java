package org.keenusa.connect.fragments;

import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.adapters.CoachListItemAdapter;
import org.keenusa.connect.data.daos.CoachDAO;
import org.keenusa.connect.models.Coach;

import android.os.AsyncTask;
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
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class AddCoachToCheckinFragment extends DialogFragment {

	private CoachListItemAdapter adapter;
	private List<Coach> coachList;
	private ListView lvCoaches;
	private EditText etAddCoach;
	private LinearLayout llLoadingCoachesIndicator;
	SearchView searchView;
	private long sessionId;

	public AddCoachToCheckinFragment() {
		// Empty constructor required for DialogFragment
	}

	// Creates a new fragment with given arguments
	public static AddCoachToCheckinFragment newInstance(long sessionId) {
		AddCoachToCheckinFragment addCoachToCheckinFragment = new AddCoachToCheckinFragment();
		addCoachToCheckinFragment.sessionId = sessionId;
		return addCoachToCheckinFragment;
	}

	public interface AddCoachDialogListener {
		void onFinishAddDialog(Coach coach);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		coachList = new ArrayList<Coach>();
		adapter = new CoachListItemAdapter(getActivity(), coachList);
		new LoadCoachListDataTask().execute(sessionId);
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
	    super.onActivityCreated(arg0);
	    getDialog().getWindow()
	    .getAttributes().windowAnimations = R.style.DialogAnimation;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_coaches_add, container, false);
		llLoadingCoachesIndicator = (LinearLayout) v.findViewById(R.id.llLoadingCoachesIndicator);
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

		etAddCoach = (EditText) v.findViewById(R.id.etAddCoach);
		etAddCoach.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
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

		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

		return v;
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

	private class LoadCoachListDataTask extends AsyncTask<Long, Void, List<Coach>> {

		@Override
		protected void onPreExecute() {
			if (llLoadingCoachesIndicator != null) {
				llLoadingCoachesIndicator.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected List<Coach> doInBackground(Long... params) {
			CoachDAO coachDAO = new CoachDAO(getActivity());
			List<Coach> coaches = coachDAO.getCoachesToRegisterForSessionList(params[0]);
			return coaches;
		}

		@Override
		protected void onPostExecute(List<Coach> coaches) {
			if (llLoadingCoachesIndicator != null) {
				llLoadingCoachesIndicator.setVisibility(View.GONE);
			}
			adapter.clear();
			adapter.addAll(coaches);
		}

	}

}
