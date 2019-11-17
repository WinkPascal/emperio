package com.swinkels.emperio.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceFilter {

	public static Date StringToDateFormatter(String date, String parse) {
		SimpleDateFormat timestampFormat = new SimpleDateFormat(parse);
		try {
			return timestampFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String DateToStringFormatter(Date date, String format) {
		SimpleDateFormat timestampFormat = new SimpleDateFormat(format);
		return timestampFormat.format(date);
	}
}
