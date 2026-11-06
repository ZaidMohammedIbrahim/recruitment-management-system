package com.zaid.recruitment.Auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        // Generate a sufficiently long test key and encode it as Base64.
        SecretKey testKey = Keys.secretKeyFor(
                io.jsonwebtoken.SignatureAlgorithm.HS256);

        String secret = Base64.getEncoder()
                .encodeToString(testKey.getEncoded());

        jwtService = new JwtService(secret);
    }

    @Test
    void shouldGenerateAndParseToken() {
        String token = jwtService.generateToken(
                "candidate", "CANDIDATE");

        Claims claims = jwtService.parseToken(token);

        assertEquals("candidate", claims.getSubject());
        assertEquals("CANDIDATE", claims.get("role"));
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    void shouldValidateUnexpiredClaims() {
        String token = jwtService.generateToken(
                "candidate", "CANDIDATE");

        Claims claims = jwtService.parseToken(token);

        assertTrue(jwtService.validateToken(claims));
    }

    @Test
    void shouldRejectExpiredClaims() {
        Date now = new Date();

        Claims expiredClaims = Jwts.claims()
                .subject("candidate")
                .expiration(new Date(now.getTime() - 60_000))
                .build();

        assertFalse(jwtService.validateToken(expiredClaims));
    }

    @Test
    void shouldReturnFalseWhenExpirationIsMissing() {
        Claims claims = Jwts.claims()
                .subject("candidate")
                .build();

        assertFalse(jwtService.validateToken(claims));
    }

    @Test
    void shouldRejectTokenSignedWithDifferentKey() {
        String token = jwtService.generateToken(
                "candidate", "CANDIDATE");

        SecretKey anotherKey = Keys.secretKeyFor(
                io.jsonwebtoken.SignatureAlgorithm.HS256);

        JwtService anotherJwtService = new JwtService(
                Base64.getEncoder()
                        .encodeToString(anotherKey.getEncoded()));

        assertThrows(
                io.jsonwebtoken.JwtException.class,
                () -> anotherJwtService.parseToken(token)
        );
    }
}