package org.keenusa.connect.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;

import org.keenusa.connect.R;
import org.keenusa.connect.activities.CoachProfileActivity;
import org.keenusa.connect.adapters.CoachStickyHeaderCheckInAdapter;
import org.keenusa.connect.fragments.AthleteCheckinFragment.AnimationExecutor;
import org.keenusa.connect.models.Coach;
import org.keenusa.connect.models.CoachAttendance;
import org.keenusa.connect.models.CoachAttendance.AttendanceValue;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.networking.KeenCivicoreClient.CivicoreUpdateDataResultListener;
import org.keenusa.connect.utilities.CheckinMenuActions;
import org.keenusa.connect.utilities.CoachAttComparator;
import org.keenusa.connect.utilities.DebugInfo;
import org.keenusa.connect.utilities.PostCheckinUpdate;
import org.keenusa.connect.utilities.StringConstants;

import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

public class CoachCheckinFragment extends Fragment {

	public static final String COACH_EXTRA_TAG = "COACH_ID";

	public String dummySearchString;
	private SearchView searchView;

	private LinearLayout llProgressBarCoachCheckin;
	// private ListView lvCoachCheckin;
	// private CoachCheckInAdapter coachCheckInAdapter;
	private TextView tvCoachAttended;

	private ExpandableStickyListHeadersListView lvCoachCheckin;
	private StickyListHeadersAdapter coachCheckInAdapter;

	private ArrayList<KeenSession> sessionList;
	private List<CoachAttendance> coachAttendanceList;
	private List<CoachAttendance> coachAttendanceListOriginal;
	WeakHashMap<View,Integer> mOriginalViewHeightPool = new WeakHashMap<View, Integer>();
	private ProgressBar progressBar;
	private boolean bDataLoaded = false;

	private KeenSession session;
	KeenCivicoreClient client;

	// Creates a new fragment with given arguments
	public static CoachCheckinFragment newInstance(KeenSession session,
			KeenCivicoreClient client) {
		CoachCheckinFragment coachCheckinFragment = new CoachCheckinFragment();
		coachCheckinFragment.session = session;
		coachCheckinFragment.client = client;
		return coachCheckinFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
		bDataLoaded = false;
		fetchEnrolledCoachList();
		setAdapter();
		removeProgressBars();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_coach_checkin, container,
				false);

		setViews(v);

		setOnClickListeners();

		return v;
	}

	private void removeProgressBars() {
		bDataLoaded = true;
		if (llProgressBarCoachCheckin != null) {
			llProgressBarCoachCheckin.setVisibility(View.GONE);
		}
	}

	private void fetchEnrolledCoachList() {
		if (session.getCoachAttendance() == null) {
			coachAttendanceList = new ArrayList<CoachAttendance>();
			session.setCoachAttendance(coachAttendanceList);
		} else {
			coachAttendanceList = session.getCoachAttendance();
		}

		Collections.sort(coachAttendanceList, new CoachAttComparator());

		coachAttendanceListOriginal = new ArrayList<CoachAttendance>();

		for (int i = 0; i < coachAttendanceList.size(); i++) {

			coachAttendanceListOriginal.add(new CoachAttendance());

			coachAttendanceListOriginal.get(i).setAttendanceValue(
					coachAttendanceList.get(i).getAttendanceValue());

		}

	}

	private void setAdapter() {
		// coachCheckInAdapter = new CoachCheckInAdapter(getActivity(),
		// coachAttendanceList);
		coachCheckInAdapter = new CoachStickyHeaderCheckInAdapter(
				getActivity(), coachAttendanceList);
	}

	private void setOnClickListeners() {
		lvCoachCheckin.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(getActivity(), CoachProfileActivity.class);
				CoachAttendance coachAtt = coachAttendanceList.get(position);
				 i.putExtra(COACH_EXTRA_TAG,coachAtt.getCoach().getId());
				startActivity(i);
				getActivity().overridePendingTransition(R.anim.right_in,
						R.anim.left_out);
			}
		});
		
		lvCoachCheckin.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
			@Override
			public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
				if (lvCoachCheckin.isHeaderCollapsed(headerId)) {
					lvCoachCheckin.expand(headerId);
				} else {
					lvCoachCheckin.collapse(headerId);
				}
			}
		});
		
		lvCoachCheckin.setAnimExecutor(new AnimationExecutor());
		
