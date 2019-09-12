package com.swinkels.emperio.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.swinkels.emperio.objects.Afspraak;
import com.swinkels.emperio.objects.Behandeling;
import com.swinkels.emperio.objects.Klant;
import com.swinkels.emperio.providers.AfspraakDao;
import com.swinkels.emperio.providers.AfspraakDaoImpl;
import com.swinkels.emperio.providers.BehandelingDao;
import com.swinkels.emperio.providers.BehandelingDaoImpl;
import com.swinkels.emperio.providers.KlantDao;
import com.swinkels.emperio.providers.KlantDaoImpl;

@Path("/service")
public class ServiceProvider {
	AfspraakDao afspraakDao = new AfspraakDaoImpl();
	BehandelingDao behandelingDao = new BehandelingDaoImpl();
	KlantDao klantDao = new KlantDaoImpl();
	
	@GET
	@Path("/behandelingen/{geslacht}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getBehandelingenByGeslacht(
			@Context SecurityContext sc,
			@PathParam("geslacht") String geslacht) {
		System.out.println("getBehandlingenByGeslacht");
		//haal de behandelingen op
		ArrayList<Behandeling> behandelingen = behandelingDao.behandelingenByGeslacht(geslacht, sc.getUserPrincipal().getName());
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for(Behandeling behandeling : behandelingen) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("id", behandeling.getId());
			job.add("naam", behandeling.getNaam());
			job.add("beschrijving", behandeling.getBeschrijving());
			job.add("tijd", behandeling.getTijd());
			job.add("prijs", behandeling.getPrijs());
			jab.add(job);
		}
		return jab.build().toString();	
	}
	
	@GET
	@Path("/afsprakenVandaag")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getAfsprakenVandaag(@Context SecurityContext sc) {
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		//vraag de afspraken op van het bedrijf en de datum van vandaag
		ArrayList<Afspraak> afsprakenVandaagList = afspraakDao.getAfsprakenVandaag(formatter.format(date), sc.getUserPrincipal().getName());

		JsonArrayBuilder jab = Json.createArrayBuilder();
		for(Afspraak afspraak : afsprakenVandaagList) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("id", afspraak.getId());
			job.add("tijd", afspraak.getTijd());
			job.add("lengte", afspraak.getLengte());
			Klant klant = afspraak.getKlant();
			job.add("klant", klant.getNaam());
			Behandeling behandeling = afspraak.getBehandeling();
			job.add("behandeling", behandeling.getNaam());
			jab.add(job);
		}
		System.out.println("getAfsprakenVandaag");

		return jab.build().toString();
	}

	@POST
	@RolesAllowed("user")
	@Path("/afspraak")
	@Produces("application/json")
	public Response setAfspraak(
			@Context SecurityContext sc,
			@FormParam("afspraakKlantNaam") String afspraakKlantNaam,
			@FormParam("afspraakKlantGeslacht") String afspraakKlantGeslacht,
			@FormParam("afspraakKlantEmail") String afspraakKlantEmail,
			@FormParam("afspraakKlantTel") String afspraakKlantTel,
			@FormParam("afspraakBehandeling") int afspraakBehandeling,
			@FormParam("afspraakTijd") String afspraakTijd,
			@FormParam("afspraakDatum") String afspraakDatum) {
		System.out.println("sada");
		String bedrijf = sc.getUserPrincipal().getName();
		afspraakDao.setAfspraak(afspraakKlantNaam, afspraakKlantGeslacht, afspraakKlantEmail,
				afspraakKlantTel, afspraakBehandeling, afspraakTijd, afspraakDatum, bedrijf);
						
		return Response.ok().build();
	}
	
	@GET
	@Path("/klantenZoekReq/{request}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getKlantenZoekRequest(@Context SecurityContext sc,
			@PathParam("request") String request) {
		String bedrijf = sc.getUserPrincipal().getName();
		System.out.println(request);
		ArrayList<Klant> klanten = klantDao.zoekKlant(bedrijf, request);
		JsonArrayBuilder jab = Json.createArrayBuilder();

		for(Klant klant : klanten) {
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
	@Path("/alleKlanten/{pageNummer}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getKlanten(@Context SecurityContext sc,
			@PathParam("pageNummer") int pageNummer) {
		System.out.println("getKlanten");
		String bedrijf = sc.getUserPrincipal().getName();
		ArrayList<Klant> klanten = klantDao.getKlanten(bedrijf, pageNummer);
		JsonArrayBuilder jab = Json.createArrayBuilder();

		for(Klant klant : klanten) {
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
	
}