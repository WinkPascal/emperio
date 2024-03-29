package com.swinkels.emperio.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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

import com.swinkels.emperio.objects.behandeling.Behandeling;
import com.swinkels.emperio.objects.instellingen.InstellingenFacade;
import com.swinkels.emperio.objects.instellingen.InstellingenFacadeInterface;
import com.swinkels.emperio.objects.klant.Klant;
import com.swinkels.emperio.objects.klant.KlantBuilder;
import com.swinkels.emperio.objects.rooster.Afspraak;
import com.swinkels.emperio.objects.rooster.Dag;
import com.swinkels.emperio.objects.security.Bedrijf;
import com.swinkels.emperio.objects.security.Security;
import com.swinkels.emperio.support.Adapter;
import com.swinkels.emperio.support.JavascriptDateAdapter;

@Path("/klantenPlanProvider")
public class klantenPlanProvider {
	
	@GET
	@Path("/getBedrijfDataStart/{data}")
	@Produces("application/json")
	public String getBedrijfDataStart(@PathParam("data") String data) {
		Bedrijf bedrijf = getBedrijf(data);
		Security.setKey(bedrijf.getBedrijfsNaam());
		InstellingenFacadeInterface instelingen = new InstellingenFacade();
		HashMap<String, String> instellingenDto = instelingen.getInplanSettingsDTO();

		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("bedrijfsNaam", bedrijf.getBedrijfsNaam());
		job.add("invoerveldEmail", instellingenDto.get("emailKlantInvoer"));
		job.add("invoerveldTelefoon", instellingenDto.get("telefoonKlantInvoer"));
		job.add("invoerveldAdres", instellingenDto.get("adresKlantInvoer"));


		job.add("bedrijfEmail", instellingenDto.get("bedrijfsEmail"));
		job.add("bedrijfsTelefoon", instellingenDto.get("bedrijfsTelefoon"));
		job.add("bedrijfsAdres", instellingenDto.get("bedrijfsAdres"));

		return job.build().toString();
	}

	@GET
	@Path("/getBehandelingenByGeslacht/{data}")
	@Produces("application/json")
	public String getBehandelingenByGeslacht(@PathParam("data") String data) {
		Bedrijf bedrijf = getBedrijf(data);
		bedrijf.retrieveBehandelingenByGeslacht(getGeslacht(data));

		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Behandeling behandeling : bedrijf.getBehandelingen()) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("id", behandeling.getId());
			job.add("naam", behandeling.getNaam());
			job.add("beschrijving", behandeling.getBeschrijving());
			job.add("lengte", JavascriptDateAdapter.DateToString(behandeling.getLengte(), "HH:mm"));
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
			Date openingsTijd = dag.getOpeningsTijd();
			if(openingsTijd != null) {
				JsonObjectBuilder job = Json.createObjectBuilder();
				job.add("dagNummmer", JavascriptDateAdapter.dagNummer(dag.getDag()));
				job.add("sluitingstijd", JavascriptDateAdapter.DateToString(dag.getSluitingsTijd(), "HH:mm"));
				job.add("opeingstijd",  JavascriptDateAdapter.DateToString(openingsTijd, "HH:mm"));
				jab.add(job);
			} 
		}
		return jab.build().toString();
	}

	@GET
	@Path("/tijdslotenOphalen/{data}")
	@Produces("application/json")
	public String tijdslotenOphalen(@PathParam("data") String data) {
		Bedrijf bedrijf = getBedrijf(data);
		Date vandaag = getDatum(data);
		bedrijf.getOpeningsTijden(vandaag);
		Dag dag = bedrijf.getDagen().get(0);

		JsonArrayBuilder jab = Json.createArrayBuilder();
		JsonObjectBuilder job0 = Json.createObjectBuilder();
		job0.add("openingsTijd", JavascriptDateAdapter.DateToString(dag.getOpeningsTijd(), "HH:mm"));
		job0.add("sluitingstijd", JavascriptDateAdapter.DateToString(dag.getSluitingsTijd(), "HH:mm"));
		jab.add(job0);

		dag.getAfsprakenBetweenDates(vandaag, Adapter.getNextDay(vandaag));

		for (Afspraak afspraak : dag.getAfspraken()) {
			int minuten = 0;
			int uur = 0;

			for (Behandeling behandeling : afspraak.getBehandelingen()) {
				Date lengte = behandeling.getLengte();
				String lengteString = JavascriptDateAdapter.DateToString(lengte, "HH:mm");
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
			HashMap<String, String> afspraakDto = afspraak.toDTO();
			String timestampString = afspraakDto.get("timestamp");;
			String beginTijd = Integer.parseInt(timestampString.substring(11, 13)) + ":"
					+ Integer.parseInt(timestampString.substring(14, 16));

			JsonObjectBuilder job1 = Json.createObjectBuilder();
			job1.add("beginTijd", beginTijd);
			job1.add("lengte", uur + ":" + minuten);
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

				

		Afspraak afspraak = new Afspraak(0, getTimestamp(data, afspraakTijd), 0.0);
		afspraak.setKlant(
				new KlantBuilder()
				.setNaam(klantVoornaam + " " + klantAchternaam)
				.setEmail(afspraakKlantEmail)
				.setTel(afspraakKlantTel)
				.setGeslacht(getGeslacht(data))
				.setAdres(klantWoonplaats + " " + klantPostcode + "" + klantAdres)
				.build()
		);
		afspraak.setBehandelingen(getBehandelingen(data));

		afspraak.save();
		return Response.ok().build();
	}
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
				
				System.out.println("binnen ");
				System.out.println(datum);
				Date beginDate = JavascriptDateAdapter.StringToDate(datum, "yyyy-MM-dd");
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
				return JavascriptDateAdapter.StringToDate(timestampString, "yyyy-MM-dd HH:mm");
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
}