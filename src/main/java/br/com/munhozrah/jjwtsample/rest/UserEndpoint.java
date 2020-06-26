package br.com.munhozrah.jjwtsample.rest;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ServerResponse;

import br.com.munhozrah.jjwtsample.dto.UserDto;
import br.com.munhozrah.jjwtsample.service.AuthService;
import br.com.munhozrah.jjwtsample.util.ReturnMessages;

@Path("/users")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
public class UserEndpoint {
	private static final String AUTHENTICATION_TYPE = "Bearer";
	
	private static final ServerResponse SERVER_ERROR = new ServerResponse(
			ReturnMessages.getMsg(ReturnMessages.SERVER_ERROR),
			ReturnMessages.SERVER_ERROR.getReturnCode(), new Headers<Object>());

	private static final ServerResponse LOGIN_UNAUTHORIZED = new ServerResponse(
			ReturnMessages.getMsg(ReturnMessages.LOGIN_UNAUTHORIZED),
			ReturnMessages.LOGIN_UNAUTHORIZED.getReturnCode(), new Headers<Object>());
	
	@Inject
	AuthService authService;
	
	@Context
	private UriInfo uriInfo;

	@POST
	@Path("/login")
	@PermitAll
	@Consumes(MediaType.APPLICATION_JSON + "; charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
	public Response authenticateUser(UserDto user) {
		try {
			user = authService.authenticate(user);
			String token = authService.issueToken(user, uriInfo);
			if (token.isEmpty())
				throw new Exception();
	
			return Response.ok().header(AUTHORIZATION, AUTHENTICATION_TYPE + " " + token).entity(user).build();
		} catch (SecurityException e) {
			return LOGIN_UNAUTHORIZED;
		} catch (Exception e) {
			return SERVER_ERROR;
		}
	}
}

