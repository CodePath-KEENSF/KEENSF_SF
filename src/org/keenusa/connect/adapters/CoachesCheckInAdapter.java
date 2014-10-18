package org.keenusa.connect.adapters;

import java.util.ArrayList;

import org.keenusa.connect.R;
import org.keenusa.connect.models.CoachAttendance;
import org.keenusa.connect.models.KeenSession;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CoachesCheckInAdapter extends ArrayAdapter<CoachAttendance> {

		TextView tvCoachCheckIn;
		Button btnAddCoach;
		EditText etCoachSearch;
		Spinner spinner;
		String[] options = {"Check-In", "Absent", "Others"};

	public CoachesCheckInAdapter(Context context, ArrayList<CoachAttendance> coachList) {
		super(context, android.R.layout.simple_list_item_1, coachList);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CoachAttendance coach = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.coach_check_in_items, parent, false);
		}
		tvCoachCheckIn = (TextView) convertView.findViewById(R.id.tvCoachCheckIn);
		btnAddCoach = (Button) convertView.findViewById(R.id.btnAddCoach);
		etCoachSearch = (EditText) convertView.findViewById(R.id.etCoachSearch);
		ImageView ivCoachImageProfile = (ImageView) convertView.findViewById(R.id.ivCoachImageProfile);
		spinner = (Spinner) convertView.findViewById(R.id.spCheckInOptions);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, options);
		spinner.setAdapter(adapter);
		
 		tvCoachCheckIn.setText(coach.getAttendedCoachFullName().toString());
 		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				int pos = spinner.getSelectedItemPosition();
//				Toast.makeText(getContext(), options[+pos] + " is selected", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
 			
		});
		return convertView;
	}

}
