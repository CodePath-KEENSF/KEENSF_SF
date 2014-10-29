package org.keenusa.connect.adapters;

import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.helpers.PersonNameFormatter;
import org.keenusa.connect.models.Coach;
import org.keenusa.connect.models.ContactPerson;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CoachListItemAdapter extends ArrayAdapter<Coach> {

	private static class ViewHolder {
		ImageView ivCoachProfilePic;
		TextView tvCoachName;
		TextView tvNumberOfSessions;
	}

	public CoachListItemAdapter(Context context, List<Coach> coaches) {
		super(context, R.layout.coach_list_item, coaches);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Coach coach = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.coach_list_item, parent, false);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.ivCoachProfilePic = (ImageView) convertView.findViewById(R.id.ivCoachProfilePic);
			viewHolder.tvCoachName = (TextView) convertView.findViewById(R.id.tvCoachName);
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

		viewHolder.tvCoachName.setText(PersonNameFormatter.getFormatedNameString(coach.getFullName()));
		viewHolder.tvNumberOfSessions.setText(String.valueOf(coach.getNumberOfSessionsAttended()));

		viewHolder.ivCoachProfilePic.setImageResource(0);
		if (coach.getGender() == ContactPerson.Gender.FEMALE) {
			viewHolder.ivCoachProfilePic.setImageResource(R.drawable.ic_user_photo_f);
		} else if (coach.getGender() == ContactPerson.Gender.MALE) {
			viewHolder.ivCoachProfilePic.setImageResource(R.drawable.ic_user_photo_m);
		} else {
			viewHolder.ivCoachProfilePic.setImageResource(R.drawable.ic_user_photo_u);
		}

		return convertView;
	}
}
