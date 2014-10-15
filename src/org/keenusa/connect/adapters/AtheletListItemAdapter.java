package org.keenusa.connect.adapters;

import java.util.List;

import org.keenusa.connect.R;
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
			convertView.setTag(viewHolder);
		}

		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.tvAthleteName.setText(athlete.getFullName());

		viewHolder.ivAthleteProfilePic.setImageResource(0);
		if (athlete.getGender() == ContactPerson.Gender.FEMALE) {
			viewHolder.ivAthleteProfilePic.setImageResource(R.drawable.ic_user_photos_f);
		} else if (athlete.getGender() == ContactPerson.Gender.MALE) {
			viewHolder.ivAthleteProfilePic.setImageResource(R.drawable.ic_user_photos_m);
		} else {
			viewHolder.ivAthleteProfilePic.setImageResource(R.drawable.ic_user_photos_u);
		}

		return convertView;
	}
}
