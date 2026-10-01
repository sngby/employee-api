package com.example.employeeapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void findEmployeeById() {

        Employee employee = new Employee(
                1001,
                "Alex Tan",
                "IT",
                5000,
                "alex@example.com"
        );

        when(employeeRepository.findById(1001))
                .thenReturn(Optional.of(employee));

        EmployeeResponse result = employeeService.findEmployeeById(1001);

        assertEquals(1001, result.id());

        verify(employeeRepository, times(1)).findById(1001);
    }

    @Test
    void findEmployeeById_whenEmployeeDoesNotExist() {
        when(employeeRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.findEmployeeById(9999)
        );

        verify(employeeRepository).findById(9999);
    }

    @Test
    void createEmployee_whenEmployeeDoesNotExist() {
        Employee employee = new Employee(
                1002,
                "John",
                "HR",
                6000,
                "john@example.com"
        );

        when(employeeRepository.findById(1002))
                .thenReturn(Optional.empty());

        when(employeeRepository.save(employee))
                .thenReturn(employee);

        EmployeeResponse result = employeeService.createEmployee(employee);

        assertEquals(1002, result.id());
        verify(employeeRepository).findById(1002);
        verify(employeeRepository).save(employee);
    }

    @Test
    void createEmployee_whenEmployeeAlreadyExists() {
        Employee employee = new Employee(
                1002,
                "John",
                "HR",
                6000,
                "john@example.com"
        );

        when(employeeRepository.findById(1002))
                .thenReturn(Optional.of(employee));

        assertThrows(
                DuplicateEmployeeException.class,
                () -> employeeService.createEmployee(employee)
        );

        verify(employeeRepository).findById(1002);
        verify(employeeRepository, never()).save(employee);
    }

    @Test
    void updateEmployee_whenEmployeeExists() {
        Employee existingEmployee = new Employee(
                1001,
                "Alex Tan",
                "IT",
                5000,
                "alex@example.com"
        );

        UpdateEmployeeRequest request = new UpdateEmployeeRequest(
                "Alex Tan",
                "Engineering",
                5500,
                "alex.new@example.com"
        );

        when(employeeRepository.findById(1001))
                .thenReturn(Optional.of(existingEmployee));

        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EmployeeResponse result = employeeService.updateEmployee(1001, request);

        assertEquals(1001, result.id());
        assertEquals("Engineering", result.department());
        assertEquals("alex.new@example.com", result.email());

        ArgumentCaptor<Employee> captor =
                ArgumentCaptor.forClass(Employee.class);

        verify(employeeRepository).save(
                captor.capture()
        );

        Employee capturedEmployee = captor.getValue();
        assertEquals("Engineering", capturedEmployee.getDepartment());
        assertEquals(5500, capturedEmployee.getSalary());
        assertEquals("alex.new@example.com", capturedEmployee.getEmail());
    }

    @Test
    void updateEmployee_whenEmployeeDoesNotExist() {
        UpdateEmployeeRequest request = new UpdateEmployeeRequest(
                "Alex Tan",
                "Engineering",
                5500,
                "alex.new@example.com"
        );

        when(employeeRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.updateEmployee(9999, request)
        );

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void deleteEmployee_whenEmployeeExists() {
        Employee employee = new Employee(
                1001,
                "Alex Tan",
                "IT",
                5000,
                "alex@example.com"
        );

        when(employeeRepository.findById(1001))
                .thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1001);

        verify(employeeRepository).findById(1001);
        verify(employeeRepository).delete(employee);
    }

    @Test
    void deleteEmployee_whenEmployeeDoesNotExist() {
        when(employeeRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                EmployeeNotFoundException.class,
                ()->employeeService.deleteEmployee(9999)
        );

        verify(employeeRepository, never()).delete(any(Employee.class));

    }

    @Test
    void findAllEmployees_whenEmployeesExist() {

        Employee employee1 = new Employee(
                1001,
                "Alex Tan",
                "IT",
                5000,
                "alex@example.com"
        );

        Employee employee2 = new Employee(
                1002,
                "John",
                "HR",
                6000,
                "john@example.com"
        );

        when(employeeRepository.findAll())
                .thenReturn(List.of(
                        employee1, employee2
                ));

        List<EmployeeResponse> result = employeeService.findAllEmployees();

        assertEquals(2, result.size());
        assertEquals("Alex Tan", result.get(0).name());
        assertEquals("John", result.get(1).name());

        verify(employeeRepository).findAll();

    }

    @Test
    void findAllEmployees_whenNoEmployeesExist() {
        when(employeeRepository.findAll())
                .thenReturn(List.of());

        List<EmployeeResponse> result = employeeService.findAllEmployees();

        assertEquals(0, result.size());

        verify(employeeRepository).findAll();

    }
}