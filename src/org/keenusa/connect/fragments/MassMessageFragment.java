package org.keenusa.connect.fragments;

import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.models.CoachAttendance;
import org.keenusa.connect.models.KeenProgramEnrolment;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.models.Parent;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
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

public class MassMessageFragment extends DialogFragment {

	private KeenSession session;
	private List<KeenProgramEnrolment> programEnrollments;
	private List<CoachAttendance> coachAttendanceList;

	private EditText etMassMessage;
	private Button btnMassMessageDone;
	private TextView tvEmail;
	private TextView tvSMS;

	public MassMessageFragment() {
		// Empty constructor required for DialogFragment
	}

	public MassMessageFragment(List<KeenProgramEnrolment> enrolledAthleteList, List<CoachAttendance> coachAttendanceList) {
		this.programEnrollments = enrolledAthleteList;
		this.coachAttendanceList = coachAttendanceList;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mass_message, container);
		setViews(view);

		setOnClickListeners();

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

	private void setOnClickListeners() {

		// set the state based on number of characters
		etMassMessage.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			public void afterTextChanged(Editable arg0) {
				int messageCharacters = etMassMessage.getText().length();
				if (messageCharacters == 0) {
					// Disable textviews, change the color to gray, change from
					// bold to normal
					tvEmail.setEnabled(false);
					tvSMS.setEnabled(false);
					tvEmail.setTextColor(getResources().getColor(android.R.color.darker_gray));
					tvSMS.setTextColor(getResources().getColor(android.R.color.darker_gray));
					tvEmail.setTypeface(null, Typeface.NORMAL);
					tvSMS.setTypeface(null, Typeface.NORMAL);
				} else {
					tvEmail.setEnabled(true);
					tvSMS.setEnabled(true);
					tvEmail.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
					tvSMS.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
					tvEmail.setTypeface(null, Typeface.BOLD);
					tvSMS.setTypeface(null, Typeface.BOLD);
				}
			}
		});

		// send email
		tvEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendMassEmail();
			}
		});

		// send sms
		tvSMS.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendMassSMS();
			}
		});

		// Close the dialog
		btnMassMessageDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
				getDialog().dismiss();
			}
		});
	}

	private void sendMassEmail() {
		String[] emails = new String[100];
		int count = 0;

		if (programEnrollments != null) {
			for (KeenProgramEnrolment enrolment : programEnrollments) {
				Parent parent = enrolment.getAthlete().getPrimaryParent();
				if (parent.getEmail() != null) {
					emails[count++] = parent.getEmail();
					;
				}
			}
		}

		if (coachAttendanceList != null) {
			for (CoachAttendance coachAtt : coachAttendanceList) {
				if (coachAtt.getCoach().getEmail() != null) {
					emails[count++] = coachAtt.getCoach().getEmail();
				}
			}
		}

		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("message/rfc822");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, emails);
		emailIntent.putExtra(Intent.EXTRA_TEXT, etMassMessage.getText().toString());
		try {
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
		startActivity(emailIntent);
		getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
	}

	private void sendMassSMS() {
		String phone = "";
		if (programEnrollments != null) {
			for (KeenProgramEnrolment enrolment : programEnrollments) {
				Parent parent = enrolment.getAthlete().getPrimaryParent();
				if (parent.getCellPhone() != null) {
					phone = phone + parent.getCellPhone() + ",";
				}
			}
		}

		if (coachAttendanceList != null) {
			for (CoachAttendance coachAtt : coachAttendanceList) {
				if (coachAtt.getCoach().getCellPhone() != null) {
					phone = phone + coachAtt.getCoach().getCellPhone() + ",";
				}
			}
		}

		if (phone != null && !phone.isEmpty()) {
			Intent smsIntent = new Intent(Intent.ACTION_VIEW);
			smsIntent.putExtra("address", phone);
			smsIntent.putExtra("sms_body", etMassMessage.getText().toString());
			smsIntent.setType("vnd.android-dir/mms-sms");
			startActivity(Intent.createChooser(smsIntent, "Send message..."));
		} else {
			Toast.makeText(getActivity(), "Can not send sms. The phone number is invalid", Toast.LENGTH_SHORT).show();
		}
	}

	private void setViews(View view) {
		etMassMessage = (EditText) view.findViewById(R.id.etMassMessage);
		btnMassMessageDone = (Button) view.findViewById(R.id.btnMassMessageDone);
		tvEmail = (TextView) view.findViewById(R.id.tvEmail);
		tvSMS = (TextView) view.findViewById(R.id.tvSMS);
		tvEmail.setEnabled(false);
		tvSMS.setEnabled(false);
	}
}
