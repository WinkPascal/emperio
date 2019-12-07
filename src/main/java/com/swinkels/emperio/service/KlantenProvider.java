package com.swinkels.emperio.service;

import java.util.ArrayList;

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

import com.swinkels.emperio.objects.Afspraak;
import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Klant;
import com.swinkels.emperio.objects.KlantBuilder;
import com.swinkels.emperio.providers.AfspraakBehandelingDao;
import com.swinkels.emperio.providers.AfspraakBehandelingDaoImpl;
import com.swinkels.emperio.providers.AfspraakDao;
import com.swinkels.emperio.providers.AfspraakDaoImpl;
import com.swinkels.emperio.providers.BehandelingDao;
import com.swinkels.emperio.providers.BehandelingDaoImpl;
import com.swinkels.emperio.providers.KlantDao;
import com.swinkels.emperio.providers.KlantDaoImpl;
import com.swinkels.emperio.support.Adapter;

@Path("/klanten")
public class KlantenProvider {
	@GET
	@Path("/alleKlanten/{pageNummer}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getKlanten(@Context SecurityContext sc, @PathParam("pageNummer") int pageNummer) {
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		ArrayList<Klant> klanten = bedrijf.getKlantenWithByPage(pageNummer);
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Klant klant : klanten) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("id", klant.getId());
			job.add("naam", klant.getNaam());
			job.add("adres", klant.getAdres());
			job.add("email", klant.getEmail());
			job.add("telefoon", klant.getTel());
			job.add("geslacht", klant.getGeslacht());
			jab.add(job);
		}
		return jab.build().toString();
	}

	// wordt gebruikt bij
	// klanten pagina zoeken klant
	@GET
	@Path("/klantenZoekReq/{request}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getKlantenZoekRequest(@Context SecurityContext sc, @PathParam("request") String request) {
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		bedrijf.zoekKlant(request);
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Klant klant : bedrijf.getKlanten()) {
			System.out.println(klant.getId());
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("id", klant.getId());
			job.add("naam", klant.getNaam());
			job.add("email", klant.getEmail());
			job.add("telefoon", klant.getTel());
			job.add("geslacht", klant.getGeslacht());
			jab.add(job);
		}
		return jab.build().toString();
	}

	@GET
	@Path("/klant/{id}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getKlant(@Context SecurityContext sc, @PathParam("id") int id) {
		Klant klant = new KlantBuilder().setId(id).make();
		klant.setBedrijf(new Bedrijf(sc.getUserPrincipal().getName()));
		klant.getInfo();

		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("naam", klant.getNaam());
		job.add("geslacht", klant.getGeslacht());
		job.add("email", klant.getEmail());		
		job.add("telefoon", klant.getTel());
		job.add("afspraken", klant.getAantalAfspraken());
		job.add("inkomsten", klant.getHoeveelheidInkomsten());

		JsonArrayBuilder jab1 = Json.createArrayBuilder();
		for (Afspraak afspraak : klant.getAfspraken()) {
			JsonObjectBuilder job1 = Json.createObjectBuilder();
			job1.add("prijs", afspraak.getPrijs());
			String timestampString = Adapter.DateToString(afspraak.getTimeStamp(), "YYYY-MM-dd HH:mm");
			job1.add("datum", timestampString.substring(0, 10));
			job1.add("tijd", timestampString.substring(11));
			jab1.add(job1);
		}
		job.add("afspraken", jab1);

		return job.build().toString();
	}
}
