package com.swinkels.emperio.support;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Adapter {
	public static Date getNextDay(Date date) {
		Calendar calendarEindDate = Calendar.getInstance();
		calendarEindDate.setTime(date);
		calendarEindDate.add(Calendar.DAY_OF_YEAR, 1);
		return calendarEindDate.getTime();
	}

	public static Date getNextWeek(Date date) {
		Calendar calendarEindDate = Calendar.getInstance();
		calendarEindDate.setTime(date);
		calendarEindDate.add(Calendar.DAY_OF_YEAR, 7);
		return calendarEindDate.getTime();
	}

	public static int getDagNummerFromDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dagnummer = c.get(Calendar.DAY_OF_WEEK);
		if (dagnummer < 0) {
			dagnummer = 6;
		}
		return dagnummer;
	}
}
