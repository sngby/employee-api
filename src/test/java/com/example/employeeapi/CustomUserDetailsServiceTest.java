package com.example.employeeapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CustomUserDetailsServiceTest {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Test
    void shouldLoadUserFromDatabase() throws Exception {

        UserDetails user =
                userDetailsService.loadUserByUsername("alex");

        assertEquals("alex", user.getUsername());
        assertEquals("ROLE_USER", user.getAuthorities()
                .iterator()
                .next()
                .getAuthority());

        assertTrue(user.getPassword().startsWith("$2"));
    }
}
