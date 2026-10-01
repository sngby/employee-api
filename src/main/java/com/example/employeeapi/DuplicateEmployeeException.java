package com.example.employeeapi;

public class DuplicateEmployeeException extends RuntimeException {

    private final int id;

    public DuplicateEmployeeException(int id) {
        super("Employee already exists");
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
