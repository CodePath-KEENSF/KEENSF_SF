package org.keenusa.connect.fragments;

import org.keenusa.connect.R;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.utilities.DebugInfo;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MassMessageFragment extends DialogFragment {

	private KeenSession session;
	
	private EditText etMassMessage;
	private Button btnMassMessageDone;
	private TextView tvEmail;
	private TextView tvSMS;
	
	public MassMessageFragment() {
		// Empty constructor required for DialogFragment
	}
	
	public MassMessageFragment(KeenSession session){
		this.session = session;
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
	
	private void setOnClickListeners(){

		// set the state based on number of characters
		etMassMessage.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			
			public void afterTextChanged(Editable arg0) {
				int messageCharacters = etMassMessage.getText().length();
				if(messageCharacters == 0){
					//Disable textviews, change the color to gray, change from bold to normal
					tvEmail.setEnabled(false);
					tvSMS.setEnabled(false);
					tvEmail.setTextColor(getResources().getColor(android.R.color.darker_gray));
					tvSMS.setTextColor(getResources().getColor(android.R.color.darker_gray));
					tvEmail.setTypeface(null, Typeface.NORMAL);
					tvSMS.setTypeface(null, Typeface.NORMAL);
				}else{
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
	
	private void sendMassEmail(){
		Log.d("temp", "email");
		DebugInfo.showToast(getActivity(), "Sending Email");
	}
	
	private void sendMassSMS(){
		Log.d("temp", "sms");
		DebugInfo.showToast(getActivity(), "Sending SMS");
	}

	private void setViews(View view){
		etMassMessage = (EditText)view.findViewById(R.id.etMassMessage);
		btnMassMessageDone = (Button)view.findViewById(R.id.btnMassMessageDone);
		tvEmail = (TextView)view.findViewById(R.id.tvEmail);
		tvSMS = (TextView)view.findViewById(R.id.tvSMS);
		tvEmail.setEnabled(false);
		tvSMS.setEnabled(false);
	}
}
