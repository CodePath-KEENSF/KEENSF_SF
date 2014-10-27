package org.keenusa.connect.adapters;

import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.models.Coach;
import org.keenusa.connect.models.CoachAttendance;
import org.keenusa.connect.models.CoachAttendance.AttendanceValue;
import org.keenusa.connect.models.ContactPerson;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.utilities.CheckinMenuActions;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CoachStickyHeaderCheckInAdapter extends ArrayAdapter<CoachAttendance> implements StickyListHeadersAdapter{

	private KeenCivicoreClient client;

	public static class ViewHolder {
		ImageView ivCoachProfilePic;
		TextView tvCoachName;
		LinearLayout llCoachAttendance;
		TextView tvCoachAttended;
		TextView tvCoachAbsent;
		TextView tvCoachCancelled;
	}
	
	public static class HeaderViewHolder {
		TextView tvAttendanceValue;
	}


	public CoachStickyHeaderCheckInAdapter(Context context, List<CoachAttendance> objects) {
		super(context, 0, objects);
		client = new KeenCivicoreClient(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		CoachAttendance coachAttendance = getItem(position);

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.coach_checkin_list_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.ivCoachProfilePic = (ImageView) convertView.findViewById(R.id.ivCoachProfilePic);
			viewHolder.tvCoachName = (TextView) convertView.findViewById(R.id.tvCoachName);
			viewHolder.tvCoachAttended = (TextView) convertView.findViewById(R.id.tvCoachAttended);
			viewHolder.tvCoachAbsent = (TextView) convertView.findViewById(R.id.tvCoachAbsent);
			viewHolder.tvCoachCancelled = (TextView) convertView.findViewById(R.id.tvCoachCancelled);
			viewHolder.llCoachAttendance = (LinearLayout) convertView.findViewById(R.id.llCoachAttendance);

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
					viewHolder.ivCoachProfilePic.setImageResource(R.drawable.ic_user_photos_f);
				} else if (coach.getGender() == ContactPerson.Gender.MALE) {
					viewHolder.ivCoachProfilePic.setImageResource(R.drawable.ic_user_photos_m);
				} else {
					viewHolder.ivCoachProfilePic.setImageResource(R.drawable.ic_user_photos_u);
				}

				viewHolder.tvCoachName.setText(coachAttendance.getCoach().getFirstLastName());
			}
			
			if(CheckinMenuActions.editMode == false){
				viewHolder.tvCoachAttended.setVisibility(View.GONE);
				viewHolder.tvCoachAbsent.setVisibility(View.GONE);
				viewHolder.tvCoachCancelled.setVisibility(View.GONE);
				viewHolder.llCoachAttendance.setVisibility(View.GONE);
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)viewHolder.tvCoachName.getLayoutParams();
				params.addRule(RelativeLayout.ALIGN_TOP, 0);
				viewHolder.tvCoachName.setGravity(Gravity.CENTER_VERTICAL);
				
				return convertView;
			}

			viewHolder.tvCoachAttended.setTag(R.color.tag1, coachAttendance);
			viewHolder.tvCoachAttended.setTag(R.color.tag2, viewHolder);

			viewHolder.tvCoachAbsent.setTag(R.color.tag1, coachAttendance);
			viewHolder.tvCoachAbsent.setTag(R.color.tag2, viewHolder);

			viewHolder.tvCoachCancelled.setTag(R.color.tag1, coachAttendance);
			viewHolder.tvCoachCancelled.setTag(R.color.tag2, viewHolder);

			viewHolder.tvCoachAttended.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));

			viewHolder.tvCoachAbsent.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));

			viewHolder.tvCoachCancelled.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));

			AttendanceValue attendanceValue = coachAttendance.getAttendanceValue();

			if (attendanceValue == CoachAttendance.AttendanceValue.ATTENDED) {
				viewHolder.tvCoachAttended.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_dark));
				viewHolder.tvCoachAttended.setTypeface(null, Typeface.BOLD);
			} else if (attendanceValue == CoachAttendance.AttendanceValue.NO_CALL_NO_SHOW) {
				viewHolder.tvCoachAbsent.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_dark));
				viewHolder.tvCoachAbsent.setTypeface(null, Typeface.BOLD);
			} else if (attendanceValue == CoachAttendance.AttendanceValue.CANCELLED
					|| attendanceValue == CoachAttendance.AttendanceValue.CALLED_IN_ABSENCE) {
				viewHolder.tvCoachCancelled.setTextColor(getContext().getResources().getColor(android.R.color.holo_orange_dark));
				viewHolder.tvCoachCancelled.setTypeface(null, Typeface.BOLD);
			}
		}

		setOnClickListeners(convertView, viewHolder);

		return convertView;
	}

	public void setOnClickListeners(View convertView, ViewHolder viewHolder) {

		viewHolder.tvCoachAttended.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CheckinMenuActions.editMode == false) {
					return;
				}
				TextView tvCoachAttended = (TextView) v;
				tvCoachAttended.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_dark));
				tvCoachAttended.setTypeface(null, Typeface.BOLD);

				ViewHolder viewHolder = (ViewHolder) v.getTag(R.color.tag2);

				viewHolder.tvCoachAbsent.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
				viewHolder.tvCoachCancelled.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));

				viewHolder.tvCoachAbsent.setTypeface(null, Typeface.NORMAL);
				viewHolder.tvCoachCancelled.setTypeface(null, Typeface.NORMAL);

				CoachAttendance coachAttendance = (CoachAttendance) v.getTag(R.color.tag1);
				coachAttendance.setAttendanceValue(CoachAttendance.AttendanceValue.ATTENDED);
			}
		});

		viewHolder.tvCoachAbsent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CheckinMenuActions.editMode == false) {
					return;
				}
				TextView tvCoachAbsent = (TextView) v;
				tvCoachAbsent.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_dark));
				tvCoachAbsent.setTypeface(null, Typeface.BOLD);

				ViewHolder viewHolder = (ViewHolder) v.getTag(R.color.tag2);

				viewHolder.tvCoachAttended.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
				viewHolder.tvCoachCancelled.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));

				viewHolder.tvCoachAttended.setTypeface(null, Typeface.NORMAL);
				viewHolder.tvCoachCancelled.setTypeface(null, Typeface.NORMAL);

				CoachAttendance coachAttendance = (CoachAttendance) v.getTag(R.color.tag1);
				coachAttendance.setAttendanceValue(CoachAttendance.AttendanceValue.NO_CALL_NO_SHOW);
			}
		});

		viewHolder.tvCoachCancelled.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CheckinMenuActions.editMode == false) {
					return;
				}

				TextView tvCoachCancelled = (TextView) v;
				tvCoachCancelled.setTextColor(getContext().getResources().getColor(android.R.color.holo_orange_dark));
				tvCoachCancelled.setTypeface(null, Typeface.BOLD);

				ViewHolder viewHolder = (ViewHolder) v.getTag(R.color.tag2);

				viewHolder.tvCoachAttended.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
				viewHolder.tvCoachAbsent.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));

				viewHolder.tvCoachAttended.setTypeface(null, Typeface.NORMAL);
				viewHolder.tvCoachAbsent.setTypeface(null, Typeface.NORMAL);

				CoachAttendance coachAttendance = (CoachAttendance) v.getTag(R.color.tag1);
				coachAttendance.setAttendanceValue(CoachAttendance.AttendanceValue.CANCELLED);
			}
		});
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		CoachAttendance coachAttendance = getItem(position);
		
		HeaderViewHolder headerViewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.attendance_header, parent, false);

			headerViewHolder = new HeaderViewHolder();
			headerViewHolder.tvAttendanceValue = (TextView) convertView.findViewById(R.id.tvAttendanceValue);

			convertView.setTag(headerViewHolder);
		} else {
			headerViewHolder = (HeaderViewHolder) convertView.getTag();
		}

		if(coachAttendance != null){
			if(coachAttendance.getAttendanceValue() == AttendanceValue.ATTENDED){
				headerViewHolder.tvAttendanceValue.setText("Attended");
			}else if(coachAttendance.getAttendanceValue() == AttendanceValue.REGISTERED){
				headerViewHolder.tvAttendanceValue.setText("Registered");
			}else if(coachAttendance.getAttendanceValue() == AttendanceValue.NO_CALL_NO_SHOW){
				headerViewHolder.tvAttendanceValue.setText("No Show");
			}else{
				headerViewHolder.tvAttendanceValue.setText("Called Absence");
			}
		}
		// TODO Auto-generated method stub
		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		CoachAttendance coachAttendance = getItem(position);
		
		if(coachAttendance != null){
			if(coachAttendance.getAttendanceValue() == AttendanceValue.ATTENDED){
				return 0;
			}else if(coachAttendance.getAttendanceValue() == AttendanceValue.REGISTERED){
				return 1;
			}else if(coachAttendance.getAttendanceValue() == AttendanceValue.NO_CALL_NO_SHOW){
				return 2;
			}else{
				return 3;
			}
		}
		return 1;
	}
}
