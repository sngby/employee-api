package com.example.employeeapi;

public record LoginRequest(
        String username,
        String password
) { }
