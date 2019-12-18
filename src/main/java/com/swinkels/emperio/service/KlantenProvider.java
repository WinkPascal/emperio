package com.swinkels.emperio.service;

import java.util.HashMap;

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
import com.swinkels.emperio.objects.KlantBuilder;
import com.swinkels.emperio.support.JavascriptDateAdapter;

@Path("/klanten")
public class KlantenProvider {
	@GET
	@Path("/klanten/{data}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getKlanten(@Context SecurityContext sc, @PathParam("data") String data) {
		HashMap<String, String> dataMap = getData(data);
		int page = Integer.parseInt(dataMap.get("page"));
		String sort = dataMap.get("sort");
		String search = dataMap.get("search");
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		bedrijf.getKlantenByRequest(page, sort, search);
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Klant klant : bedrijf.getKlanten()) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("id", klant.getId());
			job.add("naam", klant.getNaam());
			job.add("adres", klant.getAdres());
			job.add("email", klant.getEmail());
			job.add("telefoon", klant.getTel());
			job.add("geslacht", klant.getGeslacht());
			job.add("afspraken", klant.getHoeveeleheidAfspraken());
			job.add("inkomsten", klant.getHoeveelheidInkomsten());
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
		Klant klant = new KlantBuilder().setId(id).make();
		klant.setBedrijf(new Bedrijf(sc.getUserPrincipal().getName()));
		klant.getInfo();

		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("naam", klant.getNaam());
		job.add("geslacht", klant.getGeslacht());
		job.add("email", klant.getEmail());
		job.add("adres", klant.getAdres());
		job.add("telefoon", klant.getTel());
		job.add("aantalAfspraken", klant.getAantalAfspraken());
		job.add("inkomsten", klant.getHoeveelheidInkomsten());

		JsonArrayBuilder jab1 = Json.createArrayBuilder();
		for (Afspraak afspraak : klant.getAfspraken()) {
			JsonObjectBuilder job1 = Json.createObjectBuilder();
			job1.add("prijs", afspraak.getPrijs());
			String timestampString = JavascriptDateAdapter.DateToString(afspraak.getTimeStamp(), "YYYY-MM-dd HH:mm");
			job1.add("datum", timestampString.substring(0, 10));
			job1.add("tijd", timestampString.substring(11));
			jab1.add(job1);
		}
		job.add("afspraken", jab1);

		return job.build().toString();
	}
}
