package com.example.employeeapi;

public class DuplicateUserException extends RuntimeException {

    public DuplicateUserException() {
        super("Username already exists");
    }
}
