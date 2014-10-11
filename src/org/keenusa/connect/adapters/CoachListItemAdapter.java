package org.keenusa.connect.adapters;

import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.models.Coach;

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
			convertView.setTag(viewHolder);
		}

		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.tvCoachName.setText(coach.getFirstName() + " " + coach.getLastName());

		return convertView;
	}

}
