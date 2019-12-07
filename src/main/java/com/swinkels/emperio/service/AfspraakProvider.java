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
import com.swinkels.emperio.objects.DagBuilder;
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
import com.swinkels.emperio.support.JavascriptDateAdapter;

@Path("/afspraak")
public class AfspraakProvider {

	@GET
	@Path("/afsprakenByDate/{date}")
	@Produces("application/json")
	public String afsprakenByDate(@Context SecurityContext sc, @PathParam("date") String datum) throws ParseException {
		Date beginDate = JavascriptDateAdapter.StringToDate(datum, "yyyy-MM-dd");
		Date eindDate = Adapter.getNextDay(beginDate);
		
		Dag dag = new DagBuilder().setBedrijf(new Bedrijf(sc.getUserPrincipal().getName())).make();
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
			job.add("id", afspraak.getId());
			job.add("timestamp", JavascriptDateAdapter.DateToString(afspraak.getTimeStamp(), "yyyy-MM-dd HH:mm"));
			job.add("klantNaam", afspraak.getKlant().getNaam());
			job.add("lengte", uren + ":" + minuten);
			job.add("prijs", prijs);
			jab.add(job);
		}
		return jab.build().toString();
	}

	// wordt gebruikt bij
	// rooster vullen bij afspraken pagina
	@GET
	@Path("/getWeekRooster")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getWeekRooster(@Context SecurityContext sc) {
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		bedrijf.retrieveDagen();
		System.out.println("dagem");
		System.out.println(bedrijf.getDagen().size());
		JsonArrayBuilder jab = Json.createArrayBuilder();
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("vroegsteOpeningsTijd", JavascriptDateAdapter.DateToString(bedrijf.getVroegsteOpeningsTijd(), "HH:mm"));
		job.add("laatsteSluitingsTijd", JavascriptDateAdapter.DateToString(bedrijf.getLaatsteSluitingsTijd(), "HH:mm"));
		jab.add(job);
		for (Dag dag : bedrijf.getDagen()) {
			System.out.println("dag");
			System.out.println(dag.getDag());
			JsonObjectBuilder job1 = Json.createObjectBuilder();
			job1.add("weekNummer", JavascriptDateAdapter.dagNummer(dag.getDag()));
			if(dag.getOpeningsTijd() != null) {
				job1.add("openingsTijd", JavascriptDateAdapter.DateToString(dag.getOpeningsTijd(), "HH:mm"));
				job1.add("sluitingsTijd", JavascriptDateAdapter.DateToString(dag.getSluitingsTijd(), "HH:mm"));
			} else {
				job1.add("openingsTijd", "gesloten");
				job1.add("sluitingsTijd", "gesloten");
			}
			jab.add(job1);
		}
		return jab.build().toString();
	}

	@GET
	@Path("/getWeekAfspraken/{date}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getWeekAfspraken(@Context SecurityContext sc, @PathParam("date") String datum) {
		Date maandag = JavascriptDateAdapter.StringToDate(datum, "yyyy-MM-dd");
		Date zondag = Adapter.getNextWeek(maandag);

		Dag dag = new DagBuilder()
				.setBedrijf(new Bedrijf(sc.getUserPrincipal().getName()))
				.make();
		dag.getAfsprakenBetweenDates(maandag, zondag);
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Afspraak afspraak : dag.getAfspraken()) {
			System.out.println(afspraak.getId());
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("id", afspraak.getId());
			job.add("timestamp", JavascriptDateAdapter.DateToString(afspraak.getTimeStamp(), "yyyy-MM-dd HH:mm"));
			job.add("klant", afspraak.getKlant().getNaam());
			JsonArrayBuilder jab1 = Json.createArrayBuilder();
			for (Behandeling behandeling : afspraak.getBehandelingen()) {
				JsonObjectBuilder job1 = Json.createObjectBuilder();
				job1.add("naam", behandeling.getNaam());
				job1.add("lengte", JavascriptDateAdapter.DateToString(behandeling.getLengte(), "HH:mm"));
				job1.add("prijs", behandeling.getPrijs());
				jab1.add(job1);
			}
			job.add("behandelingen", jab1);
			jab.add(job);
		}
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