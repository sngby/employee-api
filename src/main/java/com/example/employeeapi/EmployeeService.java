package com.example.employeeapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeResponse> findAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(employee ->
                        new EmployeeResponse(
                                employee.getId(),
                                employee.getName(),
                                employee.getDepartment(),
                                employee.getEmail()
                        )
                )
                .toList();
    }

    public EmployeeResponse findEmployeeById(int id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Employee {} not found", id);
                    return new EmployeeNotFoundException(id);
                });

        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getDepartment(),
                employee.getEmail()
        );
    }

    public EmployeeResponse createEmployee(Employee employee) {

        if (employeeRepository.findById(employee.getId()).isPresent()) {
            throw new DuplicateEmployeeException(employee.getId());
        }

        Employee savedEmployee = employeeRepository.save(employee);

        logger.info("Employee {} created successfully", savedEmployee.getId());

        return new EmployeeResponse(
                savedEmployee.getId(),
                savedEmployee.getName(),
                savedEmployee.getDepartment(),
                savedEmployee.getEmail()
        );
    }

    public EmployeeResponse updateEmployee(int id, UpdateEmployeeRequest request) {
        Employee existingEmployee =
                employeeRepository.findById(id)
                        .orElseThrow(() -> new EmployeeNotFoundException(id));

        Employee updatedEmployee = new Employee(
                id,
                request.name(),
                request.department(),
                request.salary(),
                request.email()
        );

        employeeRepository.save(updatedEmployee);

        return new EmployeeResponse(
                updatedEmployee.getId(),
                updatedEmployee.getName(),
                updatedEmployee.getDepartment(),
                updatedEmployee.getEmail()
        );
    }

    public void deleteEmployee(int id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        employeeRepository.delete(employee);
    }

    public List<EmployeeResponse> searchEmployeesByName(String name){

        return employeeRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(employee -> new EmployeeResponse(
                        employee.getId(),
                        employee.getName(),
                        employee.getDepartment(),
                        employee.getEmail()
                    ))
                .toList();
    }
}
