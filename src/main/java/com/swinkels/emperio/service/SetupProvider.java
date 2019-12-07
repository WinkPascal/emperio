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
import com.swinkels.emperio.objects.Instellingen;
import com.swinkels.emperio.objects.Behandeling;
import com.swinkels.emperio.objects.ContactPersoon;
import com.swinkels.emperio.objects.Dag;
import com.swinkels.emperio.support.Adapter;
import com.swinkels.emperio.support.JavascriptDateAdapter;
import com.swinkels.emperio.support.Validator;

@Path("/setup")
public class SetupProvider {
	@POST
	@Path("/registreer")
	@Produces("application/json")
	public Response registreegAccount(
			//persoonlijk
			@FormParam("Voornaam") String Voornaam, 
			@FormParam("Achternaam") String Achternaam,
			@FormParam("Rekeningnummer") String Rekeningnummer, 
			@FormParam("PersoonlijkEmail") String PersoonlijkEmail, 
			@FormParam("PersoonlijkTelefoon") String PersoonlijkTelefoon,
			//bedrijf
			@FormParam("Bedrijfsnaam") String Bedrijfsnaam, 
			@FormParam("Wachtwoord") String Wachtwoord, 
			@FormParam("BedrijfsEmail") String BedrijfsEmail,
			@FormParam("BedrijfsTelefoon") String BedrijfsTelefoon,
			@FormParam("Woonplaats") String Woonplaats,
			@FormParam("Postcode") String Postcode,
			@FormParam("Adres") String Adres) {		
		Bedrijf bedrijf = new Bedrijf(Bedrijfsnaam, Wachtwoord, BedrijfsEmail, BedrijfsTelefoon, Adres, Woonplaats, Postcode);
		bedrijf.save();
		Instellingen instellingen = new Instellingen(bedrijf, true, true, true, "#52b852", 20.00, "#c78d1e", 30.00, "#d63838", true, true, true);
		instellingen.save();
		ContactPersoon contactPersoon = new ContactPersoon(bedrijf, Voornaam, Achternaam, Rekeningnummer, PersoonlijkTelefoon, PersoonlijkEmail);
		contactPersoon.save();
		
		return Response.ok().build();
	}

	@GET
	@RolesAllowed("setup")
	@Path("/getbehandelingen")
	@Produces("application/json")
	public String getBehandelingen(@Context SecurityContext sc) {
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		bedrijf.retrieveBehandelingen();
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for(Behandeling behandeling : bedrijf.getBehandelingen()) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("id", behandeling.getId());
			job.add("naam", behandeling.getNaam());
			job.add("beschrijving", behandeling.getBeschrijving());
			job.add("prijs", behandeling.getPrijs());
			job.add("geslacht", behandeling.getGeslacht());
			job.add("lengte", JavascriptDateAdapter.DateToString(behandeling.getLengte(), "HH:mm"));
			jab.add(job);
		}
		return jab.build().toString();
	}
	
	@POST
	@RolesAllowed("setup")
	@Path("/behandeling")
	@Produces("application/json")
	public Response setBehandeling(@Context SecurityContext sc, @FormParam("naam") String naam,
			@FormParam("beschrijving") String beschrijving, @FormParam("prijsBehandeling") double prijs,
			@FormParam("uur") String uur, @FormParam("minuten") String minuten,
			@FormParam("geslachten") String geslachten) {		
		String bedrijfsNaam = sc.getUserPrincipal().getName();
		Bedrijf bedrijf = new Bedrijf(bedrijfsNaam);
		Date lengte = JavascriptDateAdapter.StringToDate(uur + ":" + minuten, "HH:mm");
		
		JSONArray jsonArray = new JSONArray(geslachten);
		for (int i = 0; i < jsonArray.length(); i++) {
			Behandeling behandeling = new Behandeling(bedrijf, naam, beschrijving, prijs, lengte,
					jsonArray.get(i).toString());
			behandeling.save();
		}
		return Response.ok().build();
	}

	@GET
	@RolesAllowed("setup")
	@Path("/getInstellingen")
	@Produces("application/json")
	public String getInstellingen(@Context SecurityContext sc) {
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		Instellingen instellingen = new Instellingen(bedrijf);
		instellingen.retrieveInstellingen();
		
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("kleurKlasse1", instellingen.getKleurKlasse1());
		job.add("maximumPrijsVanKlasse1", instellingen.getMaximumPrijsVanKlasse1());
		job.add("kleurKlasse2", instellingen.getKleurKlasse2());
		job.add("maximumPrijsVanKlasse2", instellingen.getMaximumPrijsVanKlasse2());
		job.add("kleurKlasse3", instellingen.getKleurKlasse3());
		
		job.add("emailKlantInvoer", instellingen.isEmailKlantInvoer());
		job.add("telefoonKlantInvoer", instellingen.isTelefoonKlantInvoer());
		job.add("adresKlantInvoer", instellingen.isAdresKlantInvoer());

		job.add("bedrijfsEmail", instellingen.isBedrijfsEmail());
		job.add("bedrijfsTelefoon", instellingen.isBedrijfsTelefoon());
		job.add("bedrijfsAdres", instellingen.isBedrijfsAdres());
		return job.build().toString();
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
			
			@FormParam("bedrijfsEmail") boolean bedrijfsEmail,
			@FormParam("bedrijfsTelefoon") boolean bedrijfsTelefoon,
			@FormParam("bedrijfsAdres") boolean bedrijfsAdres) {
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		
		Instellingen bedrijfInstellingen = new Instellingen(bedrijf,emailKlant,telefoonKlant, adresKlant, kleurKlasse1, maximumPrijsVanKlasse1, kleurKlasse2, maximumPrijsVanKlasse2, kleurKlasse3, bedrijfsEmail, bedrijfsTelefoon, bedrijfsAdres);
		bedrijfInstellingen.update();
		
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
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		ArrayList<Dag> dagen = new ArrayList<Dag>();
		Dag zondag = new Dag(bedrijf, 1, openingsTijdZondag, sluitingsTijdZondag);
		Dag maandag = new Dag(bedrijf, 2, openingsTijdMaandag, sluitingsTijdMaandag);
		Dag dinsdag = new Dag(bedrijf, 3, openingsTijdDinsdag, sluitingsTijdDinsdag);
		Dag woensdag = new Dag(bedrijf, 4, openingsTijdWoensdag, sluitingsTijdWoensdag);
		Dag donderdag = new Dag(bedrijf, 5, openingsTijdDonderdag, sluitingsTijdDonderdag);
		Dag vrijdag = new Dag(bedrijf, 6, openingsTijdVrijdag, sluitingsTijdVrijdag);
		Dag zaterdag = new Dag(bedrijf, 7, openingsTijdZaterdag, sluitingsTijdZaterdag);
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
		bedrijf.saveDagen();
		
		return Response.ok().build();
	}	
}
