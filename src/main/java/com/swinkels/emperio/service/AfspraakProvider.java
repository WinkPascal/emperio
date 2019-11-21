package com.swinkels.emperio.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.swinkels.emperio.objects.Afspraak;
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

@Path("/afspraak")
public class AfspraakProvider {
	AfspraakDao afspraakDao = new AfspraakDaoImpl();
	BehandelingDao behandelingDao = new BehandelingDaoImpl();
	KlantDao klantDao = new KlantDaoImpl();
	AfspraakBehandelingDao afspraakBehandelingDao = new AfspraakBehandelingDaoImpl();
	BedrijfDao bedrijfDao = new BedrijfDaoImpl();

	// wordt gebruikt bij
	// startscherm afspraken vandaag
	@GET
	@Path("/afsprakenByDate/{date}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String afsprakenByDate(@Context SecurityContext sc, @PathParam("date") String datum) throws ParseException {
		// uitvoer
		// Per afspraak
		// id, timestamp, klantnaam, lengte, prijs
		// format de datum
		Date beginDate = ServiceFilter.StringToDateFormatter(datum, "yyyy-MM-dd");
		Calendar calendarBeginDate = Calendar.getInstance();
		calendarBeginDate.setTime(beginDate);
		calendarBeginDate.add(Calendar.MONTH, 1);
		beginDate = calendarBeginDate.getTime();

		// de datum van de volgende dag
		calendarBeginDate.add(Calendar.DAY_OF_YEAR, 1);
		Date eindDate = calendarBeginDate.getTime();

		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		// vraag de afspraken op van het bedrijf en de datum van vandaag
		ArrayList<Afspraak> afsprakenVandaagList = afspraakDao.getAfsprakenBetweenDates(beginDate, eindDate, bedrijf);
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Afspraak afspraak : afsprakenVandaagList) {
			double prijs = 0;
			int uren = 0;
			int minuten = 0;
			// lengte wordt berekent
			for (Behandeling behandeling : afspraak.getBehandelingen()) {
				String lengteString = ServiceFilter.DateToStringFormatter(behandeling.getLengte(), "HH:mm");
				String[] lengteArray = lengteString.split(":");

				uren = uren + Integer.parseInt(lengteArray[0]);
				minuten = minuten + Integer.parseInt(lengteArray[1]);
				prijs = prijs + behandeling.getPrijs();
			}
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("id", afspraak.getId());
			job.add("timestamp", ServiceFilter.DateToStringFormatter(afspraak.getTimeStamp(), "yyyy-MM-dd HH:mm"));
			job.add("klantNaam", afspraak.getKlant().getNaam());
			job.add("lengte", uren + ":" + minuten);
			job.add("prijs", prijs);
			jab.add(job);
		}
		return jab.build().toString();
	}

	// wordt gebruikt bij
	// rooster vullen bij afspraken pagina
	// uitvoer
	// per dag
	// weeknummer
	// openingstijd
	// sluitingstijd
	@GET
	@Path("/getWeekRooster")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getWeekRooster(@Context SecurityContext sc) {
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		// dagen worden opgehaalt
		ArrayList<Dag> dagen = bedrijfDao.getWeekRooster(bedrijf);

		Date vroegsteOpeningsTijd = ServiceFilter.StringToDateFormatter("23:59", "HH:mm");
		Date laatsteSluitingsTijd = ServiceFilter.StringToDateFormatter("00:00", "HH:mm");
		JsonArrayBuilder jab = Json.createArrayBuilder();

		for (Dag dag : dagen) {
			if (vroegsteOpeningsTijd.compareTo(dag.getOpeningsTijd()) > 0) {
				vroegsteOpeningsTijd = dag.getOpeningsTijd();
			}
			if (laatsteSluitingsTijd.compareTo(dag.getSluitingsTijd()) < 0) {
				laatsteSluitingsTijd = dag.getSluitingsTijd();
			}
		}

		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("vroegsteOpeningsTijd", ServiceFilter.DateToStringFormatter(vroegsteOpeningsTijd, "HH:mm"));
		job.add("laatsteSluitingsTijd", ServiceFilter.DateToStringFormatter(laatsteSluitingsTijd, "HH:mm"));
		jab.add(job);

		for (Dag dag : dagen) {
			JsonObjectBuilder job1 = Json.createObjectBuilder();
			job1.add("weekNummer", dag.getDag());

			String openingsTijd = ServiceFilter.DateToStringFormatter(dag.getOpeningsTijd(), "HH:mm");
			job1.add("openingsTijd", openingsTijd);

			String sluitingsTijd = ServiceFilter.DateToStringFormatter(dag.getSluitingsTijd(), "HH:mm");
			job1.add("sluitingsTijd", sluitingsTijd);

			jab.add(job1);
		}
		return jab.build().toString();
	}

	// wordt gebruikt bij
	// week rooster vullen
	@GET
	@Path("/getWeekAfspraken/{date}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getWeekAfspraken(@Context SecurityContext sc, @PathParam("date") String datum) {
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		// format de date naar de java format
		String[] beginDateString = datum.split("-");
		int maand = Integer.parseInt(beginDateString[1]) + 1;
		String dateFormatted = beginDateString[0] + "-" + maand + "-" + beginDateString[2];
		Date beginDate = ServiceFilter.StringToDateFormatter(dateFormatted, "yyyy-MM-dd");

		Calendar calendarEindDate = Calendar.getInstance();
		calendarEindDate.setTime(beginDate);
		calendarEindDate.add(Calendar.DAY_OF_YEAR, 7);
		Date eindDate = calendarEindDate.getTime();

		ArrayList<Afspraak> afspraken = afspraakDao.getAfsprakenBetweenDates(beginDate, eindDate, bedrijf);

		JsonArrayBuilder jab = Json.createArrayBuilder();
		// hieraan wordt toegevoegd:
		// afspraak.id
		// afspraak.beginTijd
		// klant.naam
		// aparte json array binnen jab1
		// behandeling.naam
		// behandeling.lengte
		// behandeling.prijs
		for (Afspraak afspraak : afspraken) {
			JsonObjectBuilder job = Json.createObjectBuilder();

			job.add("id", afspraak.getId());
			Date timestampDate = afspraak.getTimeStamp();
			String timestamp = ServiceFilter.DateToStringFormatter(timestampDate, "yyyy-MM-dd HH:mm");

			job.add("timestamp", timestamp);
			job.add("klant", afspraak.getKlant().getNaam());

			JsonArrayBuilder jab1 = Json.createArrayBuilder();
			for (Behandeling behandeling : afspraak.getBehandelingen()) {
				JsonObjectBuilder job1 = Json.createObjectBuilder();

				String lengteString = ServiceFilter.DateToStringFormatter(behandeling.getLengte(), "HH:mm");

				job1.add("naam", behandeling.getNaam());
				job1.add("lengte", lengteString);
				job1.add("prijs", behandeling.getPrijs());

				jab1.add(job1);
			}

			job.add("behandelingen", jab1);
			jab.add(job);
		}
		System.out.println("s");

		return jab.build().toString();
	};


	@GET
	@RolesAllowed("user")
	@Path("/getAfspraak/{id}")
	@Produces("application/json")
	public String getAfspraak(@Context SecurityContext sc, @PathParam("id") int afspraakId) {
		JsonObjectBuilder job = Json.createObjectBuilder();
		
		Afspraak afspraak = new Afspraak();
		afspraak.setId(afspraakId);
		afspraak.retrieveBehandelingen();
		afspraak.retrieveKlant();
				
		job.add("klantId", afspraak.getKlant().getId());
		job.add("klantNaam", afspraak.getKlant().getNaam());
		job.add("klantEmail", afspraak.getKlant().getEmail());
		job.add("klantAdres", afspraak.getKlant().getAdres());
		job.add("klantGeslacht", afspraak.getKlant().getGeslacht());
		job.add("klantTelefoon", afspraak.getKlant().getTel());

		JsonArrayBuilder jab1 = Json.createArrayBuilder();
		double totaalPrijs = 0;
		for (Behandeling behandeling : afspraak.getBehandelingen()) {
			System.out.println(behandeling.getNaam() + "is het id");
			JsonObjectBuilder job1 = Json.createObjectBuilder();
			
			job1.add("id", behandeling.getId());
			job1.add("naam", behandeling.getNaam());
			job1.add("lengte", ServiceFilter.DateToStringFormatter(behandeling.getLengte(), "HH:mm"));
			job1.add("prijs", behandeling.getPrijs());
			
			totaalPrijs = totaalPrijs + behandeling.getPrijs();
			jab1.add(job1);
		}

		job.add("behandelingen", jab1);
		job.add("totaalPrijs", totaalPrijs);

		return job.build().toString();
	}
	
	@DELETE
	@RolesAllowed("user")
	@Path("/deleteafspraak/{id}")
	@Produces("application/json")
	public Response deleteAfspraak(@PathParam("id") int afspraakId) {
		System.out.println("delete from "+afspraakId);
		Afspraak afspraak = new Afspraak();
		afspraak.setId(afspraakId);
		if(afspraak.delete()) {
			return Response.ok().build();			
		} else {
			return Response.serverError().build();
		}
	}	
}