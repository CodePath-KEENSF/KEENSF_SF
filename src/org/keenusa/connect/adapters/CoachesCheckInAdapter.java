package org.keenusa.connect.adapters;

import java.util.ArrayList;

import org.keenusa.connect.R;
import org.keenusa.connect.helpers.CivicoreCoachAttendanceStringParser;
import org.keenusa.connect.models.AthleteAttendance;
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
		String[] options = {"Options", "REGISTERED", "ATTENDED", "CALLED_IN_ABSENCE", "CANCELLED", "NO_CALL_NO_SHOW"};
		
		CoachAttendance coachAttendance;

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
		final CoachAttendance.AttendanceValue ca = null;
 		tvCoachCheckIn.setText(coach.getAttendedCoachFullName().toString());
 		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				String item = parent.getItemAtPosition(position).toString();
//				Toast.makeText(getContext(), "Item selected is " + item, Toast.LENGTH_SHORT).show();
				if (item.equals("REGISTERED")){
					coachAttendance.setAttendanceValue(CoachAttendance.AttendanceValue.REGISTERED);
				} if (item.equals("ATTENDED")){
					coachAttendance.setAttendanceValue(CoachAttendance.AttendanceValue.ATTENDED);
				} if (item.equals("CALLED_IN_ABSENCE")){
					coachAttendance.setAttendanceValue(CoachAttendance.AttendanceValue.CALLED_IN_ABSENCE);
				} if (item.equals("CANCELLED")){
					coachAttendance.setAttendanceValue(CoachAttendance.AttendanceValue.CANCELLED);
				} if (item.equals("NO_CALL_NO_SHOW")){
					coachAttendance.setAttendanceValue(CoachAttendance.AttendanceValue.NO_CALL_NO_SHOW);
				} if (item.equals("Options")){
					coachAttendance.setAttendanceValue(CoachAttendance.AttendanceValue.NO_CALL_NO_SHOW);
				}		
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
 			
		});
		return convertView;
	}

}
