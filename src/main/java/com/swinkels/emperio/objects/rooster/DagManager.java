package com.swinkels.emperio.objects.rooster;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.swinkels.emperio.providers.Dag.DagDao;
import com.swinkels.emperio.providers.Dag.DagDaoImpl;
import com.swinkels.emperio.support.JavascriptDateAdapter;

public class DagManager {
	DagDao dagDao = new DagDaoImpl();

	private Date date1;
	private Date date2;
	
	private Date openingsTijd;
	private Date sluitingsTijd;

	public DagManager(String datum) {
		Calendar c = Calendar.getInstance();
		c.setTime(JavascriptDateAdapter.StringToDate(datum, "yyyy-MM-dd"));
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		date1 = c.getTime();
		c.add(Calendar.DAY_OF_MONTH, 7);
		date2 = c.getTime();
	}

	public List<Dag> getWeekRooster() {
		List<Dag> dagen = new ArrayList<>();
		dagen = dagDao.getWeekRooster();
		Afspraak.getAfsprakenBetweenDates(date1, date2);
		sluitingsTijd = getLaatsteSluitingsTijd(dagen);
		openingsTijd = getVroegsteOpeningsTijd(dagen);
		return dagen;
	}

	private Date getLaatsteSluitingsTijd(List<Dag> dagen) {
		Date laatsteSluitingsTijd = JavascriptDateAdapter.StringToDate("00:00", "HH:mm");
		for (Dag dag : dagen) {
			if (dag.getSluitingsTijd() != null && laatsteSluitingsTijd.compareTo(dag.getSluitingsTijd()) < 0) {
				laatsteSluitingsTijd = dag.getSluitingsTijd();
			}
		}
		return laatsteSluitingsTijd;
	}

	private Date getVroegsteOpeningsTijd(List<Dag> dagen) {
		Date vroegsteOpeningsTijd = JavascriptDateAdapter.StringToDate("23:59", "HH:mm");
		for (Dag dag : dagen) {
			if (dag.getOpeningsTijd() != null && vroegsteOpeningsTijd.compareTo(dag.getOpeningsTijd()) > 0) {
				vroegsteOpeningsTijd = dag.getOpeningsTijd();
			}
		}
		return vroegsteOpeningsTijd;
	}

	public HashMap<String, String> toDTO() {
		HashMap<String, String> dto = new HashMap<>();
		dto.put("openingsTijd", JavascriptDateAdapter.DateToString(openingsTijd, "HH:mm"));
		dto.put("sluitingsTijd", JavascriptDateAdapter.DateToString(sluitingsTijd, "HH:mm"));
		return dto;
	}

}
