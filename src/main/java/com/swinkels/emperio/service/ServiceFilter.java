package com.swinkels.emperio.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceFilter {
	
	public static boolean emailCheck(String email) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(email);

        if(mat.matches()){
        	return true;
        } else {
            return false;	
        }
    }
	
	public static boolean phoneCheck(String phone) {
		if(phone.length() > 9 || phone.length() < 13) {
			return true;
		} else {
			return false;
		}
	}
	
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
