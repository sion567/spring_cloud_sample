package com.shop.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

public class JwtUtils {

    private JwtUtils() {
    }

    public static String generateToken(String secret, Long userId, String username, List<String> roles, long expirationMs) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    public static Claims parseToken(String secret, String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static boolean validateToken(String secret, String token) {
        try {
            parseToken(secret, token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Long getUserId(String secret, String token) {
        Claims claims = parseToken(secret, token);
        return claims.get("userId", Long.class);
    }

    public static String getUsername(String secret, String token) {
        Claims claims = parseToken(secret, token);
        return claims.getSubject();
    }

    @SuppressWarnings("unchecked")
    public static List<String> getRoles(String secret, String token) {
        Claims claims = parseToken(secret, token);
        return claims.get("roles", List.class);
    }

    public static boolean isTokenExpired(String secret, String token) {
        try {
            Claims claims = parseToken(secret, token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public static String refreshToken(String secret, String token, long additionalExpirationMs) {
        Claims claims = parseToken(secret, token);
        Long userId = claims.get("userId", Long.class);
        String username = claims.getSubject();
        @SuppressWarnings("unchecked")
        List<String> roles = claims.get("roles", List.class);

        Date now = new Date();
        Date newExpiration = new Date(now.getTime() + additionalExpirationMs);

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(newExpiration)
                .signWith(key)
                .compact();
    }
}
