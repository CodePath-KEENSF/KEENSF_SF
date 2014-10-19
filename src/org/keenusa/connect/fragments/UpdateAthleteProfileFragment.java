package org.keenusa.connect.fragments;

import org.keenusa.connect.R;
import org.keenusa.connect.helpers.CivicorePhoneNumberFormatConverter;
import org.keenusa.connect.helpers.EmailAddressValidator;
import org.keenusa.connect.helpers.PhoneNumberValidator;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.Parent;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.networking.KeenCivicoreClient.CivicoreUpdateDataResultListener;

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
import android.widget.Toast;

public class UpdateAthleteProfileFragment extends DialogFragment {

	private TextView tvAthleteFullName;
	private TextView tvAthleteParentFullNameRelationship;

	private EditText etAthletePhone;
	private EditText etAthleteEmail;

	private EditText etAthleteParentCellPhone;
	private EditText etAthleteParentPhone;
	private EditText etAthleteParentEmail;

	private Button btnCancelAthleteProfileUpdate;
	private Button btnSaveAthleteProfileUpdate;

	private Athlete athlete;
	KeenCivicoreClient client;
	OnAthleteProfileUpdateListener onAthleteProfileUpdateListener;

	public UpdateAthleteProfileFragment() {
	}

	public UpdateAthleteProfileFragment(Athlete athlete, OnAthleteProfileUpdateListener onAthleteProfileUpdateListener) {
		this.athlete = athlete;
		this.onAthleteProfileUpdateListener = onAthleteProfileUpdateListener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		client = new KeenCivicoreClient(getActivity());
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_update_athlete_profile, container);

		tvAthleteFullName = (TextView) view.findViewById(R.id.tvAthleteFullName);
		tvAthleteParentFullNameRelationship = (TextView) view.findViewById(R.id.tvAthleteParentFullNameRelationship);

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

