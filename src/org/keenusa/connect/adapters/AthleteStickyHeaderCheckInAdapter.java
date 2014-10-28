package org.keenusa.connect.adapters;

import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.activities.AthleteCoachCheckinActivity;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.AthleteAttendance;
import org.keenusa.connect.models.AthleteAttendance.AttendanceValue;
import org.keenusa.connect.models.ContactPerson;
import org.keenusa.connect.networking.KeenCivicoreClient;
import org.keenusa.connect.utilities.CheckinMenuActions;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AthleteStickyHeaderCheckInAdapter extends
		ArrayAdapter<AthleteAttendance> implements StickyListHeadersAdapter {

	private KeenCivicoreClient client;
	private List<AthleteAttendance> athleteAttList;
	private Context context;

	public static class ViewHolder {
		ImageView ivAthleteProfilePic;
		TextView tvAthleteName;
		LinearLayout llAthleteAttendance;
		TextView tvAthleteAttended;
		TextView tvAthleteAbsent;
		TextView tvAthleteCancelled;
	}

	public static class HeaderViewHolder {
		TextView tvAttendanceValue;
	}

	public AthleteStickyHeaderCheckInAdapter(Context context,
			List<AthleteAttendance> athleteAttList) {
		super(context, 0, athleteAttList);
		client = new KeenCivicoreClient(context);
		this.athleteAttList = athleteAttList;
		this.context = context;
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
			viewHolder.llAthleteAttendance = (LinearLayout) convertView
					.findViewById(R.id.llAthleteAttendance);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (position == 0 && getCount() == 1) {
			convertView
					.setBackgroundResource(R.drawable.single_item_list_background);
		} else if (position == 0 && getCount() > 1) {
			convertView
					.setBackgroundResource(R.drawable.list_item_background_first_item);
		} else if (position == getCount() - 1) {
			convertView
					.setBackgroundResource(R.drawable.list_item_background_last_item);
		} else {
			convertView.setBackgroundResource(R.drawable.list_item_background);
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

			if (CheckinMenuActions.editMode == false) {
				viewHolder.tvAthleteAttended.setVisibility(View.GONE);
				viewHolder.tvAthleteAbsent.setVisibility(View.GONE);
				viewHolder.tvAthleteCancelled.setVisibility(View.GONE);
				viewHolder.llAthleteAttendance.setVisibility(View.GONE);
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.tvAthleteName
						.getLayoutParams();
				params.addRule(RelativeLayout.ALIGN_TOP, 0);
				viewHolder.tvAthleteName.setGravity(Gravity.CENTER_VERTICAL);

				return convertView;
			}

			viewHolder.tvAthleteAttended
					.setTag(R.color.tag1, athleteAttendance);
			viewHolder.tvAthleteAttended.setTag(R.color.tag2, viewHolder);

			viewHolder.tvAthleteAbsent.setTag(R.color.tag1, athleteAttendance);
			viewHolder.tvAthleteAbsent.setTag(R.color.tag2, viewHolder);

			viewHolder.tvAthleteCancelled.setTag(R.color.tag1,
					athleteAttendance);
			viewHolder.tvAthleteCancelled.setTag(R.color.tag2, viewHolder);

			viewHolder.tvAthleteAttended.setTextColor(getContext()
					.getResources().getColor(android.R.color.darker_gray));

			viewHolder.tvAthleteAbsent.setTextColor(getContext().getResources()
					.getColor(android.R.color.darker_gray));

			viewHolder.tvAthleteCancelled.setTextColor(getContext()
					.getResources().getColor(android.R.color.darker_gray));

			AttendanceValue attendanceValue = athleteAttendance
					.getAttendanceValue();

			if (attendanceValue == AthleteAttendance.AttendanceValue.ATTENDED) {
				viewHolder.tvAthleteAttended.setTextColor(getContext()
						.getResources().getColor(
								android.R.color.holo_green_dark));
				viewHolder.tvAthleteAttended.setTypeface(null, Typeface.BOLD);
			} else if (attendanceValue == AthleteAttendance.AttendanceValue.NO_CALL_NO_SHOW) {
				viewHolder.tvAthleteAbsent
						.setTextColor(getContext().getResources().getColor(
								android.R.color.holo_red_dark));
				viewHolder.tvAthleteAbsent.setTypeface(null, Typeface.BOLD);
			} else if (attendanceValue == AthleteAttendance.AttendanceValue.CALLED_IN_ABSENCE) {
				viewHolder.tvAthleteCancelled.setTextColor(getContext()
						.getResources().getColor(
								android.R.color.holo_orange_dark));
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
				if (CheckinMenuActions.editMode == false) {
					return;
				}
				TextView tvAthleteAttended = (TextView) v;
				tvAthleteAttended.setTextColor(getContext().getResources()
						.getColor(android.R.color.holo_green_dark));
				tvAthleteAttended.setTypeface(null, Typeface.BOLD);

				ViewHolder viewHolder = (ViewHolder) v.getTag(R.color.tag2);

				viewHolder.tvAthleteAbsent.setTextColor(getContext()
						.getResources().getColor(android.R.color.darker_gray));
				viewHolder.tvAthleteCancelled.setTextColor(getContext()
						.getResources().getColor(android.R.color.darker_gray));

				viewHolder.tvAthleteAbsent.setTypeface(null, Typeface.NORMAL);
				viewHolder.tvAthleteCancelled
						.setTypeface(null, Typeface.NORMAL);

				AthleteAttendance athleteAttendance = (AthleteAttendance) v
						.getTag(R.color.tag1);
				athleteAttendance
						.setAttendanceValue(AthleteAttendance.AttendanceValue.ATTENDED);

				((AthleteCoachCheckinActivity) context)
						.refreshAthleteAttendance();
			}
		});

		viewHolder.tvAthleteAbsent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CheckinMenuActions.editMode == false) {
					return;
				}
				TextView tvAthleteAbsent = (TextView) v;
				tvAthleteAbsent.setTextColor(getContext().getResources()
						.getColor(android.R.color.holo_red_dark));
				tvAthleteAbsent.setTypeface(null, Typeface.BOLD);

				ViewHolder viewHolder = (ViewHolder) v.getTag(R.color.tag2);

				viewHolder.tvAthleteAttended.setTextColor(getContext()
						.getResources().getColor(android.R.color.darker_gray));
				viewHolder.tvAthleteCancelled.setTextColor(getContext()
						.getResources().getColor(android.R.color.darker_gray));

				viewHolder.tvAthleteAttended.setTypeface(null, Typeface.NORMAL);
				viewHolder.tvAthleteCancelled
						.setTypeface(null, Typeface.NORMAL);

				AthleteAttendance athleteAttendance = (AthleteAttendance) v
						.getTag(R.color.tag1);
				athleteAttendance
						.setAttendanceValue(AthleteAttendance.AttendanceValue.NO_CALL_NO_SHOW);

				((AthleteCoachCheckinActivity) context)
						.refreshAthleteAttendance();
			}
		});

		viewHolder.tvAthleteCancelled.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CheckinMenuActions.editMode == false) {
					return;
				}

				TextView tvAthleteCancelled = (TextView) v;
				tvAthleteCancelled.setTextColor(getContext().getResources()
						.getColor(android.R.color.holo_orange_dark));
				tvAthleteCancelled.setTypeface(null, Typeface.BOLD);

				ViewHolder viewHolder = (ViewHolder) v.getTag(R.color.tag2);

				viewHolder.tvAthleteAttended.setTextColor(getContext()
						.getResources().getColor(android.R.color.darker_gray));
				viewHolder.tvAthleteAbsent.setTextColor(getContext()
						.getResources().getColor(android.R.color.darker_gray));

				viewHolder.tvAthleteAttended.setTypeface(null, Typeface.NORMAL);
				viewHolder.tvAthleteAbsent.setTypeface(null, Typeface.NORMAL);

				AthleteAttendance athleteAttendance = (AthleteAttendance) v
						.getTag(R.color.tag1);
				athleteAttendance
						.setAttendanceValue(AthleteAttendance.AttendanceValue.CALLED_IN_ABSENCE);

				((AthleteCoachCheckinActivity) context)
						.refreshAthleteAttendance();
			}
		});
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		AthleteAttendance athleteAttendance = getItem(position);

		HeaderViewHolder headerViewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.attendance_header, parent, false);

			headerViewHolder = new HeaderViewHolder();
			headerViewHolder.tvAttendanceValue = (TextView) convertView
					.findViewById(R.id.tvAttendanceValue);

			convertView.setTag(headerViewHolder);
		} else {
			headerViewHolder = (HeaderViewHolder) convertView.getTag();
		}

		if (athleteAttendance != null) {
			if (athleteAttendance.getAttendanceValue() == AttendanceValue.ATTENDED) {
				headerViewHolder.tvAttendanceValue.setText("Attended");
			} else if (athleteAttendance.getAttendanceValue() == AttendanceValue.CALLED_IN_ABSENCE) {
				headerViewHolder.tvAttendanceValue.setText("Absent (Called)");
			} else if (athleteAttendance.getAttendanceValue() == AttendanceValue.NO_CALL_NO_SHOW) {
				headerViewHolder.tvAttendanceValue.setText("Absent (No Show)");
			} else {
				headerViewHolder.tvAttendanceValue.setText("Registered");
			}
//			Log.d("temp", "att: " + athleteAttendance);
//			Log.d("temp", "att: " + headerViewHolder.tvAttendanceValue.getText());
		}
		// TODO Auto-generated method stub
		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		AthleteAttendance athleteAttendance = getItem(position);

		if (athleteAttendance != null) {
			if (athleteAttendance.getAttendanceValue() == AttendanceValue.ATTENDED) {
				return 0;
			} else if (athleteAttendance.getAttendanceValue() == AttendanceValue.CALLED_IN_ABSENCE) {
				return 1;
			} else if (athleteAttendance.getAttendanceValue() == AttendanceValue.NO_CALL_NO_SHOW) {
				return 2;
			} else {
				return 3;
			}
		}
		return 1;
	}
}
