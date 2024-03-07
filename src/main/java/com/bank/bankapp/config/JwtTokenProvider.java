package com.bank.bankapp.config;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.io.Decoders;
// import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;


    @Value("${app.jwt-expiration}")
    private long jwtExpirationDate;

    @SuppressWarnings("deprecation")
    public String generateToken(Authentication authentication ){
        try {

        String userName = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + jwtExpirationDate); 

        
        return Jwts.builder()
            .setSubject(userName)
            .setIssuedAt(currentDate)
            .setExpiration(expirationDate)
            .signWith(Key())
            .compact();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        
    }

    private Key Key() {
        if (jwtSecret == null) {
            throw new IllegalArgumentException("jwtSecret is null");
        }
        byte[] bytes = Decoders.BASE64.decode(jwtSecret);
        System.out.println(jwtSecret);
        return Keys.hmacShaKeyFor(bytes);
        
    }

    public String getUserName(String token){
            Claims claims = Jwts.parser()
            .setSigningKey(Key())
            .build().parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser()
            .setSigningKey(Key())
            .build()
            .parse(token);
            return true;
        } catch (ExpiredJwtException | 
        IllegalArgumentException |
         SignatureException | MalformedJwtException e) {
           throw new RuntimeException(e);
        }
    }
}
