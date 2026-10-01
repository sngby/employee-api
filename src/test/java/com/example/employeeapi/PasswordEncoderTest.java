package com.example.employeeapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PasswordEncoderTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldVerifyCorrectPassword() {

        String encodedPassword =
                passwordEncoder.encode("password123");

        assertTrue(
                passwordEncoder.matches("password123", encodedPassword)
        );

        assertFalse(
                passwordEncoder.matches("wrongpassword", encodedPassword)
        );
    }
}
