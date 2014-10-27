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
			convertView.setTag(viewHolder);
		}

		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.tvAthleteName.setText(PersonNameFormatter.getFormatedNameString(athlete.getFullName()));
		if (athlete.getAge() > 0) {
			viewHolder.tvAthleteAge.setText(athlete.getAge() + " years");
			viewHolder.tvAthleteAge.setVisibility(View.VISIBLE);
		} else {
			viewHolder.tvAthleteAge.setVisibility(View.GONE);
		}

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
