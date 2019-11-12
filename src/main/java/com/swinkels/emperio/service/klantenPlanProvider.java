package com.swinkels.emperio.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

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

@Path("/klantenPlanProvider")
public class klantenPlanProvider {
	AfspraakDao afspraakDao = new AfspraakDaoImpl();
	BehandelingDao behandelingDao = new BehandelingDaoImpl();
	KlantDao klantDao = new KlantDaoImpl();
	AfspraakBehandelingDao afspraakBehandelingDao = new AfspraakBehandelingDaoImpl();
	BedrijfDao bedrijfDao = new BedrijfDaoImpl();

	@GET
	@Path("/getBedrijfDataStart/{data}")
	@Produces("application/json")
	public String getBedrijfDataStart(@PathParam("data") String data) {
		String[] dataPunten = data.split("&");

		JsonObjectBuilder job = Json.createObjectBuilder();
		for (String dataPunt : dataPunten) {
			String[] dataPuntDetail = dataPunt.split("=");
			if (dataPuntDetail[0].equals("bedrijf")) {
				String bedrijfEmail = dataPuntDetail[1];
				Bedrijf bedrijf = new Bedrijf(bedrijfEmail);
				bedrijf = bedrijfDao.getKlantPaginaSettings(bedrijf);
				job.add("verplichtContactVeld", bedrijf.getVerplichtContactVeld());
				job.add("invoerveldEmail", bedrijf.getInvoerveldEmail());
				job.add("invoerveldTelefoon", bedrijf.getInvoerveldTelefoon());
				job.add("invoerveldAdres", bedrijf.getInvoerveldAdres());
				job.add("bedrijfEmail", bedrijfEmail);
			}
		}
		return job.build().toString();
	}

