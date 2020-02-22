package com.swinkels.emperio.support;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.swinkels.emperio.objects.behandeling.Behandeling;
import com.swinkels.emperio.objects.rooster.Afspraak;
import com.swinkels.emperio.providers.Afspraak.AfspraakDao;
import com.swinkels.emperio.providers.Afspraak.AfspraakDaoImpl;

public class Validator {

	public static String validateAfspraak(Afspraak afspraak) {
		if (afspraak.getTimeStamp().before(new Date())) {
			return "timestamp";
		}
		if (afspraak.getKlant().getGeslacht() == null) {
			return "geslacht";
		}
		if (afspraak.getKlant().getNaam().length() < 2) {
			return "naam";
		}
		if (afspraak.getBedrijf().getInvoerveldEmail()) {
			if (emailValidation(afspraak.getKlant().getEmail())) {
				return "email";
			}
		}
		if (afspraak.getBedrijf().getInvoerveldTelefoon()) {
			if (telefoonValidation(afspraak.getKlant().getTel())) {
				return "telefoon";
			}
		}
		if (afspraak.getBehandelingen().size() < 1) {
			return "geen behandelingen";
		}
		String afspraakValidation = afspraakTijdValidation(afspraak);
		if (afspraakValidation != null) {
			return afspraakValidation;
		}
		return null;
	}

	private static String afspraakTijdValidation(Afspraak afspraak) {
		Date beginTijd = afspraak.getTimeStamp();
		
		AfspraakDao afspraakDao = new AfspraakDaoImpl();
		int minuten = afspraakDao.getMinutesOfAfspraak(afspraak);
		Calendar eindTijdCal = Calendar.getInstance();
		eindTijdCal.setTime(beginTijd);
		eindTijdCal.add(Calendar.MINUTE, minuten);
		Date eindTijd = eindTijdCal.getTime();
		
		Calendar volgendeDag = Calendar.getInstance();
		volgendeDag.setTime(beginTijd);
		volgendeDag.add(Calendar.DATE, 1);
		
		ArrayList<Afspraak> afspraken = afspraakDao.getAfsprakenBetweenDates(beginTijd, volgendeDag.getTime(), afspraak.getBedrijf());
		for(Afspraak dagAfspraak : afspraken){
			Date beginTijdDagAfspraak = dagAfspraak.getTimeStamp();

			Calendar eindTijdDagAfspraakCal = Calendar.getInstance();
			eindTijdDagAfspraakCal.setTime(beginTijdDagAfspraak);

			int minutenDagAfspraak = 0;
			for(Behandeling behandeling : dagAfspraak.getBehandelingen()) {
				minutenDagAfspraak = minutenDagAfspraak + behandeling.getLengte().getMinutes();
				minutenDagAfspraak = minutenDagAfspraak + behandeling.getLengte().getHours()*60;
			}			
			eindTijdDagAfspraakCal.add(Calendar.MINUTE, minutenDagAfspraak);
			Date eindTijdDagAfspraak = eindTijdDagAfspraakCal.getTime();
			
			System.out.println(minuten);
			System.out.println(beginTijd);
			System.out.println(eindTijd);
			System.out.println("=================================");
			System.out.println(beginTijdDagAfspraak);
			System.out.println(eindTijdDagAfspraak);
			
			if(beginTijd.before(beginTijdDagAfspraak)) {
				if(eindTijd.before(beginTijdDagAfspraak)) {
					System.out.println("potentielse afspraak begint ervoor en eindigt ervoor");
				} else {
					return "ERROR potentielse afspraak begint ervoor eindigt erin";
				}
			} else {
				if(beginTijd.after(eindTijdDagAfspraak)) {
					System.out.println("potentiele afspraak begint erna");
				} else {
					return "ERROR potentiele afspraak begint erin";
				}
			}
		}
		return null;
	}

	public static String nullValidator(String string) {
		return ((string.length() == 0) ? "-" : string);
	}
	
	private static boolean emailValidation(String email) {
		Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
		Matcher mat = pattern.matcher(email);
		if (mat.matches()) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean telefoonValidation(String phone) {
		return false;
	}

	public static boolean validateLengte(Date lengte) {
		System.out.println(lengte);
		if(lengte.after(new Date())) {
			return false;
		} else {
			return true;
		}		
	}
}
