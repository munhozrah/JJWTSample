package br.com.munhozrah.jjwtsample.util;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;

import br.com.munhozrah.jjwtsample.service.AuthService;
import io.jsonwebtoken.Claims;

public class Util {
	public static final String ROLE_CLAIM = "role";
	public static final String AUTHENTICATION_TYPE = "Bearer ";
	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_USER = "USER";
	
	@Inject
    AuthService authService;
	
	public boolean checkUserId(HttpHeaders httpHeaders, String id) {
		try {
			Claims claim = authService.parseToken(getToken(httpHeaders.getHeaderString(AUTHORIZATION)));
			if (Util.ROLE_ADMIN.equals(claim.get(Util.ROLE_CLAIM)))
				return true;		
			if (id.equals(claim.getSubject()))
				return true;
		} catch (Exception e) {
        	return false;
        }
		return false;
	}

	public String getToken (String authorizationHeader) {
		return authorizationHeader.replaceFirst(AUTHENTICATION_TYPE, "").trim();
	}
}
