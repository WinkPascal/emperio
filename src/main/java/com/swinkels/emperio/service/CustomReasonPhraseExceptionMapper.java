package com.swinkels.emperio.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

@Path("/test")
public class CustomReasonPhraseExceptionMapper implements ExceptionMapper<CustomReasonPhraseException> {
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/testss")
	@GET
	public Response toResponse(CustomReasonPhraseException bex) {
		System.out.println("SSSS");
		return Response.status(new CustomReasonPhraseExceptionStatusType(Status.BAD_REQUEST))
				.entity("Custom Reason Phrase exception occured : " + bex.getMessage())
				.build();
	}

}