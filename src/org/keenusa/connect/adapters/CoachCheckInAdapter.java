package org.keenusa.connect.adapters;

import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.models.Coach;
import org.keenusa.connect.models.CoachAttendance;
import org.keenusa.connect.models.CoachAttendance.AttendanceValue;
import org.keenusa.connect.models.ContactPerson;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.utilities.CheckinEditMode;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CoachCheckInAdapter extends ArrayAdapter<CoachAttendance> {

	private KeenCivicoreClient client;

	public static class ViewHolder {
		ImageView ivCoachProfilePic;
		TextView tvCoachName;
		TextView tvCoachRegistered;
		TextView tvCoachAttended;
		TextView tvCoachAbsent;
		TextView tvCoachCancelled;
		TextView tvCoachNCNS;
	}

	public CoachCheckInAdapter(Context context, List<CoachAttendance> objects) {
		super(context, 0, objects);
		client = new KeenCivicoreClient(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		CoachAttendance coachAttendance = getItem(position);

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.coach_checkin_list_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.ivCoachProfilePic = (ImageView) convertView
					.findViewById(R.id.ivCoachProfilePic);
			viewHolder.tvCoachName = (TextView) convertView
					.findViewById(R.id.tvCoachName);
			viewHolder.tvCoachRegistered = (TextView) convertView
					.findViewById(R.id.tvCoachRegistered);
			viewHolder.tvCoachAttended = (TextView) convertView
					.findViewById(R.id.tvCoachAttended);
			viewHolder.tvCoachAbsent = (TextView) convertView
					.findViewById(R.id.tvCoachAbsent);
			viewHolder.tvCoachCancelled = (TextView) convertView
					.findViewById(R.id.tvCoachCancelled);
			viewHolder.tvCoachNCNS = (TextView) convertView
					.findViewById(R.id.tvCoachNCNS);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (coachAttendance != null) {

			// Set the profile pic and name
			if (coachAttendance.getCoach() != null) {
				Coach coach = coachAttendance.getCoach();

				viewHolder.ivCoachProfilePic.setImageResource(0);
				if (coach.getGender() == ContactPerson.Gender.FEMALE) {
					viewHolder.ivCoachProfilePic
							.setImageResource(R.drawable.ic_user_photos_f);
				} else if (coach.getGender() == ContactPerson.Gender.MALE) {
					viewHolder.ivCoachProfilePic
							.setImageResource(R.drawable.ic_user_photos_m);
				} else {
					viewHolder.ivCoachProfilePic
							.setImageResource(R.drawable.ic_user_photos_u);
				}

				viewHolder.tvCoachName.setText(coachAttendance.getCoach()
						.getFirstLastName());
			}

			viewHolder.tvCoachRegistered.setTag(R.color.tag1, coachAttendance);
			viewHolder.tvCoachRegistered.setTag(R.color.tag2, viewHolder);
			
			viewHolder.tvCoachAttended.setTag(R.color.tag1, coachAttendance);
			viewHolder.tvCoachAttended.setTag(R.color.tag2, viewHolder);
			
			viewHolder.tvCoachAbsent.setTag(R.color.tag1, coachAttendance);
			viewHolder.tvCoachAbsent.setTag(R.color.tag2, viewHolder);

			viewHolder.tvCoachCancelled.setTag(R.color.tag1, coachAttendance);
			viewHolder.tvCoachCancelled.setTag(R.color.tag2, viewHolder);

			viewHolder.tvCoachNCNS.setTag(R.color.tag1, coachAttendance);
			viewHolder.tvCoachNCNS.setTag(R.color.tag2, viewHolder);

			viewHolder.tvCoachRegistered.setTextColor(getContext()
					.getResources().getColor(android.R.color.darker_gray));

			viewHolder.tvCoachAttended.setTextColor(getContext().getResources()
					.getColor(android.R.color.darker_gray));

			viewHolder.tvCoachAbsent.setTextColor(getContext().getResources()
					.getColor(android.R.color.darker_gray));

			viewHolder.tvCoachCancelled.setTextColor(getContext()
					.getResources().getColor(android.R.color.darker_gray));

			viewHolder.tvCoachNCNS.setTextColor(getContext().getResources()
					.getColor(android.R.color.darker_gray));

			AttendanceValue attendanceValue = coachAttendance
					.getAttendanceValue();

			if (attendanceValue == CoachAttendance.AttendanceValue.REGISTERED) {
				viewHolder.tvCoachRegistered.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_blue_dark));
			} else if (attendanceValue == CoachAttendance.AttendanceValue.ATTENDED) {
				viewHolder.tvCoachAttended.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_green_dark));
			} else if (attendanceValue == CoachAttendance.AttendanceValue.CALLED_IN_ABSENCE) {
				viewHolder.tvCoachAbsent.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_red_dark));
			} else if (attendanceValue == CoachAttendance.AttendanceValue.CANCELLED) {
				viewHolder.tvCoachCancelled.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_red_dark));
			} else if (attendanceValue == CoachAttendance.AttendanceValue.NO_CALL_NO_SHOW) {
				viewHolder.tvCoachNCNS.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_red_dark));
			}
		}

		setOnClickListeners(convertView, viewHolder);
		// TODO Auto-generated method stub
		return convertView;
	}

	public void setOnClickListeners(View convertView, ViewHolder viewHolder) {
		
		viewHolder.tvCoachRegistered.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CheckinEditMode.editMode == false){
					return;
				}

				TextView tvCoachRegistered = (TextView)v; 
				tvCoachRegistered.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_blue_dark));
				
				ViewHolder viewHolder = (ViewHolder)v.getTag(R.color.tag2);

				viewHolder.tvCoachAttended.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvCoachAbsent.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvCoachCancelled.setTextColor(getContext()
						.getResources().getColor(android.R.color.darker_gray));
				viewHolder.tvCoachNCNS.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				
				CoachAttendance coachAttendance = (CoachAttendance)v.getTag(R.color.tag1);
				coachAttendance.setAttendanceValue(CoachAttendance.AttendanceValue.REGISTERED);
			}
		});		
		viewHolder.tvCoachAttended.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CheckinEditMode.editMode == false){
					return;
				}
				TextView tvCoachAttended = (TextView)v; 
				tvCoachAttended.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_green_dark));
				
				ViewHolder viewHolder = (ViewHolder)v.getTag(R.color.tag2);

				viewHolder.tvCoachRegistered.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvCoachAbsent.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvCoachCancelled.setTextColor(getContext()
						.getResources().getColor(android.R.color.darker_gray));
				viewHolder.tvCoachNCNS.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				
				CoachAttendance coachAttendance = (CoachAttendance)v.getTag(R.color.tag1);
				coachAttendance.setAttendanceValue(CoachAttendance.AttendanceValue.ATTENDED);
			}
		});

		viewHolder.tvCoachAbsent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CheckinEditMode.editMode == false){
					return;
				}
				TextView tvCoachAbsent = (TextView)v; 
				tvCoachAbsent.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_red_dark));
				
				ViewHolder viewHolder = (ViewHolder)v.getTag(R.color.tag2);

				viewHolder.tvCoachRegistered.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvCoachAttended.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvCoachCancelled.setTextColor(getContext()
						.getResources().getColor(android.R.color.darker_gray));
				viewHolder.tvCoachNCNS.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				
				CoachAttendance coachAttendance = (CoachAttendance)v.getTag(R.color.tag1);
				coachAttendance.setAttendanceValue(CoachAttendance.AttendanceValue.CALLED_IN_ABSENCE);
			}
		});
		
		viewHolder.tvCoachCancelled.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CheckinEditMode.editMode == false){
					return;
				}

				TextView tvCoachCancelled = (TextView)v; 
				tvCoachCancelled.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_red_dark));
				
				ViewHolder viewHolder = (ViewHolder)v.getTag(R.color.tag2);

				viewHolder.tvCoachRegistered.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvCoachAttended.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvCoachAbsent.setTextColor(getContext()
						.getResources().getColor(android.R.color.darker_gray));
				viewHolder.tvCoachNCNS.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				
				CoachAttendance coachAttendance = (CoachAttendance)v.getTag(R.color.tag1);
				coachAttendance.setAttendanceValue(CoachAttendance.AttendanceValue.CANCELLED);
			}
		});
		
		viewHolder.tvCoachNCNS.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CheckinEditMode.editMode == false){
					return;
				}

				TextView tvCoachNCNS = (TextView)v; 
				tvCoachNCNS.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_red_dark));
				
				ViewHolder viewHolder = (ViewHolder)v.getTag(R.color.tag2);

				viewHolder.tvCoachRegistered.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvCoachAttended.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvCoachAbsent.setTextColor(getContext()
						.getResources().getColor(android.R.color.darker_gray));
				viewHolder.tvCoachCancelled.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				
				CoachAttendance coachAttendance = (CoachAttendance)v.getTag(R.color.tag1);
				coachAttendance.setAttendanceValue(CoachAttendance.AttendanceValue.NO_CALL_NO_SHOW);
			}
		});
		
	}
}
