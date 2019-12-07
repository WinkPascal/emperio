package com.swinkels.emperio.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.json.JSONArray;

import com.swinkels.emperio.objects.Afspraak;
import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Behandeling;
import com.swinkels.emperio.objects.Dag;
import com.swinkels.emperio.objects.Klant;
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

@Path("/plan")
public class PlanProvider {
	// wordt gebruikt bij
	// inplannen behnadelingen kiezen
	@GET
	@Path("/behandelingen/{geslacht}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getBehandelingenByGeslacht(@Context SecurityContext sc, @PathParam("geslacht") String geslacht) {
		ArrayList<Behandeling> behandelingen = behandelingDao.behandelingenByGeslacht(geslacht,
				sc.getUserPrincipal().getName());
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Behandeling behandeling : behandelingen) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("id", behandeling.getId());
			job.add("naam", behandeling.getNaam());
			job.add("beschrijving", behandeling.getBeschrijving());
			job.add("lengte", ServiceFilter.DateToStringFormatter(behandeling.getLengte(), "HH:mm"));
			job.add("prijs", behandeling.getPrijs());
			jab.add(job);
		}
		return jab.build().toString();
	}

	// wordt gebruikt bij
	// inplannen datum kiezen
	@GET
	@Path("/werkdagen")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getWerkdagen(@Context SecurityContext sc) {
		// uitvoer
		// per dag
		// dagnummer, sluitingstijd, openingstijd
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		ArrayList<Dag> dagen = bedrijfDao.getWeekRooster(bedrijf);
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Dag dag : dagen) {
			JsonObjectBuilder job = Json.createObjectBuilder();

			job.add("dagNummmer", dag.getDag());
			job.add("sluitingstijd", ServiceFilter.DateToStringFormatter(dag.getSluitingsTijd(), "HH:mm"));
			job.add("opeingstijd", ServiceFilter.DateToStringFormatter(dag.getOpeningsTijd(), "HH:mm"));

			jab.add(job);

		}

		return jab.build().toString();
	}

	// wordt gebruikt bij
	// inplannen dag ophalen
	@GET
	@Path("/tijdslotenOphalen/{datum}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String tijdslotenOphalen(@Context SecurityContext sc, @PathParam("datum") String datum) {
		// uitvoer
		// openingstijd
		// sluitingstijd
		// per afspraak
		// beginTijd
		// eindTijd
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		// format de date naar de java format
		String[] beginDateString = datum.split("-");
		int maand = Integer.parseInt(beginDateString[1]) + 1;
		String dateFormatted = beginDateString[0] + "-" + maand + "-" + beginDateString[2];
		Date beginDate = ServiceFilter.StringToDateFormatter(dateFormatted, "yyyy-MM-dd");

		// de volgende dag
		Calendar calendarEindDate = Calendar.getInstance();
		calendarEindDate.setTime(beginDate);
		calendarEindDate.add(Calendar.DAY_OF_YEAR, 1);
		Date eindDate = calendarEindDate.getTime();

		ArrayList<Date> dagTijden = bedrijfDao.getDagTijden(bedrijf, beginDate);

		JsonArrayBuilder jab = Json.createArrayBuilder();

		if (dagTijden.get(0) != null) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			String openingsTijd = ServiceFilter.DateToStringFormatter(dagTijden.get(0), "HH:mm");
			String sluitingstijd = ServiceFilter.DateToStringFormatter(dagTijden.get(1), "HH:mm");

			job.add("openingsTijd", openingsTijd);
			job.add("sluitingstijd", sluitingstijd);

			jab.add(job);
		} else {
			System.out.println("gesloten");
		}

		ArrayList<Afspraak> afsprakenVandaagList = afspraakDao.getAfsprakenBetweenDates(beginDate, eindDate, bedrijf);

		for (Afspraak afspraak : afsprakenVandaagList) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			int minuten = 0;
			int uur = 0;

			for (Behandeling behandeling : afspraak.getBehandelingen()) {
				Date lengte = behandeling.getLengte();

				String lengteString = ServiceFilter.DateToStringFormatter(lengte, "HH:mm");

				String behandelingUren = lengteString.substring(0, 2);
				int behandelingUrenInt = Integer.parseInt(behandelingUren);

				String behandelingMinuten = lengteString.substring(3, 5);
				int behandelingMinutenInt = Integer.parseInt(behandelingMinuten);

				uur = uur + behandelingUrenInt;
				minuten = minuten + behandelingMinutenInt;
				if (minuten > 60) {
					uur++;
					minuten = minuten - 60;
				}
			}
			// begin tijd bekerekenen
			String timestampString = ServiceFilter.DateToStringFormatter(afspraak.getTimeStamp(), "yyyy-MM-dd HH:mm");

			String beginUurString = timestampString.substring(11, 13);
			String beginMinuutString = timestampString.substring(14, 16);

			int beginUur = Integer.parseInt(beginUurString);
			int beginMinuut = Integer.parseInt(beginMinuutString);

			String beginTijd = beginUur + ":" + beginMinuut;
			String lengte = uur + ":" + minuten;

			job.add("beginTijd", beginTijd);
			job.add("lengte", lengte);

			jab.add(job);
		}

		return jab.build().toString();
	}

	// wordt gebruikt bij
	// afspraak inplannen
