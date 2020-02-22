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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.swinkels.emperio.objects.email.Email;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;

import com.swinkels.emperio.objects.klant.Klant;
import com.swinkels.emperio.objects.klant.KlantBuilder;
import com.swinkels.emperio.objects.security.Bedrijf;
import com.swinkels.emperio.objects.security.Security;
import com.swinkels.emperio.support.JavascriptDateAdapter;

@Path("/marketing")
public class MarketingProvider {
	
	@POST
	@RolesAllowed("user")
	@Path("/email")
	@Produces("application/json")
	public Response setEmail(@Context SecurityContext sc,
			@FormParam("inhoud") String inhoud,
			@FormParam("onderwerp") String inhoudonderwerp,
			@FormParam("klanten") String klanten) {
		Security.setKey(sc.getUserPrincipal().getName());

		ArrayList<Klant> klantenList = new ArrayList<Klant>();
		if(klanten.length() > 0) {
			for(String klant : klanten.split(",")) {
				Klant k = new KlantBuilder()
						.setId(Integer.parseInt(klant))
						.build();
				klantenList.add(k);
			}
		}
		Email email = new Email(inhoudonderwerp, inhoud, klantenList);
		email.setBedrijf(new Bedrijf(sc.getUserPrincipal().getName()));
		email.send();
		return Response.ok().build();		
	}
	
	@GET
	@Path("/email/{id}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getEmails(@Context SecurityContext sc, 
			@PathParam("id") int id) {
		Security.setKey(sc.getUserPrincipal().getName());
		
		Email email = new Email();
		email.setId(id);
		email.getEmail();
		
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("id", email.getId());
		job.add("verzendtijd", JavascriptDateAdapter.DateToString(email.getVerzendtijd(), "yyyy-MM-dd HH:mm"));
		job.add("aantalKlanten", email.getAantalKlanten());
		job.add("onderwerp", email.getOnderwerp());
		job.add("inhoud", email.getInhoud());
		
		return job.build().toString();
	}
	
	@GET
	@Path("/emails/{hoeveelheid}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getKlanten(@Context SecurityContext sc, 
			@PathParam("hoeveelheid") int hoeveelheid) {
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());

		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Email email : bedrijf.getEmailsByRequest(hoeveelheid)) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("id", email.getId());
			job.add("verzendtijd", JavascriptDateAdapter.DateToString(email.getVerzendtijd(), "yyyy-MM-dd HH:mm"));
			job.add("aantalKlanten", email.getAantalKlanten());
			job.add("onderwerp", email.getOnderwerp());
			job.add("inhoud", email.getInhoud());
			jab.add(job);
		}
		return jab.build().toString();
	}
	

}
