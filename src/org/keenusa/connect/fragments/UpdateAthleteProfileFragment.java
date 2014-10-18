package org.keenusa.connect.fragments;

import org.keenusa.connect.R;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.Parent;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UpdateAthleteProfileFragment extends DialogFragment {

	private TextView tvAthleteFullName;
	private TextView tvAthleteParentFullNameRelationship;

	private EditText etAthleteCellPhone;
	private EditText etAthletePhone;
	private EditText etAthleteEmail;

	private EditText etAthleteParentCellPhone;
	private EditText etAthleteParentPhone;
	private EditText etAthleteParentEmail;

	private Button btnCancelAthleteProfileUpdate;
	private Button btnSaveAthleteProfileUpdate;

	private Athlete athlete;

	public UpdateAthleteProfileFragment() {
	}

	public UpdateAthleteProfileFragment(Athlete athlete) {
		this.athlete = athlete;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_update_athlete_profile, container);

		tvAthleteFullName = (TextView) view.findViewById(R.id.tvAthleteFullName);
		tvAthleteParentFullNameRelationship = (TextView) view.findViewById(R.id.tvAthleteParentFullNameRelationship);

		etAthleteCellPhone = (EditText) view.findViewById(R.id.etAthleteCellPhone);
		etAthletePhone = (EditText) view.findViewById(R.id.etAthletePhone);
		etAthleteEmail = (EditText) view.findViewById(R.id.etAthleteEmail);

		etAthleteParentCellPhone = (EditText) view.findViewById(R.id.etAthleteParentCellPhone);
		etAthleteParentPhone = (EditText) view.findViewById(R.id.etAthleteParentPhone);
		etAthleteParentEmail = (EditText) view.findViewById(R.id.etAthleteParentEmail);

		populateViews();

		btnCancelAthleteProfileUpdate = (Button) view.findViewById(R.id.btnCancelAthleteProfileUpdate);
		btnSaveAthleteProfileUpdate = (Button) view.findViewById(R.id.btnSaveAthleteProfileUpdate);

		btnCancelAthleteProfileUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelAthleteProfileUpdate(v);

			}
		});

		btnSaveAthleteProfileUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateAthleteProfileDetails(v);

			}
		});

		PhoneNumberFormattingTextWatcher phoneNumberFormattingTextWatcher = new PhoneNumberFormattingTextWatcher();
		etAthleteCellPhone.addTextChangedListener(phoneNumberFormattingTextWatcher);
		etAthletePhone.addTextChangedListener(phoneNumberFormattingTextWatcher);
		etAthleteParentCellPhone.addTextChangedListener(phoneNumberFormattingTextWatcher);
		etAthleteParentPhone.addTextChangedListener(phoneNumberFormattingTextWatcher);

		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

		return view;
	}

	private void populateViews() {
		if (athlete != null) {
			tvAthleteFullName.setText(athlete.getFullName());
			String mobile = athlete.getCellPhone();
			if (mobile != null && !mobile.isEmpty()) {
				etAthleteCellPhone.setText(mobile);
			}
			String phone = athlete.getPhone();
			if (phone != null && !phone.isEmpty()) {
				etAthletePhone.setText(phone);
			}
			String email = athlete.getEmail();
			if (email != null && !email.isEmpty()) {
				etAthleteEmail.setText(email);
			}

			if (athlete.getPrimaryParent() != null) {
				Parent parent = athlete.getPrimaryParent();

				StringBuilder sbParentNameAndRelationship = new StringBuilder(parent.getFullName());
				if (parent.getParentRelationship() != null) {
					sbParentNameAndRelationship.append(" (" + parent.getParentRelationship().getDisplayName() + ")");
				}

				tvAthleteParentFullNameRelationship.setText(sbParentNameAndRelationship);

				String pmobile = parent.getCellPhone();
				if (pmobile != null && !pmobile.isEmpty()) {

					etAthleteParentCellPhone.setText(pmobile);
				}

				String pphone = parent.getPhone();
				if (pphone != null && !pphone.isEmpty()) {

					etAthleteParentPhone.setText(pphone);
				}
				String pemail = parent.getEmail();
				if (pemail != null && !pemail.isEmpty()) {

					etAthleteParentEmail.setText(pemail);
				}
			}
		}
	}

	public void cancelAthleteProfileUpdate(View view) {
		closeInputFromWindow();
		getDialog().dismiss();

	}

	public void updateAthleteProfileDetails(View view) {
		// post update to API pass listener or wait for update processed here
		closeInputFromWindow();
		getDialog().dismiss();
	}

	private void closeInputFromWindow() {
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

}
