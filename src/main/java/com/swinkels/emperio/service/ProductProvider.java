package com.swinkels.emperio.service;

import java.text.ParseException;
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
import javax.ws.rs.core.Response.ResponseBuilder;

import com.swinkels.emperio.objects.Bedrijf;
import com.swinkels.emperio.objects.Product;
import com.swinkels.emperio.providers.BedrijfDao;
import com.swinkels.emperio.providers.BedrijfDaoImpl;

@Path("/product")
public class ProductProvider {
	BedrijfDao bedrijfDao = new BedrijfDaoImpl();

	@GET
	@Path("/producten/{page}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getProductenByPage(@Context SecurityContext sc, @PathParam("page") int page) throws ParseException {
		// uitvoer
		// 10 producten (id, hoeveelheid, naam)
		Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());

		ArrayList<Product> producten = bedrijfDao.getProductenByPage(bedrijf, page);

		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Product product : producten) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("id", product.getId());
			job.add("hoeveelheid", product.getHoeveelheid());
			job.add("naam", product.getNaam());
			jab.add(job);
		}
		return jab.build().toString();
	}

	// wordt gebruikt bij
	// product toevoegen
//uitvoer
	// response
	// ok
	// error
	@POST
	@RolesAllowed("user")
	@Path("/product")
	@Produces("application/json")
	public ResponseBuilder setProduct(@Context SecurityContext sc,
			@FormParam("hoeveelheidProductToevoegen") int hoeveelheidProductToevoegen,
			@FormParam("naamProductToevoegen") String naamProductToevoegen) {

		// hier moeten validaties gedaan worden
		try {
			Bedrijf bedrijf = new Bedrijf(sc.getUserPrincipal().getName());
			Product product = new Product(bedrijf, hoeveelheidProductToevoegen, naamProductToevoegen);
			bedrijfDao.setProduct(product);
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(500);
		}

		return Response.status(201);
	}

}
