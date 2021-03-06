package org.keenusa.connect.activities;

import org.keenusa.connect.R;
import org.keenusa.connect.data.daos.AthleteDAO;
import org.keenusa.connect.fragments.AtheletsFragment;
import org.keenusa.connect.fragments.UpdateAthleteProfileFragment;
import org.keenusa.connect.helpers.LastAttendedDateFormatter;
import org.keenusa.connect.helpers.PersonNameFormatter;
import org.keenusa.connect.listeners.OnEmailLongClickListener;
import org.keenusa.connect.listeners.OnPhoneLongClickListener;
import org.keenusa.connect.listeners.OnSmsIconClickListener;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.ContactPerson;
import org.keenusa.connect.models.Parent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AthleteProfileActivity extends FragmentActivity {

	private TextView tvLastAttended;
	private TextView tvLastAttendedLabel;
	private TextView tvNumberOfSessionsAttended;
	private ImageView ivAthleteProfilePic;
	private TextView tvAthleteFullName;
	private ImageView ivActiveIcon;
	private TextView tvAthleteNickName;
	private TextView tvAthleteAge;
	private TextView tvAthleteLanguageAtHome;
	private TextView tvAthleteLocation;
	private TextView tvAthletePhone;
	private TextView tvAthleteEmail;
	private TextView tvAthleteParentFullNameRelationship;
	private TextView tvAthleteParentCellPhone;
	private ImageView ivAthleteParentCellPhoneMsg;
	private TextView tvAthleteParentPhone;
	private TextView tvAthleteParentEmail;

	private RelativeLayout rlNickName;
	private RelativeLayout rlLocation;
	private RelativeLayout rlLanguage;

	private MenuItem editMenuItem;

	private OnPhoneLongClickListener onPhoneLongClickListener;
	private OnEmailLongClickListener onEmailLongClickListener;
	private OnSmsIconClickListener onSmsIconClickListener;
	private Athlete athlete;

	private PostAndUpdateRemoteDataTaskListener<Athlete> updateListener = new PostAndUpdateRemoteDataTaskListener<Athlete>() {
		@Override
		public void onDataPostAndUpdateTaskSuccess(final Athlete recordDTO) {
			AthleteProfileActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (recordDTO != null) {
						if (recordDTO.getPhone() != null) {
							athlete.setPhone(recordDTO.getPhone());
						}
						if (recordDTO.getEmail() != null) {
							athlete.setEmail(recordDTO.getEmail());
						}
						if (recordDTO.getPrimaryParent() != null && recordDTO.getPrimaryParent().getCellPhone() != null) {
							athlete.getPrimaryParent().setCellPhone(recordDTO.getPrimaryParent().getCellPhone());
						}
						if (recordDTO.getPrimaryParent() != null && recordDTO.getPrimaryParent().getPhone() != null) {
							athlete.getPrimaryParent().setPhone(recordDTO.getPrimaryParent().getPhone());
						}
						if (recordDTO.getPrimaryParent() != null && recordDTO.getPrimaryParent().getEmail() != null) {
							athlete.getPrimaryParent().setEmail(recordDTO.getPrimaryParent().getEmail());
						}
					}
					populateViews();
					Toast.makeText(AthleteProfileActivity.this, "Athlete profile is updated", Toast.LENGTH_SHORT).show();
				}

			});
		}

		@Override
		public void onPostAndUpdateTaskError() {
			AthleteProfileActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(AthleteProfileActivity.this, "Athlete profile update is failed", Toast.LENGTH_SHORT).show();
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
		setContentView(R.layout.activity_athlete_profile);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		onPhoneLongClickListener = new OnPhoneLongClickListener(this);
		onEmailLongClickListener = new OnEmailLongClickListener(this);
		onSmsIconClickListener = new OnSmsIconClickListener(this);

		setupViews();
		Intent i = getIntent();
		long athleteId = i.getLongExtra(AtheletsFragment.ATHLETE_EXTRA_TAG, 0);
		new LoadAthleteDataTask().execute(athleteId);

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
			showUpdateAthleteProfileDialog();
			return true;
		} else if (item.getItemId() == android.R.id.home) {
			finish();
			overridePendingTransition(R.anim.left_in, R.anim.right_out);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showUpdateAthleteProfileDialog() {
		DialogFragment newFragment = new UpdateAthleteProfileFragment(athlete, updateListener);
		newFragment.show(getSupportFragmentManager(), "updateAthleteProfileDialog");
	}

	private void setupViews() {

		tvLastAttended = (TextView) findViewById(R.id.tvLastAttended);
		tvLastAttendedLabel = (TextView) findViewById(R.id.tvLastAttendedLabel);
		tvNumberOfSessionsAttended = (TextView) findViewById(R.id.tvNumberOfSessionsAttended);
		ivAthleteProfilePic = (ImageView) findViewById(R.id.ivAthleteProfilePic);
		tvAthleteFullName = (TextView) findViewById(R.id.tvAthleteFullName);
		ivActiveIcon = (ImageView) findViewById(R.id.ivActiveIcon);
		tvAthleteNickName = (TextView) findViewById(R.id.tvAthleteNickName);
		tvAthleteAge = (TextView) findViewById(R.id.tvAthleteAge);
		tvAthleteLanguageAtHome = (TextView) findViewById(R.id.tvAthleteLanguageAtHome);
		tvAthleteLocation = (TextView) findViewById(R.id.tvAthleteLocation);
		tvAthletePhone = (TextView) findViewById(R.id.tvAthletePhone);
		tvAthleteEmail = (TextView) findViewById(R.id.tvAthleteEmail);
		tvAthleteParentFullNameRelationship = (TextView) findViewById(R.id.tvAthleteParentFullNameRelationship);
		tvAthleteParentCellPhone = (TextView) findViewById(R.id.tvAthleteParentCellPhone);
		ivAthleteParentCellPhoneMsg = (ImageView) findViewById(R.id.ivAthleteParentCellPhoneMsg);
		tvAthleteParentPhone = (TextView) findViewById(R.id.tvAthleteParentPhone);
		tvAthleteParentEmail = (TextView) findViewById(R.id.tvAthleteParentEmail);
		rlNickName = (RelativeLayout) findViewById(R.id.rlNickName);
		rlLocation = (RelativeLayout) findViewById(R.id.rlLocation);
		rlLanguage = (RelativeLayout) findViewById(R.id.rlLanguage);

	}

	private void populateViews() {
		//TODO tvLastAttended
		if (athlete != null) {

			if (athlete.getGender() == ContactPerson.Gender.FEMALE) {
				ivAthleteProfilePic.setImageResource(R.drawable.ic_user_photo_f);
			} else if (athlete.getGender() == ContactPerson.Gender.MALE) {
				ivAthleteProfilePic.setImageResource(R.drawable.ic_user_photo_m);
			} else {
				ivAthleteProfilePic.setImageResource(R.drawable.ic_user_photo_u);
			}

			// athlete full name
			tvAthleteFullName.setText(PersonNameFormatter.getFormatedNameString(athlete.getFullName()));
			if (athlete.getDateLastAttended() != null) {
				tvLastAttended.setText(LastAttendedDateFormatter.getFormatedLastAttendedDateString(athlete.getDateLastAttended()));
			} else {
				tvLastAttended.setVisibility(View.GONE);
				tvLastAttendedLabel.setVisibility(View.GONE);
			}

			if (athlete.isActive()) {
				ivActiveIcon.setImageResource(R.drawable.ic_active);
			} else {
				ivActiveIcon.setImageResource(0);
			}

			if (athlete.getAge() > 0) {
				tvAthleteAge.setText(athlete.getAge() + " years");
			} else {
				tvAthleteAge.setVisibility(View.GONE);
			}

			if (athlete.getNickName() != null && !athlete.getNickName().isEmpty()) {
				tvAthleteNickName.setText(athlete.getNickName());
			} else {
				rlNickName.setVisibility(View.GONE);
			}

			String location = athlete.getLocation().getLocationString();
			if (location == null || location.isEmpty()) {
				rlLocation.setVisibility(View.GONE);
			}
			tvAthleteLocation.setText(location);

			String phone = athlete.getPhone();
			if (phone == null || phone.isEmpty()) {
				tvAthletePhone.setEnabled(false);
				phone = getResources().getString(R.string.no_phone_text);
				tvAthletePhone.setTextAppearance(this, R.style.TextView_InactiveContact_Keen);
			} else {
				tvAthletePhone.setTextAppearance(this, R.style.TextView_ActiveContact_Keen);
			}
			tvAthletePhone.setText(phone);

			String email = athlete.getEmail();
			if (email == null || email.isEmpty()) {
				tvAthleteEmail.setEnabled(false);
				email = getResources().getString(R.string.no_email_text);
				tvAthleteEmail.setTextAppearance(this, R.style.TextView_InactiveContact_Keen);
			} else {
				tvAthleteEmail.setTextAppearance(this, R.style.TextView_ActiveContact_Keen);
			}
			tvAthleteEmail.setText(email);

			String languageAtHome = athlete.getPrimaryLanguage();
			if (languageAtHome == null || languageAtHome.isEmpty()) {
				rlLanguage.setVisibility(View.GONE);
			}
			tvAthleteLanguageAtHome.setText(languageAtHome);

			if (athlete.getPrimaryParent() != null) {
				Parent parent = athlete.getPrimaryParent();

				StringBuilder sbParentNameAndRelationship = new StringBuilder(PersonNameFormatter.getFormatedNameString(parent.getFullName()));
				if (parent.getParentRelationship() != null) {
					sbParentNameAndRelationship.append(" (" + parent.getParentRelationship().getDisplayName() + ")");
				}

				tvAthleteParentFullNameRelationship.setText(sbParentNameAndRelationship);

				String pmobile = parent.getCellPhone();
				if (pmobile == null || pmobile.isEmpty()) {
					tvAthleteParentCellPhone.setEnabled(false);
					ivAthleteParentCellPhoneMsg.setVisibility(View.GONE);
					pmobile = getResources().getString(R.string.no_mobile_text);
					tvAthleteParentCellPhone.setTextAppearance(this, R.style.TextView_InactiveContact_Keen);
				} else {
					tvAthleteParentCellPhone.setTextAppearance(this, R.style.TextView_ActiveContact_Keen);
					ivAthleteParentCellPhoneMsg.setVisibility(View.VISIBLE);
					ivAthleteParentCellPhoneMsg.setTag(pmobile);
				}
				tvAthleteParentCellPhone.setText(pmobile);

				String pphone = parent.getPhone();
				if (pphone == null || pphone.isEmpty()) {
					tvAthleteParentPhone.setEnabled(false);

					pphone = getResources().getString(R.string.no_phone_text);
					tvAthleteParentPhone.setTextAppearance(this, R.style.TextView_InactiveContact_Keen);
				} else {
					tvAthleteParentPhone.setTextAppearance(this, R.style.TextView_ActiveContact_Keen);
				}
				tvAthleteParentPhone.setText(pphone);

				String pemail = parent.getEmail();
				if (pemail == null || pemail.isEmpty()) {
					tvAthleteParentEmail.setEnabled(false);
					pemail = getResources().getString(R.string.no_email_text);
					tvAthleteParentEmail.setTextAppearance(this, R.style.TextView_InactiveContact_Keen);
				} else {
					tvAthleteParentEmail.setTextAppearance(this, R.style.TextView_ActiveContact_Keen);
				}
				tvAthleteParentEmail.setText(pemail);
				tvNumberOfSessionsAttended.setText(String.valueOf(athlete.getNumberOfSessionsAttended()));
				setupOnPhoneLongClickListeners();
				setupOnEmailLongClickListeners();
				setupOnSmsIconClickListeners();
			}
		}

	}

	private void setupOnPhoneLongClickListeners() {
		tvAthletePhone.setOnLongClickListener(onPhoneLongClickListener);
		tvAthleteParentCellPhone.setOnLongClickListener(onPhoneLongClickListener);
		tvAthleteParentPhone.setOnLongClickListener(onPhoneLongClickListener);

	}

	private void setupOnEmailLongClickListeners() {
		tvAthleteEmail.setOnLongClickListener(onEmailLongClickListener);
		tvAthleteParentEmail.setOnLongClickListener(onEmailLongClickListener);

	}

	private void setupOnSmsIconClickListeners() {
		ivAthleteParentCellPhoneMsg.setOnClickListener(onSmsIconClickListener);
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
	}

	private class LoadAthleteDataTask extends AsyncTask<Long, Void, Athlete> {

		@Override
		protected void onPreExecute() {
			//			if (llProgressBar != null) {
			//				llProgressBar.setVisibility(View.VISIBLE);
			//			}
		}

		@Override
		protected Athlete doInBackground(Long... params) {
			AthleteDAO athleteDAO = new AthleteDAO(AthleteProfileActivity.this);
			Athlete athlete = athleteDAO.getAthleteById(params[0]);
			return athlete;
		}

		@Override
		protected void onPostExecute(Athlete dbAthlete) {
			//			if (llProgressBar != null) {
			//				llProgressBar.setVisibility(View.GONE);
			//			}
			athlete = dbAthlete;
			populateViews();

		}

	}
}