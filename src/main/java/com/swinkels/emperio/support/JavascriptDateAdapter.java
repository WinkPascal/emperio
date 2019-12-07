package com.swinkels.emperio.support;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.json.JsonValue;

public class JavascriptDateAdapter {
	public static Date StringToDate(String date, String parse) {
		String[] datumOnderdelen = date.split("-");
		int maand;
		switch (parse) {
		case "yyyy-MM-dd HH:mm":
			maand = Integer.parseInt(datumOnderdelen[1]) + 1;
			if (maand > 12) {
				maand = 1;
			}
			date = datumOnderdelen[0] + "-" + maand + "-" + datumOnderdelen[2];
			break;
		case "yyyy-MM-dd":
			maand = Integer.parseInt(datumOnderdelen[1]) + 1;
			if (maand > 12) {
				maand = 1;
			}
			date = datumOnderdelen[0] + "-" + maand + "-" + datumOnderdelen[2];
			break;
		case "HH:mm":
			// niks
			break;
		}
		SimpleDateFormat timestampFormat = new SimpleDateFormat(parse);
		try {
			return timestampFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String DateToString(Date date, String format) {
		SimpleDateFormat timestampFormat = new SimpleDateFormat(format);
		return timestampFormat.format(date);
	}

	public static int dagNummer(int dag) {
		dag = dag - 2;
		if(dag < 0) {
			dag = dag + 7;
		}
		return dag;
	}
}