//		lvCoachCheckin.setOnItemLongClickListener(new OnItemLongClickListener() {
//			@Override
//			public boolean onItemLongClick(AdapterView<?> adapter, View view,
//					int pos, long id) {
//				LinearLayout back = (LinearLayout)view.findViewById(R.id.llCoachCheckinBack);
//				RelativeLayout front = (RelativeLayout)view.findViewById(R.id.rlCoachCheckinFront);
//				if(front.getAlpha() == 1.0f){
//					Animator animFront = AnimatorInflater.loadAnimator(getActivity(), R.animator.card_flip_left_out);
//					animFront.setTarget(front);
//					Animator animBack = AnimatorInflater.loadAnimator(getActivity(), R.animator.card_flip_left_in);
//					animBack.setTarget(back);
//					animFront.start();
//					animBack.start();
//					Log.d("temp", "front");
//				}else{
//					Animator animFront = AnimatorInflater.loadAnimator(getActivity(), R.animator.card_flip_left_in);
//					animFront.setTarget(front);
//					Animator animBack = AnimatorInflater.loadAnimator(getActivity(), R.animator.card_flip_left_out);
//					animBack.setTarget(back);
//					animBack.start();
//					animFront.start();
//					Log.d("temp", "back");
//				}
//				return true;
//			}
//		});
	}

	private void setViews(View v) {
		llProgressBarCoachCheckin = (LinearLayout) v
				.findViewById(R.id.llProgressBarCoachCheckin);
		if (!bDataLoaded) {
			llProgressBarCoachCheckin.setVisibility(View.VISIBLE);
			loadProgressBar();
		}

		lvCoachCheckin = (ExpandableStickyListHeadersListView) v
				.findViewById(R.id.lvCoachCheckin);
		lvCoachCheckin.setAdapter(coachCheckInAdapter);
		tvCoachAttended = (TextView) v.findViewById(R.id.tvCoachAttended);
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

	@Override
	public void onPrepareOptionsMenu(Menu menu) {

		MenuItem miAddCoaches = menu.findItem(R.id.miAddCoaches);
		if (CheckinMenuActions.editMode == true) {
			miAddCoaches.setVisible(true);
		} else {
			miAddCoaches.setVisible(false);
		}

		if (CheckinMenuActions.sendMassMessages == true) {
			menu.findItem(R.id.miSendMessageCoaches).setVisible(true);
		} else {
			menu.findItem(R.id.miSendMessageCoaches).setVisible(false);
		}

		super.onPrepareOptionsMenu(menu);
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.coach_checkin, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search_coaches_checkin);
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
				ArrayList<CoachAttendance> tempCoachAttendanceList = new ArrayList<CoachAttendance>();
				int searchTextlength = searchText.length();

				// Create the new arraylist for each search character
				for (CoachAttendance coachAttendance : coachAttendanceList) {

					String fullName = coachAttendance.getCoach()
							.getFirstLastName();

					if (searchTextlength <= coachAttendance.getCoach()
							.getFirstName().length()
							|| searchTextlength <= coachAttendance.getCoach()
									.getLastName().length()) {

						if (fullName.toLowerCase().contains(
								searchText.toLowerCase())) {
							tempCoachAttendanceList.add(coachAttendance);
						}
					}
				}

				coachCheckInAdapter = new CoachStickyHeaderCheckInAdapter(
						getActivity(), tempCoachAttendanceList);
				lvCoachCheckin.setAdapter(coachCheckInAdapter);

				return true;
			}
		});
		super.onCreateOptionsMenu(menu, inflater);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.miSendMessageCoaches) {
			showMassMessageDialog();
		} else if (item.getItemId() == R.id.miAddCoaches) {
			DialogFragment newFragment = new AddCoachToCheckinFragment();
			newFragment.show(getActivity().getSupportFragmentManager(),
					"Add Coach");
		} else if (item.getItemId() == R.id.miCheckAllCoachIn) {
			checkInAllCoaches();
		}

		return super.onOptionsItemSelected(item);
	}

	private void checkInAllCoaches() {
		if (session.getCoachAttendance() == null) {
			coachAttendanceList = new ArrayList<CoachAttendance>();
			session.setCoachAttendance(coachAttendanceList);
		} else {
			coachAttendanceList = session.getCoachAttendance();
		}
		coachAttendanceListOriginal = new ArrayList<CoachAttendance>();
		for (int i = 0; i < coachAttendanceList.size(); i++) {
			coachAttendanceListOriginal.add(new CoachAttendance());
			coachAttendanceListOriginal.get(i).setAttendanceValue(
					coachAttendanceList.get(i).getAttendanceValue());
			// TODO - Update TextView on UI to show all Athletes are set as
			// "ATTENDED"
			// tvCoachAttended.setText(coachAttendanceListOriginal.get(i).setAttendanceValue(CoachAttendance.AttendanceValue.ATTENDED));
			Toast.makeText(
					getActivity(),
					coachAttendanceList.get(i).getAttendedCoachFullName()
							+ " has set as "
							+ coachAttendanceListOriginal.get(i)
									.getAttendanceValue(), Toast.LENGTH_LONG)
					.show();
		}
	}

	private void showMassMessageDialog() {
		DialogFragment newFragment = new MassMessageFragment(null,
				coachAttendanceList);
		newFragment.show(getActivity().getSupportFragmentManager(),
				"Mass Message Dialog");
	}

	public void postAttendance() {
		for (int i = 0; i < coachAttendanceList.size(); i++) {
			if (i < coachAttendanceListOriginal.size()) {
				// need hash key check here to make sure wrong attendances are not posted
				if (coachAttendanceListOriginal.get(i) != null) {
					if (coachAttendanceListOriginal.get(i).getAttendanceValue() != coachAttendanceList
							.get(i).getAttendanceValue()) {

						updateRecord(coachAttendanceList.get(i));
						coachAttendanceListOriginal.get(i)
								.setAttendanceValue(
										coachAttendanceList.get(i)
												.getAttendanceValue());
					}
				}
			} else { // new attendance records
				coachAttendanceList.get(i).setRemoteSessionId(
						session.getRemoteId());
				addRecord(coachAttendanceList.get(i));
				CoachAttendance addedCoachAttendance = new CoachAttendance();
				addedCoachAttendance.setAttendanceValue(coachAttendanceList
						.get(i).getAttendanceValue());
				addedCoachAttendance.setCoach(coachAttendanceList.get(i)
						.getCoach());
				coachAttendanceListOriginal.add(addedCoachAttendance);
			}
		}
		Collections.sort(coachAttendanceList, new CoachAttComparator());
		Collections.sort(coachAttendanceListOriginal, new CoachAttComparator());
	}

	public void updateRecord(CoachAttendance coach) {
		PostCheckinUpdate.done++;
		client.updateCoachAttendanceRecord(coach,
				new CivicoreUpdateDataResultListener<CoachAttendance>() {

					@Override
					public void onRecordUpdateResult(
							CoachAttendance updatedCoachAtt) {
						PostCheckinUpdate.done--;
						if (PostCheckinUpdate.done == 0) {
							DebugInfo.showToast(getActivity(),
									"Attendance Posted!");
						}
						Log.d("temp", "attendance updated");
					}

					@Override
					public void onRecordUpdateError() {
						Log.d("temp", "attendance update error");

					}
				});
	}

	public void addRecord(CoachAttendance coach) {
		PostCheckinUpdate.done++;
		client.insertNewCoachAttendanceRecord(coach,
				new CivicoreUpdateDataResultListener<CoachAttendance>() {

					@Override
					public void onRecordUpdateResult(CoachAttendance object) {
						PostCheckinUpdate.done--;
						if (PostCheckinUpdate.done == 0) {
							DebugInfo.showToast(getActivity(),
									"Attendance Posted!");
						}
						Log.d("temp", "attendance added");
					}

					@Override
					public void onRecordUpdateError() {
						Log.d("temp", "attendance add error");

					}
				});
	}

	public void addCoach(Coach coach) {
		int i = 0;
		for (CoachAttendance coachAtt : coachAttendanceList) {
			i++;
			if (coachAtt.getCoach().getFirstLastName()
					.equals(coach.getFirstLastName())) {
				lvCoachCheckin.setSelection(i);
				return;
			}
		}

		CoachAttendance addedCoachAttendance = new CoachAttendance();
		addedCoachAttendance.setAttendanceValue(AttendanceValue.REGISTERED);
		addedCoachAttendance.setCoach(coach);
		coachAttendanceList.add(addedCoachAttendance);
		Collections.sort(coachAttendanceList, new CoachAttComparator());
		// coachCheckInAdapter.notifyDataSetChanged();
	}

	public void refreshAttendance(){
		Collections.sort(coachAttendanceList, new CoachAttComparator());
		coachCheckInAdapter = new CoachStickyHeaderCheckInAdapter(
				getActivity(), coachAttendanceList);
		lvCoachCheckin.setAdapter(coachCheckInAdapter);
	}

    class AnimationExecutor implements ExpandableStickyListHeadersListView.IAnimationExecutor {

        @Override
        public void executeAnim(final View target, final int animType) {
            if(ExpandableStickyListHeadersListView.ANIMATION_EXPAND==animType&&target.getVisibility()==View.VISIBLE){
                return;
            }
            if(ExpandableStickyListHeadersListView.ANIMATION_COLLAPSE==animType&&target.getVisibility()!=View.VISIBLE){
                return;
            }
            if(mOriginalViewHeightPool.get(target)==null){
                mOriginalViewHeightPool.put(target,target.getHeight());
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
