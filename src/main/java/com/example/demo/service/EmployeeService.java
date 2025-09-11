package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.exception.InvalidAgeAndSalaryEmployeeException;
import com.example.demo.exception.InvalidAgeEmployeeException;
import com.example.demo.exception.InvalidStatusEmployeeException;
import com.example.demo.repository.IEmployeeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final IEmployeeRepository employeeRepository;

    public EmployeeService(IEmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        if (gender == null) {
            if (page == null || size == null) {
                return employeeRepository.findAll();
            } else {
                Pageable pageable = PageRequest.of(page - 1, size);
                return employeeRepository.findAll(pageable).stream().toList();
            }
        } else {
            if (page == null || size == null) {
                return employeeRepository.findEmployeesByGender(gender);
            } else {
                Pageable pageable = PageRequest.of(page - 1, size);
                return employeeRepository.findEmployeesByGender(gender, pageable);
            }
        }
    }

    public Employee getEmployeeById(int id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        return employee.get();
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
        employee.setStatus(true);
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(int id, Employee updatedEmployee) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        if (!employee.get().getStatus()) {
            throw new InvalidStatusEmployeeException("employee has left the company!");
        }
        updatedEmployee.setId(id);
        return employeeRepository.save(updatedEmployee);
    }

    public void deleteEmployee(int id) {
        Employee employee = getEmployeeById(id);
        employee.setStatus(false);
        employeeRepository.save(employee);
    }
}
