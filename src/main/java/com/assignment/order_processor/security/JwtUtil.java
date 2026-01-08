package com.assignment.order_processor.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    private final SecretKey secretKey =
            Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(Authentication authentication) {

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("roles", authentication.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 60 * 60 * 1000)
                )
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public List<SimpleGrantedAuthority> extractRoles(String token) {
        List<Map<String, String>> roles =
                getClaims(token).get("roles", List.class);

        return roles.stream()
                .map(r -> new SimpleGrantedAuthority(r.get("authority")))
                .toList();
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

