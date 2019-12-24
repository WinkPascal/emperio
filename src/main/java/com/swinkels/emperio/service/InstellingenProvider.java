package com.swinkels.emperio.service;

import java.util.ArrayList;

import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Dag;
import com.swinkels.emperio.objects.Instellingen;

@Path("/instellingen")
public class InstellingenProvider {	

	
	@GET
	@RolesAllowed({"user", "setup"})
	@Path("/getAfspraakInfo")
	@Produces("application/json")
	public String getAfspraakInfo(@Context SecurityContext sc) {
		JsonObjectBuilder job = Json.createObjectBuilder();
		Instellingen instellingen = new Instellingen(sc.getUserPrincipal().getName());
		instellingen.getInplanSettings();
		
		job.add("emailKlantInvoer", instellingen.isEmailKlantInvoer());
		job.add("telefoonKlantInvoer", instellingen.isTelefoonKlantInvoer());
		job.add("adresKlantInvoer", instellingen.isAdresKlantInvoer());
		
		String email = instellingen.getBedrijfsEmailString();
		System.out.println("email "+email);
		if(email != null) {
			job.add("bedrijfsEmail", email);
		} else {
			job.add("bedrijfsEmail", "");
		}
		String telefoon = instellingen.getBedrijfsTelefoonString();
		if(telefoon != null) {
			job.add("bedrijfsTelefoon", telefoon);
		} else {
			job.add("bedrijfsTelefoon", "");
		}
		String adres = instellingen.getBedrijfsAdresString();
		if(adres != null) {
			job.add("bedrijfsAdres", adres);
		} else {
			job.add("bedrijfsAdres", "");
		}
		
		return job.build().toString();
	}
	
	@POST
	@RolesAllowed({"user", "setup"})
	@Path("/saveAfspraakInfo")
	@Produces("application/json")
	public Response saveAfspraakInfo(@Context SecurityContext sc, 
			@FormParam("telefoonKlant") boolean telefoonKlant, 
			@FormParam("emailKlant") boolean emailKlant,
			@FormParam("adresKlant") boolean adresKlant,
	
			@FormParam("bedrijfsEmail") String bedrijfsEmail, 
			@FormParam("bedrijfsTelefoon") String bedrijfsTelefoon,
			@FormParam("bedrijfsAdres") String bedrijfsAdres) {
		System.out.println("====");
		Instellingen bedrijfInstellingen = new Instellingen(sc.getUserPrincipal().getName(), 
				telefoonKlant, emailKlant, adresKlant,
				bedrijfsEmail, bedrijfsTelefoon, bedrijfsAdres);
		
		bedrijfInstellingen.updateInplanSettings();
		
		return Response.ok().build();
	}

	@POST
	@RolesAllowed({"user", "setup"})
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


	@GET
	@RolesAllowed("user")
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
	@RolesAllowed("user")
	@Path("/saveAfspraakColor")
	@Produces("application/json")
	public Response saveAfspraakColor(@Context SecurityContext sc, @FormParam("kleurKlasse1") String kleurKlasse1,
			@FormParam("tot1") double maximumPrijsVanKlasse1, @FormParam("kleurKlasse2") String kleurKlasse2,
			@FormParam("tot2") double maximumPrijsVanKlasse2, @FormParam("kleurKlasse3") String kleurKlasse3
			) {

		Instellingen bedrijfInstellingen = new Instellingen(sc.getUserPrincipal().getName(), 
				kleurKlasse1, maximumPrijsVanKlasse1, kleurKlasse2, maximumPrijsVanKlasse2, kleurKlasse3);
		bedrijfInstellingen.updateColors();

		return Response.ok().build();
	}
	
	@POST
	@RolesAllowed("user")
	@Produces("application/json")
	public Response getKlanten(@Context SecurityContext sc, 
			@FormParam("contact") String contact,
			@FormParam("telefoon") boolean telefoon, 
			@FormParam("email") boolean email,
			@FormParam("adres") boolean adres) {
		if (contact.equals("email") && email == false) {
			return Response
					.status(412, "De email moet kunnen worden ingevoerd " + "als het een verplicht invoerveld is.")
					.build();
		}
		if (contact.equals("telefoon") && telefoon == false) {
			return Response.status(412,
					"Het telefoon nummer moet kunnen worden ingevoerd als " + "het een verplicht invoerveld is.")
					.build();
		}

		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());

		if (bedrijfDao.setInvoerKlant(bedrijf, contact, telefoon, email, adres)) {
			System.out.println("GOED");
			return Response.status(202).build();
		} else {
			System.out.println("FOUT");
			return Response.status(500).build();
		}
	}
}
