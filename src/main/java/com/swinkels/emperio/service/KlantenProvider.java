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

@Path("/klanten")
public class KlantenProvider {
	AfspraakDao afspraakDao = new AfspraakDaoImpl();
	BehandelingDao behandelingDao = new BehandelingDaoImpl();
	KlantDao klantDao = new KlantDaoImpl();
	AfspraakBehandelingDao afspraakBehandelingDao = new AfspraakBehandelingDaoImpl();
	BedrijfDao bedrijfDao = new BedrijfDaoImpl();
	// wordt gebruikt bij
	// klanten pagina klanten lijst
	@GET
	@Path("/alleKlanten/{pageNummer}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getKlanten(@Context SecurityContext sc, @PathParam("pageNummer") int pageNummer) {
		System.out.println("tes");
		// uitvoer
		// per klant
		// id, naam, geslacht
		// opt
		
		// telefoon
		// email
		String bedrijf = sc.getUserPrincipal().getName();
		ArrayList<Klant> klanten = klantDao.getKlanten(bedrijf, pageNummer);
		JsonArrayBuilder jab = Json.createArrayBuilder();

		for (Klant klant : klanten) {
			JsonObjectBuilder job = Json.createObjectBuilder();

			String email = klant.getEmail();
			String telefoon = klant.getTel();

			if (email == null) {
				if (telefoon == null) {
					// heeft geen telefoon en email
					job.add("id", klant.getId());
					job.add("naam", klant.getNaam());
					job.add("geslacht", klant.getGeslacht());
				} else {
					// geen email wel telefoon
					job.add("id", klant.getId());
					job.add("naam", klant.getNaam());
					job.add("geslacht", klant.getGeslacht());
					job.add("telefoon", klant.getTel());
				}
			} else {
				if (telefoon == null) {
					// alleen mail geen telefoon
					job.add("id", klant.getId());
					job.add("naam", klant.getNaam());
					job.add("geslacht", klant.getGeslacht());
					job.add("email", klant.getEmail());
				} else {
					// heeft alles
					job.add("id", klant.getId());
					job.add("naam", klant.getNaam());
					job.add("email", klant.getEmail());
					job.add("telefoon", klant.getTel());
					job.add("geslacht", klant.getGeslacht());
				}
			}
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
		// uitvoer
		// per klant
		// id, naam, geslacht
		// opt
		// telefoon
		// email
		String bedrijf = sc.getUserPrincipal().getName();
		ArrayList<Klant> klanten = klantDao.zoekKlant(bedrijf, request);
		JsonArrayBuilder jab = Json.createArrayBuilder();

		for (Klant klant : klanten) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			String email = klant.getEmail();
			String telefoon = klant.getTel();
			if (email == null) {
				if (telefoon == null) {
					// heeft geen telefoon en email
					job.add("id", klant.getId());
					job.add("naam", klant.getNaam());
					job.add("geslacht", klant.getGeslacht());
				} else {
					// geen email wel telefoon
					job.add("id", klant.getId());
					job.add("naam", klant.getNaam());
					job.add("geslacht", klant.getGeslacht());
					job.add("telefoon", klant.getTel());
				}
			} else {
				if (telefoon == null) {
					// alleen mail geen telefoon
					job.add("id", klant.getId());
					job.add("naam", klant.getNaam());
					job.add("geslacht", klant.getGeslacht());
					job.add("email", klant.getEmail());
				} else {
					// heeft alles
					job.add("id", klant.getId());
					job.add("naam", klant.getNaam());
					job.add("email", klant.getEmail());
					job.add("telefoon", klant.getTel());
					job.add("geslacht", klant.getGeslacht());
				}
			}

			jab.add(job);

		}
		return jab.build().toString();
	}

	// wordt gebruikt bij
	// klanten pagina klanten lijst
	@GET
	@Path("/klant/{id}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getKlant(@Context SecurityContext sc, @PathParam("id") int id) {
		// uitvoer
		// naam, geslacht, email, telefoon,
		// aantal afspraken, hoeveelheid inkomsten
		// laatste 3 afspraken

		String bedrijfsNaam = sc.getUserPrincipal().getName();
		Bedrijf bedrijf = new Bedrijf(bedrijfsNaam);
		Klant klant = klantDao.getKlant(bedrijf, id);

		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("naam", klant.getNaam());
		job.add("geslacht", klant.getGeslacht());

		String email = klant.getEmail();
		if (email != null) {
			job.add("email", email);
		}
		String tel = klant.getTel();
		if (tel != null) {
			job.add("telefoon", tel);
		}
		// de aantal afspraken en hoeveel heid inkomsten van de klant
		ArrayList<Double> data = afspraakDao.getAantalAfsprakenEnInkomstenByklant(bedrijf, klant);
		job.add("afspraken", data.get(0));
		job.add("inkomsten", data.get(1));

		// laatste 5 afspraken ophalen
		JsonArrayBuilder jab1 = Json.createArrayBuilder();
		ArrayList<Afspraak> afspraken = afspraakDao.getLaatste3Afspraken(bedrijf, klant);
		for (Afspraak afspraak : afspraken) {
			JsonObjectBuilder job1 = Json.createObjectBuilder();
			job1.add("prijs", afspraak.getPrijs());

			String timestampString = ServiceFilter.DateToStringFormatter(afspraak.getTimeStamp(), "YYYY-MM-dd HH:mm");
			job1.add("datum", timestampString.substring(0, 10));
			System.out.println(timestampString.substring(0, 10));
			job1.add("tijd", timestampString.substring(11));
			jab1.add(job1);
		}
		job.add("afspraken", jab1);

		return job.build().toString();
	}
}
