package org.keenusa.connect.adapters;

import java.util.ArrayList;
import java.util.List;

import org.keenusa.connect.R;
import org.keenusa.connect.models.Coach;
import org.keenusa.connect.models.TestDataFactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class CoachesSubListAdapter extends BaseExpandableListAdapter {
	private Context context;
	private ArrayList<Headers> coachesSubListArray;
	
	public CoachesSubListAdapter(Context context, ArrayList<Headers> coachesSubListArray) {
		this.context = context;
		this.coachesSubListArray = coachesSubListArray;
	}

	@Override
	public int getGroupCount() {
		return coachesSubListArray.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		List<Coach> coachNameList = TestDataFactory.getInstance().getCoachList();
		return coachNameList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return coachesSubListArray.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<Coach> coachesList = coachesSubListArray.get(groupPosition).getCoachNameList();
		return coachesList.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		Headers header = (Headers) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.group_people, null);
		}
		
		TextView heading = (TextView) convertView.findViewById(R.id.tvHeading);
		heading.setText(header.getName().trim());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		Coach coachesList = (Coach) getChild(groupPosition, childPosition);
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.sublist_people, null);
		}
		
		TextView tvSequence = (TextView) convertView.findViewById(R.id.tvSequence);
		tvSequence.setText(coachesList.getSequence().trim() + ") ");
		TextView tvCoahesList = (TextView) convertView.findViewById(R.id.tvCoahesList);
		tvCoahesList.setText(coachesList.getName().trim());
		return null;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
