package com.swinkels.emperio.service;

import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.json.JSONArray;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Behandeling;
import com.swinkels.emperio.providers.BehandelingDao;
import com.swinkels.emperio.providers.BehandelingDaoImpl;

@Path("/plan")
public class BehandelingProvider {
	BehandelingDao behandelingDao = new BehandelingDaoImpl();
	// wordt gebruikt bij
		// behandeling maken
		//uitvoer
		// response
		// ok
		// error
	@POST
	@RolesAllowed("user")
	@Path("/behandeling")
	@Produces("application/json")
	public Response setBehandeling(@Context SecurityContext sc, @FormParam("naam") String naam,
			@FormParam("beschrijving") String beschrijving, @FormParam("prijs") double prijs,
			@FormParam("uur") String uur, @FormParam("minuten") String minuten,
			@FormParam("geslachten") String geslachten) {
		String bedrijfsNaam = sc.getUserPrincipal().getName();
		Bedrijf bedrijf = new Bedrijf(bedrijfsNaam);
		String lengteString = uur + ":" + minuten;
		Date lengte = ServiceFilter.StringToDateFormatter(lengteString, "HH:mm");

		JSONArray jsonArray = new JSONArray(geslachten);
		for (int i = 0; i < jsonArray.length(); i++) {
			Behandeling behandeling = new Behandeling(bedrijf, naam, beschrijving, prijs, lengte,
					jsonArray.get(i).toString());
			behandelingDao.setBehandeling(behandeling);
		}

		return Response.ok().build();
	}
}