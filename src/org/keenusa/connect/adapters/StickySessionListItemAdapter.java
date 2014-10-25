package org.keenusa.connect.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.joda.time.DateTime;
import org.keenusa.connect.R;
import org.keenusa.connect.activities.AthleteCoachCheckinActivity;
import org.keenusa.connect.models.KeenProgram;
import org.keenusa.connect.models.KeenSession;
import org.keenusa.connect.utilities.CheckinEditMode;
import org.keenusa.connect.utilities.IntentCode;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StickySessionListItemAdapter extends ArrayAdapter<KeenSession> implements StickyListHeadersAdapter {

	public static final String DATE_FORMAT = "MM/dd/yyyy";
	public static final String DATE_FORMAT_LONG = "MMddyyyy";
	private Context context;

	public static class ViewHolder {
		TextView tvSessionName;
		TextView tvSessionLocation;
		TextView tvNumAthletes;
		TextView tvNumCoaches;
		TextView tvSessionTime;
		RelativeLayout rlCheckInAction;

	}

	public static class HeaderViewHolder {
		TextView tvSessionDate;
	}

	public StickySessionListItemAdapter(Context context, ArrayList<KeenSession> sessionList) {
		super(context, 0, sessionList);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		KeenSession session = getItem(position);
		KeenProgram program = session.getProgram();

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.session_list_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.tvSessionName = (TextView) convertView.findViewById(R.id.tvSessionName);
			viewHolder.tvSessionLocation = (TextView) convertView.findViewById(R.id.tvSessionLocation);
			viewHolder.tvNumAthletes = (TextView) convertView.findViewById(R.id.tvNumAthletes);
			viewHolder.tvNumCoaches = (TextView) convertView.findViewById(R.id.tvNumCoaches);
			viewHolder.tvSessionTime = (TextView) convertView.findViewById(R.id.tvSessionTime);
			viewHolder.rlCheckInAction = (RelativeLayout) convertView.findViewById(R.id.rlCheckInAction);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.rlCheckInAction.setTag(session);

		if (program != null && program.getName() != null) {
			viewHolder.tvSessionName.setText(session.getProgram().getName());
		} else {
			viewHolder.tvSessionName.setText("Dummy Program");
		}

		if (program != null && program.getLocation() != null && program.getLocation().getCity() != null && program.getLocation().getState() != null) {
			viewHolder.tvSessionLocation.setText(session.getProgram().getLocation().getCity() + ", " + session.getProgram().getLocation().getState());
		} else {
			viewHolder.tvSessionLocation.setText("San Francisco");
		}

		viewHolder.tvNumAthletes.setText(session.getNumberOfAthletesCheckedIn() + " / " + session.getRegisteredAthleteCount());

		viewHolder.tvNumCoaches.setText(session.getNumberOfCoachesCheckedIn() + " / " + session.getRegisteredCoachCount() + "");

		if (program != null && program.getProgramTimes() != null) {
			viewHolder.tvSessionTime.setText(session.getProgram().getProgramTimes());
		} else {
			viewHolder.tvSessionTime.setText("12pm - 1pm");
		}

		viewHolder.rlCheckInAction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				KeenSession session = (KeenSession) v.getTag();
				CheckinEditMode.editMode = true;
				Intent checkinIntent = new Intent(getContext(), AthleteCoachCheckinActivity.class);
				checkinIntent.putExtra("session", session);
				((Activity) getContext()).startActivityForResult(checkinIntent, IntentCode.REQUEST_CODE);
				((Activity) getContext()).overridePendingTransition(R.anim.right_in, R.anim.left_out);
			}
		});

		return convertView;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		try {
			KeenSession session = getItem(position);

			HeaderViewHolder headerViewHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.session_list_header, parent, false);

				headerViewHolder = new HeaderViewHolder();
				headerViewHolder.tvSessionDate = (TextView) convertView.findViewById(R.id.tvSessionDate);

				convertView.setTag(headerViewHolder);
			} else {
				headerViewHolder = (HeaderViewHolder) convertView.getTag();
			}

			if (session.getDate() != null) {
				DateTime dt = session.getDate();
				String date = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(dt.toDate());
				headerViewHolder.tvSessionDate.setText(date);
			} else {
				headerViewHolder.tvSessionDate.setText("01/01/2001");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		try {
			KeenSession session = getItem(position);
			DateTime dt = session.getDate();
			String date = new SimpleDateFormat(DATE_FORMAT_LONG, Locale.ENGLISH).format(dt.toDate());
			return (Long.parseLong(date));
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
