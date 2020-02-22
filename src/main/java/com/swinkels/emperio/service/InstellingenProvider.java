package com.swinkels.emperio.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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

import com.swinkels.emperio.objects.instellingen.InstellingenFacade;
import com.swinkels.emperio.objects.instellingen.InstellingenFacadeInterface;
import com.swinkels.emperio.objects.rooster.Dag;
import com.swinkels.emperio.objects.security.Bedrijf;
import com.swinkels.emperio.objects.security.Security;
import com.swinkels.emperio.support.JavascriptDateAdapter;

@Path("/instellingen")
public class InstellingenProvider {	

	@GET
	@RolesAllowed({"user", "setup"})
	@Path("/getAfspraakInfo")
	@Produces("application/json")
	public String getAfspraakInfo(@Context SecurityContext sc) {
		JsonObjectBuilder job = Json.createObjectBuilder();
		Security.setKey(sc.getUserPrincipal().getName());
		InstellingenFacadeInterface InstellingenFacade = new InstellingenFacade();
		HashMap<String, String> instellingenDto = InstellingenFacade.getInplanSettingsDTO();

		job.add("emailKlantInvoer", instellingenDto.get("emailKlantInvoer"));
		job.add("telefoonKlantInvoer", instellingenDto.get("telefoonKlantInvoer"));
		job.add("adresKlantInvoer", instellingenDto.get("adresKlantInvoer"));

		job.add("bedrijfsEmail", instellingenDto.get("bedrijfsEmail"));
		job.add("bedrijfsTelefoon", instellingenDto.get("bedrijfsTelefoon"));
		job.add("bedrijfsAdres", instellingenDto.get("bedrijfsAdres"));

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
		InstellingenFacade bedrijfInstellingen = new InstellingenFacade();

		bedrijfInstellingen.saveInplanSettings(telefoonKlant, emailKlant, adresKlant,
				bedrijfsEmail, bedrijfsTelefoon, bedrijfsAdres);

		return Response.ok().build();
	}

	@GET
	@RolesAllowed({"user", "setup"})
	@Path("/getDagen")
	@Produces("application/json")
	public String getDagen(@Context SecurityContext sc) {
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());

		bedrijf.retrieveDagen();
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Dag dag : bedrijf.getDagen()) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("dagNummmer", JavascriptDateAdapter.dagNummer(dag.getDag()));
			Date openingsTijd = dag.getOpeningsTijd();
			if(openingsTijd != null) {
				job.add("sluitingstijd", JavascriptDateAdapter.DateToString(dag.getSluitingsTijd(), "HH:mm"));
				job.add("opeingstijd",  JavascriptDateAdapter.DateToString(dag.getOpeningsTijd(), "HH:mm"));
			}
			System.out.println(dag.getDag());
			jab.add(job);
		}
		return jab.build().toString();
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
		Dag zondag = new Dag(1, openingsTijdZondag, sluitingsTijdZondag);
		Dag maandag = new Dag(2, openingsTijdMaandag, sluitingsTijdMaandag);
		Dag dinsdag = new Dag(3, openingsTijdDinsdag, sluitingsTijdDinsdag);
		Dag woensdag = new Dag(4, openingsTijdWoensdag, sluitingsTijdWoensdag);
		Dag donderdag = new Dag(5, openingsTijdDonderdag, sluitingsTijdDonderdag);
		Dag vrijdag = new Dag(6, openingsTijdVrijdag, sluitingsTijdVrijdag);
		Dag zaterdag = new Dag(7, openingsTijdZaterdag, sluitingsTijdZaterdag);
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
		Security.setKey(sc.getUserPrincipal().getName());
		InstellingenFacadeInterface instellingen = new InstellingenFacade();
		HashMap<String, String> instellingenDto = instellingen.toDto();

		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("kleurKlasse1", instellingenDto.get("kleurKlasse1"));
		job.add("maximumPrijsVanKlasse1", instellingenDto.get("maximumPrijsVanKlasse1"));
		job.add("kleurKlasse2", instellingenDto.get("kleurKlasse2"));
		job.add("maximumPrijsVanKlasse2", instellingenDto.get("maximumPrijsVanKlasse2"));
		job.add("kleurKlasse3", instellingenDto.get("kleurKlasse3"));

		job.add("emailKlantInvoer", instellingenDto.get("emailKlantInvoer"));
		job.add("telefoonKlantInvoer", instellingenDto.get("telefoonKlantInvoer"));
		job.add("adresKlantInvoer", instellingenDto.get("adresKlantInvoer"));

		job.add("bedrijfsEmail", instellingenDto.get("bedrijfsEmail"));
		job.add("bedrijfsTelefoon", instellingenDto.get("bedrijfsTelefoon"));
		job.add("bedrijfsAdres", instellingenDto.get("bedrijfsAdres"));

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

		InstellingenFacadeInterface instellingenFacade = new InstellingenFacade();
		instellingenFacade.saveAfspraakKleuren(kleurKlasse1, maximumPrijsVanKlasse1, kleurKlasse2, maximumPrijsVanKlasse2, kleurKlasse3);

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

//		if (bedrijfDao.setInvoerKlant(bedrijf, contact, telefoon, email, adres)) {
//			return Response.status(202).build();
//		} else {
//			return Response.status(500).build();
//		}
		return null;
	}
}
