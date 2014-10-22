package org.keenusa.connect.adapters;

import java.util.ArrayList;
import java.util.Arrays;

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
		String[] options = {"","REGISTERED", "ATTENDED", "CALLED_IN_ABSENCE", "CANCELLED", "NO_CALL_NO_SHOW"};
		
		Context context;
		KeenCivicoreClient client;

	public CoachesCheckInAdapter(Context context, ArrayList<CoachAttendance> coachList) {
		super(context, android.R.layout.simple_list_item_1, coachList);
		client = new KeenCivicoreClient(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 final CoachAttendance coach = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.coach_check_in_items, parent, false);
		}
		tvCoachCheckIn = (TextView) convertView.findViewById(R.id.tvCoachCheckIn);
		ImageView ivCoachImageProfile = (ImageView) convertView.findViewById(R.id.ivCoachImageProfile);
		spinner = (Spinner) convertView.findViewById(R.id.spCheckInOptions);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, options);
		spinner.setAdapter(adapter);

		CoachAttendance.AttendanceValue attendanceValue = coach.getAttendanceValue();
		String result = attendanceValue == null ? "" : attendanceValue.toString();
 		tvCoachCheckIn.setText(coach.getAttendedCoachFullName().toString());

 		spinner.setSelection(Arrays.asList(options).indexOf(result), false);	
 		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				if (position == 0) {return;}
				CoachAttendance.AttendanceValue coachAttendance = coach.getAttendanceValue();
				CoachAttendance.AttendanceValue attendanceValue = CoachAttendance.AttendanceValue.valueOf(options[position]);
				if (coachAttendance == attendanceValue) { return; }
				coach.setAttendanceValue(attendanceValue);
				updateRecord(coach);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
		return convertView;
	}

	public void updateRecord(CoachAttendance coach) {
		client.updateCoachAttendanceRecord(coach, new CivicoreUpdateDataResultListener<CoachAttendance>() {
			
			@Override
			public void onRecordUpdateResult(CoachAttendance object) {
				
			}
			
			@Override
			public void onRecordUpdateError() {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
