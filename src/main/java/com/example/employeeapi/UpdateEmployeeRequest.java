package com.example.employeeapi;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateEmployeeRequest (

        @Schema(
                description = "Employee's full name",
                example = "Alex Tan"
        )
        @NotBlank
        String name,

        @Schema(
                description = "Employee's department",
                example = "IT"
        )
        @NotBlank
        String department,

        @Schema(
                description = "Employee's annual salary",
                example = "75000"
        )
        @Positive
        double salary,

        @Schema(
                description = "Employee's email address",
                example = "alex@example.com"
        )
        String email
) {}
