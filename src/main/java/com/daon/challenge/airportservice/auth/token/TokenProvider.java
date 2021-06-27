package com.daon.challenge.airportservice.auth.token;

import com.daon.challenge.airportservice.auth.principal.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final TokenProperties tokenProperties;

    public String generateToken(Authentication authentication) {
        var userPrincipal = (UserPrincipal) authentication.getPrincipal();
        var issuedAt = LocalDateTime.now();

        return Jwts.builder()
                    .setSubject(userPrincipal.getUsername())
                    .setIssuedAt(toDate(issuedAt))
                    .setExpiration(toDate(issuedAt.plusSeconds(tokenProperties.getExpirationTime())))
                    .signWith(SignatureAlgorithm.HS512, tokenProperties.getSecret())
                    .compact();
    }

    public boolean isValid(String token) {
        return getAllClaims(token).getExpiration().after(toDate(LocalDateTime.now()));
    }

    public String getUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                    .setSigningKey(tokenProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
    }

    private Date toDate(LocalDateTime time) {
        return Date.from(time.toInstant(ZoneOffset.ofTotalSeconds(0)));
    }
}
