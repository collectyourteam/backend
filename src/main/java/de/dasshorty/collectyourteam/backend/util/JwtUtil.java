package de.dasshorty.collectyourteam.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key = Jwts.SIG.HS512.key().build();

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours validity
                .signWith(key)
                .compact();
    }

    public Claims extractAllClaims(String token) throws Exception {
        return Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) throws Exception {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) throws Exception {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String username) throws Exception {
        return (extractUsername(token).equals(username) && !isTokenExpired(token));
    }
}