package org.keenusa.connect.fragments;

import org.keenusa.connect.R;
import org.keenusa.connect.helpers.CivicorePhoneNumberFormatConverter;
import org.keenusa.connect.helpers.EmailAddressValidator;
import org.keenusa.connect.helpers.PhoneNumberValidator;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.Parent;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.networking.PostAndUpdateRemoteAthleteTask;
import org.keenusa.connect.networking.PostAndUpdateRemoteDataTask.PostAndUpdateRemoteDataTaskListener;

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
	PostAndUpdateRemoteDataTaskListener<Athlete> onAthleteProfileUpdateListener;

	public UpdateAthleteProfileFragment() {
	}

	public UpdateAthleteProfileFragment(Athlete athlete, PostAndUpdateRemoteDataTaskListener<Athlete> onAthleteProfileUpdateListener) {
		this.athlete = athlete;
		this.onAthleteProfileUpdateListener = onAthleteProfileUpdateListener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		client = new KeenCivicoreClient(getActivity());
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
	    super.onActivityCreated(arg0);
	    getDialog().getWindow()
	    .getAttributes().windowAnimations = R.style.DialogAnimation;
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

	private Athlete createNewAthleteDTO() {
		Athlete athleteDTO = new Athlete();
		athleteDTO.setRemoteId(athlete.getRemoteId());
		athleteDTO.setId(athlete.getId());
		return athleteDTO;
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
					athleteDTO = createNewAthleteDTO();
				}
				athleteDTO.setPhone(CivicorePhoneNumberFormatConverter.toCivicorePhoneNumberFormat(phone));
			}
			if ((athlete.getEmail() != null && !athlete.getEmail().equals(email)) || (athlete.getEmail() == null && !email.isEmpty())) {
				if (athleteDTO == null) {
					athleteDTO = createNewAthleteDTO();
				}
				athleteDTO.setEmail(email);
			}
			if ((athlete.getPrimaryParent().getEmail() != null && !athlete.getPrimaryParent().getEmail().equals(pemail))
					|| (athlete.getPrimaryParent().getEmail() == null && !pemail.isEmpty())) {
				if (athleteDTO == null) {
					athleteDTO = createNewAthleteDTO();
				}
				if (athleteDTO.getPrimaryParent() == null) {
					athleteDTO.setPrimaryParent(new Parent());
				}
				athleteDTO.getPrimaryParent().setEmail(pemail);
			}
			if ((athlete.getPrimaryParent().getCellPhone() != null && !athlete.getPrimaryParent().getCellPhone().equals(pmobile))
					|| (athlete.getPrimaryParent().getCellPhone() == null && !pmobile.isEmpty())) {
				if (athleteDTO == null) {
					athleteDTO = createNewAthleteDTO();
				}
				if (athleteDTO.getPrimaryParent() == null) {
					athleteDTO.setPrimaryParent(new Parent());
				}
				athleteDTO.getPrimaryParent().setCellPhone(CivicorePhoneNumberFormatConverter.toCivicorePhoneNumberFormat(pmobile));
			}

			if ((athlete.getPrimaryParent().getPhone() != null && !athlete.getPrimaryParent().getPhone().equals(pphone))
					|| (athlete.getPrimaryParent().getPhone() == null && !pphone.isEmpty())) {
				if (athleteDTO == null) {
					athleteDTO = createNewAthleteDTO();
				}
				if (athleteDTO.getPrimaryParent() == null) {
					athleteDTO.setPrimaryParent(new Parent());
				}
				athleteDTO.getPrimaryParent().setPhone(CivicorePhoneNumberFormatConverter.toCivicorePhoneNumberFormat(pphone));
			}

			if (athleteDTO != null) {
				postToRemoteAndUpdateLocally(athleteDTO);
			} else {
				Toast.makeText(getActivity(), "Nothing is changed", Toast.LENGTH_LONG).show();
			}
			closeInputFromWindow();
			getDialog().dismiss();
		}
	}

	private void postToRemoteAndUpdateLocally(Athlete athleteDTO) {
		new PostAndUpdateRemoteAthleteTask(getActivity(), onAthleteProfileUpdateListener, athleteDTO).start();
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

}
