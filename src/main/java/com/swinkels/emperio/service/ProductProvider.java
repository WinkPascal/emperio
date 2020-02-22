package com.swinkels.emperio.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;

import com.swinkels.emperio.objects.product.Product;
import com.swinkels.emperio.objects.product.ProductInterface;
import com.swinkels.emperio.objects.product.ProductManager;
import com.swinkels.emperio.objects.security.Bedrijf;
import com.swinkels.emperio.objects.security.Security;

@Path("/product")
public class ProductProvider {

	@GET
	@Path("/producten/{data}")
	@RolesAllowed("user")
	@Produces("application/json")
	public String getProductenByPage(@Context SecurityContext sc, @PathParam("data") String data) throws ParseException {
		Security.setKey(sc.getUserPrincipal().getName());
		System.out.println(data);
		HashMap<String, String> dataHash = getData(data);
		
		ProductManager manager = new ProductManager(Integer.parseInt(dataHash.get("page")), dataHash.get("search"));
		List<Product> producten = manager.getProducten();

		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (ProductInterface product : producten) {
			HashMap<String, String> dto = product.toDTO();
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("id", dto.get("id"));
			job.add("hoeveelheid", dto.get("hoeveelheid"));
			job.add("naam", dto.get("naam"));
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
	@POST
	@RolesAllowed("user")
	@Path("/product")
	@Produces("application/json")
	public ResponseBuilder setProduct(@Context SecurityContext sc,
			@FormParam("hoeveelheidProductToevoegen") int hoeveelheidProductToevoegen,
			@FormParam("naamProductToevoegen") String naamProductToevoegen) {

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
