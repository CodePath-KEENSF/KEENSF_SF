package org.keenusa.connect.adapters;

import java.util.ArrayList;

import org.keenusa.connect.R;
import org.keenusa.connect.models.AthleteAttendance;
import org.keenusa.connect.models.CoachAttendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AthletesCheckInAdapter extends ArrayAdapter<AthleteAttendance> {
	TextView tvAthleteCheckIn;
	Button btnChkIn;
	Button btnAbsent;
	Button btnOthers;
	Button btnAddAthlete;
	EditText etSearchAthlete;
	
	public AthletesCheckInAdapter(Context context, ArrayList<AthleteAttendance> athleteList) {
		super(context, android.R.layout.simple_list_item_1, athleteList);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final AthleteAttendance athlete = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.athlete_check_in_items, parent, false);
		}
		tvAthleteCheckIn = (TextView) convertView.findViewById(R.id.tvAthleteCheckIn);
		btnChkIn = (Button) convertView.findViewById(R.id.btnChkIn);
		btnAbsent = (Button) convertView.findViewById(R.id.btnAbsent);
		btnOthers = (Button) convertView.findViewById(R.id.btnOthers);
		btnAddAthlete = (Button) convertView.findViewById(R.id.btnAddAthlete);
		ImageView ivAthleteImageProfile = (ImageView) convertView.findViewById(R.id.ivAthleteImageProfile);
		etSearchAthlete = (EditText) convertView.findViewById(R.id.etSearchAthlete);
		
		
		
		tvAthleteCheckIn.setText(athlete.getAttendedAthleteFullName().toString());
//		ivCoachImageProfile.setImageResource(position);
		btnChkIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "Check-in " + athlete.getAttendedAthleteFullName().toString(), Toast.LENGTH_SHORT).show();
			}
		});;
		btnAbsent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "Absent " + athlete.getAttendedAthleteFullName().toString(), Toast.LENGTH_SHORT).show();
			}
		});;
		btnOthers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "Others " + athlete.getAttendedAthleteFullName().toString(), Toast.LENGTH_SHORT).show();
			}
		});;
		return convertView;
	}


}