		if (isInputValid()) {
			Athlete athleteDTO = null;

			String phone = etAthletePhone.getText().toString().trim();
			String email = etAthleteEmail.getText().toString().trim();
			String pmobile = etAthleteParentCellPhone.getText().toString().trim();
			String pphone = etAthleteParentPhone.getText().toString().trim();
			String pemail = etAthleteParentEmail.getText().toString().trim();

			if ((athlete.getPhone() != null && !athlete.getPhone().equals(phone)) || (athlete.getPhone() == null && !phone.isEmpty())) {
				if (athleteDTO == null) {
					athleteDTO = new Athlete();
					athleteDTO.setRemoteId(athlete.getRemoteId());
				}
				athleteDTO.setPhone(CivicorePhoneNumberFormatConverter.toCivicorePhoneNumberFormat(phone));
			}
			if ((athlete.getEmail() != null && !athlete.getEmail().equals(email)) || (athlete.getEmail() == null && !email.isEmpty())) {
				if (athleteDTO == null) {
					athleteDTO = new Athlete();
					athleteDTO.setRemoteId(athlete.getRemoteId());

				}
				athleteDTO.setEmail(email);
			}
			if ((athlete.getPrimaryParent().getEmail() != null && !athlete.getPrimaryParent().getEmail().equals(pemail))
					|| (athlete.getPrimaryParent().getEmail() == null && !pemail.isEmpty())) {
				if (athleteDTO == null) {
					athleteDTO = new Athlete();
					athleteDTO.setRemoteId(athlete.getRemoteId());
				}
				if (athleteDTO.getPrimaryParent() == null) {
					athleteDTO.setPrimaryParent(new Parent());
				}
				athleteDTO.getPrimaryParent().setEmail(pemail);
			}
			if ((athlete.getPrimaryParent().getCellPhone() != null && !athlete.getPrimaryParent().getCellPhone().equals(pmobile))
					|| (athlete.getPrimaryParent().getCellPhone() == null && !pmobile.isEmpty())) {
				if (athleteDTO == null) {
					athleteDTO = new Athlete();
					athleteDTO.setRemoteId(athlete.getRemoteId());
				}
				if (athleteDTO.getPrimaryParent() == null) {
					athleteDTO.setPrimaryParent(new Parent());
				}
				athleteDTO.getPrimaryParent().setCellPhone(CivicorePhoneNumberFormatConverter.toCivicorePhoneNumberFormat(pmobile));
			}

			if ((athlete.getPrimaryParent().getPhone() != null && !athlete.getPrimaryParent().getPhone().equals(pphone))
					|| (athlete.getPrimaryParent().getPhone() == null && !pphone.isEmpty())) {
				if (athleteDTO == null) {
					athleteDTO = new Athlete();
					athleteDTO.setRemoteId(athlete.getRemoteId());
				}
				if (athleteDTO.getPrimaryParent() == null) {
					athleteDTO.setPrimaryParent(new Parent());
				}
				athleteDTO.getPrimaryParent().setPhone(CivicorePhoneNumberFormatConverter.toCivicorePhoneNumberFormat(pphone));
			}

			if (athleteDTO != null) {

				client.updateAfthetProfileRecord(athleteDTO, new CivicoreUpdateDataResultListener<Athlete>() {

					@Override
					public void onUpdateResult(Athlete athleteDTO) {
						if (onAthleteProfileUpdateListener != null) {
							if (athleteDTO != null) {
								if (athleteDTO.getPhone() != null) {
									athlete.setPhone(athleteDTO.getPhone());
								}
								if (athleteDTO.getEmail() != null) {
									athlete.setEmail(athleteDTO.getEmail());
								}
								if (athleteDTO.getPrimaryParent() != null && athleteDTO.getPrimaryParent().getCellPhone() != null) {
									athlete.getPrimaryParent().setCellPhone(athleteDTO.getPrimaryParent().getCellPhone());
								}
								if (athleteDTO.getPrimaryParent() != null && athleteDTO.getPrimaryParent().getPhone() != null) {
									athlete.getPrimaryParent().setPhone(athleteDTO.getPrimaryParent().getPhone());
								}
								if (athleteDTO.getPrimaryParent() != null && athleteDTO.getPrimaryParent().getEmail() != null) {
									athlete.getPrimaryParent().setEmail(athleteDTO.getPrimaryParent().getEmail());
								}
							}
							onAthleteProfileUpdateListener.OnAthleteProfileUpdate(athlete);

						}
					}

					@Override
					public void onUpdateError() {
						onAthleteProfileUpdateListener.OnAthleteProfileUpdateError();

					}
				});
			}
			closeInputFromWindow();
			getDialog().dismiss();
		}
	}

	private boolean isInputValid() {
		String phone = etAthletePhone.getText().toString().trim();
		String email = etAthleteEmail.getText().toString().trim();
		String pmobile = etAthleteParentCellPhone.getText().toString().trim();
		String pphone = etAthleteParentPhone.getText().toString().trim();
		String pemail = etAthleteParentEmail.getText().toString().trim();

		if (!phone.isEmpty() && !PhoneNumberValidator.isValidPhoneNumber(phone)) {
			Toast.makeText(getActivity(), "Athlete phone is invalid. It should be 10 digits long", Toast.LENGTH_LONG).show();
			return false;
		}
		if (!pmobile.isEmpty() && !PhoneNumberValidator.isValidPhoneNumber(pmobile)) {
			Toast.makeText(getActivity(), "Parent mobile is invalid. It should be 10 digits long", Toast.LENGTH_LONG).show();
			return false;
		}
		if (!pphone.isEmpty() && !PhoneNumberValidator.isValidPhoneNumber(pphone)) {
			Toast.makeText(getActivity(), "Parent phone is invalid. It should be 10 digits long", Toast.LENGTH_LONG).show();
			return false;
		}
		if (!email.isEmpty() && !EmailAddressValidator.isValidEmail(email)) {
			Toast.makeText(getActivity(), "Email is invalid.", Toast.LENGTH_LONG).show();
			return false;
		}
		if (!pemail.isEmpty() && !EmailAddressValidator.isValidEmail(pemail)) {
			Toast.makeText(getActivity(), "Parent email is invalid.", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	private void closeInputFromWindow() {
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	public interface OnAthleteProfileUpdateListener {
		public void OnAthleteProfileUpdate(Athlete athlete);

		public void OnAthleteProfileUpdateError();
	}

}
