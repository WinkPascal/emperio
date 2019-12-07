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

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Email;
import com.swinkels.emperio.objects.Klant;

public class MarketingProvider {
	@GET
	@Path("/email/{request}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getEmails(@Context SecurityContext sc, @PathParam("request") String request) {
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		int page = 1;
		bedrijf.getEmailsByRequest(request, page);
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Email email : bedrijf.getEmails()) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("id", email.getId());
			job.add("aantalKlanten", email.getTel());
			job.add("onderwerp", email.getNaam());
			job.add("tekst", email.getAdres());
			jab.add(job);
		}
		return jab.build().toString();
	}
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
}
