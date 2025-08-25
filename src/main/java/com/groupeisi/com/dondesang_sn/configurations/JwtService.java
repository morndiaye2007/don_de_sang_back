package com.groupeisi.com.dondesang_sn.configurations;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationMillis}")
    private long expirationMillis;

    private SecretKey secretKey;

    public String generateToken(String subject, Map<String, Object> claims) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            Date exp = claims.getExpiration();
            return exp == null || exp.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public <T> T extractClaim(String token, Function<io.jsonwebtoken.Claims, T> resolver) {
        var claims = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return resolver.apply(claims);
    }

    private SecretKey getSignInKey() {
        if (secretKey == null) {
            // Si la clé par défaut est trop courte, générer une clé sécurisée
            if (secret == null || secret.length() < 32 || "change_this_secret".equals(secret)) {
                // Générer une clé sécurisée de 256 bits (32 bytes)
                secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            } else {
                try {
                    // Essayer de décoder en base64 d'abord
                    byte[] keyBytes = java.util.Base64.getDecoder().decode(secret);
                    if (keyBytes.length < 32) {
                        // Si la clé est trop courte, générer une nouvelle clé
                        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
                    } else {
                        secretKey = Keys.hmacShaKeyFor(keyBytes);
                    }
                } catch (IllegalArgumentException ex) {
                    // Fallback: utiliser la clé brute si elle est suffisamment longue
                    if (secret.length() >= 32) {
                        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                    } else {
                        // Générer une clé sécurisée
                        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
                    }
                }
            }
        }
        return secretKey;
    }
}


