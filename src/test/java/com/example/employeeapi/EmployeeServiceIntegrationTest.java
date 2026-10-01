package com.example.employeeapi;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EmployeeServiceIntegrationTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @AfterEach
    void cleanUp() {
        employeeRepository.deleteAll();
    }

    @Test
    void createAndFindEmployee() {
        Employee employee = new Employee(
                3001,
                "Charlie",
                "IT",
                8000,
                "charlie@example.com"
        );

        employeeService.createEmployee(employee);

        EmployeeResponse result = employeeService.findEmployeeById(3001);

        assertEquals(3001, result.id());
        assertEquals("Charlie", result.name());
        assertEquals("IT", result.department());
    }
}
