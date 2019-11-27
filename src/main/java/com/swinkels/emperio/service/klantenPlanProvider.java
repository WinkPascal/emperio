package com.swinkels.emperio.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.AbstractMap.SimpleEntry;

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
import com.swinkels.emperio.objects.Instellingen;
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
import com.swinkels.emperio.support.Adapter;
import com.swinkels.emperio.support.Validator;

@Path("/klantenPlanProvider")
public class klantenPlanProvider {
	KlantDao klantDao = new KlantDaoImpl();
	AfspraakBehandelingDao afspraakBehandelingDao = new AfspraakBehandelingDaoImpl();
	BedrijfDao bedrijfDao = new BedrijfDaoImpl();

	private Bedrijf getBedrijf(String data) {
		for (String dataPunt : data.split("&")) {
			String[] dataPuntDetail = dataPunt.split("=");
			if (dataPuntDetail[0].equals("bedrijf")) {
				String bedrijfsNaam = dataPuntDetail[1].substring(0, dataPuntDetail[1].length() - 4);
				return new Bedrijf(bedrijfsNaam);
			}
		}
		return null;
	}

	private Date getDatum(String data) {
		String[] dataPunten = data.split("&");
		for (String dataPunt : dataPunten) {
			String[] dataPuntDetail = dataPunt.split("=");
			if (dataPuntDetail[0].equals("datum")) {
				String datum = dataPuntDetail[1];
				String[] beginDateString = datum.split("-");
				int maand = Integer.parseInt(beginDateString[1]) + 1;
				String dateFormatted = beginDateString[0] + "-" + maand + "-" + beginDateString[2];
				Date beginDate = Adapter.StringToDate(dateFormatted, "yyyy-MM-dd");
				return beginDate;
			}
		}
		return null;
	}

	private Date getTimestamp(String data, String afspraakTijd) {
		String[] dataPunten = data.split("&");
		for (String dataPunt : dataPunten) {
			String[] dataPuntDetail = dataPunt.split("=");
			if (dataPuntDetail[0].equals("datum")) {
				String[] beginDateString = dataPuntDetail[1].split("-");
				String dateFormatted = beginDateString[0] + "-" + beginDateString[1] + "-" + beginDateString[2];
				String timestampString = dateFormatted + " " + afspraakTijd;
				return Adapter.StringToDate(timestampString, "yyyy-MM-dd HH:mm");
			}
		}
		return null;
	}

	private ArrayList<Behandeling> getBehandelingen(String data) {
		ArrayList<Behandeling> behandelingen = new ArrayList<Behandeling>();
		String[] dataPunten = data.split("&");
		for (String dataPunt : dataPunten) {
			String[] dataPuntDetail = dataPunt.split("=");
			if (dataPuntDetail[0].equals("behandelingen")) {
				for (String behandelingId : dataPuntDetail[1].split(",")) {
					Behandeling behandeling = new Behandeling(Integer.parseInt(behandelingId));
					behandelingen.add(behandeling);
				}
			}
		}
		return behandelingen;
	}

	private String getGeslacht(String data) {
		String[] dataPunten = data.split("&");
		for (String dataPunt : dataPunten) {
			String[] dataPuntDetail = dataPunt.split("=");
			if (dataPuntDetail[0].equals("geslacht")) {
				return dataPuntDetail[1];
			}
		}
		return null;
	}

	@GET
	@Path("/getBedrijfDataStart/{data}")
	@Produces("application/json")
	public String getBedrijfDataStart(@PathParam("data") String data) {
		Bedrijf bedrijf = getBedrijf(data);
		System.out.println(bedrijf.getBedrijfsNaam());

		Instellingen instellingen = new Instellingen(bedrijf);
		bedrijf.setInstellingen(instellingen);

		bedrijf.getKlantPaginaSettings();

		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("bedrijfsNaam", bedrijf.getBedrijfsNaam());

		job.add("invoerveldEmail", instellingen.isEmailKlantInvoer());
		job.add("invoerveldTelefoon", instellingen.isTelefoonKlantInvoer());
		job.add("invoerveldAdres", instellingen.isAdresKlantInvoer());

		if (instellingen.isBedrijfsEmail()) {
			job.add("bedrijfEmail", bedrijf.getEmail());
		}
		if (instellingen.isBedrijfsTelefoon()) {
			job.add("bedrijfsTelefoon", bedrijf.getTelefoon());
		}
		if (instellingen.isBedrijfsAdres()) {
			String adres = bedrijf.getWoonplaats() + " " + bedrijf.getPostcode() + " " + bedrijf.getAdres();
			job.add("bedrijfsAdres", adres);
		}

		return job.build().toString();
	}

