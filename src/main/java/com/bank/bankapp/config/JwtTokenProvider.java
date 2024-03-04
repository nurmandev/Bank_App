package com.bank.bankapp.config;

import java.security.Key;
import java.util.Date;
import java.util.Base64.Decoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtTokenProvider {

    @Value("app.jwt-secret")
    private String jwtSecret;


    @Value("app.jwt-expiration")
    private long jwtExpirationDate;

    @SuppressWarnings("deprecation")
    public String generateToken(Authentication authentication ){
        String userName = authentication.getUsername();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + jwtExpirationDate); 

        return Jwts.builder()
            .setSubject(userName)
            .setIssuedAt(currentDate)
            .setExpiration(expirationDate).signWith(Key()).compact();
        
    }

    private Key Key() {
        byte[] bytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String getUserName(String token){
        @SuppressWarnings("deprecation")
            Claims claims = Jwts.parser()
            .setSigningKey(Key())
            .build().parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(Key()).build().parse(token);
            return true;
        } catch (ExpiredJwtException | IllegalArgumentException | SignatureException | MalformedJwtException e) {
           throw new RuntimeException(e);
        }
    }
}
