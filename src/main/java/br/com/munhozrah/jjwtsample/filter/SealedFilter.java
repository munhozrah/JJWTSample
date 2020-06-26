package br.com.munhozrah.jjwtsample.filter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;

import br.com.munhozrah.jjwtsample.service.AuthService;
import br.com.munhozrah.jjwtsample.util.ReturnMessages;
import br.com.munhozrah.jjwtsample.util.Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Provider
@Sealed(value = { "" })
public class SealedFilter implements ContainerRequestFilter {
	private static final ServerResponse INVALID_TOKEN = new ServerResponse(
			ReturnMessages.getMsg(ReturnMessages.INVALID_TOKEN), ReturnMessages.INVALID_TOKEN.getReturnCode(),
			new Headers<Object>());

	private static final ServerResponse SERVER_ERROR = new ServerResponse(
			ReturnMessages.getMsg(ReturnMessages.SERVER_ERROR), ReturnMessages.SERVER_ERROR.getReturnCode(), new Headers<Object>());

	private static final ServerResponse ACCESS_FORBIDDEN = new ServerResponse(
			ReturnMessages.getMsg(ReturnMessages.ACCESS_FORBIDDEN), ReturnMessages.ACCESS_FORBIDDEN.getReturnCode(),
			new Headers<Object>());

	@Inject
	AuthService authService;

	@Inject
	Util util;

	public void filter(ContainerRequestContext requestContext) throws IOException {
		ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext
				.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
		Method method = methodInvoker.getMethod();

		if (method.isAnnotationPresent(PermitAll.class))
			return;

		if (method.isAnnotationPresent(DenyAll.class)) {
			requestContext.abortWith(ACCESS_FORBIDDEN);
			return;
		}

		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (authorizationHeader == null || authorizationHeader.isEmpty()
				|| !authorizationHeader.startsWith(Util.AUTHENTICATION_TYPE)) {
			requestContext.abortWith(ACCESS_FORBIDDEN);
			return;
		}

		String token = util.getToken(authorizationHeader);
		Claims claims = null;
		try {
			claims = authService.parseToken(token);
		} catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException e) {
			requestContext.abortWith(INVALID_TOKEN);
			return;
		} catch (Exception e) {
			requestContext.abortWith(SERVER_ERROR);
			return;
		}

		try {
			if (method.isAnnotationPresent(Sealed.class)) {
				Sealed rolesAnnotation = method.getAnnotation(Sealed.class);
				Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
				if (!isUserAllowed(claims.get(Util.ROLE_CLAIM, String.class), rolesSet)) {
					requestContext.abortWith(ACCESS_FORBIDDEN);
					return;
				}
			}
		} catch (Exception e) {
			requestContext.abortWith(SERVER_ERROR);
			return;
		}
	}

	private boolean isUserAllowed(String role, Set<String> roleSet) {
		return ((roleSet.contains(role)) ? true : false);
	}
}