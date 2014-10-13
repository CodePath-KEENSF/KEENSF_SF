package org.keenusa.connect.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.joda.time.DateTime;

import android.util.Log;

public class CivicoreDateStringParser {

	public static final String DATE_FORMAT = "MM/dd/yyyy";

	public static DateTime parseDate(String stringDate) {
		if (stringDate != null && !stringDate.isEmpty()) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
			dateFormat.setLenient(true);
			DateTime date = null;
			try {
				date = new DateTime(dateFormat.parse(stringDate).getTime());
			} catch (ParseException pe) {
				Log.e("DATE_PARSING_ERROR", pe.toString());
				return null;
			}
			return date;
		}
		return null;
	}

}
