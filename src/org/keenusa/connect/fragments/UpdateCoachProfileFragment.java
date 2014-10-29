package org.keenusa.connect.fragments;

import org.keenusa.connect.R;
import org.keenusa.connect.helpers.CivicorePhoneNumberFormatConverter;
import org.keenusa.connect.helpers.EmailAddressValidator;
import org.keenusa.connect.helpers.PhoneNumberValidator;
import org.keenusa.connect.models.Coach;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.networking.PostAndUpdateRemoteCoachTask;
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

public class UpdateCoachProfileFragment extends DialogFragment {

	private TextView tvCoachFullName;

	private EditText etCoachCellPhone;
	private EditText etCoachPhone;
	private EditText etCoachEmail;

	private Button btnCancelCoachProfileUpdate;
	private Button btnSaveCoachProfileUpdate;

	private Coach coach;
	KeenCivicoreClient client;
	PostAndUpdateRemoteDataTaskListener<Coach> onCoachProfileUpdateListener;

	public UpdateCoachProfileFragment() {
	}

	public UpdateCoachProfileFragment(Coach coach, PostAndUpdateRemoteDataTaskListener<Coach> onCoachProfileUpdateListener) {
		this.coach = coach;
		this.onCoachProfileUpdateListener = onCoachProfileUpdateListener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		client = new KeenCivicoreClient(getActivity());
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_update_coach_profile, container);

		tvCoachFullName = (TextView) view.findViewById(R.id.tvCoachFullName);
		etCoachCellPhone = (EditText) view.findViewById(R.id.etCoachCellPhone);
		etCoachPhone = (EditText) view.findViewById(R.id.etCoachPhone);
		etCoachEmail = (EditText) view.findViewById(R.id.etCoachEmail);

		populateViews();

		btnCancelCoachProfileUpdate = (Button) view.findViewById(R.id.btnCancelCoachProfileUpdate);
		btnSaveCoachProfileUpdate = (Button) view.findViewById(R.id.btnSaveCoachProfileUpdate);

		btnCancelCoachProfileUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelCoachProfileUpdate(v);
			}
		});

		btnSaveCoachProfileUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateCoachProfileDetails(v);
			}
		});

		PhoneNumberFormattingTextWatcher phoneNumberFormattingTextWatcher = new PhoneNumberFormattingTextWatcher();
		etCoachCellPhone.addTextChangedListener(phoneNumberFormattingTextWatcher);
		etCoachPhone.addTextChangedListener(phoneNumberFormattingTextWatcher);

		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
	    super.onActivityCreated(arg0);
	    getDialog().getWindow()
	    .getAttributes().windowAnimations = R.style.DialogAnimation;
	}

	private void populateViews() {
		if (coach != null) {
			tvCoachFullName.setText(coach.getFullName());

			String mobile = coach.getCellPhone();
			if (mobile != null && !mobile.isEmpty()) {
				etCoachCellPhone.setText(mobile);
			}
			String phone = coach.getPhone();
			if (phone != null && !phone.isEmpty()) {
				etCoachPhone.setText(phone);
			}
			String email = coach.getEmail();
			if (email != null && !email.isEmpty()) {
				etCoachEmail.setText(email);
			}

		}
	}

	public void cancelCoachProfileUpdate(View view) {
		closeInputFromWindow();
		getDialog().dismiss();

	}

	private Coach createNewCoachDTO() {
		Coach coachDTO = new Coach();
		coachDTO.setRemoteId(coach.getRemoteId());
		coachDTO.setId(coach.getId());
		return coachDTO;
	}

	public void updateCoachProfileDetails(View view) {

		if (isInputValid()) {
			Coach coachDTO = null;

			String phone = etCoachPhone.getText().toString().trim();
			String email = etCoachEmail.getText().toString().trim();
			String mobile = etCoachCellPhone.getText().toString().trim();

			if ((coach.getPhone() != null && !coach.getPhone().equals(phone)) || (coach.getPhone() == null && !phone.isEmpty())) {
				if (coachDTO == null) {
					coachDTO = createNewCoachDTO();
				}
				coachDTO.setPhone(CivicorePhoneNumberFormatConverter.toCivicorePhoneNumberFormat(phone));
			}
			if ((coach.getEmail() != null && !coach.getEmail().equals(email)) || (coach.getEmail() == null && !email.isEmpty())) {
				if (coachDTO == null) {
					coachDTO = createNewCoachDTO();
				}
				coachDTO.setEmail(email);
			}
			if ((coach.getCellPhone() != null && !coach.getCellPhone().equals(mobile)) || (coach.getCellPhone() == null && !mobile.isEmpty())) {
				if (coachDTO == null) {
					coachDTO = createNewCoachDTO();
				}
				coachDTO.setCellPhone(CivicorePhoneNumberFormatConverter.toCivicorePhoneNumberFormat(mobile));
			}

			if (coachDTO != null) {
				postToRemoteAndUpdateLocally(coachDTO);
			} else {
				Toast.makeText(getActivity(), "Nothing is changed", Toast.LENGTH_LONG).show();
			}
			closeInputFromWindow();
			getDialog().dismiss();
		}
	}

	private void postToRemoteAndUpdateLocally(Coach coach) {
		new PostAndUpdateRemoteCoachTask(getActivity(), onCoachProfileUpdateListener, coach).start();
	}

	private boolean isInputValid() {
		String phone = etCoachPhone.getText().toString().trim();
		String email = etCoachEmail.getText().toString().trim();
		String mobile = etCoachCellPhone.getText().toString().trim();

		if (!phone.isEmpty() && !PhoneNumberValidator.isValidPhoneNumber(phone)) {
			Toast.makeText(getActivity(), "Phone is invalid. It should be 10 digits long", Toast.LENGTH_LONG).show();
			return false;
		}
		if (!mobile.isEmpty() && !PhoneNumberValidator.isValidPhoneNumber(mobile)) {
			Toast.makeText(getActivity(), "Mobile is invalid. It should be 10 digits long", Toast.LENGTH_LONG).show();
			return false;
		}
		// cannot delete existing email via API it is a required field now even if it was empty before
		if (mobile.isEmpty() && coach.getCellPhone() != null && !coach.getCellPhone().isEmpty()) {
			Toast.makeText(getActivity(), "Mobile is required", Toast.LENGTH_LONG).show();
			return false;
		}
		if (!email.isEmpty() && !EmailAddressValidator.isValidEmail(email)) {
			Toast.makeText(getActivity(), "Email is invalid.", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	private void closeInputFromWindow() {
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

}
