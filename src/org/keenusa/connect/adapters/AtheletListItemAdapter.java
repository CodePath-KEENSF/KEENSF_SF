package org.keenusa.connect.adapters;

import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.helpers.PersonNameFormatter;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.ContactPerson;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AtheletListItemAdapter extends ArrayAdapter<Athlete> {

	private static class ViewHolder {
		ImageView ivAthleteProfilePic;
		TextView tvAthleteName;
		TextView tvAthleteAge;
		TextView tvNumberOfSessions;
	}

	public AtheletListItemAdapter(Context context, List<Athlete> athletes) {
		super(context, R.layout.athlete_list_item, athletes);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Athlete athlete = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.athlete_list_item, parent, false);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.ivAthleteProfilePic = (ImageView) convertView.findViewById(R.id.ivAthleteProfilePic);
			viewHolder.tvAthleteName = (TextView) convertView.findViewById(R.id.tvAthleteName);
			viewHolder.tvAthleteAge = (TextView) convertView.findViewById(R.id.tvAthleteAge);
			viewHolder.tvNumberOfSessions = (TextView) convertView.findViewById(R.id.tvNumberOfSessions);
			convertView.setTag(viewHolder);
		}

		ViewHolder viewHolder = (ViewHolder) convertView.getTag();

		if (position == 0 && getCount() == 1) {
			convertView.setBackgroundResource(R.drawable.selector_single_item_list_background);
		} else if (position == 0 && getCount() > 1) {
			convertView.setBackgroundResource(R.drawable.selector_single_item_list_background);
		} else if (position == getCount() - 1) {
			convertView.setBackgroundResource(R.drawable.selector_single_item_list_background);
		} else {
			convertView.setBackgroundResource(R.drawable.selector_single_item_list_background);
		}

		viewHolder.tvAthleteName.setText(PersonNameFormatter.getFormatedNameString(athlete.getFullName()));
		viewHolder.tvNumberOfSessions.setText(String.valueOf(athlete.getNumberOfSessionsAttended()));
		if (athlete.getAge() > 0) {
			viewHolder.tvAthleteAge.setText(athlete.getAge() + " years");
			viewHolder.tvAthleteAge.setVisibility(View.VISIBLE);
		} else {
			viewHolder.tvAthleteAge.setVisibility(View.GONE);
		}

		viewHolder.ivAthleteProfilePic.setImageResource(0);
		if (athlete.getGender() == ContactPerson.Gender.FEMALE) {
			viewHolder.ivAthleteProfilePic.setImageResource(R.drawable.ic_user_photo_f);
		} else if (athlete.getGender() == ContactPerson.Gender.MALE) {
			viewHolder.ivAthleteProfilePic.setImageResource(R.drawable.ic_user_photo_m);
		} else {
			viewHolder.ivAthleteProfilePic.setImageResource(R.drawable.ic_user_photo_u);
		}

		return convertView;
	}
}
