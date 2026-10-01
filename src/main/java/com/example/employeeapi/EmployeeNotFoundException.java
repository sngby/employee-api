package com.example.employeeapi;

public class EmployeeNotFoundException extends RuntimeException {

    private final int id;

    public EmployeeNotFoundException(int id) {
        super("Employee not found");
        this.id = id;
    }

    public int getId(){
        return this.id;
    }
}
