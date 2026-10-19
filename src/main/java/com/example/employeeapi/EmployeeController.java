package com.example.employeeapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from my Employee API!";
    }

    @Operation(
            summary = "List all employees",
            description = "Retrieves all employees. Requires a valid JWT and the USER role."
    )
    @GetMapping("/employees")
    public List<EmployeeResponse> employees(){
        return employeeService.findAllEmployees();
    }

    @Operation(
            summary = "Search employees by name",
            description = "Searches for employees whose names contain the specified text, ignoring case. Requires a valid JWT and the USER role."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employees retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @GetMapping("/employees/search")
    public List<EmployeeResponse> searchEmployeeByName(@RequestParam String name){
        return employeeService.searchEmployeesByName(name);
    }

    @Operation(
            summary = "Get employee by ID",
            description = "Retrieves an employee by ID. Requires a valid JWT and the USER role."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee found"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/employees/{id}")
    public EmployeeResponse employee(@PathVariable int id){
        return employeeService.findEmployeeById(id);
    }

    @Operation(
            summary = "Create an employee",
            description = "Creates a new employee. Requires a valid JWT and the USER role."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employee created successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid employee data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Employee already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)
                    )
            )
    })
    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse createEmployee(@Valid @RequestBody Employee employee){
        return employeeService.createEmployee(employee);
    }

    @Operation(
            summary = "Update an employee",
            description = "Updates an employee by ID. Requires a valid JWT and the USER role."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid employee data"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PutMapping("/employees/{id}")
    public EmployeeResponse updateEmployee(
            @PathVariable int id,
            @Valid @RequestBody UpdateEmployeeRequest request
    ){
        return employeeService.updateEmployee(id, request);
    }

    @Operation(
            summary = "Delete an employee",
            description = "Deletes an employee by ID. Requires a valid JWT and the ADMIN role."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @DeleteMapping("/employees/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable int id){
        employeeService.deleteEmployee(id);
    }

    @Operation(
            summary = "Admin-only endpoint",
            description = "Requires a valid JWT and the ADMIN role."
    )
    @GetMapping("/admin")
    public String admin(){
        return "Welcome, administrator";
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEmployeeNotFound(EmployeeNotFoundException ex){
        return new ErrorResponse(
                ex.getMessage(),
                ex.getId()
        );
    }

}
