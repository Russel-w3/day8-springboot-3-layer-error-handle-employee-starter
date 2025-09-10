package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.exception.InvalidAgeAndSalaryEmployeeException;
import com.example.demo.exception.InvalidAgeEmployeeException;
import com.example.demo.exception.InvalidStatusEmployeeException;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        return employeeRepository.getEmployees(gender, page, size);
    }

    public Employee getEmployeeById(int id) {
        Employee employee = employeeRepository.getEmployeeById(id);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        return employee;
    }

    public Employee createEmployee(Employee employee) {
        if (employee.getAge() == null) {
            throw new InvalidAgeEmployeeException("employee age is null!");
        }
        if (employee.getAge() < 18 || employee.getAge() > 65) {
            throw new InvalidAgeEmployeeException("employee age less than 18 or greater than 65!");
        }
        if (employee.getAge() >= 30 && employee.getSalary() <= 20000.0) {
            throw new InvalidAgeAndSalaryEmployeeException("employee age greater than or equal 30 and salary less than 20000!");
        }
        return employeeRepository.createEmployee(employee);
    }

    public Employee updateEmployee(int id, Employee updatedEmployee) {
        if(!employeeRepository.getEmployeeById(id).getStatus()) {
            throw new InvalidStatusEmployeeException("employee has left the company!");
        }
        Employee found = employeeRepository.updateEmployee(id, updatedEmployee);
        if (found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        return found;
    }

    public void deleteEmployee(int id) {
        Employee employee = employeeRepository.deleteEmployee(id);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
    }

    public void deleteAll() {
        employeeRepository.deleteAll();
    }
}
