package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class EmployeeRequest {
    private String name;
    private Integer age;
    @NotNull(message = "Gender cannot be null")
    private String gender;
    @Min(value = 0, message = "Salary must be positive number")
    private Double salary;

    public EmployeeRequest(Integer age, String gender, String name, Double salary) {
        this.age = age;
        this.gender = gender;
        this.name = name;
        this.salary = salary;
    }

    public EmployeeRequest() {
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public @NotNull(message = "Gender cannot be null") String getGender() {
        return gender;
    }

    public void setGender(@NotNull(message = "Gender cannot be null") String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @Min(value = 0, message = "Salary must be positive number") Double getSalary() {
        return salary;
    }

    public void setSalary(@Min(value = 0, message = "Salary must be positive number") Double salary) {
        this.salary = salary;
    }
}
