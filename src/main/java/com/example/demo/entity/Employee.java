package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer age;
    private String gender;
    private Double salary;
    private Boolean status;

    public Employee() {

    }

    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Integer getAge() {
        return age;
    }
    public String getGender() {
        return gender;
    }
    public Double getSalary() {
        return salary;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Employee(Integer age, String gender, Integer id, String name, Double salary) {
        this.age = age;
        this.gender = gender;
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.status = true;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
