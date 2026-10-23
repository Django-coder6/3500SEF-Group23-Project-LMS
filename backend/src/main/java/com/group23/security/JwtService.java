package com.group23.security;

import com.group23.user.User;
import com.group23.user.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final String issuer;
    private final Duration tokenLifetime;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.issuer}") String issuer,
            @Value("${app.jwt.token-lifetime}") Duration tokenLifetime
    ) {
        if (issuer == null || issuer.isBlank()) {
            throw new IllegalArgumentException("JWT issuer must not be blank");
        }
        if (tokenLifetime == null || tokenLifetime.isNegative() || tokenLifetime.isZero()) {
            throw new IllegalArgumentException("JWT token lifetime must be positive");
        }
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.tokenLifetime = tokenLifetime;
    }

    public String issue(User user) {
        Instant now = Instant.now();
        var builder = Jwts.builder()
                .issuer(issuer)
                .subject(user.getId().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(tokenLifetime)))
                .claim("role", user.getRole().name());

        if (user.getCollectionPointId() != null) {
            builder.claim("collectionPointId", user.getCollectionPointId().toString());
        }

        return builder.signWith(signingKey).compact();
    }

    public JwtClaims parse(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String collectionPointId = claims.get("collectionPointId", String.class);
        return new JwtClaims(
                UUID.fromString(claims.getSubject()),
                UserRole.valueOf(claims.get("role", String.class)),
                collectionPointId == null ? null : UUID.fromString(collectionPointId)
        );
    }

    public record JwtClaims(UUID userId, UserRole role, UUID collectionPointId) {
    }
}
