package org.keenusa.connect.adapters;

import java.util.ArrayList;

import org.keenusa.connect.R;
import org.keenusa.connect.models.AthleteAttendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AthletesCheckInAdapter extends ArrayAdapter<AthleteAttendance> {
	TextView tvAthleteCheckIn;
	Button btnAddAthlete;
	EditText etSearchAthlete;
	Spinner spinner;
	String[] options = {"Check-In", "Absent", "Others"};
	
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
		btnAddAthlete = (Button) convertView.findViewById(R.id.btnAddAthlete);
		ImageView ivAthleteImageProfile = (ImageView) convertView.findViewById(R.id.ivAthleteImageProfile);
		etSearchAthlete = (EditText) convertView.findViewById(R.id.etSearchAthlete);
		spinner = (Spinner) convertView.findViewById(R.id.spAthCheckInOptions);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, options);
		spinner.setAdapter(adapter);
		
		tvAthleteCheckIn.setText(athlete.getAttendedAthleteFullName().toString());
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				int pos = spinner.getSelectedItemPosition();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		return convertView;
	}
}
