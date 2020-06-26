package br.com.munhozrah.jjwtsample.rest;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ServerResponse;

import br.com.munhozrah.jjwtsample.filter.Sealed;
import br.com.munhozrah.jjwtsample.util.ReturnMessages;
import br.com.munhozrah.jjwtsample.util.Util;

@Path("/sealed")
public class SealedEndpoint {
	private static final ServerResponse ACCESS_FORBIDDEN = new ServerResponse(
			ReturnMessages.getMsg(ReturnMessages.ACCESS_FORBIDDEN), ReturnMessages.ACCESS_FORBIDDEN.getReturnCode(),
			new Headers<Object>());

	private static final ServerResponse SERVER_ERROR = new ServerResponse(
			ReturnMessages.getMsg(ReturnMessages.SERVER_ERROR), ReturnMessages.SERVER_ERROR.getReturnCode(), new Headers<Object>());

	@Context
	private HttpHeaders httpHeaders;

	@Inject
	Util util;

	@GET
	@Sealed({Util.ROLE_USER})
	@Produces(MediaType.TEXT_PLAIN)
	public Response sealed(@QueryParam("id") String id) {
		try {
			if (util.checkUserId(httpHeaders, id))
				return Response.ok().entity(id).build();
			else
				return ACCESS_FORBIDDEN;

		} catch (Exception e) {
			return SERVER_ERROR;
		}
	}

	@GET
	@Path("/message")
	@PermitAll
	@Produces(MediaType.TEXT_PLAIN)
	public Response sealedMessage(@QueryParam("message") String message) {
		return Response.ok().entity(message).build();
	}
}