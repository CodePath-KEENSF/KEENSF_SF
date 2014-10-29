package org.keenusa.connect.activities;

import org.keenusa.connect.R;
import org.keenusa.connect.data.daos.CoachDAO;
import org.keenusa.connect.fragments.CoachesFragment;
import org.keenusa.connect.fragments.UpdateCoachProfileFragment;
import org.keenusa.connect.helpers.LastAttendedDateFormatter;
import org.keenusa.connect.helpers.PersonNameFormatter;
import org.keenusa.connect.listeners.OnEmailLongClickListener;
import org.keenusa.connect.listeners.OnPhoneLongClickListener;
import org.keenusa.connect.listeners.OnSmsIconClickListener;
import org.keenusa.connect.models.Coach;
import org.keenusa.connect.models.ContactPerson;
import org.keenusa.connect.networking.PostAndUpdateRemoteDataTask.PostAndUpdateRemoteDataTaskListener;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CoachProfileActivity extends FragmentActivity {

	private TextView tvLastAttended;
	private TextView tvLastAttendedLabel;
	private TextView tvNumSessionsAttended;
	private ImageView ivCoachProfilePic;
	private TextView tvCoachFullName;
	private ImageView ivActiveIcon;
	private TextView tvCoachAge;
	private TextView tvCoachLocation;
	private TextView tvCoachCellPhone;
	private ImageView ivCoachCellPhoneMsg;
	private TextView tvCoachPhone;
	private TextView tvCoachEmail;
	private TextView tvCoachForeignLanguages;
	private TextView tvCoachSkills;

	private OnPhoneLongClickListener onPhoneLongClickListener;
	private OnEmailLongClickListener onEmailLongClickListener;
	private OnSmsIconClickListener onSmsIconClickListener;

	private Coach coach;
	private MenuItem editMenuItem;

	private PostAndUpdateRemoteDataTaskListener<Coach> updateListener = new PostAndUpdateRemoteDataTaskListener<Coach>() {
		@Override
		public void onDataPostAndUpdateTaskSuccess(final Coach recordDTO) {
			CoachProfileActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (recordDTO != null) {
						// or load data from DB
						if (recordDTO.getPhone() != null) {
							coach.setPhone(recordDTO.getPhone());
						}
						if (recordDTO.getEmail() != null) {
							coach.setEmail(recordDTO.getEmail());
						}
						if (recordDTO.getCellPhone() != null) {
							coach.setCellPhone(recordDTO.getCellPhone());
						}
					}
					populateViews();
					Toast.makeText(CoachProfileActivity.this, "Coach profile is updated", Toast.LENGTH_SHORT).show();
				}

			});

		}

		@Override
		public void onPostAndUpdateTaskError() {
			CoachProfileActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(CoachProfileActivity.this, "Coach profile update is failed", Toast.LENGTH_SHORT).show();
				}
			});

		}

		@Override
		public void onPostAndUpdateTaskProgress(String progressMessage) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coach_profile);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		overridePendingTransition(R.anim.left_out, R.anim.right_in);
		onPhoneLongClickListener = new OnPhoneLongClickListener(this);
		onEmailLongClickListener = new OnEmailLongClickListener(this);
		onSmsIconClickListener = new OnSmsIconClickListener(this);

		setupViews();
		Intent i = getIntent();
		long coachId = i.getLongExtra(CoachesFragment.COACH_EXTRA_TAG, 0);
		new LoadCoachDataTask().execute(coachId);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.athlete_profile, menu);
		editMenuItem = menu.findItem(R.id.action_edit);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_edit) {
			showUpdateCoachProfileDialog();
			return true;
		} else if (item.getItemId() == android.R.id.home) {
			finish();
			overridePendingTransition(R.anim.left_in, R.anim.right_out);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showUpdateCoachProfileDialog() {
		DialogFragment newFragment = new UpdateCoachProfileFragment(coach, updateListener);
		newFragment.show(getSupportFragmentManager(), "updateCoachProfileDialog");
	}

	private void setupViews() {
		tvLastAttended = (TextView) findViewById(R.id.tvLastAttended);
		tvLastAttendedLabel = (TextView) findViewById(R.id.tvLastAttendedLabel);
		tvNumSessionsAttended = (TextView) findViewById(R.id.tvNumSessionsAttended);
		ivCoachProfilePic = (ImageView) findViewById(R.id.ivCoachProfilePic);
		tvCoachFullName = (TextView) findViewById(R.id.tvCoachFullName);
		ivActiveIcon = (ImageView) findViewById(R.id.ivActiveIcon);
		tvCoachAge = (TextView) findViewById(R.id.tvCoachAge);
		tvCoachLocation = (TextView) findViewById(R.id.tvCoachLocation);
		tvCoachCellPhone = (TextView) findViewById(R.id.tvCoachCellPhone);
		ivCoachCellPhoneMsg = (ImageView) findViewById(R.id.ivCoachCellPhoneMsg);
		tvCoachPhone = (TextView) findViewById(R.id.tvCoachPhone);
		tvCoachEmail = (TextView) findViewById(R.id.tvCoachEmail);
		tvCoachForeignLanguages = (TextView) findViewById(R.id.tvCoachForeignLanguages);
		tvCoachSkills = (TextView) findViewById(R.id.tvCoachSkills);
	}

	private void populateViews() {
		//TODO tvLastAttended
		if (coach != null) {

			if (coach.getGender() == ContactPerson.Gender.FEMALE) {
				ivCoachProfilePic.setImageResource(R.drawable.ic_user_photos_f);
			} else if (coach.getGender() == ContactPerson.Gender.MALE) {
				ivCoachProfilePic.setImageResource(R.drawable.ic_user_photos_m);
			} else {
				ivCoachProfilePic.setImageResource(R.drawable.ic_user_photos_u);
			}

			tvCoachFullName.setText(PersonNameFormatter.getFormatedNameString(coach.getFullName()));
			if (coach.getDateLastAttended() != null) {
				tvLastAttended.setText(LastAttendedDateFormatter.getFormatedLastAttendedDateString(coach.getDateLastAttended()));
			} else {
				tvLastAttended.setVisibility(View.GONE);
				tvLastAttendedLabel.setVisibility(View.GONE);
			}

			if (coach.isActive()) {
				ivActiveIcon.setImageResource(R.drawable.ic_active);
			} else {
				ivActiveIcon.setImageResource(0);
			}

			if (coach.getAge() > 0) {
				tvCoachAge.setText(coach.getAge() + " years");
			} else {
				tvCoachAge.setVisibility(View.GONE);
			}

			String location = coach.getLocation().getLocationString();
			if (location == null || location.isEmpty()) {
				location = getResources().getString(R.string.no_location_text);
				tvCoachLocation.setTextAppearance(this, R.style.TextView_NoInfo_Keen);
			}
			tvCoachLocation.setText(location);

			String mobile = coach.getCellPhone();
			if (mobile == null || mobile.isEmpty()) {
				tvCoachCellPhone.setEnabled(false);
				ivCoachCellPhoneMsg.setVisibility(View.GONE);
				mobile = getResources().getString(R.string.no_mobile_text);
				tvCoachCellPhone.setTextAppearance(this, R.style.TextView_InactiveContact_Keen);
			} else {
				tvCoachCellPhone.setTextAppearance(this, R.style.TextView_ActiveContact_Keen);
				ivCoachCellPhoneMsg.setVisibility(View.VISIBLE);
				ivCoachCellPhoneMsg.setTag(mobile);
			}
			tvCoachCellPhone.setText(mobile);

			String phone = coach.getPhone();
			if (phone == null || phone.isEmpty()) {
				tvCoachPhone.setEnabled(false);
				phone = getResources().getString(R.string.no_phone_text);
				tvCoachPhone.setTextAppearance(this, R.style.TextView_InactiveContact_Keen);
			} else {
				tvCoachPhone.setTextAppearance(this, R.style.TextView_ActiveContact_Keen);
			}
			tvCoachPhone.setText(phone);

			String email = coach.getEmail();
			if (email == null || email.isEmpty()) {
				tvCoachEmail.setEnabled(false);
				email = getResources().getString(R.string.no_email_text);
				tvCoachEmail.setTextAppearance(this, R.style.TextView_InactiveContact_Keen);
			} else {
				tvCoachEmail.setTextAppearance(this, R.style.TextView_ActiveContact_Keen);
			}
			tvCoachEmail.setText(email);

			String foreignLanguages = coach.getForeignLanguages();
			if (foreignLanguages == null || foreignLanguages.isEmpty()) {
				foreignLanguages = getResources().getString(R.string.no_foreign_languages_text);
				tvCoachForeignLanguages.setTextAppearance(this, R.style.TextView_NoInfo_Keen);
			}
			tvCoachForeignLanguages.setText(foreignLanguages);

			String skills = coach.getSkillsExperience();
			if (skills == null || skills.isEmpty()) {
				skills = getResources().getString(R.string.no_skills_text);
				tvCoachSkills.setTextAppearance(this, R.style.TextView_NoInfo_Keen);
			}
			tvCoachSkills.setText(skills);
			tvNumSessionsAttended.setText(coach.getNumberOfSessionsAttended() + " sessions");
			setupOnPhoneLongClickListeners();
			setupOnEmailLongClickListeners();
			setupOnSmsIconClickListeners();
		}

	}

	private void setupOnPhoneLongClickListeners() {
		tvCoachCellPhone.setOnLongClickListener(onPhoneLongClickListener);
		tvCoachPhone.setOnLongClickListener(onPhoneLongClickListener);
	}

	private void setupOnEmailLongClickListeners() {
		tvCoachEmail.setOnLongClickListener(onEmailLongClickListener);

	}

	private void setupOnSmsIconClickListeners() {
		ivCoachCellPhoneMsg.setOnClickListener(onSmsIconClickListener);
	}

	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
	}

	private class LoadCoachDataTask extends AsyncTask<Long, Void, Coach> {

		@Override
		protected void onPreExecute() {
			//			if (llProgressBar != null) {
			//				llProgressBar.setVisibility(View.VISIBLE);
			//			}
		}

		@Override
		protected Coach doInBackground(Long... params) {
			CoachDAO coachDAO = new CoachDAO(CoachProfileActivity.this);
			Coach coach = coachDAO.getCoachById(params[0]);
			return coach;
		}

		@Override
		protected void onPostExecute(Coach dbCoach) {
			//			if (llProgressBar != null) {
			//				llProgressBar.setVisibility(View.GONE);
			//			}
			coach = dbCoach;
			populateViews();

		}

	}

}
