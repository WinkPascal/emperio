package com.swinkels.emperio.service;

import java.util.ArrayList;
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
import com.swinkels.emperio.providers.AfspraakBehandelingDao;
import com.swinkels.emperio.providers.AfspraakBehandelingDaoImpl;
import com.swinkels.emperio.providers.AfspraakDao;
import com.swinkels.emperio.providers.AfspraakDaoImpl;
import com.swinkels.emperio.providers.BedrijfDao;
import com.swinkels.emperio.providers.BedrijfDaoImpl;
import com.swinkels.emperio.providers.BehandelingDao;
import com.swinkels.emperio.providers.BehandelingDaoImpl;
import com.swinkels.emperio.providers.KlantDao;
import com.swinkels.emperio.providers.KlantDaoImpl;

@Path("/statistics")
public class StatisticsProvider {
	AfspraakDao afspraakDao = new AfspraakDaoImpl();
	BehandelingDao behandelingDao = new BehandelingDaoImpl();
	KlantDao klantDao = new KlantDaoImpl();
	AfspraakBehandelingDao afspraakBehandelingDao = new AfspraakBehandelingDaoImpl();
	BedrijfDao bedrijfDao = new BedrijfDaoImpl();

	@GET
	@Path("/getData/{lengte}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getData(@Context SecurityContext sc, @PathParam("lengte") String lengte) {
		// uitvoer
		// hoeveelheid afspraken, inkomsten
		// top 5 behandelingen
		// hoeveelheid, naam, id
		// per dag
		// aantal afspraken
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		Date date = new Date();
		Calendar dateCal = Calendar.getInstance();
		dateCal.setTime(date);
		// de geselecteerde lengte word berekent
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
		date = dateCal.getTime();

		JsonArrayBuilder jab = Json.createArrayBuilder();
		// inkomsten / hoeveelheid afspraken worden opgeroepen
		ArrayList<Double> aantalAfsprakenEnInkomsten = afspraakDao.getInkomsten(bedrijf, date);
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("afspraken", aantalAfsprakenEnInkomsten.get(0));
		job.add("inkomsten", aantalAfsprakenEnInkomsten.get(1));
		jab.add(job);

		// top 5 behandelingen worden opgehaalt
		ArrayList<Behandeling> behandelingen = behandelingDao.getTop5Behandelingen(bedrijf, date);
		JsonArrayBuilder jab1 = Json.createArrayBuilder();
		for (Behandeling behandeling : behandelingen) {
			JsonObjectBuilder job1 = Json.createObjectBuilder();

			job1.add("count", behandeling.getCount());
			job1.add("naam", behandeling.getNaam());
			job1.add("id", behandeling.getId());

			jab1.add(job1);
		}
		jab.add(jab1);

		// per dag worden de hoeveelheid afspraken opgeroepen
		JsonArrayBuilder jab2 = Json.createArrayBuilder();
		ArrayList<Dag> dagen = afspraakDao.getAantalAfsprakenPerDag(bedrijf, date);
		for (Dag dag : dagen) {
			JsonObjectBuilder job1 = Json.createObjectBuilder();
			job1.add("dagNummer", dag.getDag());
			job1.add("aantalAfspraken", dag.getAantalAfspraken());

			jab2.add(job1);
		}
		jab.add(jab2);

		return jab.build().toString();
	}
}