	// wordt gebruikt bij
	// inplannen behnadelingen kiezen
	@GET
	@Path("/getBehandelingenByGeslacht/{data}")
	@Produces("application/json")
	public String getBehandelingenByGeslacht(@PathParam("data") String data) {
		String geslacht = "";
		String bedrijf = "";

		System.out.println(data);
		String[] dataPunten = data.split("&");
		for (String dataPunt : dataPunten) {
			String[] dataPuntDetail = dataPunt.split("=");
			if (dataPuntDetail[0].equals("bedrijf")) {
				bedrijf = dataPuntDetail[1];
			}
			if (dataPuntDetail[0].equals("geslacht")) {
				geslacht = dataPuntDetail[1];
			}
		}
		ArrayList<Behandeling> behandelingen = behandelingDao.behandelingenByGeslacht(geslacht, bedrijf);
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
	@Path("/werkdagen/{data}")
	@Produces("application/json")
	public String getWerkdagen(@PathParam("data") String data) {
		System.out.println(data);
		String bedrijfsnaam = "";

		String[] dataPunten = data.split("&");
		for (String dataPunt : dataPunten) {
			String[] dataPuntDetail = dataPunt.split("=");
			if (dataPuntDetail[0].equals("bedrijf")) {
				bedrijfsnaam = dataPuntDetail[1];
			}
		}

		Bedrijf bedrijf = new Bedrijf(bedrijfsnaam);
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
	@Path("/tijdslotenOphalen/{data}")
	@Produces("application/json")
	public String tijdslotenOphalen(@PathParam("data") String data) {
		System.out.println(data);
		String bedrijfsnaam = "";
		String datum = "";
		String[] dataPunten = data.split("&");
		for (String dataPunt : dataPunten) {
			String[] dataPuntDetail = dataPunt.split("=");
			if (dataPuntDetail[0].equals("bedrijf")) {
				bedrijfsnaam = dataPuntDetail[1];
			}
			if (dataPuntDetail[0].equals("datum")) {
				datum = dataPuntDetail[1];
			}
		}
		Bedrijf bedrijf = new Bedrijf(bedrijfsnaam);
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

	@POST
	@Path("/afspraak/{data}")
	@Produces("application/json")
	public Response setAfspraak(@PathParam("data") String data, @FormParam("klantNaam") String afspraakKlantNaam,
			@FormParam("afspraakTijd") String afspraakTijd,

			@FormParam("klantEmail") String afspraakKlantEmail, @FormParam("klantTelefoon") String afspraakKlantTel) {
			//response stattussen
				// validaties
					// 409
				// gepland
					// 201

		ArrayList<Behandeling> behandelingen = new ArrayList<Behandeling>();
		Date timestamp = new Date();
		Bedrijf bedrijf = new Bedrijf();
		String geslacht = null;

		String[] dataPunten = data.split("&");
		for (String dataPunt : dataPunten) {
			String[] dataPuntDetail = dataPunt.split("=");
			if (dataPuntDetail[0].equals("bedrijf")) {
				// bedrijf wordt opgehaalt
				bedrijf = bedrijfDao.getKlantPaginaSettings(new Bedrijf(dataPuntDetail[1]));
			}
			if (dataPuntDetail[0].equals("datum")) {
				// timestamp wordt opgehaalt
				String[] beginDateString = dataPuntDetail[1].split("-");
				int maand = Integer.parseInt(beginDateString[1]) + 1;
				String dateFormatted = beginDateString[0] + "-" + maand + "-" + beginDateString[2];

				System.out.println(dateFormatted);
				String timestampString = dateFormatted + " " + afspraakTijd;
				System.out.println(timestampString);

				timestamp = ServiceFilter.StringToDateFormatter(timestampString, "yyyy-MM-dd HH:mm");
			}
			if (dataPuntDetail[0].equals("behandelingen")) {
				// behandelingen worden opgehaalt
				String[] behandelingenStringLijst = dataPuntDetail[1].split("%2C");
				for (String behandelingId : behandelingenStringLijst) {
					System.out.println(behandelingId);
					Behandeling behandeling = new Behandeling(Integer.parseInt(behandelingId));
					behandelingen.add(behandeling);
				}
			}
			if (dataPuntDetail[0].equals("geslacht")) {
				// geslacht wordt opgehaalt
				geslacht = dataPuntDetail[1];
			}
		}
		// validaties
		if (timestamp.before(new Date())) {
			return Response.status(409).build();
		}
		if (geslacht == null) {
			return Response.status(409).build();
		}
		if (bedrijf.getVerplichtContactVeld() == null) {
			return Response.status(409).build();
		}
		if (behandelingen.size() < 1) {
			return Response.status(409).build();
		}
		if (bedrijf.getInvoerveldEmail()) {
			if (afspraakKlantEmail == null) {
				return Response.status(409).build();
			}
			if (!ServiceFilter.emailCheck(afspraakKlantEmail)) {
				return Response.status(409).build();
			}
		}
		if (bedrijf.getInvoerveldTelefoon()) {
			if (ServiceFilter.phoneCheck(afspraakKlantTel)) {
				return Response.status(409).build();
			}
		}
		if (afspraakKlantNaam.length() < 2) {
			return Response.status(409).build();
		}

		// aanmaken klant object
		Klant klant = new Klant(afspraakKlantNaam, afspraakKlantEmail, afspraakKlantTel, geslacht, bedrijf);

		// een klant zoeken in de database
		if (bedrijf.getVerplichtContactVeld().equals("email")) {
			klantDao.getKlantIdByEmail(klant);
		} else {
			klantDao.getKlantIdByPhone(klant);
		}
		if (klant.getId() == 0) {
			// er is geen klant gevonden in de database
			klantDao.setKlant(klant);
			klant = klantDao.getKlantId(klant);
		}
		Afspraak afspraak = new Afspraak(timestamp, bedrijf, klant);
		afspraak.setBehandelingen(behandelingen);

		// afspraak word in de database gezet
		afspraakDao.setAfspraak(afspraak);
		// afspraak id wordt opgehaalt
		afspraak = afspraakDao.getAfspraakId(afspraak);
		for (Behandeling behandeling : behandelingen) {
			if (afspraakBehandelingDao.saveAfspraakBehandeling(behandeling, afspraak)) {
				// goed
			} else {
				return Response.status(409).build();
			}
		}
		return Response.status(201).build();
	}
}