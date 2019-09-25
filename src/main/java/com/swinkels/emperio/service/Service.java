package com.swinkels.emperio.service;

import java.util.Date;

public class Service {
	public static void main(String [] args)
	{
		String date ="2019-08-23 10:00";
		Date datum = ServiceFilter.StringToDateFormatter(date, "yyyy-MM-dd HH:mm");
		System.out.println(datum);
	}
}
