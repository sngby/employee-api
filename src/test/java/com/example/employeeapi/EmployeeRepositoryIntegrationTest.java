package com.example.employeeapi;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EmployeeRepositoryIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @AfterEach
    void cleanUp() {
        employeeRepository.deleteAll();
    }

    @Test
    void saveAndFindEmployees() {
        Employee employee = new Employee(
                2001,
                "Alice",
                "Finance",
                7000,
                "alice@example.com"
        );

        employeeRepository.save(employee);

        Employee foundEmployee = employeeRepository.findById(2001).orElseThrow();

        assertEquals(2001, foundEmployee.getId());
        assertEquals("Alice", foundEmployee.getName());
        assertEquals("Finance", foundEmployee.getDepartment());
        assertEquals(7000, foundEmployee.getSalary());
        assertEquals("alice@example.com", foundEmployee.getEmail());
    }

    @Test
    void findEmployee_whenEmployeeDoesNotExist() {
        Optional<Employee> result = employeeRepository.findById(9999);

        assertEquals(Optional.empty(), result);
    }

    @Test
    void deleteEmployee() {
        Employee employee = new Employee(
                2002,
                "Bob",
                "HR",
                6000,
                "bob@example.com"
        );

        employeeRepository.save(employee);

        employeeRepository.deleteById(2002);

        Optional<Employee> result = employeeRepository.findById(2002);

        assertEquals(Optional.empty(), result);
    }
}
