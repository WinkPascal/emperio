package com.swinkels.emperio.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import org.json.JSONArray;

import com.swinkels.emperio.objects.Afspraak;
import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Behandeling;
import com.swinkels.emperio.objects.Dag;
import com.swinkels.emperio.objects.Klant;
import com.swinkels.emperio.providers.AfspraakBehandelingDao;
import com.swinkels.emperio.providers.AfspraakBehandelingDaoImpl;
import com.swinkels.emperio.providers.AfspraakDao;
import com.swinkels.emperio.providers.AfspraakDaoImpl;
import com.swinkels.emperio.providers.BedrijfDao;
import com.swinkels.emperio.providers.BedrijfDaoImpl;
import com.swinkels.emperio.providers.BehandelingDao;
import com.swinkels.emperio.providers.BehandelingDaoImpl;
import com.swinkels.emperio.providers.DagDao;
import com.swinkels.emperio.providers.DagDaoImpl;
import com.swinkels.emperio.providers.KlantDao;
import com.swinkels.emperio.providers.KlantDaoImpl;

@Path("/service")
public class ServiceProvider {
	AfspraakDao afspraakDao = new AfspraakDaoImpl();
	BehandelingDao behandelingDao = new BehandelingDaoImpl();
	KlantDao klantDao = new KlantDaoImpl();
	AfspraakBehandelingDao afspraakBehandelingDao = new AfspraakBehandelingDaoImpl();
	DagDao dagDao = new DagDaoImpl();
	BedrijfDao bedrijfDao = new BedrijfDaoImpl();
	
	@GET
	@Path("/afsprakenByDate/{date}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String afsprakenByDate(@Context SecurityContext sc,
									@PathParam("date") String datum ) throws ParseException {
		Date beginDate = ServiceFilter.StringToDateFormatter(datum, "yyyy-MM-dd");
		System.out.println("=====================afspraken By date");
		System.out.println(beginDate);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(beginDate);            
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date eindDate = calendar.getTime();
		
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		// vraag de afspraken op van het bedrijf en de datum van vandaag
		ArrayList<Afspraak> afsprakenVandaagList = afspraakDao.getAfsprakenBetweenDates(beginDate, eindDate, bedrijf);
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Afspraak afspraak : afsprakenVandaagList) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("id", afspraak.getId());
			job.add("timestamp", ServiceFilter.DateToStringFormatter(afspraak.getTimeStamp(), "yyyy-MM-dd HH:mm"));
			job.add("klant", afspraak.getKlant().getNaam());
			
			int minuten = 0;
			int uur = 0;
			double prijs = 0;
			
