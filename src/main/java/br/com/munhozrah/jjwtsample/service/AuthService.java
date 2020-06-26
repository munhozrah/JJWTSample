package br.com.munhozrah.jjwtsample.service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

import br.com.munhozrah.jjwtsample.dto.UserDto;
import br.com.munhozrah.jjwtsample.util.KeyGenerator;
import br.com.munhozrah.jjwtsample.util.Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthService {
	@Inject
	KeyGenerator keyGenarator;

	public UserDto authenticate(UserDto user) throws Exception {
		/*In real application it actually consumes third-party service to authenticate user and ger role */
		return user.setRole(Util.ROLE_USER);
	}
	
	public String issueToken(UserDto userDto, UriInfo uriInfo) {
		Key key = keyGenarator.generateKey();
		if (key != null)
			return Jwts.builder().setSubject(userDto.getId())
				.setIssuer(uriInfo.getAbsolutePath().toString())
				.setIssuedAt(new Date())
				.setExpiration(toDate(LocalDateTime.now().plusMinutes(120L)))
				.signWith(SignatureAlgorithm.RS256, key)
				.claim(Util.ROLE_CLAIM, userDto.getRole())
				.compact();
		else
			return "";
	}

	private Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public Claims parseToken(String token) {
		Key key = keyGenarator.generateKey();
		return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
	}
}