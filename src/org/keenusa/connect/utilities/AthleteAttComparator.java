package org.keenusa.connect.utilities;

import java.util.Comparator;

import org.keenusa.connect.models.AthleteAttendance;

public class AthleteAttComparator implements Comparator<AthleteAttendance>{
	@Override
	public int compare(AthleteAttendance a1, AthleteAttendance a2) {
		if(a1.getAttendanceValue() == null || a2.getAttendanceValue() == null){
			return 0;
		}
		return (Integer.parseInt(a1.getAttendanceValue().getRemoteKeyString()) - Integer.parseInt(a2.getAttendanceValue().getRemoteKeyString()));
	}
}