	@GET
	@Path("/getBehandelingenByGeslacht/{data}")
	@Produces("application/json")
	public String getBehandelingenByGeslacht(@PathParam("data") String data) {
		String geslacht = getGeslacht(data);
		Bedrijf bedrijf = getBedrijf(data);

		bedrijf.retrieveBehandelingenByGeslacht(geslacht);

		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Behandeling behandeling : bedrijf.getBehandelingen()) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("id", behandeling.getId());
			job.add("naam", behandeling.getNaam());
			job.add("beschrijving", behandeling.getBeschrijving());
			job.add("lengte", Adapter.DateToString(behandeling.getLengte(), "HH:mm"));
			job.add("prijs", behandeling.getPrijs());
			jab.add(job);
		}
		return jab.build().toString();
	}

	@GET
	@Path("/werkdagen/{data}")
	@Produces("application/json")
	public String getWerkdagen(@PathParam("data") String data) {
		Bedrijf bedrijf = getBedrijf(data);
		bedrijf.retrieveDagen();

		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Dag dag : bedrijf.getDagen()) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("dagNummmer", dag.getDag());
			job.add("sluitingstijd", Adapter.DateToString(dag.getSluitingsTijd(), "HH:mm"));
			job.add("opeingstijd", Adapter.DateToString(dag.getOpeningsTijd(), "HH:mm"));
			jab.add(job);
		}
		return jab.build().toString();
	}

	@GET
	@Path("/tijdslotenOphalen/{data}")
	@Produces("application/json")
	public String tijdslotenOphalen(@PathParam("data") String data) {
		// data ontvangen
		Bedrijf bedrijf = getBedrijf(data);
		Date vandaag = getDatum(data);
		Date morgen = Adapter.getNextDay(vandaag);

		bedrijf.getOpeningsTijden(vandaag);
		Dag dag = bedrijf.getDagen().get(0);

		JsonArrayBuilder jab = Json.createArrayBuilder();
		JsonObjectBuilder job0 = Json.createObjectBuilder();
		job0.add("openingsTijd", Adapter.DateToString(dag.getOpeningsTijd(), "HH:mm"));
		job0.add("sluitingstijd", Adapter.DateToString(dag.getSluitingsTijd(), "HH:mm"));
		jab.add(job0);

		dag.getAfsprakenBetweenDates(vandaag, morgen);

		for (Afspraak afspraak : dag.getAfspraken()) {
			int minuten = 0;
			int uur = 0;

			for (Behandeling behandeling : afspraak.getBehandelingen()) {
				Date lengte = behandeling.getLengte();
				String lengteString = Adapter.DateToString(lengte, "HH:mm");
				int behandelingUrenInt = Integer.parseInt(lengteString.substring(0, 2));
				int behandelingMinutenInt = Integer.parseInt(lengteString.substring(3, 5));

				uur = uur + behandelingUrenInt;
				minuten = minuten + behandelingMinutenInt;
				if (minuten > 60) {
					uur++;
					minuten = minuten - 60;
				}
			}
			// begin tijd bekerekenen
			String timestampString = Adapter.DateToString(afspraak.getTimeStamp(), "yyyy-MM-dd HH:mm");

			String beginTijd = Integer.parseInt(timestampString.substring(11, 13)) + ":"
					+ Integer.parseInt(timestampString.substring(14, 16));
			String lengte = uur + ":" + minuten;

			JsonObjectBuilder job1 = Json.createObjectBuilder();
			job1.add("beginTijd", beginTijd);
			job1.add("lengte", lengte);
			jab.add(job1);
		}
		return jab.build().toString();
	}

	@POST
	@Path("/afspraak/{data}")
	@Produces("application/json")
	public Response setAfspraak(@PathParam("data") String data, @FormParam("klantVoornaam") String klantVoornaam,
			@FormParam("klantAchternaam") String klantAchternaam, @FormParam("klantWoonplaats") String klantWoonplaats,
			@FormParam("klantPostcode") String klantPostcode, @FormParam("klantAdres") String klantAdres,
			@FormParam("afspraakTijd") String afspraakTijd, @FormParam("klantEmail") String afspraakKlantEmail,
			@FormParam("klantTelefoon") String afspraakKlantTel) {
		Bedrijf bedrijf = getBedrijf(data);
		String geslacht = getGeslacht(data);
		String afspraakKlantNaam = klantVoornaam + " " + klantAchternaam;
		String klantPlek = klantWoonplaats + " " + klantPostcode + "" + klantAdres;

		Klant klant = new Klant(afspraakKlantNaam, afspraakKlantEmail, afspraakKlantTel, geslacht, klantPlek, bedrijf);
		klant.saveOrFindAndGetId();

		Date timestamp = getTimestamp(data, afspraakTijd);
		Afspraak afspraak = new Afspraak(timestamp, bedrijf, klant);
		afspraak.save();
		afspraak.retrieveId();

		ArrayList<Behandeling> behandelingen = getBehandelingen(data);
		
		afspraak.setBehandelingen(behandelingen);
		for (Behandeling behandeling : behandelingen) {
			behandeling.saveAfspraakBehandeling(afspraak);
		}
		return Response.ok().build();
	}
//	private void stahsh() {
//		System.out.println("validaties doorgaan");
//		String error = Validator.validateAfspraak(afspraak);
//		if (error != null) {
//			System.out.println("error " + error);
//			SimpleEntry<String, String> errorMsg = new SimpleEntry<String, String>("errorMsg", error);
//
////			return Response.status(409).build();
//		SimpleEntry<String, String> JWT = new SimpleEntry<String, String>("JWT", token);
	// return Response.ok(JWT).build();
//		}
//		System.out.println("alle validaties doorgegaan");
	// }

}