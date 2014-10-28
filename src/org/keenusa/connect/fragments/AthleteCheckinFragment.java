package org.keenusa.connect.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

import org.keenusa.connect.R;
import org.keenusa.connect.activities.AthleteProfileActivity;
import org.keenusa.connect.adapters.AthleteStickyHeaderCheckInAdapter;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.AthleteAttendance;
import org.keenusa.connect.models.AthleteAttendance.AttendanceValue;
import org.keenusa.connect.models.KeenProgramEnrolment;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.networking.KeenCivicoreClient.CivicoreDataResultListener;
import org.keenusa.connect.networking.KeenCivicoreClient.CivicoreUpdateDataResultListener;
import org.keenusa.connect.utilities.AthleteAttComparator;
import org.keenusa.connect.utilities.CheckinMenuActions;
import org.keenusa.connect.utilities.DebugInfo;
import org.keenusa.connect.utilities.PostCheckinUpdate;
import org.keenusa.connect.utilities.StringConstants;

import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

public class AthleteCheckinFragment extends Fragment {

	public static final String ATHLETE_EXTRA_TAG = "ATHLETE_ID";

	public String dummySearchString;
	private SearchView searchView;

	private LinearLayout llProgressBarAthleteCheckin;
	// private ListView lvAthleteCheckin;
	// private AthleteCheckinAdapter athleteCheckInAdapter;
	private TextView tvAthleteAttended;

	private ExpandableStickyListHeadersListView lvAthleteCheckin;
	private StickyListHeadersAdapter athleteCheckInAdapter;

	private ArrayList<KeenSession> sessionList;
	private List<AthleteAttendance> athleteAttendanceList;
	private List<AthleteAttendance> athleteAttendanceListOriginal;
	private HashMap<String, Athlete> AthleteAttendanceMap = new HashMap<String, Athlete>();
	WeakHashMap<View, Integer> mOriginalViewHeightPool = new WeakHashMap<View, Integer>();
	private ProgressBar progressBar;
	private boolean bDataLoaded = false;

	private KeenSession session;
	KeenCivicoreClient client;

	// Creates a new fragment with given arguments
	public static AthleteCheckinFragment newInstance(KeenSession session, KeenCivicoreClient client) {
		AthleteCheckinFragment athleteCheckinFragment = new AthleteCheckinFragment();
		athleteCheckinFragment.session = session;
		athleteCheckinFragment.client = client;
		return athleteCheckinFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
		bDataLoaded = false;
		fetchEnrolledAthleteList();
		setAdapter();
		removeProgressBars();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_athlete_checkin, container, false);

		setViews(v);

		setOnClickListeners();

