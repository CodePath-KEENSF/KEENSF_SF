package org.keenusa.connect.utilities;

import java.util.Comparator;

import org.keenusa.connect.models.CoachAttendance;

public class CoachAttComparator implements Comparator<CoachAttendance>{
	@Override
	public int compare(CoachAttendance c1, CoachAttendance c2) {
		return (Integer.parseInt(c1.getAttendanceValue().getRemoteKeyString()) - Integer.parseInt(c2.getAttendanceValue().getRemoteKeyString()));
	}
}
