package com.myl117.authservice.authservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final String secret = "+eVbSYTDyFL5/dRYIce1RD6GUZaiUcxPgLdGmEAb8HYgdnhL+POTzRlFdrCU1vJA"; // TODO: need to move to env later
    private final long expirationMs =  60 * 60 * 1000; // 1 hour (60 mins)

    private Key getSigningKey() {
      return Keys.hmacShaKeyFor(secret.getBytes());
    }
    
     public String generateToken(String email) {
      return Jwts.builder()
        .setSubject(email)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
    }

    public String extractEmail(String token) {
      return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
    }
}
