package org.keenusa.connect.activities;

import org.keenusa.connect.R;
import org.keenusa.connect.fragments.CoachesFragment;
import org.keenusa.connect.fragments.UpdateCoachProfileFragment;
import org.keenusa.connect.listeners.OnEmailLongClickListener;
import org.keenusa.connect.listeners.OnPhoneLongClickListener;
import org.keenusa.connect.listeners.OnSmsIconClickListener;
import org.keenusa.connect.models.Coach;
import org.keenusa.connect.models.ContactPerson;
import org.keenusa.connect.networking.KeenCivicoreClient.CivicoreUpdateDataResultListener;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CoachProfileActivity extends FragmentActivity implements CivicoreUpdateDataResultListener<Coach> {

	private TextView tvLastAttended;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coach_profile);
		onPhoneLongClickListener = new OnPhoneLongClickListener(this);
		onEmailLongClickListener = new OnEmailLongClickListener(this);
		onSmsIconClickListener = new OnSmsIconClickListener(this);

		setupViews();
		Intent i = getIntent();
		coach = (Coach) i.getSerializableExtra(CoachesFragment.COACH_EXTRA_TAG);
		populateViews();

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
		}
		return super.onOptionsItemSelected(item);
	}

	private void showUpdateCoachProfileDialog() {
		DialogFragment newFragment = new UpdateCoachProfileFragment(coach, this);
		newFragment.show(getSupportFragmentManager(), "updateCoachProfileDialog");
	}

	private void setupViews() {
		tvLastAttended = (TextView) findViewById(R.id.tvLastAttended);
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

			tvCoachFullName.setText(coach.getFullName());

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
				tvCoachLocation.setTextColor(getResources().getColor(R.color.no_data_message_text_color));
				tvCoachLocation.setTypeface(null, Typeface.ITALIC);
			}
			tvCoachLocation.setText(location);

			String mobile = coach.getCellPhone();
			if (mobile == null || mobile.isEmpty()) {
				tvCoachCellPhone.setEnabled(false);
				ivCoachCellPhoneMsg.setVisibility(View.GONE);
				mobile = getResources().getString(R.string.no_mobile_text);
				tvCoachCellPhone.setTextColor(getResources().getColor(R.color.no_data_message_text_color));
				tvCoachCellPhone.setTypeface(null, Typeface.ITALIC);
			} else {
				ivCoachCellPhoneMsg.setTag(mobile);
			}
			tvCoachCellPhone.setText(mobile);

			String phone = coach.getPhone();
			if (phone == null || phone.isEmpty()) {
				tvCoachPhone.setEnabled(false);
				phone = getResources().getString(R.string.no_phone_text);
				tvCoachPhone.setTextColor(getResources().getColor(R.color.no_data_message_text_color));
				tvCoachPhone.setTypeface(null, Typeface.ITALIC);
			}
			tvCoachPhone.setText(phone);

			String email = coach.getEmail();
			if (email == null || email.isEmpty()) {
				tvCoachEmail.setEnabled(false);
				email = getResources().getString(R.string.no_email_text);
				tvCoachEmail.setTextColor(getResources().getColor(R.color.no_data_message_text_color));
				tvCoachEmail.setTypeface(null, Typeface.ITALIC);
			}
			tvCoachEmail.setText(email);

			String foreignLanguages = coach.getForeignLanguages();
			if (foreignLanguages == null || foreignLanguages.isEmpty()) {
				foreignLanguages = getResources().getString(R.string.no_foreign_languages_text);
				tvCoachForeignLanguages.setTextColor(getResources().getColor(R.color.no_data_message_text_color));
				tvCoachForeignLanguages.setTypeface(null, Typeface.ITALIC);
			}
			tvCoachForeignLanguages.setText(foreignLanguages);

			String skills = coach.getSkillsExperience();
			if (skills == null || skills.isEmpty()) {
				skills = getResources().getString(R.string.no_skills_text);
				tvCoachSkills.setTextColor(getResources().getColor(R.color.no_data_message_text_color));
				tvCoachSkills.setTypeface(null, Typeface.ITALIC);
			}
			tvCoachSkills.setText(skills);
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

	@Override
	public void onUpdateResult(Coach coachDTO) {
		if (coachDTO != null) {
			if (coachDTO.getPhone() != null) {
				coach.setPhone(coachDTO.getPhone());
			}
			if (coachDTO.getEmail() != null) {
				coach.setEmail(coachDTO.getEmail());
			}
			if (coachDTO.getCellPhone() != null) {
				coach.setCellPhone(coachDTO.getCellPhone());
			}
		}
		populateViews();
		Toast.makeText(this, "Coach profile is updated", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUpdateError() {
		Toast.makeText(this, "Coach profile update is failed", Toast.LENGTH_SHORT).show();

	}
}
