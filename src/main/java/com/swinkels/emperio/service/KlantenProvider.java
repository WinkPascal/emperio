package com.swinkels.emperio.service;

import java.util.HashMap;
import java.util.List;

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

import com.swinkels.emperio.objects.klant.Klant;
import com.swinkels.emperio.objects.klant.KlantBuilder;
import com.swinkels.emperio.objects.klant.KlantInterface;
import com.swinkels.emperio.objects.klant.KlantManager;
import com.swinkels.emperio.objects.klant.KlantStatestieken;
import com.swinkels.emperio.objects.rooster.Afspraak;
import com.swinkels.emperio.objects.rooster.AfspraakInterface;
import com.swinkels.emperio.objects.security.Bedrijf;
import com.swinkels.emperio.objects.security.Security;
import com.swinkels.emperio.support.JavascriptDateAdapter;

@Path("/klanten")
public class KlantenProvider {
	@GET
	@Path("/klanten/{data}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getKlanten(@Context SecurityContext sc, @PathParam("data") String data) {
		HashMap<String, String> dataMap = getData(data);
		System.out.println(data);
		int page = Integer.parseInt(dataMap.get("page"));
		int sort = Integer.parseInt(dataMap.get("sort"));
		String search = dataMap.get("search");
		
		Security.setKey(sc.getUserPrincipal().getName());
		KlantManager klantManager = new KlantManager(page, search, sort);
		List<KlantInterface> klanten = klantManager.getKlanten();
		
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (KlantInterface klant : klanten) {
			HashMap<String, String> klantDto = klant.toDTO();
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("id", klantDto.get("id"));
			job.add("naam", klantDto.get("naam"));
			job.add("adres", klantDto.get("adres"));
			job.add("email", klantDto.get("email"));
			job.add("telefoon", klantDto.get("telefoon"));
			job.add("geslacht", klantDto.get("geslacht"));
			
			KlantStatestieken statestieken = klant.getStatestieken();
			HashMap<String, String> statestiekenDto = statestieken.toDTO();
			job.add("afspraken", statestiekenDto.get("aantalAfspraken"));
			job.add("inkomsten", statestiekenDto.get("hoeveelheidInkomsten"));
			jab.add(job);
		}
		return jab.build().toString();
	}

	private HashMap<String, String> getData(String data) {
		HashMap<String, String> dataMap = new HashMap<String, String>();
		for (String dataPunt : data.split("&")) {
			String[] dataPuntDetail = dataPunt.split("=");
			if (dataPuntDetail[0].equals("page")) {
				dataMap.put("page", dataPuntDetail[1]);
			}
			if (dataPuntDetail[0].equals("sort")) {
				dataMap.put("sort", dataPuntDetail[1]);
			}
			if (dataPuntDetail[0].equals("search")) {
				if(dataPuntDetail[1].equals("-")) {
					dataMap.put("search", "");
				} else {
					dataMap.put("search", dataPuntDetail[1].substring(1));
				}
			}
		}
		return dataMap;
	}

	@GET
	@Path("/klant/{id}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getKlant(@Context SecurityContext sc, @PathParam("id") int id) {
		Security.setKey(sc.getUserPrincipal().getName());
		KlantInterface klant = Klant.getKlantById(id);

		JsonObjectBuilder job = Json.createObjectBuilder();
		HashMap<String, String> KlantDto = klant.toDTO();
		job.add("naam", KlantDto.get("naam"));
		job.add("geslacht", KlantDto.get("geslacht"));
		job.add("email", KlantDto.get("email"));
		job.add("adres", KlantDto.get("adres"));
		job.add("telefoon", KlantDto.get("telefoon"));
		
		KlantStatestieken statestieken = klant.getStatestieken();
		HashMap<String, String> statestiekenDto = statestieken.toDTO();
		job.add("aantalAfspraken", statestiekenDto.get("aantalAfspraken"));
		job.add("inkomsten", statestiekenDto.get("hoeveelheidInkomsten"));

		JsonArrayBuilder jab1 = Json.createArrayBuilder();
		for (AfspraakInterface afspraak : klant.getAfspraken()) {
			HashMap<String, String> afapraakDto = afspraak.toDTO();
			JsonObjectBuilder job1 = Json.createObjectBuilder();
			job1.add("id",afapraakDto.get("id"));
			job1.add("prijs",afapraakDto.get("prijs"));
			String timestampString = afapraakDto.get("timestamp");
			job1.add("datum", timestampString.substring(0, 10));
			job1.add("tijd", timestampString.substring(11));
			jab1.add(job1);
		}
		job.add("afspraken", jab1);

		return job.build().toString();
	}
}
