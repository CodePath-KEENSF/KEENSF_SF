package org.keenusa.connect.adapters;

import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.AthleteAttendance;
import org.keenusa.connect.models.AthleteAttendance.AttendanceValue;
import org.keenusa.connect.models.ContactPerson;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.utilities.CheckinEditMode;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AthleteCheckinAdapter extends ArrayAdapter<AthleteAttendance> {

	private KeenCivicoreClient client;

	public static class ViewHolder {
		ImageView ivAthleteProfilePic;
		TextView tvAthleteName;
		TextView tvAthleteAttended;
		TextView tvAthleteAbsent;
		TextView tvAthleteCancelled;
	}

	public AthleteCheckinAdapter(Context context, List<AthleteAttendance> objects) {
		super(context, 0, objects);
		client = new KeenCivicoreClient(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		AthleteAttendance athleteAttendance = getItem(position);

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.athlete_checkin_list_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.ivAthleteProfilePic = (ImageView) convertView
					.findViewById(R.id.ivAthleteProfilePic);
			viewHolder.tvAthleteName = (TextView) convertView
					.findViewById(R.id.tvAthleteName);
			viewHolder.tvAthleteAttended = (TextView) convertView
					.findViewById(R.id.tvAthleteAttended);
			viewHolder.tvAthleteAbsent = (TextView) convertView
					.findViewById(R.id.tvAthleteAbsent);
			viewHolder.tvAthleteCancelled = (TextView) convertView
					.findViewById(R.id.tvAthleteCancelled);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (athleteAttendance != null) {

			// Set the profile pic and name
			if (athleteAttendance.getAthlete() != null) {
				Athlete athlete = athleteAttendance.getAthlete();

				viewHolder.ivAthleteProfilePic.setImageResource(0);
				if (athlete.getGender() == ContactPerson.Gender.FEMALE) {
					viewHolder.ivAthleteProfilePic
							.setImageResource(R.drawable.ic_user_photos_f);
				} else if (athlete.getGender() == ContactPerson.Gender.MALE) {
					viewHolder.ivAthleteProfilePic
							.setImageResource(R.drawable.ic_user_photos_m);
				} else {
					viewHolder.ivAthleteProfilePic
							.setImageResource(R.drawable.ic_user_photos_u);
				}

				viewHolder.tvAthleteName.setText(athleteAttendance.getAthlete()
						.getFirstLastName());
			}

			viewHolder.tvAthleteAttended.setTag(R.color.tag1, athleteAttendance);
			viewHolder.tvAthleteAttended.setTag(R.color.tag2, viewHolder);
			
			viewHolder.tvAthleteAbsent.setTag(R.color.tag1, athleteAttendance);
			viewHolder.tvAthleteAbsent.setTag(R.color.tag2, viewHolder);

			viewHolder.tvAthleteCancelled.setTag(R.color.tag1, athleteAttendance);
			viewHolder.tvAthleteCancelled.setTag(R.color.tag2, viewHolder);

			viewHolder.tvAthleteAttended.setTextColor(getContext().getResources()
					.getColor(android.R.color.darker_gray));

			viewHolder.tvAthleteAbsent.setTextColor(getContext().getResources()
					.getColor(android.R.color.darker_gray));

			viewHolder.tvAthleteCancelled.setTextColor(getContext()
					.getResources().getColor(android.R.color.darker_gray));

			AttendanceValue attendanceValue = athleteAttendance
					.getAttendanceValue();

			if (attendanceValue == AthleteAttendance.AttendanceValue.ATTENDED) {
				viewHolder.tvAthleteAttended.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_green_dark));
				viewHolder.tvAthleteAttended.setTypeface(null, Typeface.BOLD);
			} else if (attendanceValue == AthleteAttendance.AttendanceValue.NO_CALL_NO_SHOW) {
				viewHolder.tvAthleteAbsent.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_red_dark));
				viewHolder.tvAthleteAbsent.setTypeface(null, Typeface.BOLD);
			} else if (attendanceValue == AthleteAttendance.AttendanceValue.CALLED_IN_ABSENCE) {
				viewHolder.tvAthleteCancelled.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_red_dark));
				viewHolder.tvAthleteCancelled.setTypeface(null, Typeface.BOLD);
			}
		}

		setOnClickListeners(convertView, viewHolder);

		return convertView;
	}

	public void setOnClickListeners(View convertView, ViewHolder viewHolder) {
		
		viewHolder.tvAthleteAttended.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CheckinEditMode.editMode == false){
					return;
				}
				TextView tvAthleteAttended = (TextView)v; 
				tvAthleteAttended.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_green_dark));
				tvAthleteAttended.setTypeface(null, Typeface.BOLD);
				
				ViewHolder viewHolder = (ViewHolder)v.getTag(R.color.tag2);

				viewHolder.tvAthleteAbsent.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvAthleteCancelled.setTextColor(getContext()
						.getResources().getColor(android.R.color.darker_gray));
				
				viewHolder.tvAthleteAbsent.setTypeface(null, Typeface.NORMAL);
				viewHolder.tvAthleteCancelled.setTypeface(null, Typeface.NORMAL);
				
				AthleteAttendance athleteAttendance = (AthleteAttendance)v.getTag(R.color.tag1);
				athleteAttendance.setAttendanceValue(AthleteAttendance.AttendanceValue.ATTENDED);
			}
		});

		viewHolder.tvAthleteAbsent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CheckinEditMode.editMode == false){
					return;
				}
				TextView tvAthleteAbsent = (TextView)v; 
				tvAthleteAbsent.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_red_dark));
				tvAthleteAbsent.setTypeface(null, Typeface.BOLD);
				
				ViewHolder viewHolder = (ViewHolder)v.getTag(R.color.tag2);

				viewHolder.tvAthleteAttended.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvAthleteCancelled.setTextColor(getContext()
						.getResources().getColor(android.R.color.darker_gray));
				
				viewHolder.tvAthleteAttended.setTypeface(null, Typeface.NORMAL);
				viewHolder.tvAthleteCancelled.setTypeface(null, Typeface.NORMAL);

				AthleteAttendance athleteAttendance = (AthleteAttendance)v.getTag(R.color.tag1);
				athleteAttendance.setAttendanceValue(AthleteAttendance.AttendanceValue.NO_CALL_NO_SHOW);
			}
		});
		
		viewHolder.tvAthleteCancelled.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CheckinEditMode.editMode == false){
					return;
				}

				TextView tvAthleteCancelled = (TextView)v; 
				tvAthleteCancelled.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_red_dark));
				tvAthleteCancelled.setTypeface(null, Typeface.BOLD);
				
				ViewHolder viewHolder = (ViewHolder)v.getTag(R.color.tag2);

				viewHolder.tvAthleteAttended.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvAthleteAbsent.setTextColor(getContext()
						.getResources().getColor(android.R.color.darker_gray));
				
				viewHolder.tvAthleteAttended.setTypeface(null, Typeface.NORMAL);
				viewHolder.tvAthleteAbsent.setTypeface(null, Typeface.NORMAL);

				AthleteAttendance athleteAttendance = (AthleteAttendance)v.getTag(R.color.tag1);
				athleteAttendance.setAttendanceValue(AthleteAttendance.AttendanceValue.CALLED_IN_ABSENCE);
			}
		});
	}
}
