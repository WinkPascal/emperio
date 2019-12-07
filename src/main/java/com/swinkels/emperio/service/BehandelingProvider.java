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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.json.JSONArray;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Behandeling;
import com.swinkels.emperio.objects.BehandelingBuilder;
import com.swinkels.emperio.providers.BehandelingDao;
import com.swinkels.emperio.providers.BehandelingDaoImpl;
import com.swinkels.emperio.support.Adapter;
import com.swinkels.emperio.support.DatabaseDateAdapter;
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
			Behandeling behandeling = new Behandeling(bedrijf, naam, beschrijving, prijs, lengte,
					jsonArray.get(i).toString());
			behandeling.save();
		}
		return Response.ok().build();
	}

	@GET
	@Path("/alleBehandelingen/{data}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getBehandelingen(@Context SecurityContext sc, @PathParam("data") String data) {
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		HashMap<String, String> hmap = getData(data);
		bedrijf.zoekBehandelingen(Integer.parseInt(hmap.get("pageNumber")), hmap.get("geslacht"), hmap.get("sort"));

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

	private HashMap<String, String> getData(String data) {
		HashMap<String, String> hmap = new HashMap<String, String>();
		String geslacht = "IS NOT NULL";
		String pageNumber = "1";
		String sort = "afspraken desc";
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
		}
		hmap.put("geslacht", geslacht);
		hmap.put("pageNumber", pageNumber);
		hmap.put("sort", sort);
		return hmap;
	}

}
