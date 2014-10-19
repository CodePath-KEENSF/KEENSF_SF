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
//		Button btnAddCoach;
//		EditText etCoachSearch;
		Spinner spinner;
		String[] options = {"REGISTERED", "ATTENDED", "CALLED_IN_ABSENCE", "CANCELLED", "NO_CALL_NO_SHOW"};
		
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
//		btnAddCoach = (Button) convertView.findViewById(R.id.btnAddCoach);
//		etCoachSearch = (EditText) convertView.findViewById(R.id.etCoachSearch);
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
 		
 		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				String item = parent.getItemAtPosition(position).toString();
//				Toast.makeText(getContext(), "Item selected is " + item, Toast.LENGTH_SHORT).show();
				if (item.equals("REGISTERED")){
					coach.setAttendanceValue(CoachAttendance.AttendanceValue.REGISTERED);
//					Toast.makeText(getContext(), item + " is Registered", Toast.LENGTH_SHORT).show();
				} if (item.equals("ATTENDED")){
					coach.setAttendanceValue(CoachAttendance.AttendanceValue.ATTENDED);
//					Toast.makeText(getContext(), item + " is Attended", Toast.LENGTH_SHORT).show();
				} if (item.equals("CALLED_IN_ABSENCE")){
					coach.setAttendanceValue(CoachAttendance.AttendanceValue.CALLED_IN_ABSENCE);
//					Toast.makeText(getContext(), item + " is Called in absence", Toast.LENGTH_SHORT).show();
				} if (item.equals("CANCELLED")){
					coach.setAttendanceValue(CoachAttendance.AttendanceValue.CANCELLED);
//					Toast.makeText(getContext(), item + " is Registered", Toast.LENGTH_SHORT).show();
				} if (item.equals("NO_CALL_NO_SHOW")){
					coach.setAttendanceValue(CoachAttendance.AttendanceValue.NO_CALL_NO_SHOW);
//					Toast.makeText(getContext(), item + " is No call no show", Toast.LENGTH_SHORT).show();
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
