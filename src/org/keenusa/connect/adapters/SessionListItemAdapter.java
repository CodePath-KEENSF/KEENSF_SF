package org.keenusa.connect.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.joda.time.DateTime;
import org.keenusa.connect.R;
import org.keenusa.connect.models.KeenSession;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SessionListItemAdapter extends ArrayAdapter<KeenSession> {

	public static final String DATE_FORMAT = "MM/dd/yyyy";
	
	public static class ViewHolder {
		TextView tvSessionName;
		TextView tvSessionLocation;
		TextView tvNumAthletes;
		TextView tvNumCoaches;
		TextView tvSessionDate;
		TextView tvSessionTime;
	}

	public SessionListItemAdapter(Context context,
			ArrayList<KeenSession> sessionList) {
		super(context, 0, sessionList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		KeenSession session = getItem(position);

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.session_list_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.tvSessionName = (TextView) convertView
					.findViewById(R.id.tvSessionName);
			viewHolder.tvSessionLocation = (TextView) convertView
					.findViewById(R.id.tvSessionLocation);
			viewHolder.tvNumAthletes = (TextView) convertView
					.findViewById(R.id.tvNumAthletes);
			viewHolder.tvNumCoaches = (TextView) convertView
					.findViewById(R.id.tvNumCoaches);
			viewHolder.tvSessionDate = (TextView) convertView
					.findViewById(R.id.tvSessionDate);
			viewHolder.tvSessionTime = (TextView) convertView
					.findViewById(R.id.tvSessionTime);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvSessionName.setText(session.getProgram().getName());
		
		viewHolder.tvSessionLocation.setText(session.getProgram().getLocation()
				.getCity()
				+ ", " + session.getProgram().getLocation().getState());
		
		viewHolder.tvNumAthletes.setText(session.getRegisteredAthleteCount() + "");
		
		viewHolder.tvNumCoaches.setText(session.getRegisteredCoachCount() + "");
		
		DateTime dt = session.getDate();
		String date = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(dt.toDate());
		viewHolder.tvSessionDate.setText(date);
		
		viewHolder.tvSessionTime.setText(session.getProgram().getProgramTimes());

		return convertView;
	}

}
