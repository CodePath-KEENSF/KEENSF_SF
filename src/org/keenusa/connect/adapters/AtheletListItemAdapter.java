package org.keenusa.connect.adapters;

import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.models.Athlete;
import org.keenusa.connect.models.Coach;

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
		viewHolder.tvAthleteName.setText(athlete.getFirstName() + " " + athlete.getLastName());

		return convertView;
	}
}
