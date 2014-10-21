package org.keenusa.connect.adapters;

import java.util.ArrayList;
import java.util.Arrays;

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
	String[] options = {"","ATTENDED", "CALLED_IN_ABSENCE", "NO_CALL_NO_SHOW"};

	Context context;
	KeenCivicoreClient client;
	
	public AthletesCheckInAdapter(Context context, ArrayList<AthleteAttendance> athleteList) {
		super(context, android.R.layout.simple_list_item_1, athleteList);
		client = new KeenCivicoreClient(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final AthleteAttendance athlete = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.athlete_check_in_items, parent, false);
		}
		tvAthleteCheckIn = (TextView) convertView.findViewById(R.id.tvAthleteCheckIn);
		ImageView ivAthleteImageProfile = (ImageView) convertView.findViewById(R.id.ivAthleteImageProfile);
		spinner = (Spinner) convertView.findViewById(R.id.spAthCheckInOptions);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, options);
		spinner.setAdapter(adapter);
		
		AthleteAttendance.AttendanceValue attendanceValue = athlete.getAttendanceValue();
		String result = attendanceValue == null ? "" : attendanceValue.toString();
		tvAthleteCheckIn.setText(athlete.getAttendedAthleteFullName().toString());
		
		// get current getAttendanceValue 
		spinner.setSelection(Arrays.asList(options).indexOf(result), false);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0) { return; }
				AthleteAttendance.AttendanceValue athleteAttendance = athlete.getAttendanceValue();
				AthleteAttendance.AttendanceValue attendanceValue = AthleteAttendance.AttendanceValue.valueOf(options[position]);
				if (athleteAttendance == attendanceValue) { return; }
				athlete.setAttendanceValue(attendanceValue);
				updateRecord(athlete);
 			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		return convertView;
	}

	public void updateRecord(AthleteAttendance athlete) {
		client.updateAthleteAttendanceRecord(athlete, new CivicoreUpdateDataResultListener<AthleteAttendance>() {
			
			@Override
			public void onRecordUpdateResult(AthleteAttendance object) {
				
			}
			
			@Override
			public void onRecordUpdateError() {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
