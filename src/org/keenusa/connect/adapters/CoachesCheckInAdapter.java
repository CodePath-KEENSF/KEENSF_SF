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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CoachesCheckInAdapter extends ArrayAdapter<CoachAttendance> {

		TextView tvCoachCheckIn;
		Button btnChkIn;
		Button btnAbsent;
		Button btnOthers;
		Button btnAddCoach;
		EditText etCoachSearch;

	public CoachesCheckInAdapter(Context context, ArrayList<CoachAttendance> coachList) {
		super(context, android.R.layout.simple_list_item_1, coachList);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CoachAttendance coach = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.coach_check_in_items, parent, false);
		}
		tvCoachCheckIn = (TextView) convertView.findViewById(R.id.tvAthleteCheckIn);
		btnChkIn = (Button) convertView.findViewById(R.id.btnChkIn);
		btnAbsent = (Button) convertView.findViewById(R.id.btnAbsent);
		btnOthers = (Button) convertView.findViewById(R.id.btnOthers);
		btnAddCoach = (Button) convertView.findViewById(R.id.btnAddCoach);
		etCoachSearch = (EditText) convertView.findViewById(R.id.etCoachSearch);
		ImageView ivCoachImageProfile = (ImageView) convertView.findViewById(R.id.ivCoachImageProfile);
		
		
		tvCoachCheckIn.setText(coach.getAttendedCoachFullName().toString());
//		ivCoachImageProfile.setImageResource(position);
		btnChkIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "Check-in " + coach.getAttendedCoachFullName().toString(), Toast.LENGTH_SHORT).show();
			}
		});;
		btnAbsent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "Absent " + coach.getAttendedCoachFullName().toString(), Toast.LENGTH_SHORT).show();
			}
		});;
		btnOthers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "Others " + coach.getComments().toString(), Toast.LENGTH_SHORT).show();
			}
		});;
		return convertView;
	}

}
