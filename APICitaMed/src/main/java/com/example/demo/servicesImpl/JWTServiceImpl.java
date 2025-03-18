package com.example.demo.servicesImpl;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Service("JwtService")
public class JWTServiceImpl {
	
	 private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	 private static final long EXPIRATION_TIME = 86400000;

	    public String generateToken(String username) {
	        return Jwts.builder()
	                .subject(username)
	                .issuedAt(new Date())
	                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
	                .signWith(SECRET_KEY)
	                .compact();
	    }

	    public String extractUsername(String token) {
	        return Jwts.parser()
	        	    .setSigningKey(SECRET_KEY)
	        	    .build()
	        	    .parseClaimsJws(token)
	                .getBody()
	                .getSubject();
	    }
	    
	    
}

