package com.swinkels.emperio.service;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Behandeling;
import com.swinkels.emperio.objects.Dag;

@Path("/statistics")
public class StatisticsProvider {

	@GET
	@Path("/getData/{lengte}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getData(@Context SecurityContext sc, @PathParam("lengte") String lengte) {
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		JsonArrayBuilder jab = Json.createArrayBuilder();		
		JsonObjectBuilder job = Json.createObjectBuilder();
		Date date = getLengte(lengte);
		bedrijf.getHoeveelheden(date);
		job.add("afspraken", bedrijf.getHoeveelheidAfspraken());
		job.add("inkomsten", bedrijf.getHoeveelheidInkomsten());
		jab.add(job);
		// top 5 behandelingen worden opgehaalt voor de pie chart
		bedrijf.getTop5Behandelingen(date);;
		JsonArrayBuilder jab1 = Json.createArrayBuilder();
		for (Behandeling behandeling : bedrijf.getBehandelingen()) {
			JsonObjectBuilder job1 = Json.createObjectBuilder();
			job1.add("count", behandeling.getCount());
			job1.add("naam", behandeling.getNaam());
			job1.add("id", behandeling.getId());
			jab1.add(job1);
		}
		jab.add(jab1);
		// per dag worden de hoeveelheid afspraken opgeroepen
		bedrijf.getAantalAfsprakenPerDag(date);
		JsonArrayBuilder jab2 = Json.createArrayBuilder();
		for (Dag dag : bedrijf.getDagen()) {
			JsonObjectBuilder job1 = Json.createObjectBuilder();
			job1.add("dagNummer", dag.getDag());
			job1.add("aantalAfspraken", dag.getAantalAfspraken());
			jab2.add(job1);
		}
		jab.add(jab2);
		return jab.build().toString();
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
			dateCal.set(Calendar.DATE, 1);
		} else {
			dateCal.set(Calendar.YEAR, 2000);
		}
		return dateCal.getTime();
	}
}
