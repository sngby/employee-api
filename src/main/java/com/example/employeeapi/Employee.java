package com.example.employeeapi;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "employees")
public class Employee {

        @Schema(description = "Unique employee ID", example = "1001")
        @Id
        private int id;

        @Schema(
                description = "Employee's full name",
                example = "Alex Tan"
        )
        @NotBlank
        private String name;

        @Schema(
                description = "Employee's department",
                example = "IT"
        )
        @NotBlank
        private String department;

        @Schema(
                description = "Employee's annual salary",
                example = "75000"
        )
        @Positive
        private double salary;

        @Schema(description = "Employee's email address", example = "alex@example.com")
        private String email;

        public Employee() {}

        public Employee(int id, String name, String department, double salary, String email) {
                this.id = id;
                this.name = name;
                this.department = department;
                this.salary = salary;
                this.email = email;
        }

        public int getId() {
                return id;
        }

        public String getName() {
                return name;
        }

        public String getDepartment() {
                return department;
        }

        public double getSalary() {
                return salary;
        }

        public String getEmail() { return email; }
}

