package com.example.employeeapi;

import io.swagger.v3.oas.annotations.media.Schema;

public record EmployeeResponse(

        @Schema(description = "Unique employee ID", example = "1001")
        int id,

        @Schema(description = "Employee's full name", example = "Alex Tan")
        String name,

        @Schema(description = "Employee's department", example = "IT")
        String department,

        @Schema(description = "Employee's email address", example = "alex@example.com")
        String email
){}
