package org.keenusa.connect.adapters;

import java.util.ArrayList;

import org.keenusa.connect.R;
import org.keenusa.connect.models.CoachAttendance;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.networking.KeenCivicoreClient.CivicoreUpdateDataResultListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CoachesCheckInAdapter extends ArrayAdapter<CoachAttendance> {

		TextView tvCoachCheckIn;
		Spinner spinner;
		String[] options = {"REGISTERED", "ATTENDED", "CALLED_IN_ABSENCE", "CANCELLED", "NO_CALL_NO_SHOW"};
		
		CoachAttendance coach;
		Context context;
		KeenCivicoreClient client;

	public CoachesCheckInAdapter(Context context, ArrayList<CoachAttendance> coachList) {
		super(context, android.R.layout.simple_list_item_1, coachList);
		client = new KeenCivicoreClient(context);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 coach = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.coach_check_in_items, parent, false);
		}
		tvCoachCheckIn = (TextView) convertView.findViewById(R.id.tvCoachCheckIn);
		ImageView ivCoachImageProfile = (ImageView) convertView.findViewById(R.id.ivCoachImageProfile);
		spinner = (Spinner) convertView.findViewById(R.id.spCheckInOptions);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, options);
		spinner.setAdapter(adapter);

		String result = coach.getAttendanceValue().toString();
 		tvCoachCheckIn.setText(coach.getAttendedCoachFullName().toString());
 		// get current getAttendanceValue 
 		if (result.equals("REGISTERED")) {
 			spinner.setSelection(0);
 		} else if (result.equals("ATTENDED")) {
 			spinner.setSelection(1);
 		} else if (result.equals("CALLED_IN_ABSENCE")) {
 			spinner.setSelection(2);
 		} else if (result.equals("CANCELLED")) {
 			spinner.setSelection(3);
 		} else if (result.equals("NO_CALL_NO_SHOW")) {
 			spinner.setSelection(4);
 		}
 		
 		Toast.makeText(getContext(), "Context is " + context, Toast.LENGTH_LONG).show();
 		
 		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				String item = parent.getItemAtPosition(position).toString();
				if (item.equals("REGISTERED")){
					coach.setAttendanceValue(CoachAttendance.AttendanceValue.REGISTERED);
					updateRecord(item);
				} if (item.equals("ATTENDED")){
					coach.setAttendanceValue(CoachAttendance.AttendanceValue.ATTENDED);
					updateRecord(item);
				} if (item.equals("CALLED_IN_ABSENCE")){
					coach.setAttendanceValue(CoachAttendance.AttendanceValue.CALLED_IN_ABSENCE);
					updateRecord(item);
				} if (item.equals("CANCELLED")){
					coach.setAttendanceValue(CoachAttendance.AttendanceValue.CANCELLED);
					updateRecord(item);
				} if (item.equals("NO_CALL_NO_SHOW")){
					coach.setAttendanceValue(CoachAttendance.AttendanceValue.NO_CALL_NO_SHOW);
					updateRecord(item);
				} 	
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
 			
		});
		return convertView;
	}

	public void updateRecord(String string) {
		client.updateCoachAttendanceRecord(coach, new CivicoreUpdateDataResultListener<CoachAttendance>() {
			
			@Override
			public void onRecordUpdateResult(CoachAttendance object) {
				Toast.makeText(getContext(), "Updated to " + object.getAttendanceValue(), Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onRecordUpdateError() {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
