package org.keenusa.connect.adapters;

import java.util.ArrayList;

import org.keenusa.connect.R;
import org.keenusa.connect.models.CoachAttendance;
import org.keenusa.connect.models.KeenSession;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class CheckInAdapter extends ArrayAdapter<CoachAttendance> {

		TextView tvCoachCheckIn;
		Button btnChkIn;
		Button btnAbsent;
		Button btnOthers;

	public CheckInAdapter(Context context, ArrayList<CoachAttendance> coachList) {
		super(context, 0, coachList);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CoachAttendance coach = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.check_in_items, parent, false);
			
			tvCoachCheckIn = (TextView) convertView.findViewById(R.id.tvCoachCheckIn);
			btnChkIn = (Button) convertView.findViewById(R.id.btnChkIn);
			btnAbsent = (Button) convertView.findViewById(R.id.btnAbsent);
			btnOthers = (Button) convertView.findViewById(R.id.btnOthers);
		}
		
		tvCoachCheckIn.setText(coach.getAttendedCoachFullName().toString());
		
		return convertView;
	}

}
