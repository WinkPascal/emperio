package com.swinkels.emperio.service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

import com.swinkels.emperio.objects.behandeling.Behandeling;
import com.swinkels.emperio.objects.klant.Klant;
import com.swinkels.emperio.objects.rooster.*;
import com.swinkels.emperio.objects.security.Bedrijf;
import com.swinkels.emperio.objects.security.Security;
import com.swinkels.emperio.support.Adapter;
import com.swinkels.emperio.support.JavascriptDateAdapter;

@Path("/afspraak")
public class AfspraakProvider {

	@GET
	@Path("/afsprakenByDate/{date}")
	@Produces("application/json")
	public String afsprakenByDate(@Context SecurityContext sc, @PathParam("date") String datum) throws ParseException {
		Date beginDate = JavascriptDateAdapter.StringToDate(datum, "yyyy-MM-dd");
		Date eindDate = Adapter.getNextDay(beginDate);

		DagBuilderInterface dagBuilder = new DagBuilder();
		Security.setKey(sc.getUserPrincipal().getName());
		Dag dag = dagBuilder.build();
		dag.getAfsprakenBetweenDates(beginDate, eindDate);

		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Afspraak afspraak : dag.getAfspraken()) {
			double prijs = 0;
			int uren = 0;
			int minuten = 0;
			for (Behandeling behandeling : afspraak.getBehandelingen()) {
				String[] lengteArray = JavascriptDateAdapter.DateToString(behandeling.getLengte(), "HH:mm").split(":");

				uren = uren + Integer.parseInt(lengteArray[0]);
				minuten = minuten + Integer.parseInt(lengteArray[1]);
				prijs = prijs + behandeling.getPrijs();
			}
			JsonObjectBuilder job = Json.createObjectBuilder();
			HashMap<String, String> afspraakDto = afspraak.toDTO();
			HashMap<String, String> klantDto = afspraak.getKlant().toDTO();
			job.add("id", afspraakDto.get("id"));
			job.add("timestamp", afspraakDto.get("timestamp"));
			job.add("klantNaam", klantDto.get("naam"));
			job.add("lengte", uren + ":" + minuten);
			job.add("prijs", prijs);
			jab.add(job);
		}
		return jab.build().toString();
	}

	// wordt gebruikt bij
	// rooster vullen bij afspraken pagina
	@GET
	@Path("/getWeekRooster/{date}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getWeekRooster(@Context SecurityContext sc, @PathParam("date") String datum) {
		Security.setKey(sc.getUserPrincipal().getName());
		DagManager manager = new DagManager(datum);
		JsonObjectBuilder job = Json.createObjectBuilder();
		JsonArrayBuilder dagen = Json.createArrayBuilder();
		for (Dag dag : manager.getWeekRooster()) {
			JsonObjectBuilder dagJob = Json.createObjectBuilder();
			dagJob.add("weekNummer", JavascriptDateAdapter.dagNummer(dag.getDag()));
			if(dag.getOpeningsTijd() != null) {
				dagJob.add("openingsTijd", JavascriptDateAdapter.DateToString(dag.getOpeningsTijd(), "HH:mm"));
				dagJob.add("sluitingsTijd", JavascriptDateAdapter.DateToString(dag.getSluitingsTijd(), "HH:mm"));
			} else {
				dagJob.add("openingsTijd", "gesloten");
				dagJob.add("sluitingsTijd", "gesloten");
			}
			dagen.add(dagJob);
		}
		job.add("dagTijden", dagen);

		HashMap<String, String> dagManagerDto = manager.toDTO();
		JsonObjectBuilder uitersteTijden = Json.createObjectBuilder();
		uitersteTijden.add("vroegsteOpeningsTijd", dagManagerDto.get("openingsTijd"));
		uitersteTijden.add("laatsteSluitingsTijd", dagManagerDto.get("sluitingsTijd"));
		job.add("uitersteTijden", uitersteTijden);
		return job.build().toString();
	}

//	@GET
//	@Path("/getWeekAfspraken/{date}")
//	@RolesAllowed("user")
//	@Produces("application/json")
//	public String getWeekAfspraken(@Context SecurityContext sc, @PathParam("date") String datum) {
//
//		Date maandag = JavascriptDateAdapter.StringToDate(datum, "yyyy-MM-dd");
//		Date zondag = Adapter.getNextWeek(maandag);
//
//		DagBuilderInterface dagBuilder = new DagBuilder();
//		Security.setKey(sc.getUserPrincipal().getName());
//		Dag dag = dagBuilder.build();
//		dag.getAfsprakenBetweenDates(maandag, zondag);
//
//		JsonArrayBuilder jab = Json.createArrayBuilder();
//		for (Afspraak afspraak : dag.getAfspraken()) {
//			System.out.println(afspraak.getId());
//			JsonObjectBuilder job = Json.createObjectBuilder();
//			job.add("id", afspraak.getId());
//			job.add("timestamp", JavascriptDateAdapter.DateToString(afspraak.getTimeStamp(), "yyyy-MM-dd HH:mm"));
//			job.add("klant", afspraak.getKlant().getNaam());
//			JsonArrayBuilder jab1 = Json.createArrayBuilder();
//			for (Behandeling behandeling : afspraak.getBehandelingen()) {
//				JsonObjectBuilder job1 = Json.createObjectBuilder();
//				job1.add("naam", behandeling.getNaam());
//				job1.add("lengte", JavascriptDateAdapter.DateToString(behandeling.getLengte(), "HH:mm"));
//				job1.add("prijs", behandeling.getPrijs());
//				jab1.add(job1);
//			}
//			job.add("behandelingen", jab1);
//			jab.add(job);
//		}
//		return jab.build().toString();
//	};


	@GET
	@RolesAllowed("user")
	@Path("/getAfspraak/{id}")
	@Produces("application/json")
	public String getAfspraak(@Context SecurityContext sc, @PathParam("id") int afspraakId) {
		JsonObjectBuilder job = Json.createObjectBuilder();


		AfspraakInterface afspraak = new Afspraak(afspraakId, null, null);
		afspraak.getAfspraakInfoFromDatabase();
		Klant klant = afspraak.getKlant();
		HashMap<String, String> klantDto = klant.toDTO();
		job.add("klantId", klantDto.get("id"));
		job.add("klantNaam", klantDto.get("naam"));
		job.add("klantEmail", klantDto.get("email"));
		job.add("klantAdres", klantDto.get("adres"));
		job.add("klantGeslacht", klantDto.get("geslacht"));
		job.add("klantTelefoon", klantDto.get("telefoonnummer"));

		JsonArrayBuilder jab1 = Json.createArrayBuilder();
		double totaalPrijs = 0;
		for (Behandeling behandeling : afspraak.getBehandelingen()) {
			JsonObjectBuilder job1 = Json.createObjectBuilder();
			job1.add("id", behandeling.getId());
			job1.add("naam", behandeling.getNaam());
			job1.add("lengte", JavascriptDateAdapter.DateToString(behandeling.getLengte(), "HH:mm"));
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
		Afspraak afspraak = new Afspraak(afspraakId, null, null);
		if(afspraak.delete()) {
			return Response.ok().build();			
		} else {
			return Response.serverError().build();
		}
	}	
}