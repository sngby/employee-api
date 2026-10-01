package com.example.employeeapi;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest (
        @NotBlank
        String username,

        @NotBlank
        String password

) {}
