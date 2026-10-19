package com.example.employeeapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void hello() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello from my Employee API!"));
    }

    @Test
    void getEmployeeById() throws Exception {
        EmployeeResponse response =
                new EmployeeResponse(
                        1001,
                        "Alex Tan",
                        "IT",
                        "alex@example.com"
                );

        when(employeeService.findEmployeeById(1001))
                .thenReturn(response);

        mockMvc.perform(get("/employees/1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1001))
                .andExpect(jsonPath("$.name").value("Alex Tan"))
                .andExpect(jsonPath("$.department").value("IT"))
                .andExpect(jsonPath("$.email").value("alex@example.com"));
    }

    @Test
    void searchEmployeesByName() throws Exception {
        EmployeeResponse response = new EmployeeResponse(
                1001,
                "Alex Tan",
                "IT",
                "alex@example.com"
        );

        when(employeeService.searchEmployeesByName("ale"))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/employees/search?name=ale"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1001))
                .andExpect(jsonPath("$[0].name").value("Alex Tan"))
                .andExpect(jsonPath("$[0].department").value("IT"))
                .andExpect(jsonPath("$[0].email").value("alex@example.com"));
    }

    @Test
    void searchEmployeesByName_whenEmployeeDoesNotExist() throws Exception {
        when(employeeService.searchEmployeesByName("nobody"))
                .thenReturn(List.of());

        mockMvc.perform(get("/employees/search?name=nobody"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getEmployeeById_whenEmployeeDoesNotExist() throws Exception {

        when(employeeService.findEmployeeById(9999))
                .thenThrow(new EmployeeNotFoundException(9999));

        mockMvc.perform(get("/employees/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found"))
                .andExpect(jsonPath("$.id").value(9999));

    }

    @Test
    void createEmployee() throws Exception {

        EmployeeResponse response =
                new EmployeeResponse(
                        1002,
                        "Alice",
                        "Finance",
                        "alice@example.com"
                );

        when(employeeService.createEmployee(any(Employee.class)))
                .thenReturn(response);

        String requestBody = """
                {
                    "id":1002,
                    "name": "Alice",
                    "department": "Finance",
                    "salary": 7000,
                    "email": "alice@example.com"
                }
                """;

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1002))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.department").value("Finance"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void createEmployee_whenRequestIsInvalid() throws Exception {

        String requestBody = """
                {
                    "id": 1003,
                    "name": "",
                    "department": "IT",
                    "salary": 0,
                    "email": "test@example.com"
                }
                """;

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.salary").exists());

    }

    @Test
    void updateEmployee() throws Exception {

        EmployeeResponse response =
                new EmployeeResponse(
                        1001,
                        "Alex Tan Updated",
                        "Engineering",
                        "alex.updated@example.com"
                );

        when(employeeService.updateEmployee(
                eq(1001),
                any(UpdateEmployeeRequest.class)
        )).thenReturn(response);

        String requestBody = """
                {
                    "name": "Alex Tan Updated",
                    "department": "Engineering",
                    "salary": 8500,
                    "email": "alex.updated@example.com"
                }
                """;

        mockMvc.perform(put("/employees/1001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1001))
                .andExpect(jsonPath("$.name").value("Alex Tan Updated"))
                .andExpect(jsonPath("$.department").value("Engineering"))
                .andExpect(jsonPath("$.email").value("alex.updated@example.com"));

    }

    @Test
    void updateEmployee_whenEmployeeDoesNotExist() throws Exception {
        when(employeeService.updateEmployee(
                eq(9999),
                any(UpdateEmployeeRequest.class)
        )).thenThrow(new EmployeeNotFoundException(9999));

        String requestBody = """
                {
                    "name": "Someone",
                    "department": "IT",
                    "salary": 7000,
                    "email": "someone@example.com"
                }
                """;

        mockMvc.perform(put("/employees/9999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found"))
                .andExpect(jsonPath("$.id").value(9999));
    }

    @Test
    void deleteEmployee() throws Exception {
       mockMvc.perform(delete("/employees/1001"))
               .andExpect(status().isNoContent());

    }

    @Test
    void deleteEmployee_whenEmployeeDoesNotExist() throws Exception {
        doThrow(new EmployeeNotFoundException(9999))
                .when(employeeService)
                .deleteEmployee(9999);

        mockMvc.perform(delete("/employees/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found"))
                .andExpect(jsonPath("$.id").value(9999));
    }
}
