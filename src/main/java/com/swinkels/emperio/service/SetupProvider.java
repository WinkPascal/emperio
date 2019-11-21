package com.swinkels.emperio.service;

import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.json.JSONArray;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Behandeling;

@Path("/setup")
public class SetupProvider {
	@POST
	@Path("/registreer")
	@Produces("application/json")
	public Response registreegAccount(@Context SecurityContext sc, @FormParam("Bedrijfsnaam") String Bedrijfsnaam,
			@FormParam("Voornaam") String Voornaam, @FormParam("Achternaam") String Achternaam,
			@FormParam("Email") String Email, @FormParam("Telefoon") String Telefoon, @FormParam("Adres") String Adres,
			@FormParam("Wachtwoord") String Wachtwoord) {
		System.out.println("setup registreer");
		Bedrijf bedrijf = new Bedrijf(Bedrijfsnaam, Voornaam + " " + Achternaam, Email, Telefoon, Adres, Wachtwoord);
		bedrijf.saveBedrijf();

		return Response.ok().build();
	}

	@POST
	@RolesAllowed("setup")
	@Path("/behandeling")
	@Produces("application/json")
	public Response setBehandeling(@Context SecurityContext sc, @FormParam("naam") String naam,
			@FormParam("beschrijving") String beschrijving, @FormParam("prijs") double prijs,
			@FormParam("uur") String uur, @FormParam("minuten") String minuten,
			@FormParam("geslachten") String geslachten) {
		String bedrijfsNaam = sc.getUserPrincipal().getName();
		Bedrijf bedrijf = new Bedrijf(bedrijfsNaam);
		String lengteString = uur + ":" + minuten;
		Date lengte = ServiceFilter.StringToDateFormatter(lengteString, "HH:mm");

		JSONArray jsonArray = new JSONArray(geslachten);

		for (int i = 0; i < jsonArray.length(); i++) {
			System.out.println(jsonArray.get(i).toString());
			Behandeling behandeling = new Behandeling(bedrijf, naam, beschrijving, prijs, lengte,
					jsonArray.get(i).toString());
			behandeling.save();
		}
		return Response.ok().build();
	}

	@GET
	@RolesAllowed("setup")
	@Path("/getbehandelingen")
	@Produces("application/json")
	public String getBehandelingen(@Context SecurityContext sc) {
		System.out.println("2");

		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		List<Behandeling> behandelingen = bedrijf.getBehandelingen();
		JsonArrayBuilder jab = Json.createArrayBuilder();
		System.out.println("sssssss2314521");

		for(Behandeling behandeling : behandelingen) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("id", behandeling.getId());
			job.add("naam", behandeling.getNaam());
			job.add("beschrijving", behandeling.getBeschrijving());
			job.add("prijs", behandeling.getPrijs());
			job.add("geslacht", behandeling.getGeslacht());
			job.add("lengte", ServiceFilter.DateToStringFormatter(behandeling.getLengte(), "HH:mm"));
			jab.add(job);
		}
		return jab.build().toString();
	}
}
