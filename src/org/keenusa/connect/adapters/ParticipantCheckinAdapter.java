package org.keenusa.connect.adapters;

import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.models.Participant;
import org.keenusa.connect.models.ParticipantAttendance;
import org.keenusa.connect.models.ParticipantAttendance.AttendanceValue;
import org.keenusa.connect.models.ContactPerson;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.utilities.CheckinMenuActions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ParticipantCheckinAdapter extends ArrayAdapter<ParticipantAttendance> {

	private KeenCivicoreClient client;

	public static class ViewHolder {
		ImageView ivParticipantProfilePic;
		TextView tvParticipantName;
		TextView tvParticipantRegistered;
		TextView tvParticipantAttended;
		TextView tvParticipantAbsent;
		TextView tvParticipantCancelled;
		TextView tvParticipantNCNS;
	}

	public ParticipantCheckinAdapter(Context context, List<ParticipantAttendance> objects) {
		super(context, 0, objects);
		client = new KeenCivicoreClient(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ParticipantAttendance participantAttendance = getItem(position);

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.participant_checkin_list_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.ivParticipantProfilePic = (ImageView) convertView
					.findViewById(R.id.ivParticipantProfilePic);
			viewHolder.tvParticipantName = (TextView) convertView
					.findViewById(R.id.tvParticipantName);
			viewHolder.tvParticipantRegistered = (TextView) convertView
					.findViewById(R.id.tvParticipantRegistered);
			viewHolder.tvParticipantAttended = (TextView) convertView
					.findViewById(R.id.tvParticipantAttended);
			viewHolder.tvParticipantAbsent = (TextView) convertView
					.findViewById(R.id.tvParticipantAbsent);
			viewHolder.tvParticipantCancelled = (TextView) convertView
					.findViewById(R.id.tvParticipantCancelled);
			viewHolder.tvParticipantNCNS = (TextView) convertView
					.findViewById(R.id.tvParticipantNCNS);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (participantAttendance != null) {

			// Set the profile pic and name
			if (participantAttendance.getParticipant() != null) {
				Participant participant = participantAttendance.getParticipant();

				viewHolder.ivParticipantProfilePic.setImageResource(0);
				if (participant.getGender() == ContactPerson.Gender.FEMALE) {
					viewHolder.ivParticipantProfilePic
							.setImageResource(R.drawable.ic_user_photos_f);
				} else if (participant.getGender() == ContactPerson.Gender.MALE) {
					viewHolder.ivParticipantProfilePic
							.setImageResource(R.drawable.ic_user_photos_m);
				} else {
					viewHolder.ivParticipantProfilePic
							.setImageResource(R.drawable.ic_user_photos_u);
				}

				viewHolder.tvParticipantName.setText(participantAttendance.getParticipant()
						.getFirstLastName());
			}

			viewHolder.tvParticipantRegistered.setTag(R.color.tag1, participantAttendance);
			viewHolder.tvParticipantRegistered.setTag(R.color.tag2, viewHolder);
			
			viewHolder.tvParticipantAttended.setTag(R.color.tag1, participantAttendance);
			viewHolder.tvParticipantAttended.setTag(R.color.tag2, viewHolder);
			
			viewHolder.tvParticipantAbsent.setTag(R.color.tag1, participantAttendance);
			viewHolder.tvParticipantAbsent.setTag(R.color.tag2, viewHolder);

			viewHolder.tvParticipantCancelled.setTag(R.color.tag1, participantAttendance);
			viewHolder.tvParticipantCancelled.setTag(R.color.tag2, viewHolder);

			viewHolder.tvParticipantNCNS.setTag(R.color.tag1, participantAttendance);
			viewHolder.tvParticipantNCNS.setTag(R.color.tag2, viewHolder);

			viewHolder.tvParticipantRegistered.setTextColor(getContext()
					.getResources().getColor(android.R.color.darker_gray));

			viewHolder.tvParticipantAttended.setTextColor(getContext().getResources()
					.getColor(android.R.color.darker_gray));

			viewHolder.tvParticipantAbsent.setTextColor(getContext().getResources()
					.getColor(android.R.color.darker_gray));

			viewHolder.tvParticipantCancelled.setTextColor(getContext()
					.getResources().getColor(android.R.color.darker_gray));

			viewHolder.tvParticipantNCNS.setTextColor(getContext().getResources()
					.getColor(android.R.color.darker_gray));

			AttendanceValue attendanceValue = participantAttendance
					.getAttendanceValue();

			if (attendanceValue == ParticipantAttendance.AttendanceValue.REGISTERED) {
				viewHolder.tvParticipantRegistered.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_blue_dark));
			} else if (attendanceValue == ParticipantAttendance.AttendanceValue.ATTENDED) {
				viewHolder.tvParticipantAttended.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_green_dark));
			} else if (attendanceValue == ParticipantAttendance.AttendanceValue.CALLED_IN_ABSENCE) {
				viewHolder.tvParticipantAbsent.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_red_dark));
			} else if (attendanceValue == ParticipantAttendance.AttendanceValue.CANCELLED) {
				viewHolder.tvParticipantCancelled.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_red_dark));
			} else if (attendanceValue == ParticipantAttendance.AttendanceValue.NO_CALL_NO_SHOW) {
				viewHolder.tvParticipantNCNS.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_red_dark));
			}
		}

		setOnClickListeners(convertView, viewHolder);
		return convertView;
	}

	public void setOnClickListeners(View convertView, ViewHolder viewHolder) {
		
		viewHolder.tvParticipantRegistered.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CheckinMenuActions.editMode == false){
					return;
				}

				TextView tvParticipantRegistered = (TextView)v; 
				tvParticipantRegistered.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_blue_dark));
				
				ViewHolder viewHolder = (ViewHolder)v.getTag(R.color.tag2);

				viewHolder.tvParticipantAttended.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvParticipantAbsent.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvParticipantCancelled.setTextColor(getContext()
						.getResources().getColor(android.R.color.darker_gray));
				viewHolder.tvParticipantNCNS.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				
				ParticipantAttendance participantAttendance = (ParticipantAttendance)v.getTag(R.color.tag1);
				participantAttendance.setAttendanceValue(ParticipantAttendance.AttendanceValue.REGISTERED);
			}
		});		
		viewHolder.tvParticipantAttended.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CheckinMenuActions.editMode == false){
					return;
				}
				TextView tvParticipantAttended = (TextView)v; 
				tvParticipantAttended.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_green_dark));
				
				ViewHolder viewHolder = (ViewHolder)v.getTag(R.color.tag2);

				viewHolder.tvParticipantRegistered.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvParticipantAbsent.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvParticipantCancelled.setTextColor(getContext()
						.getResources().getColor(android.R.color.darker_gray));
				viewHolder.tvParticipantNCNS.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				
				ParticipantAttendance participantAttendance = (ParticipantAttendance)v.getTag(R.color.tag1);
				participantAttendance.setAttendanceValue(ParticipantAttendance.AttendanceValue.ATTENDED);
			}
		});

		viewHolder.tvParticipantAbsent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CheckinMenuActions.editMode == false){
					return;
				}
				TextView tvParticipantAbsent = (TextView)v; 
				tvParticipantAbsent.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_red_dark));
				
				ViewHolder viewHolder = (ViewHolder)v.getTag(R.color.tag2);

				viewHolder.tvParticipantRegistered.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvParticipantAttended.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvParticipantCancelled.setTextColor(getContext()
						.getResources().getColor(android.R.color.darker_gray));
				viewHolder.tvParticipantNCNS.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				
				ParticipantAttendance participantAttendance = (ParticipantAttendance)v.getTag(R.color.tag1);
				participantAttendance.setAttendanceValue(ParticipantAttendance.AttendanceValue.CALLED_IN_ABSENCE);
			}
		});
		
		viewHolder.tvParticipantCancelled.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CheckinMenuActions.editMode == false){
					return;
				}

				TextView tvParticipantCancelled = (TextView)v; 
				tvParticipantCancelled.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_red_dark));
				
				ViewHolder viewHolder = (ViewHolder)v.getTag(R.color.tag2);

				viewHolder.tvParticipantRegistered.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvParticipantAttended.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvParticipantAbsent.setTextColor(getContext()
						.getResources().getColor(android.R.color.darker_gray));
				viewHolder.tvParticipantNCNS.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				
				ParticipantAttendance participantAttendance = (ParticipantAttendance)v.getTag(R.color.tag1);
				participantAttendance.setAttendanceValue(ParticipantAttendance.AttendanceValue.CANCELLED);
			}
		});
		
		viewHolder.tvParticipantNCNS.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CheckinMenuActions.editMode == false){
					return;
				}

				TextView tvParticipantNCNS = (TextView)v; 
				tvParticipantNCNS.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.holo_red_dark));
				
				ViewHolder viewHolder = (ViewHolder)v.getTag(R.color.tag2);

				viewHolder.tvParticipantRegistered.setTextColor(getContext()
						.getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvParticipantAttended.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				viewHolder.tvParticipantAbsent.setTextColor(getContext()
						.getResources().getColor(android.R.color.darker_gray));
				viewHolder.tvParticipantCancelled.setTextColor(getContext().getResources()
						.getColor(android.R.color.darker_gray));
				
				ParticipantAttendance participantAttendance = (ParticipantAttendance)v.getTag(R.color.tag1);
				participantAttendance.setAttendanceValue(ParticipantAttendance.AttendanceValue.NO_CALL_NO_SHOW);
			}
		});
		
	}
}