package com.swinkels.emperio.service;
 
import java.util.Date;
import java.util.HashMap;

import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.json.JSONArray;

import com.swinkels.emperio.objects.behandeling.Behandeling;
import com.swinkels.emperio.objects.behandeling.BehandelingBuilder;
import com.swinkels.emperio.objects.security.Bedrijf;
import com.swinkels.emperio.support.JavascriptDateAdapter;

@Path("/behandelingProvider")
public class BehandelingProvider {
	@POST
	@RolesAllowed("user")
	@Path("/behandeling")
	@Produces("application/json")
	public Response saveBehandeling(@Context SecurityContext sc, @FormParam("naam") String naam,
			@FormParam("beschrijving") String beschrijving, @FormParam("prijsBehandeling") double prijs,
			@FormParam("uur") String uur, @FormParam("minuten") String minuten,
			@FormParam("geslachten") String geslachten) {
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		Date lengte = JavascriptDateAdapter.StringToDate(uur + ":" + minuten, "HH:mm");
		JSONArray jsonArray = new JSONArray(geslachten);
		for (int i = 0; i < jsonArray.length(); i++) {
			Behandeling behandeling = new BehandelingBuilder()
					.setBedrijf(bedrijf)
					.setNaam(naam)
					.setBeschrijving(beschrijving)
					.setPrijs(prijs)
					.setLengte(lengte)
					.setGeslacht(jsonArray.get(i).toString())
					.make();
					
			behandeling.save();
		}
		return Response.ok().build();
	}
	
	@PUT
	@RolesAllowed("user")
	@Path("/behandeling")
	@Produces("application/json")
	public Response wijzigBehandeling(@Context SecurityContext sc,
			@FormParam("id") int id,
			@FormParam("naam") String naam,
			@FormParam("beschrijving") String beschrijving, @FormParam("prijsBehandeling") double prijs,
			@FormParam("uur") String uur, @FormParam("minuten") String minuten,
			@FormParam("geslachten") String geslacht) {
		Date lengte = JavascriptDateAdapter.StringToDate(uur +":"+minuten, "HH:mm");
		Behandeling behandeling = new BehandelingBuilder()
				.setId(id)
				.setBedrijf(new Bedrijf(sc.getUserPrincipal().getName()))
				.setBeschrijving(beschrijving)
				.setNaam(naam)
				.setPrijs(prijs)
				.setGeslacht(geslacht)
				.setLengte(lengte)
				.make();
		behandeling.update();
		
		return Response.ok().build();
	}

	@DELETE
	@Path("/behandeling/{id}")
	@RolesAllowed("user")
	@Produces("application/json")
	public Response deleteBehandeling(@Context SecurityContext sc, @PathParam("id") int id) {
		Behandeling behandeling = new BehandelingBuilder().setBedrijf(new Bedrijf(sc.getUserPrincipal().getName()))
				.setId(id).make();
		if (behandeling.delete()) {
			return Response.ok().build();
		} else {
			return Response.status(500).build();
		}
	}
	
	@GET
	@Path("/alleBehandelingen/{data}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getBehandelingen(@Context SecurityContext sc, @PathParam("data") String data) {
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		HashMap<String, String> hmap = getData(data);
		bedrijf.zoekBehandelingen(Integer.parseInt(hmap.get("pageNumber")), 
				hmap.get("geslacht"), 
				hmap.get("sort"),
				hmap.get("zoek"));

		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Behandeling behandeling : bedrijf.getBehandelingen()) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("id", behandeling.getId());
			job.add("naam", behandeling.getNaam());
			job.add("beschrijving", behandeling.getBeschrijving());
			job.add("prijs", behandeling.getPrijs());
			job.add("geslacht", behandeling.getGeslacht());
			job.add("lengte", JavascriptDateAdapter.DateToString(behandeling.getLengte(), "HH:mm"));
			job.add("inkomsten", behandeling.getInkomsten());
			job.add("afspraken", behandeling.getAfspraken());
			jab.add(job);
		}
		return jab.build().toString();
	}

	@GET
	@Path("/getBehandeling/{id}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getBehandeling(@Context SecurityContext sc, @PathParam("id") int id) {
		Behandeling behandeling = new BehandelingBuilder().setBedrijf(new Bedrijf(sc.getUserPrincipal().getName()))
				.setId(id).make();
		behandeling.getInfo();
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("id", behandeling.getId());
		job.add("naam", behandeling.getNaam());
		job.add("beschrijving", behandeling.getBeschrijving());
		job.add("prijs", behandeling.getPrijs());
		job.add("geslacht", behandeling.getGeslacht());
		
		job.add("lengte", JavascriptDateAdapter.DateToString(behandeling.getLengte(), "HH:mm"));
		
		job.add("inkomsten", behandeling.getInkomsten());
		job.add("afspraken", behandeling.getAfspraken());
		return job.build().toString();
	}

	private HashMap<String, String> getData(String data) {
		HashMap<String, String> hmap = new HashMap<String, String>();
		String geslacht = "IS NOT NULL";
		String pageNumber = "1";
		String sort = "afspraken desc";
		String zoek = " ";
		for (String dataPoint : data.split("&")) {
			String[] dataArray = dataPoint.split("=");
			if (dataArray[0].equals("geslacht")) {
				int i = 0;
				for (String geslachtString : dataArray[1].split(",")) {
					if (i == 0) {
						geslacht = "='" + geslachtString + "'";
						i++;
					} else {
						geslacht = geslacht + "OR geslacht ='" + geslachtString + "'";
					}
				}
				if (geslacht.equals("='alle'")) {
					geslacht = "IS NOT NULL";
				}
			}
			if (dataArray[0].equals("pageNumber")) {
				pageNumber = dataArray[1];
			}
			if (dataArray[0].equals("sort")) {
				String sorting = dataArray[1];
				if (sorting.equals("meesteAfspraken")) {
					sort = "afspraken desc";
				} else if (sorting.equals("meesteInkomen")) {
					sort = "inkomsten desc";
				} else if (sorting.equals("minsteAfspraken")) {
					sort = "afspraken asc";
				} else if (sorting.equals("minsteInkomen")) {
					sort = "inkomsten asc";
				}
			}	
			if (dataArray[0].equals("zoek")) {
				String zoekString = dataArray[1];
				if(zoekString.equals("-")) {
					zoek = "";
				}  else {
					zoek = zoekString;
				}
			}
		}
		hmap.put("geslacht", geslacht);
		hmap.put("pageNumber", pageNumber);
		hmap.put("sort", sort);
		hmap.put("zoek", zoek);
		return hmap;
	}
}
