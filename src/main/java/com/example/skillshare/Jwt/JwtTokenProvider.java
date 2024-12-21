package com.example.skillshare.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    // Load secret key from application.properties or environment variable
    @Value("${jwt.secret}")
    private String secret;

    // Load expiration time (in milliseconds) from application.properties or use default (24 hours)
    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        try {
            return Jwts.builder()
                    .setSubject(email) // Email is the subject
                    .setIssuer("SkillShareApp")
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                    .compact();
        } catch (Exception e) {
            logger.error("Error generating JWT token: {}", e.getMessage(), e);
            throw new RuntimeException("Error generating JWT token");
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token); // Parse and validate
            return true;
        } catch (Exception e) {
            logger.error("Invalid token: {}", e.getMessage(), e);
            return false;
        }
    }

    public String extractEmail(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject(); // Extract subject (email)
        } catch (Exception e) {
            logger.error("Error extracting email from token: {}", e.getMessage(), e);
            return null;
        }
    }
}
