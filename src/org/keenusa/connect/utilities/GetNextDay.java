package org.keenusa.connect.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

public class GetNextDay {

	public static Date getNextSunday() {
        LocalDate today = new LocalDate();
        int dow = DateTimeConstants.SUNDAY;
        LocalDate date = getNextDayOfWeek(today ,dow );
        return date.toDate();
   }
	
	public static LocalDate getNextDayOfWeek(LocalDate t0, int dow) {
	    LocalDate t1 = t0.withDayOfWeek(dow);
	    
	    if(t1.isBefore(t0)) return t1.plusWeeks(1);
	    else return t1;
	}

}
