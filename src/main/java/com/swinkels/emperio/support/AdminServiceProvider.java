package com.swinkels.emperio.support;

import java.util.ArrayList;

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

import com.swinkels.emperio.objects.security.Bedrijf;
import com.swinkels.emperio.providers.Bedrijf.BedrijfDao;
import com.swinkels.emperio.providers.Bedrijf.BedrijfDaoImpl;

@Path("/adminService")
@RolesAllowed("admin")
public class AdminServiceProvider {
	BedrijfDao bedrijfDao = new BedrijfDaoImpl();

	
	@GET
	@Path("/getBedrijven/{page}")
	@Produces("application/json")
	public Response getKlanten(@Context SecurityContext sc, 
									@PathParam("page") int page ) {
		ArrayList<Bedrijf> bedrijven = bedrijfDao.getBedrijven(page);
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Bedrijf bedrijf : bedrijven) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			System.out.println(bedrijf.getEmail());

			job.add("email", bedrijf.getEmail());
			job.add("adres", bedrijf.getAdres());
			job.add("naam", bedrijf.getNaam());
			job.add("telefoon", bedrijf.getTel());
			
			jab.add(job);
		}
		
		return Response.ok(jab.build().toString()).build();
	}
	
	@POST
	@Path("/bedrijf")
	@Produces("application/json")
	public Response setKlant(@FormParam("naamToevoegen") String naamToevoegen,
			@FormParam("emailToevoegen") String emailToevoegen,
			@FormParam("wachtwoordToevoegen") String wachtwoordToevoegen,
			@FormParam("telefoonToevoegen") String telefoonToevoegen,
			@FormParam("adresToevoegen") String adresToevoegen) {
		System.out.println("=======================");
		//validaties op deze bitch gooien
		Bedrijf bedrijf = new Bedrijf(emailToevoegen, naamToevoegen, telefoonToevoegen, adresToevoegen, wachtwoordToevoegen);
		
		if(bedrijfDao.saveBedrijf(bedrijf)) {
			return Response.ok().build();
		} 
		return null;
	}
}