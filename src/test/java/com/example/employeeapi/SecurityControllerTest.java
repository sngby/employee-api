package com.example.employeeapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void employeesShouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/employees"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void userCanAccessEmployees() throws Exception {
        mockMvc.perform(get("/employees")
                .with(user("alex").roles("USER")))
                .andExpect(status().isOk());
    }

    @Test
    void userCannotAccessAdmin() throws Exception {
        mockMvc.perform(get("/admin")
                .with(user("alex").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanAccessAdmin() throws Exception {
        mockMvc.perform(get("/admin")
                .with(user("admin")
                        .roles("ADMIN")))
                .andExpect(status().isOk());
    }

}
