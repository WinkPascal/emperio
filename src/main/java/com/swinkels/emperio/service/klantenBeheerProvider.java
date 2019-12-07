package com.swinkels.emperio.service;

import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
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

	@GET
	@Path("/getKlantPaginaSettings")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getKlantPaginaSettings(@Context SecurityContext sc) {
		Bedrijf bedrijf= new Bedrijf(sc.getUserPrincipal().getName());
		
		bedrijf = bedrijfDao.getKlantPaginaSettings(bedrijf);
		JsonObjectBuilder job = Json.createObjectBuilder();
		
		job.add("verplichtContactVeld", bedrijf.getVerplichtContactVeld());
		job.add("invoerveldEmail", bedrijf.getInvoerveldEmail());
		job.add("invoerveldTelefoon", bedrijf.getInvoerveldTelefoon());
		job.add("invoerveldAdres", bedrijf.getInvoerveldAdres());
		
		return job.build().toString();
	}
	
	@POST
	@RolesAllowed("user")
	@Produces("application/json")
	public Response getKlanten(@Context SecurityContext sc,
			@FormParam("contact") String contact,
			@FormParam("telefoon") boolean telefoon,
			@FormParam("email") boolean email,
			@FormParam("adres") boolean adres) {
		if(contact.equals("email") && email == false) {
			return Response.status(412, "De email moet kunnen worden ingevoerd "
			    + "als het een verplicht invoerveld is.").build();
		}
		if(contact.equals("telefoon") && telefoon == false) {
			return Response.status(412, 
				   "Het telefoon nummer moet kunnen worden ingevoerd als "
				 + "het een verplicht invoerveld is.").build();
		}
		
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
		
		if(bedrijfDao.setInvoerKlant(bedrijf, contact, telefoon, email, adres)) {
			System.out.println("GOED");
			return Response.status(202).build();
		} else {
			System.out.println("FOUT");
			return Response.status(500).build();
		}
	}
}
