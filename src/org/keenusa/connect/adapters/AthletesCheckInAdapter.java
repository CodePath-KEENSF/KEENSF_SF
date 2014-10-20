package org.keenusa.connect.adapters;

import java.util.ArrayList;

import org.keenusa.connect.R;
import org.keenusa.connect.models.AthleteAttendance;
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

public class AthletesCheckInAdapter extends ArrayAdapter<AthleteAttendance> {
	TextView tvAthleteCheckIn;
	Spinner spinner;
	String[] options = {"ATTENDED", "CALLED_IN_ABSENCE", "NO_CALL_NO_SHOW"};
	AthleteAttendance athlete;
	Context context;
	KeenCivicoreClient client;
	
	public AthletesCheckInAdapter(Context context, ArrayList<AthleteAttendance> athleteList) {
		super(context, android.R.layout.simple_list_item_1, athleteList);
		client = new KeenCivicoreClient(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		athlete = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.athlete_check_in_items, parent, false);
		}
		tvAthleteCheckIn = (TextView) convertView.findViewById(R.id.tvAthleteCheckIn);
		ImageView ivAthleteImageProfile = (ImageView) convertView.findViewById(R.id.ivAthleteImageProfile);
		spinner = (Spinner) convertView.findViewById(R.id.spAthCheckInOptions);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, options);
		spinner.setAdapter(adapter);
		
		String result = athlete.getAttendanceValue().toString();
		tvAthleteCheckIn.setText(athlete.getAttendedAthleteFullName().toString());
		
		// get current getAttendanceValue 
		 		if (result.equals("ATTENDED")) {
		 			spinner.setSelection(0);
		 		} else if (result.equals("CALLED_IN_ABSENCE")) {
		 			spinner.setSelection(1);
		 		}  else if (result.equals("NO_CALL_NO_SHOW")) {
		 			spinner.setSelection(2);
		 		}
		
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String item = parent.getItemAtPosition(position).toString();
				if (item.equals("ATTENDED")){
					athlete.setAttendanceValue(AthleteAttendance.AttendanceValue.ATTENDED);
					updateRecord(item);
				} if (item.equals("CALLED_IN_ABSENCE")){
					athlete.setAttendanceValue(AthleteAttendance.AttendanceValue.CALLED_IN_ABSENCE);
					updateRecord(item);
				} if (item.equals("NO_CALL_NO_SHOW")){
					athlete.setAttendanceValue(AthleteAttendance.AttendanceValue.NO_CALL_NO_SHOW);
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

	public void updateRecord(String item) {
		client.updateAthleteAttendanceRecord(athlete, new CivicoreUpdateDataResultListener<AthleteAttendance>() {
			
			@Override
			public void onRecordUpdateResult(AthleteAttendance object) {
				Toast.makeText(getContext(), "Updated to " + object.getAttendanceValue(), Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onRecordUpdateError() {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