			JsonArrayBuilder jab1 = Json.createArrayBuilder();
			JsonObjectBuilder job1 = Json.createObjectBuilder();
			for(Behandeling behandeling : afspraak.getBehandelingen()) {
				job.add("naam", behandeling.getNaam());
				
				Date lengte = behandeling.getLengte();
				minuten = minuten + lengte.getMinutes();
				if(minuten > 59) {
					uur = uur + 1;
					minuten = minuten - 60;
				}
				uur = uur + lengte.getHours();
				
				prijs = prijs + behandeling.getPrijs();
				
				System.out.println(behandeling.getLengte());
				jab1.add(job);
			}
			job.add("lengte", uur+":"+minuten);
			job.add("prijs", prijs);
			job.add("namen", jab1);
			System.out.println(uur);
			jab.add(job);
		}
		return jab.build().toString();
	}
	
	
	
	@GET
	@Path("/tijdslotenOphalen/{datum}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String tijdslotenOphalen(@Context SecurityContext sc,
			@PathParam("datum") String beginDateString) throws ParseException {
		System.out.println("=====================");
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		
		Date beginDate = ServiceFilter.StringToDateFormatter(beginDateString, "yyyy-MM-dd");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(beginDate);            
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date eindDate = calendar.getTime();

		ArrayList<Afspraak> afsprakenVandaagList = afspraakDao.getAfsprakenBetweenDates(beginDate, eindDate,  bedrijf);
		
		ArrayList<Date> dagTijden = bedrijfDao.getDagTijden(bedrijf, beginDate);
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		if(dagTijden.get(0) != null) {
			JsonObjectBuilder job1 = Json.createObjectBuilder();
			String openingsTijd = ServiceFilter.DateToStringFormatter(dagTijden.get(0), "HH-mm");
			String sluitingstijd = ServiceFilter.DateToStringFormatter(dagTijden.get(1), "HH-mm");
			
			System.out.println(openingsTijd);
			System.out.println(sluitingstijd);
			
			String openingsTijdUurString = openingsTijd.substring(0, 2);
			String openingsTijdMinuutString = openingsTijd.substring(3, 5);
			
			String sluitingsTijdUurString = sluitingstijd.substring(0, 2);
			String sluitingsTijdMinuutString = sluitingstijd.substring(3, 5);

			int openingsTijdUur = Integer.parseInt(openingsTijdUurString);
			int openingsTijdMinuut= Integer.parseInt(openingsTijdMinuutString);
			int sluitingsTijdUur = Integer.parseInt(sluitingsTijdUurString);
			int sluitingsTijdMinuut = Integer.parseInt(sluitingsTijdMinuutString);
			
			job1.add("openingsTijdUur", openingsTijdUur);
			job1.add("openingsTijdMinuut", openingsTijdMinuut);

			job1.add("sluitingsTijdUur", sluitingsTijdUur);
			job1.add("sluitingsTijdMinuut", sluitingsTijdMinuut);
			
			jab.add(job1);
		}

		for (Afspraak afspraak : afsprakenVandaagList) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			int minuten = 0;
			int uur = 0;
			
			for(Behandeling behandeling : afspraak.getBehandelingen()) {
				Date lengte = behandeling.getLengte();
				System.out.println(lengte);
				
				String lengteString =  ServiceFilter.DateToStringFormatter(lengte, "HH:mm");
				
				String behandelingUren = lengteString.substring(0, 2);
				int behandelingUrenInt = Integer.parseInt(behandelingUren);
				
				String behandelingMinuten = lengteString.substring(3, 5);
				int behandelingMinutenInt = Integer.parseInt(behandelingMinuten);
				
				uur = uur + behandelingUrenInt;
				minuten = minuten + behandelingMinutenInt;
				if(minuten > 60) {
					uur ++;
					minuten = minuten - 60;
				}
			}
			String timestampString = ServiceFilter.DateToStringFormatter(afspraak.getTimeStamp(), "yyyy-MM-dd HH:mm");
			
			String beginUurString = timestampString.substring(11, 13);
			String beginMinuutString = timestampString.substring(14, 16);
			
			int beginUur = Integer.parseInt(beginUurString);
			int beginMinuut = Integer.parseInt(beginMinuutString);

			int eindUur = beginUur + uur;
			int eindMinuut = beginMinuut + minuten;
			if(eindMinuut > 60) {
				eindMinuut=eindMinuut - 60;
				eindUur ++;
			}

			job.add("beginUur", beginUur);			
			job.add("beginMinuut", beginMinuut);			

			job.add("eindUur", eindUur);
			job.add("eindMinuut", eindMinuut);
			
			jab.add(job);
		}
		return jab.build().toString();	
	}
	
	@GET
	@Path("/getWeekRooster/{date}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getWeekRooster(@Context SecurityContext sc, 
			@PathParam("date") String datum) {
		
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		System.out.println(datum);
		Date date = ServiceFilter.StringToDateFormatter(datum, "yyyy-MM-dd");
		
		//dagen worden opgehaalt
		ArrayList<Dag> dagen = bedrijfDao.getWeekRooster(bedrijf);
		JsonArrayBuilder jab = Json.createArrayBuilder();
		JsonArrayBuilder jab1 = Json.createArrayBuilder();
		for(Dag dag : dagen) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("id", dag.getDag());

			String openingsTijd = ServiceFilter.DateToStringFormatter(dag.getOpeningsTijd(), "HH:mm");
			job.add("openingsTijd", openingsTijd);
			
			String sluitingsTijd = ServiceFilter.DateToStringFormatter(dag.getSluitingsTijd(), "HH:mm");
			job.add("sluitingsTijd", sluitingsTijd);
			
			jab1.add(job);
		}
		jab.add(jab1);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);            
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date eindDate = calendar.getTime();
		
		ArrayList<Afspraak> afspraken = afspraakDao.getAfsprakenBetweenDates(date, eindDate, bedrijf);
		
		JsonArrayBuilder jab2 = Json.createArrayBuilder();
		//hieraan wordt toegevoegd:
		//afspraak.id
		//afspraak.week
		//afspraak.beginTijd
		//klant.naam
		//aparte json array binnen jab2
			//behandeling.naam
			//behandeling.lengte
			//behandeling.prijs
		for(Afspraak afspraak : afspraken) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			
			job.add("id", afspraak.getId());
			Date timestampDate = afspraak.getTimeStamp();
			String timestamp = ServiceFilter.DateToStringFormatter(timestampDate, "yyyy-MM-dd HH:mm");
			System.out.println(timestamp);
			
			job.add("week", timestamp);
			job.add("beginTijd", afspraak.getId());
			
			JsonArrayBuilder jab3 = Json.createArrayBuilder();
			for(Behandeling behandeling : afspraak.getBehandelingen()) {
				JsonObjectBuilder job1 = Json.createObjectBuilder();
				
				String lengteString = ServiceFilter.DateToStringFormatter(behandeling.getLengte(), "HH:mm");
				System.out.println(lengteString);
				
				job1.add("naam", behandeling.getNaam());
				job1.add("lengte", lengteString);
				job1.add("prijs", behandeling.getPrijs());

				jab3.add(job1);
			}			
			jab1.add(job);
		}
		jab.add(jab2);
		
		return jab.build().toString();
	};

	
	@GET
	@Path("/behandelingen/{geslacht}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getBehandelingenByGeslacht(@Context SecurityContext sc, 
			@PathParam("geslacht") String geslacht) {

		// haal de behandelingen op
		ArrayList<Behandeling> behandelingen = behandelingDao.behandelingenByGeslacht(geslacht,
				sc.getUserPrincipal().getName());
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Behandeling behandeling : behandelingen) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("id", behandeling.getId());
			job.add("naam", behandeling.getNaam());
			job.add("beschrijving", behandeling.getBeschrijving());
			job.add("lengte", ServiceFilter.DateToStringFormatter(behandeling.getLengte(), "HH:mm"));
			job.add("prijs", behandeling.getPrijs());
			jab.add(job);
		}
		return jab.build().toString();
	}

	@POST
	@RolesAllowed("user")
	@Path("/afspraak")
	@Produces("application/json")
	public Response setAfspraak(@Context SecurityContext sc, 
			@FormParam("klantNaam") String afspraakKlantNaam,
			@FormParam("klantGeslacht") String afspraakKlantGeslacht,
			@FormParam("klantEmail") String afspraakKlantEmail,
			@FormParam("klantTelefoon") String afspraakKlantTel,
			
			@FormParam("afspraakBehandeling") String afspraakBehandelingen,
			@FormParam("afspraakTijd") String afspraakTijd, 
			@FormParam("afspraakDatum") String afspraakDatum) throws ParseException {
		//objecten van de behandelingen van de afspraak maken
		ArrayList<Behandeling> behandelingenList = new ArrayList<Behandeling>();
		JSONArray jsonArray = new JSONArray(afspraakBehandelingen);
		
		for (int i = 0; i < jsonArray.length(); i++) {
			Behandeling behandeling = new Behandeling(jsonArray.get(i).toString());
			behandeling.setId(Integer.parseInt(jsonArray.get(i).toString()));
			System.out.println("t "+ behandeling.getId());
			behandelingenList.add(behandeling);
		}
		// aanmaken bedrijf object
		String bedrijfsNaam = sc.getUserPrincipal().getName();
		Bedrijf bedrijf = new Bedrijf(bedrijfsNaam);
		// aanmaken klant object
		Klant klant = new Klant(afspraakKlantGeslacht, afspraakKlantNaam, bedrijf);
		// valideren email
		String email = ServiceFilter.emailCheck(afspraakKlantEmail);
		if (email.equals("fout")) {
			System.out.println("email is fout");
		}
		if (email.equals("email")) {
			klant.setEmail(afspraakKlantEmail);
		}
		if (ServiceFilter.phoneCheck(afspraakKlantTel)) {
			klant.setTel(afspraakKlantTel);
		}
		
		// aanmaken afspraak object
		String timestampString = afspraakDatum+" "+afspraakTijd;
		System.out.println(timestampString);
		SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date timestamp =timestampFormat.parse(timestampString);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(timestamp);            
		calendar.add(Calendar.MONTH, 1);
		timestamp = calendar.getTime();
		
		System.out.println(timestamp);
		Afspraak afspraak = new Afspraak(timestamp, bedrijf, klant);

		// contact met de database
		// klant wordt in de database gezet

		klant = klantDao.getKlantId(klant);

		if (klant.getId() == 0) {
			// er is geen klant aangemaakt dus die word aangemaakt in de database
			klantDao.setKlant(klant);
			klant = klantDao.getKlantId(klant);
		}

		if (afspraakDao.setAfspraak(afspraak)) {
			afspraak = afspraakDao.getAfspraakId(afspraak);
		} else {
		}
		// per behandeling die verbonden is met de afspraak word er een verbinding in de
		// database gelegd
		for (Behandeling behandeling : behandelingenList) {
			afspraakBehandelingDao.saveAfspraakBehandeling(behandeling, afspraak);
		}
		return Response.ok().build();
	}

	@GET
	@Path("/klantenZoekReq/{request}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getKlantenZoekRequest(@Context SecurityContext sc, 
			@PathParam("request") String request) {
		System.out.println("sdasda");
		String bedrijf = sc.getUserPrincipal().getName();
		ArrayList<Klant> klanten = klantDao.zoekKlant(bedrijf, request);
		JsonArrayBuilder jab = Json.createArrayBuilder();

		for (Klant klant : klanten) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			System.out.println(klant.getId());
			System.out.println(klant.getNaam());
			System.out.println(klant.getEmail());
			System.out.println(klant.getTel());
			System.out.println(klant.getGeslacht());
			String email =klant.getEmail();
			String telefoon = klant.getTel();
			if(email==null) {
				if(telefoon == null) {
					//heeft geen telefoon en email
					job.add("id", klant.getId());
					job.add("naam", klant.getNaam());
					job.add("geslacht", klant.getGeslacht());
				} else {
					//geen email wel telefoon
					job.add("id", klant.getId());
					job.add("naam", klant.getNaam());
					job.add("geslacht", klant.getGeslacht());				}
					job.add("telefoon", klant.getTel());
			} else if(telefoon == null) {
				//alleen mail geen telefoon
				job.add("id", klant.getId());
				job.add("naam", klant.getNaam());
				job.add("geslacht", klant.getGeslacht());				
				job.add("email", klant.getEmail());
			} else {
				//heeft alles
				job.add("id", klant.getId());
				job.add("naam", klant.getNaam());
				job.add("email", klant.getEmail());
				job.add("telefoon", klant.getTel());
				job.add("geslacht", klant.getGeslacht());
			}

			jab.add(job);

		}
		return jab.build().toString();
	}

	@GET
	@Path("/alleKlanten/{pageNummer}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getKlanten(@Context SecurityContext sc, @PathParam("pageNummer") int pageNummer) {

		String bedrijf = sc.getUserPrincipal().getName();
		ArrayList<Klant> klanten = klantDao.getKlanten(bedrijf, pageNummer);
		JsonArrayBuilder jab = Json.createArrayBuilder();
		
		for (Klant klant : klanten) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			System.out.println(klant.getId());
			System.out.println(klant.getNaam());
			System.out.println(klant.getEmail());
			System.out.println(klant.getTel());
			System.out.println(klant.getGeslacht());
			String email =klant.getEmail();
			String telefoon = klant.getTel();
			if(email==null) {
				if(telefoon == null) {
					//heeft geen telefoon en email
					job.add("id", klant.getId());
					job.add("naam", klant.getNaam());
					job.add("geslacht", klant.getGeslacht());
				} else {
					//geen email wel telefoon
					job.add("id", klant.getId());
					job.add("naam", klant.getNaam());
					job.add("geslacht", klant.getGeslacht());				}
					job.add("telefoon", klant.getTel());
			} else if(telefoon == null) {
				//alleen mail geen telefoon
				job.add("id", klant.getId());
				job.add("naam", klant.getNaam());
				job.add("geslacht", klant.getGeslacht());				
				job.add("email", klant.getEmail());
			} else {
				//heeft alles
				job.add("id", klant.getId());
				job.add("naam", klant.getNaam());
				job.add("email", klant.getEmail());
				job.add("telefoon", klant.getTel());
				job.add("geslacht", klant.getGeslacht());
			}

			jab.add(job);

		}
		return jab.build().toString();
	}

	@POST
	@RolesAllowed("user")
	@Path("/behandeling")
	@Produces("application/json")
	public Response setAfspraak(@Context SecurityContext sc, 
			@FormParam("naam") String naam,
			@FormParam("beschrijving") String beschrijving, 
			@FormParam("prijs") double prijs,
			@FormParam("uur") String uur,
			@FormParam("minuten") String minuten,
			@FormParam("geslachten") String geslachten) {
		String bedrijfsNaam = sc.getUserPrincipal().getName();
		Bedrijf bedrijf = new Bedrijf(bedrijfsNaam);
		String lengteString = uur + ":" + minuten;
		Date lengte = ServiceFilter.StringToDateFormatter(lengteString, "HH:mm");
		
		JSONArray jsonArray = new JSONArray(geslachten);
		for (int i = 0; i < jsonArray.length(); i++) {
			Behandeling behandeling = new Behandeling(bedrijf, naam, beschrijving, prijs, lengte, jsonArray.get(i).toString());
			behandelingDao.setBehandeling(behandeling);
		}
		
		return Response.ok().build();
	}
}