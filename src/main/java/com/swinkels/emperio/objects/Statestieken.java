package com.swinkels.emperio.objects;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.swinkels.emperio.support.Adapter;
import com.swinkels.emperio.support.DatabaseDateAdapter;

public class Statestieken extends Bedrijf {
	private Double hoeveelheidInkomsten;
	private int hoeveelheidAfspraken;

	public Statestieken(String bedrfijsNaam) {
		super(bedrfijsNaam);
	}

	public void getHoeveelheden(Date date) {
		afspraakDao.getInkomsten(this, date);
	}

	public void getTop5Behandelingen(Date date) {
		behandelingDao.getTop5Behandelingen(this, date);
	}
	


	public HashMap<String, String> getGeslachtenVanAfspraken(Date date) {
		HashMap<String, String> geslachten = afspraakDao.getGeslachtenVanAfspraken(this, date);
		if (geslachten.get("man") == null) {
			geslachten.put("man", "0");
		}
		if (geslachten.get("vrouw") == null) {
			geslachten.put("vrouw", "0");
		}
		if (geslachten.get("meisje") == null) {
			geslachten.put("meisje", "0");
		}
		if (geslachten.get("jongen") == null) {
			geslachten.put("jongen", "0");
		}
		return geslachten;
	}

	public HashMap<String, String> getLengtesAfspraken(Date date) {
		HashMap<String, String> data = new HashMap<String, String>();
		List<Date> lengtes = afspraakDao.getLengtesVanAfspraak(this, date);
		int min10 = 0;
		int min20 = 0;
		int min30 = 0;
		int min60 = 0;

		Date min10Date = DatabaseDateAdapter.StringToDate("00:10", "HH:mm");
		Date min20Date = DatabaseDateAdapter.StringToDate("00:20", "HH:mm");
		Date min30Date = DatabaseDateAdapter.StringToDate("00:30", "HH:mm");
		Date min60Date = DatabaseDateAdapter.StringToDate("01:00", "HH:mm");

		for (Date lengte : lengtes) {
			if (lengte.before(min10Date)) {
				min10++;
			} else if (lengte.before(min20Date)) {
				min20++;
			} else if (lengte.before(min30Date)) {
				min30++;
			} else if (lengte.before(min60Date)) {
				min60++;
			}
		}
		data.put("min10", Integer.toString(min10));
		data.put("min20", Integer.toString(min20));
		data.put("min30", Integer.toString(min30));
		data.put("min60", Integer.toString(min60));
		return data;
	}

	public HashMap<String, String> getUitgaveAfspraken(Date date) {
		HashMap<String, String> data = new HashMap<String, String>();
		int afspraken10 = 0;
		int afspraken20 = 0;
		int afspraken30 = 0;
		int afsprakenBig = 0;

		List<Double> afspraken = afspraakDao.getPrijzenVanAfspraak(this, date);
		for (Double prijs : afspraken) {
			if (prijs < 10) {
				afspraken10++;
			} else if (prijs < 20) {
				afspraken20++;
			} else if (prijs < 30) {
				afspraken30++;
			} else {
				afsprakenBig++;
			}
		}
		data.put("afspraken10", Integer.toString(afspraken10));
		data.put("afspraken20", Integer.toString(afspraken20));
		data.put("afspraken30", Integer.toString(afspraken30));
		data.put("afsprakenBig", Integer.toString(afsprakenBig));

		return data;
	}

	public void getAantalAfsprakenPerDag(Date date) {
		afspraakDao.getAantalAfsprakenPerDag(this, date);
	}

	public HashMap<String, String> getInkomsten(Date date, String format) {
		HashMap<String, String> data = new HashMap<String, String>();
		List<Afspraak> afspraken = afspraakDao.getInkomstenForStatistics(this, date);

		switch (format) {
		case "week":

			break;
		case "maand":

			break;
		case "jaar":

			break;
		default:

			break;
		}

		return data;
	}

	public Double getHoeveelheidInkomsten() {
		return hoeveelheidInkomsten;
	}

	public void setHoeveelheidInkomsten(Double hoeveelheidInkomsten) {
		this.hoeveelheidInkomsten = hoeveelheidInkomsten;
	}

	public int getHoeveelheidAfspraken() {
		return hoeveelheidAfspraken;
	}

	public void setHoeveelheidAfspraken(int hoeveelheidAfspraken) {
		this.hoeveelheidAfspraken = hoeveelheidAfspraken;
	}

}