		return v;
	}

	private void removeProgressBars() {
		bDataLoaded = true;
		if (llProgressBarAthleteCheckin != null) {
			llProgressBarAthleteCheckin.setVisibility(View.GONE);
		}
	}

	private void fetchEnrolledAthleteList() {
		if (session.getAthleteAttendance() == null) {
			athleteAttendanceList = new ArrayList<AthleteAttendance>();
			session.setAthleteAttendance(athleteAttendanceList);
		} else {
			athleteAttendanceList = session.getAthleteAttendance();
		}

		athleteAttendanceListOriginal = new ArrayList<AthleteAttendance>();

		for (int i = 0; i < athleteAttendanceList.size(); i++) {

			athleteAttendanceListOriginal.add(new AthleteAttendance());

			athleteAttendanceListOriginal.get(i).setAttendanceValue(athleteAttendanceList.get(i).getAttendanceValue());

		}

		// Add existing athletes in hash map
		for (AthleteAttendance athleteAttendance : athleteAttendanceList) {
			AthleteAttendanceMap.put(athleteAttendance.getAthlete().getFirstLastName(), athleteAttendance.getAthlete());
		}

		for (KeenProgramEnrolment enrolment : session.getProgram().getProgramEnrolments()) {

			if (AthleteAttendanceMap.get(enrolment.getAthlete().getFirstLastName()) == null) {
				athleteAttendanceList.add(new AthleteAttendance());
				AthleteAttendance athleteAttendance = athleteAttendanceList.get(athleteAttendanceList.size() - 1);
				athleteAttendance.setAthlete(enrolment.getAthlete());
				athleteAttendance.setRemoteSessionId(session.getRemoteId());
				athleteAttendance.setAttendanceValue(AttendanceValue.REGISTERED);
			}
		}

		Collections.sort(athleteAttendanceList, new AthleteAttComparator());
	}

	private void setAdapter() {
		// athleteCheckInAdapter = new AthleteCheckinAdapter(getActivity(),
		// athleteAttendanceList);

		athleteCheckInAdapter = new AthleteStickyHeaderCheckInAdapter(getActivity(), athleteAttendanceList);

	}

	private void setOnClickListeners() {
		lvAthleteCheckin.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(getActivity(), AthleteProfileActivity.class);
				i.putExtra(ATHLETE_EXTRA_TAG, athleteAttendanceList.get(position).getAthlete().getId());
				startActivity(i);
				getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
			}
		});

		lvAthleteCheckin.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
			@Override
			public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
				if (lvAthleteCheckin.isHeaderCollapsed(headerId)) {
					lvAthleteCheckin.expand(headerId);
				} else {
					lvAthleteCheckin.collapse(headerId);
				}
			}
		});

		lvAthleteCheckin.setAnimExecutor(new AnimationExecutor());
	}

	private void setViews(View v) {
		llProgressBarAthleteCheckin = (LinearLayout) v.findViewById(R.id.llProgressBarAthleteCheckin);
		if (!bDataLoaded) {
			llProgressBarAthleteCheckin.setVisibility(View.VISIBLE);
			loadProgressBar();
		}

		lvAthleteCheckin = (ExpandableStickyListHeadersListView) v.findViewById(R.id.lvAthleteCheckin);
		lvAthleteCheckin.setAdapter(athleteCheckInAdapter);
		tvAthleteAttended = (TextView) v.findViewById(R.id.tvAthleteAttended);
	}

	private void loadProgressBar() {
		// progressBar.getProgressDrawable().setColorFilter(Color.GREEN,
		// Mode.MULTIPLY);
		try {
			for (int i = 1; i <= 10; i++) {
				progressBar.setProgress(i * 10);
				Thread.sleep(500);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fetchSessionList() {
		client.fetchSessionListData(new CivicoreDataResultListener<KeenSession>() {

			@Override
			public void onListResult(List<KeenSession> list) {
				sessionList.clear();
				sessionList.addAll(list);

				for (KeenSession fetchedSession : sessionList) {
					if (fetchedSession.getRemoteId() == session.getRemoteId()) {
						fetchedSession = session;
					}
				}
			}

			@Override
			public void onListResultError() {
			}

		});
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if (CheckinMenuActions.sendMassMessages == true) {
			menu.findItem(R.id.miSendMessageAthletes).setVisible(true);
		} else {
			menu.findItem(R.id.miSendMessageAthletes).setVisible(false);
		}
		super.onPrepareOptionsMenu(menu);
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.athlete_checkin, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search_athletees_checkin);
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
				ArrayList<AthleteAttendance> tempAthleteAttendanceList = new ArrayList<AthleteAttendance>();
				int searchTextlength = searchText.length();

				// Create the new arraylist for each search character
				for (AthleteAttendance athleteAttendance : athleteAttendanceList) {

					String fullName = athleteAttendance.getAthlete().getFirstLastName();

					if (searchTextlength <= athleteAttendance.getAthlete().getFirstName().length()
							|| searchTextlength <= athleteAttendance.getAthlete().getLastName().length()) {

						if (fullName.toLowerCase().contains(searchText.toLowerCase())) {
							tempAthleteAttendanceList.add(athleteAttendance);
						}
					}
				}

				athleteCheckInAdapter = new AthleteStickyHeaderCheckInAdapter(getActivity(), tempAthleteAttendanceList);
				lvAthleteCheckin.setAdapter(athleteCheckInAdapter);

				return true;
			}
		});
		super.onCreateOptionsMenu(menu, inflater);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.miSendMessageAthletes) {
			showMassMessageDialog();
		} else if (item.getItemId() == R.id.miCheckAllAthleteIn) {
			checkInAllAthletes();
		}

		return super.onOptionsItemSelected(item);
	}

	private void checkInAllAthletes() {
		if (session.getAthleteAttendance() == null) {
			athleteAttendanceList = new ArrayList<AthleteAttendance>();
			session.setAthleteAttendance(athleteAttendanceList);
		} else {
			athleteAttendanceList = session.getAthleteAttendance();
		}
		athleteAttendanceListOriginal = new ArrayList<AthleteAttendance>();
		for (int i = 0; i < athleteAttendanceList.size(); i++) {
			athleteAttendanceListOriginal.add(new AthleteAttendance());
			athleteAttendanceListOriginal.get(i).setAttendanceValue(AthleteAttendance.AttendanceValue.ATTENDED);
			// TODO - Update TextView on UI to show all Athletes are set as
			// "ATTENDED"
			// tvAthleteAttended.setText(athleteAttendanceListOriginal.get(i).setAttendanceValue(AthleteAttendance.AttendanceValue.ATTENDED));
			Toast.makeText(
					getActivity(),
					athleteAttendanceList.get(i).getAttendedAthleteFullName() + " has set as "
							+ athleteAttendanceListOriginal.get(i).getAttendanceValue(), Toast.LENGTH_LONG).show();
		}
	}

	private void showMassMessageDialog() {
		DialogFragment newFragment = new MassMessageFragment(session.getProgram().getProgramEnrolments(), null);
		newFragment.show(getActivity().getSupportFragmentManager(), "Mass Message Dialog");
	}

	public void postAttendance() {
		for (int i = 0; i < athleteAttendanceList.size(); i++) {
			if (i < athleteAttendanceListOriginal.size()) {
				if (athleteAttendanceListOriginal.get(i) != null) {
					if (athleteAttendanceListOriginal.get(i).getAttendanceValue() != athleteAttendanceList.get(i).getAttendanceValue()) {

						updateRecord(athleteAttendanceList.get(i));
						athleteAttendanceListOriginal.get(i).setAttendanceValue(athleteAttendanceList.get(i).getAttendanceValue());
					}
				}
			} else { // new attendance records
				if (athleteAttendanceList.get(i).getAttendanceValue() != null) {
					athleteAttendanceList.get(i).setRemoteSessionId(session.getRemoteId());
					addRecord(athleteAttendanceList.get(i));

					AthleteAttendance addedAthleteAttendance = new AthleteAttendance();
					addedAthleteAttendance.setAttendanceValue(athleteAttendanceList.get(i).getAttendanceValue());
					addedAthleteAttendance.setAthlete(athleteAttendanceList.get(i).getAthlete());
					athleteAttendanceListOriginal.add(addedAthleteAttendance);

				}
			}

		}
		Collections.sort(athleteAttendanceList, new AthleteAttComparator());
		Collections.sort(athleteAttendanceListOriginal, new AthleteAttComparator());
	}

	public void updateRecord(AthleteAttendance athlete) {
		PostCheckinUpdate.done++;
		client.updateAthleteAttendanceRecord(athlete, new CivicoreUpdateDataResultListener<AthleteAttendance>() {

			@Override
			public void onRecordUpdateResult(AthleteAttendance object) {
				PostCheckinUpdate.done--;
				if (PostCheckinUpdate.done == 0) {
					DebugInfo.showToast(getActivity(), "Attendance Posted!");
				}
				Log.d("temp", "attendance updated");
			}

			@Override
			public void onRecordUpdateError() {
				Log.d("temp", "attendance update error");

			}
		});
	}

	public void addRecord(AthleteAttendance athlete) {
		PostCheckinUpdate.done++;
		client.insertNewAthleteAttendanceRecord(athlete, new CivicoreUpdateDataResultListener<AthleteAttendance>() {

			@Override
			public void onRecordUpdateResult(AthleteAttendance object) {
				PostCheckinUpdate.done--;
				if (PostCheckinUpdate.done == 0) {
					DebugInfo.showToast(getActivity(), "Attendance Posted!");
				}
				Log.d("temp", "attendance added");
			}

			@Override
			public void onRecordUpdateError() {
				Log.d("temp", "attendance add error");

			}
		});
	}

	public void refreshAttendance() {
		for (int i = 0; i < athleteAttendanceList.size(); i++) {
			Log.d("temp", "before: " + athleteAttendanceList.get(i).getAttendanceValue());
		}
		Collections.sort(athleteAttendanceList, new AthleteAttComparator());
		for (int i = 0; i < athleteAttendanceList.size(); i++) {
			Log.d("temp", "after: " + athleteAttendanceList.get(i).getAttendanceValue());
		}
		athleteCheckInAdapter = new AthleteStickyHeaderCheckInAdapter(getActivity(), athleteAttendanceList);
		lvAthleteCheckin.setAdapter(athleteCheckInAdapter);
	}

	// animation executor
	class AnimationExecutor implements ExpandableStickyListHeadersListView.IAnimationExecutor {

		@Override
		public void executeAnim(final View target, final int animType) {
			if (ExpandableStickyListHeadersListView.ANIMATION_EXPAND == animType && target.getVisibility() == View.VISIBLE) {
				return;
			}
			if (ExpandableStickyListHeadersListView.ANIMATION_COLLAPSE == animType && target.getVisibility() != View.VISIBLE) {
				return;
			}
			if (mOriginalViewHeightPool.get(target) == null) {
				mOriginalViewHeightPool.put(target, target.getHeight());
			}
			final int viewHeight = mOriginalViewHeightPool.get(target);
			float animStartY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? 0f : viewHeight;
			float animEndY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? viewHeight : 0f;
			final ViewGroup.LayoutParams lp = target.getLayoutParams();

			ValueAnimator animator = ValueAnimator.ofFloat(animStartY, animEndY);
			animator.setDuration(300);
			target.setVisibility(View.VISIBLE);
			animator.addListener(new AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animator) {
				}

				@Override
				public void onAnimationEnd(Animator animator) {
					if (animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND) {
						target.setVisibility(View.VISIBLE);
					} else {
						target.setVisibility(View.GONE);
					}
					target.getLayoutParams().height = viewHeight;
				}

				@Override
				public void onAnimationCancel(Animator animator) {

				}

				@Override
				public void onAnimationRepeat(Animator animator) {

				}
			});

			animator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator valueAnimator) {
					lp.height = ((Float) valueAnimator.getAnimatedValue()).intValue();
					target.setLayoutParams(lp);
					target.requestLayout();
				}
			});
			animator.start();
		}
	}

}
