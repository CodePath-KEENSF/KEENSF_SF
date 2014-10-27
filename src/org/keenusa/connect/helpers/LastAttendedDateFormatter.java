package org.keenusa.connect.helpers;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class LastAttendedDateFormatter {

	public static String getFormatedLastAttendedDateString(DateTime date) {
		if (date != null) {
			DateTimeFormatter fmt = DateTimeFormat.forPattern("dd MMM, yyyy");
			return fmt.print(date);
		}
		return null;
	}
}
