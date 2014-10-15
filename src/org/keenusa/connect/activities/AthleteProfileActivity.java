package org.keenusa.connect.activities;

import org.keenusa.connect.R;
import org.keenusa.connect.fragments.AtheletsFragment;
import org.keenusa.connect.listeners.OnEmailLongClickListener;
import org.keenusa.connect.listeners.OnPhoneLongClickListener;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.ContactPerson;
import org.keenusa.connect.models.Parent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AthleteProfileActivity extends Activity {

	private TextView tvLastAttended;
	private ImageView ivAthleteProfilePic;
	private TextView tvAthleteFullName;
	private ImageView ivActiveIcon;
	private TextView tvAthleteNickName;
	private TextView tvAthleteAge;
	private TextView tvAthleteLanguageAtHome;
	private TextView tvAthleteLocation;
	private TextView tvAthleteCellPhone;
	private TextView tvAthletePhone;
	private TextView tvAthleteEmail;
	private TextView tvAthleteParentFullNameRelationship;
	private TextView tvAthleteParentCellPhone;
	private TextView tvAthleteParentPhone;
	private TextView tvAthleteParentEmail;

	private OnPhoneLongClickListener onPhoneLongClickListener;
	private OnEmailLongClickListener onEmailLongClickListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_athlete_profile);
		onPhoneLongClickListener = new OnPhoneLongClickListener(this);
		onEmailLongClickListener = new OnEmailLongClickListener(this);

		Intent i = getIntent();
		Athlete athlete = (Athlete) i.getSerializableExtra(AtheletsFragment.ATHLETE_EXTRA_TAG);
		setupViews();
		populateViews(athlete);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.athlete_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setupViews() {

		tvLastAttended = (TextView) findViewById(R.id.tvLastAttended);
		ivAthleteProfilePic = (ImageView) findViewById(R.id.ivAthleteProfilePic);
		tvAthleteFullName = (TextView) findViewById(R.id.tvAthleteFullName);
		ivActiveIcon = (ImageView) findViewById(R.id.ivActiveIcon);
		tvAthleteNickName = (TextView) findViewById(R.id.tvAthleteNickName);
		tvAthleteAge = (TextView) findViewById(R.id.tvAthleteAge);
		tvAthleteLanguageAtHome = (TextView) findViewById(R.id.tvAthleteLanguageAtHome);
		tvAthleteLocation = (TextView) findViewById(R.id.tvAthleteLocation);

		tvAthleteCellPhone = (TextView) findViewById(R.id.tvAthleteCellPhone);

		tvAthletePhone = (TextView) findViewById(R.id.tvAthletePhone);
		tvAthleteEmail = (TextView) findViewById(R.id.tvAthleteEmail);

		tvAthleteParentFullNameRelationship = (TextView) findViewById(R.id.tvAthleteParentFullNameRelationship);
		tvAthleteParentCellPhone = (TextView) findViewById(R.id.tvAthleteParentCellPhone);
		tvAthleteParentPhone = (TextView) findViewById(R.id.tvAthleteParentPhone);
		tvAthleteParentEmail = (TextView) findViewById(R.id.tvAthleteParentEmail);
	}

	private void populateViews(Athlete athlete) {
		//TODO tvLastAttended
		if (athlete != null) {
			// profile pic
			if (athlete.getGender() == ContactPerson.Gender.FEMALE) {
				ivAthleteProfilePic.setImageResource(R.drawable.ic_user_photos_f);
			} else if (athlete.getGender() == ContactPerson.Gender.MALE) {
				ivAthleteProfilePic.setImageResource(R.drawable.ic_user_photos_m);
			} else {
				ivAthleteProfilePic.setImageResource(R.drawable.ic_user_photos_u);
			}

			// athlete full name
			tvAthleteFullName.setText(athlete.getFullName());

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
				tvAthleteNickName.setText("(" + athlete.getNickName() + ")");
			} else {
				tvAthleteNickName.setVisibility(View.GONE);
			}

			String location = athlete.getLocation().getLocationString();
			if (location == null || location.isEmpty()) {
				location = getResources().getString(R.string.no_location_text);
				tvAthleteLocation.setTextColor(getResources().getColor(R.color.no_data_message_text_color));
				tvAthleteLocation.setTypeface(null, Typeface.ITALIC);
			}
			tvAthleteLocation.setText(location);

			String mobile = athlete.getCellPhone();
			if (mobile == null || mobile.isEmpty()) {
				tvAthleteCellPhone.setEnabled(false);
				mobile = getResources().getString(R.string.no_mobile_text);
				tvAthleteCellPhone.setTextColor(getResources().getColor(R.color.no_data_message_text_color));
				tvAthleteCellPhone.setTypeface(null, Typeface.ITALIC);
			}
			tvAthleteCellPhone.setText(mobile);

			String phone = athlete.getPhone();
			if (phone == null || phone.isEmpty()) {
				tvAthletePhone.setEnabled(false);
				phone = getResources().getString(R.string.no_phone_text);
				tvAthletePhone.setTextColor(getResources().getColor(R.color.no_data_message_text_color));
				tvAthletePhone.setTypeface(null, Typeface.ITALIC);
			}
			tvAthletePhone.setText(phone);

			String email = athlete.getEmail();
			if (email == null || email.isEmpty()) {
				tvAthleteEmail.setEnabled(false);
				email = getResources().getString(R.string.no_email_text);
				tvAthleteEmail.setTextColor(getResources().getColor(R.color.no_data_message_text_color));
				tvAthleteEmail.setTypeface(null, Typeface.ITALIC);
			}
			tvAthleteEmail.setText(email);

			String languageAtHome = athlete.getPrimaryLanguage();
			if (languageAtHome == null || languageAtHome.isEmpty()) {
				languageAtHome = getResources().getString(R.string.no_language_at_home_text);
				tvAthleteLanguageAtHome.setTextColor(getResources().getColor(R.color.no_data_message_text_color));
				tvAthleteLanguageAtHome.setTypeface(null, Typeface.ITALIC);
			}
			tvAthleteLanguageAtHome.setText(languageAtHome);

			if (athlete.getPrimaryParent() != null) {
				Parent parent = athlete.getPrimaryParent();

				StringBuilder sbParentNameAndRelationship = new StringBuilder(parent.getFullName());
				if (parent.getParentRelationship() != null) {
					sbParentNameAndRelationship.append(" (" + parent.getParentRelationship().getDisplayName() + ")");
				}

				tvAthleteParentFullNameRelationship.setText(sbParentNameAndRelationship);

				String pmobile = parent.getCellPhone();
				if (pmobile == null || pmobile.isEmpty()) {
					tvAthleteParentCellPhone.setEnabled(false);
					pmobile = getResources().getString(R.string.no_mobile_text);
					tvAthleteParentCellPhone.setTextColor(getResources().getColor(R.color.no_data_message_text_color));
					tvAthleteParentCellPhone.setTypeface(null, Typeface.ITALIC);
				}
				tvAthleteParentCellPhone.setText(pmobile);
				String pphone = parent.getPhone();

				if (pphone == null || pphone.isEmpty()) {
					tvAthleteParentPhone.setEnabled(false);
					pphone = getResources().getString(R.string.no_phone_text);
					tvAthleteParentPhone.setTextColor(getResources().getColor(R.color.no_data_message_text_color));
					tvAthleteParentPhone.setTypeface(null, Typeface.ITALIC);
				}
				tvAthleteParentPhone.setText(pphone);

				String pemail = parent.getEmail();
				if (pemail == null || pemail.isEmpty()) {
					tvAthleteParentEmail.setEnabled(false);
					pemail = getResources().getString(R.string.no_email_text);
					tvAthleteParentEmail.setTextColor(getResources().getColor(R.color.no_data_message_text_color));
					tvAthleteParentEmail.setTypeface(null, Typeface.ITALIC);
				} else {
					tvAthleteParentEmail.setTag(pemail);
				}
				tvAthleteParentEmail.setText(pemail);

				setupOnPhoneLongClickListeners();
				setupOnEmailLongClickListeners();
			}
		}

	}

	private void setupOnPhoneLongClickListeners() {
		tvAthleteCellPhone.setOnLongClickListener(onPhoneLongClickListener);
		tvAthletePhone.setOnLongClickListener(onPhoneLongClickListener);
		tvAthleteParentCellPhone.setOnLongClickListener(onPhoneLongClickListener);
		tvAthleteParentPhone.setOnLongClickListener(onPhoneLongClickListener);

	}

	private void setupOnEmailLongClickListeners() {
		tvAthleteEmail.setOnLongClickListener(onEmailLongClickListener);
		tvAthleteParentEmail.setOnLongClickListener(onEmailLongClickListener);

	}

}