//uitvoer
	// respone
	// ok
	// error
	// validaties
	@POST
	@RolesAllowed("user")
	@Path("/afspraak")
	@Produces("application/json")
	public Response setAfspraak(@Context SecurityContext sc, @FormParam("klantNaam") String afspraakKlantNaam,
			@FormParam("klantGeslacht") String afspraakKlantGeslacht,
			@FormParam("klantEmail") String afspraakKlantEmail, @FormParam("klantTelefoon") String afspraakKlantTel,

			@FormParam("afspraakBehandeling") String afspraakBehandelingen,
			@FormParam("afspraakTijd") String afspraakTijd, @FormParam("afspraakDatum") String datum)
			throws ParseException {
		// objecten van de behandelingen van de afspraak maken
		JSONArray jsonArray = new JSONArray(afspraakBehandelingen);

		String[] beginDateString = datum.split("-");
		int maand = Integer.parseInt(beginDateString[1]) + 1;
		String dateFormatted = beginDateString[0] + "-" + maand + "-" + beginDateString[2];																						
		// gemaakt

		ArrayList<Behandeling> behandelingenList = new ArrayList<Behandeling>();
		for (int i = 0; i < jsonArray.length(); i++) {
			Behandeling behandeling = new Behandeling(jsonArray.get(i).toString());
			behandeling.setId(Integer.parseInt(jsonArray.get(i).toString()));
			behandelingenList.add(behandeling);
		}
		// aanmaken bedrijf object
		String bedrijfsNaam = sc.getUserPrincipal().getName();
		Bedrijf bedrijf = new Bedrijf(bedrijfsNaam);
		// aanmaken klant object
		Klant klant = new Klant(afspraakKlantGeslacht, afspraakKlantNaam, bedrijf);
		// valideren email en telefoon
		String email = ServiceFilter.emailCheck(afspraakKlantEmail);
		if (email.equals("fout")) {
		}
		if (email.equals("email")) {
			klant.setEmail(afspraakKlantEmail);
		}
		if (ServiceFilter.phoneCheck(afspraakKlantTel)) {
			klant.setTel(afspraakKlantTel);
		}

		// contact met de database
		// klant wordt in de database gezet

		klant = klantDao.getKlantId(klant);

		if (klant.getId() == 0) {
			// er is geen klant aangemaakt dus die word aangemaakt in de database
			klantDao.setKlant(klant);
			klant = klantDao.getKlantId(klant);
		}

		// aanmaken afspraak object
		String timestampString = dateFormatted + " " + afspraakTijd;

		SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date timestamp = timestampFormat.parse(timestampString);

		Afspraak afspraak = new Afspraak(timestamp, bedrijf, klant);

		// afspraak word in de database gezet
		afspraakDao.setAfspraak(afspraak);
		// afspraak id wordt opgehaalt
		afspraak = afspraakDao.getAfspraakId(afspraak);

		// per behandeling die verbonden is met de afspraak word er een tussentabel
		// gemaakt
		for (Behandeling behandeling : behandelingenList) {
			afspraakBehandelingDao.saveAfspraakBehandeling(behandeling, afspraak);
		}
		return Response.ok().build();
	}
}

