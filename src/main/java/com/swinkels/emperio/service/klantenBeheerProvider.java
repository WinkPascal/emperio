package com.swinkels.emperio.service;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.providers.BedrijfDao;
import com.swinkels.emperio.providers.BedrijfDaoImpl;

@Path("/klantenBeheer")
public class klantenBeheerProvider {
	BedrijfDao bedrijfDao = new BedrijfDaoImpl();

	@POST
	@RolesAllowed("user")
	@Produces("application/json")
	public Response getKlanten(@Context SecurityContext sc,
			@FormParam("contact") String contact,
			@FormParam("telefoon") boolean telefoon,
			@FormParam("email") boolean email,
			@FormParam("adres") boolean adres) {
		System.out.println("test");
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		bedrijfDao.setInvoerKlant(bedrijf, contact, telefoon, email, adres);

		return Response.ok().build();
	}
}
