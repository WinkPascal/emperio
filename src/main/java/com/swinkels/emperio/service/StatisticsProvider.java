package com.swinkels.emperio.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import com.swinkels.emperio.objects.behandeling.Behandeling;
import com.swinkels.emperio.objects.rooster.Afspraak;
import com.swinkels.emperio.objects.rooster.Dag;
import com.swinkels.emperio.objects.security.Bedrijf;
import com.swinkels.emperio.objects.statestieken.Statestieken;
import com.swinkels.emperio.support.Adapter;

@Path("/statistics")
public class StatisticsProvider {

	@GET
	@Path("/getData/{lengte}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getData(@Context SecurityContext sc, @PathParam("lengte") String lengte) {
		Statestieken Statestieken = new Statestieken(sc.getUserPrincipal().getName());
		Date date = getLengte(lengte);
		
		JsonObjectBuilder job = Json.createObjectBuilder();
		// getallen
		Statestieken.getHoeveelheden(date);
		job.add("getallen", getGetallen(Statestieken));
		//piecharts
		Statestieken.getTop5Behandelingen(date);
		job.add("behandelingen", getBehandelingen(Statestieken));
		job.add("geslachten", getGeslachtenVanAfspraken(Statestieken.getGeslachtenVanAfspraken(date)));
		job.add("lengtes", getLengteAfspraken(Statestieken.getLengtesAfspraken(date)));
		job.add("uitgave", getUitgaveAfspraken(Statestieken.getUitgaveAfspraken(date)));
		// grafieken
		Statestieken.getAantalAfsprakenPerDag(date);
		job.add("afsprakenPerDag", getAfsprakenVanDagen(Statestieken));
		
		//HashMap<String, String> inkosmeten = Statestieken.getInkomsten(date, lengte);

		return job.build().toString();
	}
	
	private JsonObjectBuilder getWeekInkomsten(List<Afspraak> afspraken) {
		Double week1 = 0.0;
		Double week2 = 0.0;
		Double week3 = 0.0;
		Double week4 = 0.0;
		Double week5 = 0.0;
		for(Afspraak afspraak : afspraken) {
			int dagnummer = Adapter.getDagNummerFromDate(afspraak.getTimeStamp());
			switch(dagnummer) {
			
			}
		}

		
	}

	
	private JsonObjectBuilder getWeekInkomsten(List<Afspraak> afspraken) {
		Double dag1 = 0.0;
		Double dag2 = 0.0;
		Double dag3 = 0.0;
		Double dag4 = 0.0;
		Double dag5 = 0.0;
		Double dag6 = 0.0;
		Double dag7 = 0.0;
		for(Afspraak afspraak : afspraken) {
			int dagnummer = Adapter.getDagNummerFromDate(afspraak.getTimeStamp());
			switch(dagnummer) {
			case 1:
				dag1 = dag1 + afspraak.getPrijs();
				break;
			case 2:
				dag2 = dag2 + afspraak.getPrijs();
				break;
			case 3:
				dag3 = dag3 + afspraak.getPrijs();
				break;
			case 4:
				dag4 = dag4 + afspraak.getPrijs();
				break;
			case 5:
				dag5 = dag5 + afspraak.getPrijs();
				break;
			case 6:
				dag6 = dag6 + afspraak.getPrijs();
				break;
			case 7:
				dag7 = dag7 + afspraak.getPrijs();
				break;
			}
		}
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("dag1", dag1);
		job.add("dag2", dag2);
		job.add("dag3", dag3);
		job.add("dag4", dag4);
		job.add("dag5", dag5);
		job.add("dag6", dag6);
		job.add("dag7", dag7);
		return job;
	}

	private JsonObjectBuilder getLengteAfspraken(HashMap<String, String> lengtesAfspraken) {
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("min10", lengtesAfspraken.get("min10"));
		job.add("min20", lengtesAfspraken.get("min20"));
		job.add("min30", lengtesAfspraken.get("min30"));
		job.add("min60", lengtesAfspraken.get("min60"));
		return job;
	}

	private JsonObjectBuilder getUitgaveAfspraken(HashMap<String, String> uitgaveAfspraken) {
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("d10", uitgaveAfspraken.get("afspraken10"));
		job.add("d20", uitgaveAfspraken.get("afspraken20"));
		job.add("d30", uitgaveAfspraken.get("afspraken30"));
		job.add("d40", uitgaveAfspraken.get("afsprakenBig"));
		return job;
	}

	private JsonArrayBuilder getGeslachtenVanAfspraken(HashMap<String, String> hashMap) {
		JsonArrayBuilder jab = Json.createArrayBuilder();
		String manData = hashMap.get("man");
		String vrouwData = hashMap.get("vrouw");
		String jongenData = hashMap.get("jongen");
		String meisjeData = hashMap.get("meisje");
		
		JsonObjectBuilder job = Json.createObjectBuilder();
		
		job.add("naam","man");
		job.add("count",manData);
		jab.add(job);
	
		job.add("naam","vrouw");
		job.add("count", vrouwData);
		jab.add(job);
	 
		job.add("naam","jongen");
		job.add("count",jongenData);
		jab.add(job);
	 
		job.add("naam","meisje");
		job.add("count",meisjeData);
		jab.add(job);
		
		return jab;
	}

	private JsonObjectBuilder getGetallen(Statestieken bedrijf) {
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("afspraken", bedrijf.getHoeveelheidAfspraken());
		job.add("inkomsten", bedrijf.getHoeveelheidInkomsten());
		return job;
	}

	private JsonArrayBuilder getAfsprakenVanDagen(Bedrijf bedrijf) {
		JsonArrayBuilder jab2 = Json.createArrayBuilder();
		for (Dag dag : bedrijf.getDagen()) {
			JsonObjectBuilder job1 = Json.createObjectBuilder();
			job1.add("dagNummer", dag.getDag());
			job1.add("aantalAfspraken", dag.getAantalAfspraken());
			jab2.add(job1);
		}
		return jab2;
	}

	private JsonArrayBuilder getBehandelingen(Bedrijf bedrijf) {
		JsonArrayBuilder jab1 = Json.createArrayBuilder();
		for (Behandeling behandeling : bedrijf.getBehandelingen()) {
			JsonObjectBuilder job1 = Json.createObjectBuilder();
			job1.add("naam", behandeling.getNaam());
			job1.add("count", behandeling.getCount());
			job1.add("id", behandeling.getId());
			jab1.add(job1);
		}
		return jab1;
	}

	private Date getLengte(String lengte) {
		Calendar dateCal = Calendar.getInstance();
		dateCal.setTime(new Date());
		if (lengte.equals("week")) {
			dateCal.set(Calendar.DAY_OF_WEEK, dateCal.getFirstDayOfWeek());
		} else if (lengte.equals("maand")) {
			dateCal.set(Calendar.DAY_OF_MONTH, 1);
		} else if (lengte.equals("jaar")) {
			dateCal.set(Calendar.DAY_OF_MONTH, 1);
			dateCal.set(Calendar.MONTH, 0);
		} else {
			dateCal.set(Calendar.YEAR, 2000);
		}
		return dateCal.getTime();
	}
}
