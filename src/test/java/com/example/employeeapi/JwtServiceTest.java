package com.example.employeeapi;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Test
    void shouldGenerateToken() {
        String token = jwtService.generateToken("alex", "USER");

        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3);
    }

    @Test
    void shouldValidateToken() {
        String token = jwtService.generateToken("alex", "USER");

        assertTrue(jwtService.validateToken(token));
    }

    @Test
    void shouldRejectTamperedToken() {
        String token = jwtService.generateToken("alex", "USER");

        String[] parts = token.split("\\.");

        String tempPayload =
                Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString("{\"sub\":\"alex\",\"role\":\"ADMIN\"}"
                        .getBytes(StandardCharsets.UTF_8)
                );

        String tamperedToken =
                parts[0] + "." + tempPayload + "." + parts[2];

        assertFalse(jwtService.validateToken(tamperedToken));
    }

    @Test
    void shouldRejectExpiredToken() {
        String token = Jwts.builder()
                .subject("alex")
                .claim("role", "USER")
                .issuedAt(new Date(System.currentTimeMillis() - 10000))
                .expiration(new Date(System.currentTimeMillis() - 5000))
                .signWith(
                        Keys.hmacShaKeyFor(
                                jwtSecret.getBytes(StandardCharsets.UTF_8)
                        )
                )
                .compact();

        assertFalse(jwtService.validateToken(token));
    }
}
