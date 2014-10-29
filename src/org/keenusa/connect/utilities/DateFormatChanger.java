package org.keenusa.connect.utilities;

import org.keenusa.connect.R;

import android.content.Context;

public class DateFormatChanger {
	public static String dateToDescriptiveFormat(Context context, String date) {
		String formattedDate = "";
		String MonthDayYear = date.toString();
		String Month = MonthDayYear.substring(0, MonthDayYear.indexOf("/"));
		String DayYear = MonthDayYear.substring(MonthDayYear.indexOf("/") + 1);
		String Day = DayYear.substring(0, DayYear.indexOf("/"));
		String Year = DayYear.substring(DayYear.indexOf("/") + 1);

		int month = Integer.parseInt(Month) - 1;
		String[] monthArray = context.getResources().getStringArray(
				R.array.month_names);

		formattedDate = monthArray[month] + " " + Day + ", " + Year;

		return formattedDate;
	}
}
