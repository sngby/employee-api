package com.example.employeeapi;

public record ErrorResponse (
        String message,
        int id
){ }
