package com.swinkels.emperio.service;

import java.util.ArrayList;
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
import com.swinkels.emperio.objects.BedrijfsInstellingen;
import com.swinkels.emperio.objects.Behandeling;
import com.swinkels.emperio.objects.Dag;
import com.swinkels.emperio.support.Validator;

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
		if(Validator.validateLengte(lengte)) {
			return Response.status(501).entity("Some message here").build();
		}
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
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		List<Behandeling> behandelingen = bedrijf.getBehandelingen();
		JsonArrayBuilder jab = Json.createArrayBuilder();

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
	
	@POST
	@RolesAllowed("setup")
	@Path("/afspraakSettings")
	@Produces("application/json")
	public Response afspraakSettings(@Context SecurityContext sc, 
			@FormParam("kleurKlasse1") String kleurKlasse1,
			@FormParam("tot1") double maximumPrijsVanKlasse1,
			@FormParam("kleurKlasse2") String kleurKlasse2,
			@FormParam("tot2") double maximumPrijsVanKlasse2, 
			@FormParam("kleurKlasse3") String kleurKlasse3,
			
			@FormParam("telefoonKlant") boolean telefoonKlant,
			@FormParam("emailKlant") boolean emailKlant,
			@FormParam("adresKlant") boolean adresKlant,
			
			@FormParam("emailBedrijfInput") String emailBedrijfInput,
			@FormParam("telefoonBedrijfInput") String telefoonBedrijfInput,
			@FormParam("adresBedrijfInput") String adresBedrijfInput) {
		BedrijfsInstellingen bedrijfInstellingen = new BedrijfsInstellingen(telefoonBedrijfInput,emailBedrijfInput,adresBedrijfInput,emailKlant,telefoonKlant, adresKlant, kleurKlasse1, maximumPrijsVanKlasse1, kleurKlasse2, maximumPrijsVanKlasse2, kleurKlasse3);
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		bedrijfInstellingen.setBedrijf(bedrijf);
		bedrijfInstellingen.saveBedrijfInstellingen();
		return Response.ok().build();
	}
	
	
	@POST
	@RolesAllowed("setup")
	@Path("/dagen")
	@Produces("application/json")
	public Response dagen(@Context SecurityContext sc, 
			@FormParam("openingsTijdMaandag") String openingsTijdMaandag,
			@FormParam("sluitingsTijdMaandag") String sluitingsTijdMaandag,
			
			@FormParam("openingsTijdDinsdag") String openingsTijdDinsdag,
			@FormParam("sluitingsTijdDinsdag") String sluitingsTijdDinsdag,
			
			@FormParam("openingsTijdWoensdag") String openingsTijdWoensdag,
			@FormParam("sluitingsTijdWoensdag") String sluitingsTijdWoensdag,
			
			@FormParam("openingsTijdDonderdag") String openingsTijdDonderdag,
			@FormParam("sluitingsTijdDonderdag") String sluitingsTijdDonderdag,
			
			@FormParam("openingsTijdVrijdag") String openingsTijdVrijdag,
			@FormParam("sluitingsTijdVrijdag") String sluitingsTijdVrijdag,
			
			@FormParam("openingsTijdZaterdag") String openingsTijdZaterdag,
			@FormParam("sluitingsTijdZaterdag") String sluitingsTijdZaterdag,
			
			@FormParam("openingsTijdZondag") String openingsTijdZondag,
			@FormParam("sluitingsTijdZondag") String sluitingsTijdZondag) {
		System.out.println("=============dagen===================");
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		ArrayList<Dag> dagen = new ArrayList<Dag>();
		Dag maandag = new Dag(bedrijf, 0, openingsTijdMaandag, sluitingsTijdMaandag);
		Dag dinsdag = new Dag(bedrijf, 1, openingsTijdDinsdag, sluitingsTijdDinsdag);
		Dag woensdag = new Dag(bedrijf, 2, openingsTijdWoensdag, sluitingsTijdWoensdag);
		Dag donderdag = new Dag(bedrijf, 3, openingsTijdDonderdag, sluitingsTijdDonderdag);
		Dag vrijdag = new Dag(bedrijf, 4, openingsTijdVrijdag, sluitingsTijdVrijdag);
		Dag zaterdag = new Dag(bedrijf, 5, openingsTijdZaterdag, sluitingsTijdZaterdag);
		Dag zondag = new Dag(bedrijf, 6, openingsTijdZondag, sluitingsTijdZondag);
		dagen.add(maandag);
		dagen.add(dinsdag);
		dagen.add(woensdag);
		dagen.add(donderdag);
		dagen.add(vrijdag);
		dagen.add(zaterdag);
		dagen.add(zondag);
		for(Dag dag : dagen) {
			if(dag.validateTijden()) {
				return Response.status(409).build();
			}
		}
		bedrijf.setDagen(dagen);
		
		bedrijf.saveBehandelingen(); 
		return Response.ok().build();
	}	
}